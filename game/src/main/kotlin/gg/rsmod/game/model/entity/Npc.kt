package gg.rsmod.game.model.entity

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.sync.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc private constructor(val id: Int, override val world: World) : Pawn(world) {

    constructor(id: Int, tile: Tile, world: World) : this(id, world) {
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

    private var hitpoints = 10

    lateinit var combatDef: NpcCombatDef

    var combatClass = CombatClass.MELEE

    var combatStyle = CombatStyle.NONE

    var aggroCheck: ((Npc, Player) -> Boolean)? = null

    override fun getType(): EntityType = EntityType.NPC

    override fun isRunning(): Boolean = false

    override fun getTileSize(): Int = world.definitions.get(NpcDef::class.java, id).size

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
        timerCycle()
        hitsCycle()
    }

    fun setActive(active: Boolean) {
        this.active = active
    }

    fun isActive(): Boolean = active

    fun isSpawned(): Boolean = index > 0
}