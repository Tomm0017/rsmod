package gg.rsmod.game

import com.google.common.base.Stopwatch
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.skill.SkillSet
import gg.rsmod.game.protocol.ClientChannelInitializer
import gg.rsmod.game.service.GameService
import gg.rsmod.game.service.rsa.RsaService
import gg.rsmod.util.ServerProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import mu.KLogging
import net.runelite.cache.fs.Store
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

/**
 * The [Server] is responsible for starting any and all games.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Server {

    /**
     * The properties specific to our API.
     */
    private val apiProperties = ServerProperties()

    private val acceptGroup = NioEventLoopGroup(2)

    private val ioGroup = NioEventLoopGroup(1)

    val bootstrap = ServerBootstrap()

    /**
     * Prepares and handles any API related logic that must be handled
     * before the game can be launched properly.
     */
    fun startServer(apiProps: Path) {
        Thread.setDefaultUncaughtExceptionHandler { t, e -> logger.error("Uncaught server exception in thread $t!", e) }
        val stopwatch = Stopwatch.createStarted()

        /*
         * Load the API property file.
         */
        apiProperties.loadYaml(apiProps.toFile())
        logger.info("Preparing ${getApiName()}...")

        /*
         * Inform the time it took to load the API related logic.
         */
        logger.info("${getApiName()} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")
        logger.info("Visit our site ${getApiSite()} to purchase & sell plugins.")
    }

    /**
     * Prepares and handles any game related logic that was specified by the
     * user.
     *
     * Due to being decoupled from the API logic that will always be used, you
     * can start multiple servers with different game property files.
     */
    fun startGame(filestore: Path, gameProps: Path, packets: Path, blocks: Path, devProps: Path?, args: Array<String>): World {
        val stopwatch = Stopwatch.createStarted()
        val individualStopwatch = Stopwatch.createUnstarted()

        /*
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

        /*
         * Create a game context for our configurations and services to run.
         */
        val gameContext = GameContext(initialLaunch = initialLaunch,
                name = gameProperties.get<String>("name")!!,
                revision = gameProperties.get<Int>("revision")!!,
                cycleTime = gameProperties.getOrDefault("cycle-time", 600),
                playerLimit = gameProperties.getOrDefault("max-players", 2048),
                home = Tile(gameProperties.get<Int>("home-x")!!, gameProperties.get<Int>("home-z")!!, gameProperties.getOrDefault("home-height", 0)),
                skillCount = gameProperties.getOrDefault("skill-count", SkillSet.DEFAULT_SKILL_COUNT),
                npcStatCount = gameProperties.getOrDefault("npc-stat-count", Npc.Stats.DEFAULT_NPC_STAT_COUNT),
                runEnergy = gameProperties.getOrDefault("run-energy", true),
                gItemPublicDelay = gameProperties.getOrDefault("gitem-public-spawn-delay", GroundItem.DEFAULT_PUBLIC_SPAWN_CYCLES),
                gItemDespawnDelay = gameProperties.getOrDefault("gitem-despawn-delay", GroundItem.DEFAULT_DESPAWN_CYCLES))

        val devContext = DevContext(
                debugExamines = devProperties.getOrDefault("debug-examines", false),
                debugObjects = devProperties.getOrDefault("debug-objects", false),
                debugButtons = devProperties.getOrDefault("debug-buttons", false),
                debugItemActions = devProperties.getOrDefault("debug-items", false),
                debugMagicSpells = devProperties.getOrDefault("debug-spells", false))

        val world = World(gameContext, devContext)

        /*
         * Load the file store.
         */
        individualStopwatch.reset().start()
        world.filestore = Store(filestore.toFile())
        world.filestore.load()
        world.definitions.loadAll(world.filestore)
        logger.info("Loaded filestore from path {} in {}ms.", filestore, individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the services required to run the server.
         */
        world.loadServices(this, gameProperties)
        world.init()

        /*
         * Load the packets for the game.
         */
        world.getService(type = GameService::class.java)?.let { gameService ->
            individualStopwatch.reset().start()
            gameService.messageStructures.load(packets.toFile())
            gameService.messageEncoders.init()
            gameService.messageDecoders.init(gameService.messageStructures)
            logger.info("Loaded message codec and handlers in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))
        }

        /*
         * Load the update blocks for the game.
         */
        individualStopwatch.reset().start()
        world.loadUpdateBlocks(blocks.toFile())
        logger.info("Loaded update blocks in {}ms.", individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the privileges for the game.
         */
        individualStopwatch.reset().start()
        world.privileges.load(gameProperties)
        logger.info("Loaded {} privilege levels in {}ms.", world.privileges.size(), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Load the plugins for game content.
         */
        individualStopwatch.reset().start()
        world.plugins.init(
                server = this,
                jarPluginsDirectory = gameProperties.getOrDefault("plugin-packed-path", "./plugins"))
        logger.info("Loaded {} plugins in {}ms.", DecimalFormat().format(world.plugins.getPluginCount()), individualStopwatch.elapsed(TimeUnit.MILLISECONDS))

        /*
         * Post load world.
         */
        world.postLoad()

        /*
         * Inform the time it took to load up all non-network logic.
         */
        logger.info("${gameProperties.get<String>("name")!!} loaded up in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)}ms.")

        /*
         * Set our bootstrap's groups and parameters.
         */
        val rsaService = world.getService(RsaService::class.java)
        val clientChannelInitializer = ClientChannelInitializer(revision = gameContext.revision,
                rsaExponent = rsaService?.getExponent(), rsaModulus = rsaService?.getModulus(),
                filestore = world.filestore, world = world)

        bootstrap.group(acceptGroup, ioGroup)
        bootstrap.channel(NioServerSocketChannel::class.java)
        bootstrap.childHandler(clientChannelInitializer)
        bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)

        /*
         * Bind all service networks, if applicable.
         */
        world.bindServices(this)

        /*
         * Bind the game port.
         */
        val port = gameProperties.getOrDefault("game-port", 43594)
        bootstrap.bind(InetSocketAddress(port)).sync().awaitUninterruptibly()
        logger.info("Now listening for incoming connections on port $port...")

        System.gc()

        return world
    }

    /**
     * Gets the API-specific org name.
     */
    fun getApiName(): String = apiProperties.getOrDefault("org", "RS Mod")

    fun getApiSite(): String = apiProperties.getOrDefault("org-site", "rspsmods.com")

    companion object: KLogging()
}
