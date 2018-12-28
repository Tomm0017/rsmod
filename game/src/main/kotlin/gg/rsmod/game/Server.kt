package gg.rsmod.game

import com.google.common.base.Stopwatch
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.protocol.ClientChannelInitializer
import gg.rsmod.game.service.GameService
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The [Server] is responsible for starting any and all games.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Server {

    companion object {
        private val logger = LogManager.getLogger(Server::class.java)
    }

    /**
     * The properties specific to our API.
     */
    private val apiProperties = ServerProperties()

    private val acceptGroup = NioEventLoopGroup(2)

    private val ioGroup = NioEventLoopGroup(1)

    /**
     * Prepares and handles any API related logic that must be handled
     * before the game can be launched properly.
     */
    @Throws(Exception::class)
    fun startServer(apiProps: Path) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> logger.error("Uncaught server exception in thread $t!", e) }
        val overallStopwatch = Stopwatch.createStarted()

        /**
         * Load the API property file.
         */
        apiProperties.loadYaml(apiProps.toFile())
        logger.info("Preparing ${getApiName()}...")

        /**
         * Inform the time it took to load the API related logic.
         */
        logger.info("${getApiName()} loaded up in ${overallStopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
        logger.info("Visit our site ${getApiSite()} to purchase & sell plugins.")
    }

    /**
     * Prepares and handles any game related logic that was specified by the
     * user.
     *
     * Due to being decoupled from the API logic that will always be used, you
     * can start multiple servers with different game property files.
     */
    @Throws(Exception::class)
    fun startGame(filestore: Path, gameProps: Path, packets: Path, devProps: Path?): World {
        val stopwatch = Stopwatch.createStarted()
        val individualStopwatch = Stopwatch.createUnstarted()


        /**
         * Load the game property file.
         */
        val initialLaunch = Files.deleteIfExists(Paths.get("./first-launch"))
        val gameProperties = ServerProperties()
        val devProperties = ServerProperties()
        gameProperties.loadYaml(gameProps.toFile())
        if (devProps != null && Files.exists(devProps)) {
            devProperties.loadYaml(devProps.toFile())
        }
        logger.info("Loaded properties for ${gameProperties.get<String>("name")!!}.")

        /**
         * Create a game context for our configurations and services to run.
         */
        val gameContext = GameContext(initialLaunch = initialLaunch,
                name = gameProperties.get<String>("name")!!,
                revision = gameProperties.get<Int>("revision")!!,
                cycleTime = gameProperties.getOrDefault("cycle-time", 600),
                playerLimit = gameProperties.getOrDefault("max-players", 2000),
                home = Tile(gameProperties.get<Int>("home-x")!!, gameProperties.get<Int>("home-z")!!, gameProperties.getOrDefault("home-height", 0)),
                rsaEncryption = gameProperties.getOrDefault("rsa-encryption", false),
                skillCount = gameProperties.get<Int>("skill-count")!!,
                runEnergy = gameProperties.getOrDefault("run-energy", true))

        val devContext = DevContext(debugObjects = devProperties.getOrDefault("debug-objects", false),
                debugButtons = devProperties.getOrDefault("debug-buttons", false))

        val world = World(this, gameContext, devContext)

        /**
         * Load the file store.
         */
        individualStopwatch.reset().start()
        world.filestore = Store(filestore.toFile())
        world.filestore.load()
        world.definitions.init(world.filestore)
        logger.info("Loaded filestore from path {} in {}ms.", filestore, individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Load the services required to run the server.
         */
        loadServices(world, gameProperties)

        /**
         * Fetch the [GameService].
         */
        val gameService = world.getService(type = GameService::class.java, searchSubclasses = false).get()

        /**
         * Load the packets for the game.
         */
        individualStopwatch.reset().start()
        gameService.messageStructures.load(packets.toFile())
        gameService.messageEncoders.init()
        gameService.messageDecoders.init(gameService.messageStructures)
        logger.info("Loaded message codec and handlers in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Load the privileges for the game.
         */
        individualStopwatch.reset().start()
        world.privileges.load(gameProperties)
        logger.info("Loaded {} privilege levels in {}ms.", world.privileges.size(), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Load the plugins for game content.
         */
        individualStopwatch.reset().start()
        world.plugins.init(gameService = gameService, packagePath = gameProperties.get<String>("plugin-path")!!)
        logger.info("Loaded {} plugins from path {} in {}ms.", DecimalFormat().format(world.plugins.getCount()), gameProperties.get<String>("plugin-path")!!, individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Inform the time it took to load up all non-network logic.
         */
        logger.info("${gameProperties.get<String>("name")!!} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")

        /**
         * Binding the network to allow incoming and outgoing connections.
         */
        val serverBootstrap = ServerBootstrap()
        val clientChannelInitializer = ClientChannelInitializer(revision = gameContext.revision, filestore = world.filestore, world = world)
        val port = gameProperties.get<Int>("game-port")!!

        serverBootstrap.group(acceptGroup, ioGroup)
        serverBootstrap.channel(NioServerSocketChannel::class.java)
        serverBootstrap.childHandler(clientChannelInitializer)
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
        serverBootstrap.bind(InetSocketAddress(port)).sync().awaitUninterruptibly()
        logger.info("Now listening for incoming connections on port $port...")

        System.gc()

        return world
    }

    /**
     * Loads all the services listed on our game properties file.
     */
    private fun loadServices(world: World, gameProperties: ServerProperties) {
        val stopwatch = Stopwatch.createUnstarted()
        val foundServices = gameProperties.get<ArrayList<Any>>("services")!!
        foundServices.forEach { it ->
            val values = it as LinkedHashMap<*, *>
            val className = values["class"] as String
            val clazz = Class.forName(className).asSubclass(Service::class.java)!!
            val service = clazz.newInstance()

            val properties = hashMapOf<String, Any>()
            values.filterKeys { it != "class" }.forEach { key, value ->
                properties[key as String] = value
            }

            stopwatch.reset().start()
            service.init(this, world, ServerProperties().loadMap(properties))
            stopwatch.stop()

            world.services.add(service)
            logger.info("Initiated service '{}' in {}ms.", service.javaClass.simpleName, stopwatch.elapsed(TimeUnit.MILLISECONDS))
        }
        logger.info("Loaded {} game services.", world.services.size)
    }

    /**
     * Gets the API-specific org name.
     */
    fun getApiName(): String = apiProperties.getOrDefault("org", "RS Mod")

    fun getApiSite(): String = apiProperties.getOrDefault("org-site", "rsmods.gg")
}