package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.sync.block.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc private constructor(val id: Int, world: World, val spawnTile: Tile) : Pawn(world) {

    constructor(id: Int, tile: Tile, world: World) : this(id, world, spawnTile = Tile(tile)) {
        this.tile = tile
    }

    /**
     * This flag indicates whether or not this npc's AI should be processed.
     *
     * As there's a small chance that most npcs will be in the viewport of a real
     * player, we don't really need to process a lot of the logic that comes with
     * the AI.
     */
    private var active = false

    /**
     * This flag indicates whether or not this npc will respawn after death.
     */
    var respawns = false

    /**
     * The radius from [spawnTile], in tiles, which the npc can randomly walk.
     */
    var walkRadius = 0

    /**
     * The current hitpoints the npc has.
     */
    private var hitpoints = 10

    /**
     * The [NpcCombatDef] assigned to our npc. This can change at any point to
     * another combat definition, for example if we want to transmogify the npc,
     * it may want to use a different [NpcCombatDef].
     */
    lateinit var combatDef: NpcCombatDef

    /**
     * The [CombatClass] the npc will use on its next attack.
     */
    var combatClass = CombatClass.MELEE

    /**
     * The [AttackStyle] the npc will use on its next attack.
     */
    var attackStyle = AttackStyle.CONTROLLED

    /**
     * The [CombatStyle] the npc will use on its next attack.
     */
    var combatStyle = CombatStyle.STAB

    /**
     * Check if the npc will be aggressive towards the parameter player.
     */
    var aggroCheck: ((Npc, Player) -> Boolean)? = null

    val name: String get() = def.name

    override fun getType(): EntityType = EntityType.NPC

    override fun isRunning(): Boolean = false

    override fun getSize(): Int = world.definitions.get(NpcDef::class.java, id).size

    override fun getCurrentHp(): Int = hitpoints

    override fun getMaxHp(): Int = combatDef.hitpoints

    override fun setCurrentHp(level: Int) {
        this.hitpoints = level
    }

    override fun addBlock(block: UpdateBlockType) {
        val bits = world.npcUpdateBlocks.updateBlocks[block]!!
        blockBuffer.addBit(bits.bit)
    }

    override fun hasBlock(block: UpdateBlockType): Boolean {
        val bits = world.npcUpdateBlocks.updateBlocks[block]!!
        return blockBuffer.hasBit(bits.bit)
    }

    override fun cycle() {
        if (timers.isNotEmpty) {
            timerCycle()
        }
        hitsCycle()
    }

    /**
     * This method will get the "visually correct" npc id for this npc from
     * [player]'s view point.
     *
     * Npcs can change their appearance for each player depending on their
     * [NpcDef.transforms] and [NpcDef.varp]/[NpcDef.varbit].
     */
    fun getTransform(player: Player): Int {
        if (def.varbit != -1) {
            val varbitDef = world.definitions.get(VarbitDef::class.java, def.varbit)
            val state = player.varps.getBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit)
            return def.transforms!![state]
        }

        if (def.varp != -1) {
            val state = player.varps.getState(def.varp)
            return def.transforms!![state]
        }

        return id
    }

    /**
     * @see active
     */
    fun setActive(active: Boolean) {
        this.active = active
    }

    /**
     * @see active
     */
    fun isActive(): Boolean = active

    /**
     * Verifies if the npc is currently spawned in the world.
     */
    fun isSpawned(): Boolean = index > 0

    /**
     * Gets the [NpcDef] corresponding to our [id].
     */
    val def: NpcDef = world.definitions.get(NpcDef::class.java, id)

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("name", def.name).add("index", index).add("active", active).toString()

    companion object {
        internal const val RESET_PAWN_FACE_DELAY = 25
    }
}
