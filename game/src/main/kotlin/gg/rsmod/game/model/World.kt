package gg.rsmod.game.model

import gg.rsmod.game.DevContext
import gg.rsmod.game.GameContext
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.message.MessageValue
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.region.ChunkSet
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.plugin.PluginExecutor
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.service.Service
import gg.rsmod.game.service.xtea.XteaKeyService
import gg.rsmod.game.sync.UpdateBlockStructure
import gg.rsmod.game.sync.UpdateBlockType
import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType
import gg.rsmod.util.ServerProperties
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager
import java.io.File
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * The game world, which stores all the entities and nodes that the world
 * needs to keep track of.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class World(val server: Server, val gameContext: GameContext, val devContext: DevContext) {

    companion object {
        private val logger = LogManager.getLogger(World::class.java)
    }

    val players = PawnList<Player>(gameContext.playerLimit)

    val npcs = PawnList<Npc>(Short.MAX_VALUE.toInt())

    val collision = CollisionManager(this)

    val chunks = ChunkSet(this)

    /**
     * A collection of our [Service]s specified in our game [ServerProperties]
     * files.
     */
    val services = arrayListOf<Service>()

    /**
     * The [PluginExecutor] is responsible for executing plugins as requested by
     * the game.
     */
    val pluginExecutor = PluginExecutor()

    /**
     * The [Store] is responsible for handling the data in our cache.
     */
    lateinit var filestore: Store

    /**
     * The [DefinitionSet] that holds general filestore data.
     */
    val definitions = DefinitionSet()

    /**
     * The plugin repository that's responsible for storing all the plugins found.
     */
    val plugins = PluginRepository()

    /**
     * The [PrivilegeSet] that is attached to our game.
     */
    val privileges = PrivilegeSet()

    /**
     * A cached value for [gg.rsmod.game.service.xtea.XteaKeyService] since it
     * is used frequently and in performance critical code. This value is set
     * when [XteaKeyService.init] is called.
     */
    var xteaKeyService: XteaKeyService? = null

    /**
     * When [gg.rsmod.game.sync.UpdateBlockBuffer.blockValue] exceeds the value
     * 0xFF, we have to write it as a short. The client uses a certain bit to
     * identify when this is the case.
     */
    var updateBlockExcessMask = 0

    /**
     * The order in which [UpdateBlockType]s must be handled in the player
     * synchronization process.
     */
    val updateBlockOrder = arrayListOf<UpdateBlockType>()

    val updateBlocks = EnumMap<UpdateBlockType, UpdateBlockStructure>(UpdateBlockType::class.java)

    /**
     * A [Random] implementation used for pseudo-random purposes through-out
     * the game world.
     */
    private val random: Random = ThreadLocalRandom.current()

    /**
     * The amount of game cycles that have gone by since the world was first
     * initialized. This can reset back to [0], if it's signalled to overflow
     * any time soon.
     */
    var currentCycle = 0

    fun register(p: Player): Boolean {
        val registered = players.add(p)
        if (registered) {
            p.attr.put(INDEX_ATTR, p.index)
            return true
        }
        return false
    }

    fun unregister(p: Player) {
        players.remove(p)
    }

    fun spawn(npc: Npc) {

    }

    fun remove(npc: Npc) {

    }

    fun spawn(obj: GameObject) {
        val tile = obj.tile

        val chunk = chunks.getForTile(tile)

        val oldObj = chunk.getEntities<GameObject>(tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull { it.type == obj.type }
        if (oldObj != null) {
            chunk.removeEntity(this, oldObj, tile)
        }

        chunk.addEntity(this, obj, tile)
    }

    fun remove(obj: GameObject) {
        val tile = obj.tile

        val chunk = chunks.getForTile(tile)

        chunk.removeEntity(this, obj, tile)
    }

    fun isSpawned(obj: GameObject): Boolean = chunks.getForTile(obj.tile).getEntities<GameObject>(obj.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).contains(obj)

    fun getPlayerForName(username: String): Optional<Player> {
        for (i in 0 until players.capacity) {
            val player = players.get(i) ?: continue
            if (player.username == username) {
                return Optional.of(player)
            }
        }
        return Optional.empty()
    }

    fun random(boundInclusive: Int) = random.nextInt(boundInclusive + 1)

    fun random(range: IntRange): Int = random.nextInt(range.endInclusive - range.start + 1) + range.start

    fun executePlugin(plugin: Function1<Plugin, Unit>) {
        pluginExecutor.execute(this, plugin)
    }

    @Throws(Exception::class)
    fun loadUpdateBlocks(updateBlocks: File) {
        check(this.updateBlockExcessMask == 0)
        check(this.updateBlockOrder.isEmpty())
        check(this.updateBlocks.isEmpty())

        val properties = ServerProperties().loadYaml(updateBlocks)

        val excessMask = Integer.decode(properties.get<String>("excess-mask"))
        updateBlockExcessMask = excessMask

        val orders = properties.get<ArrayList<Any>>("order")!!
        orders.forEach { order ->
            val blockType = UpdateBlockType.valueOf((order as String).toUpperCase())
            this.updateBlockOrder.add(blockType)
        }

        val blocks = properties.get<ArrayList<Any>>("blocks")!!
        blocks.forEach { packet ->
            val values = packet as LinkedHashMap<*, *>
            val blockType = (values["block"] as String).toUpperCase()
            val playerBits = if (values.containsKey("pbit")) Integer.decode(values["pbit"] as String) else -1
            val npcBits = if (values.containsKey("nbit")) Integer.decode(values["nbit"] as String) else -1
            val structureValues = arrayListOf<MessageValue>()

            if (values.containsKey("structure")) {
                val structures = values["structure"] as ArrayList<*>
                structures.forEach { structure ->
                    val map = structure as LinkedHashMap<*, *>
                    val name = map["name"] as String
                    val order = if (map.containsKey("order")) DataOrder.valueOf(map["order"] as String) else DataOrder.BIG
                    val transform = if (map.containsKey("trans")) DataTransformation.valueOf(map["trans"] as String) else DataTransformation.NONE
                    val type = DataType.valueOf(map["type"] as String)
                    val signature = if (map.containsKey("sign")) DataSignature.valueOf((map["sign"] as String).toUpperCase()) else DataSignature.SIGNED
                    structureValues.add(MessageValue(id = name, order = order, transformation = transform, type = type, signature = signature))
                }
            }

            val block = UpdateBlockType.valueOf(blockType)
            val structure = UpdateBlockStructure(playerBit = playerBits, npcBit = npcBits, values = structureValues)
            this.updateBlocks[block] = structure
        }
    }

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
}