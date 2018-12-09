package gg.rsmod.game

import com.google.common.base.Stopwatch
import gg.rsmod.game.model.Tile
import gg.rsmod.game.plugin.PluginRepository
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

    /**
     * The properties specific to the game being launched by the customer/user.
     */
    private val gameProperties = ServerProperties()

    /**
     * A collection of our [Service]s specified in our game [ServerProperties]
     * files.
     */
    private val services = arrayListOf<Service>()

    /**
     * The plugin repository that's responsible for storing all the plugins found.
     */
    private val plugins = PluginRepository()

    /**
     * The [Store] is responsible for handling the data in our cache.
     */
    private lateinit var filestore: Store

    private val acceptGroup = NioEventLoopGroup(2)

    private val ioGroup = NioEventLoopGroup(1)

    private val serverBootstrap = ServerBootstrap()

    /**
     * Prepares and handles any API related logic that must be handled
     * before the game can be launched properly.
     */
    @Throws(Exception::class)
    fun startServer(apiProps: Path) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> logger.error("Uncaught server exception in thread $t!", e) }
        val overallStopwatch = Stopwatch.createStarted()
        val specificStopwatch = Stopwatch.createUnstarted()

        /**
         * Load the API property file.
         */
        apiProperties.loadYaml(apiProps.toFile())
        logger.info("Preparing ${getApiName()}...")

        /**
         * Load the file store.
         */
        specificStopwatch.reset().start()
        filestore = Store(Paths.get("./data", "cache").toFile())
        filestore.load()
        logger.info("Loaded filestore in {}ms.", specificStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Inform the time it took to load the API related logic.
         */
        logger.info("${getApiName()} loaded up in ${overallStopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
    }

    /**
     * Prepares and handles any game related logic that was specified by the
     * user.
     *
     * Due to being decoupled from the API logic that will always be used, you
     * can start multiple servers with different game property files.
     */
    @Throws(Exception::class)
    fun startGame(gameProps: Path, packets: Path) {
        val stopwatch = Stopwatch.createStarted()
        val individualStopwatch = Stopwatch.createUnstarted()

        /**
         * Load the game property file.
         */
        gameProperties.loadYaml(gameProps.toFile())
        logger.info("Loaded properties for ${gameProperties.get<String>("name")!!}.")

        /**
         * Create a game context for our configurations and services to run.
         */
        val gameContext = GameContext(name = gameProperties.get<String>("name")!!,
                revision = gameProperties.get<Int>("revision")!!,
                cycleTime = gameProperties.get<Int>("cycle-time")!!,
                playerLimit = gameProperties.get<Int>("max-players")!!,
                home = Tile(gameProperties.get<Int>("home-x")!!, gameProperties.get<Int>("home-z")!!, gameProperties.getOrDefault("home-height", 0)),
                rsaEncryption = gameProperties.get<Boolean>("rsa-encryption")!!,
                devMode = gameProperties.getOrDefault("dev-mode", false),
                skillCount = gameProperties.get<Int>("skill-count")!!)
        
        /**
         * Load the services required to run the server.
         */
        loadServices(gameContext)

        /**
         * Fetch the [GameService].
         */
        val gameService = getService(type = GameService::class.java, searchSubclasses = false).get()

        /**
         * Load the packets for the game.
         */
        individualStopwatch.reset().start()
        gameService.messageStructures.load(packets.toFile())
        gameService.messageEncoders.init(gameService.messageStructures)
        gameService.messageDecoders.init(gameService.messageStructures)
        gameService.messageHandlers.init(gameService.messageStructures)
        logger.info("Loaded message codec and handlers in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Load the privileges for the game.
         */
        individualStopwatch.reset().start()
        gameContext.privileges.load(gameProperties)
        logger.info("Loaded {} privilege levels in {}ms.", gameContext.privileges.size(), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Load the plugins for game content.
         */
        individualStopwatch.reset().start()
        plugins.init(gameService = gameService, packagePath = gameProperties.get<String>("plugin-path")!!)
        logger.info("Loaded {} plugins from path {} in {}ms.", DecimalFormat().format(plugins.getCount()), gameProperties.get<String>("plugin-path")!!, individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /**
         * Inform the time it took to load up all non-network logic.
         */
        logger.info("${gameProperties.get<String>("name")!!} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")

        /**
         * Binding the network to allow incoming and outgoing connections.
         */
        val clientChannelInitializer = ClientChannelInitializer(revision = gameContext.revision, filestore = getFilestore(), world = gameService.world)
        serverBootstrap.group(acceptGroup, ioGroup)
        serverBootstrap.channel(NioServerSocketChannel::class.java)
        serverBootstrap.childHandler(clientChannelInitializer)
        serverBootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
        serverBootstrap.bind(InetSocketAddress(gameProperties.get<Int>("game-port")!!)).sync().awaitUninterruptibly()
        logger.info("Now listening for incoming connections...")

        System.gc()
    }

    /**
     * Loads all the services listed on our game properties file.
     */
    private fun loadServices(gameContext: GameContext) {
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
            service.init(this, gameContext, ServerProperties().loadMap(properties))
            stopwatch.stop()

            services.add(service)
            logger.info("Initiated service '{}' in {}ms.", service.javaClass.simpleName, stopwatch.elapsed(TimeUnit.MILLISECONDS))
        }
        logger.info("Loaded {} game services.", services.size)
    }

    fun getPlugins(): PluginRepository = plugins

    fun getFilestore(): Store = filestore

    /**
     * Gets the first service that can be found which meets the criteria of:
     *
     * When [searchSubclasses] is true: the service class must be assignable to the [type].
     * When [searchSubclasses] is false: the service class must be equal to the [type].
     */
    @Suppress("UNCHECKED_CAST")
    fun <T: Service> getService(type: Class<out T>, searchSubclasses: Boolean): Optional<T> {
        if (searchSubclasses) {
            val service = services.firstOrNull { type.isAssignableFrom(it::class.java) }
            return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
        }
        val service = services.firstOrNull { it::class.java == type }
        return if (service != null) Optional.of(service) as Optional<T> else Optional.empty()
    }

    /**
     * Gets the API-specific org name.
     */
    private fun getApiName(): String = apiProperties.get("org")!!
}