package gg.rsmod.game.model.combat

/**
 * Represents the combat definition for an npc.
 *
 * @param hitpoints
 * The maximum amount of hitpoints.
 *
 * @param combatLvl
 * The combat level.
 *
 * @param attackLvl
 * The combat attack level.
 *
 * @param strengthLvl
 * The combat strength level.
 *
 * @param defenceLvl
 * The combat defence level.
 *
 * @param magicLvl
 * The combat magic level.
 *
 * @param rangedLvl
 * The combat ranged level.
 *
 * @param meleeMaxHit
 * The max hit while using melee.
 *
 * @param magicMaxHit
 * The max hit while casting magic.
 *
 * @param rangedMaxHit
 * The max hit while using ranged.
 *
 * @param attackSpeed
 * The attack speed, in game cycles.
 *
 * @param aggressiveRadius
 * The radius in which a target can be found, in tiles.
 *
 * @param findTargetDelay
 * The amount of time in between searching for new targets within the
 * [aggressiveRadius], in game cycles.
 *
 * @param meleeAnimation
 * The animation while using melee.
 *
 * @param magicAnimation
 * The animation while casting magic.
 *
 * @param rangedAnimation
 * The animation while using ranged.
 *
 * @param deathAnimation
 * The animation to perform on death.
 *
 * @param deathDelay
 * The time length of the death animation, in game cycles.
 *
 * @param respawnDelay
 * The delay to respawn after being killed, in game cycles.
 *
 * @param poisonChance
 * The chance of afflicting poison onto targets, can range from [0.0 - 1.0].
 *
 * @param poisonImmunity
 * Cannot be poisoned when true.
 *
 * @param venomImmunity
 * Cannot be venomed when true.
 *
 * @param slayerReq
 * Slayer level required be attacked, if none, set to -1.
 *
 * @param slayerXp
 * Slayer XP given when killed, if none, set to -1.0.
 *
 * @param bonuses
 * The bonuses.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class NpcCombatDef(val hitpoints: Int, val combatLvl: Int, val attackLvl: Int, val strengthLvl: Int,
                        val defenceLvl: Int, val magicLvl: Int, val rangedLvl: Int, val meleeMaxHit: Int,
                        val magicMaxHit: Int, val rangedMaxHit: Int, val attackSpeed: Int, val aggressiveRadius: Int,
                        val findTargetDelay: Int, val meleeAnimation: Int, val magicAnimation: Int, val rangedAnimation: Int,
                        val deathAnimation: Int, val deathDelay: Int, val respawnDelay: Int, val poisonChance: Double,
                        val poisonImmunity: Boolean, val venomImmunity: Boolean, val slayerReq: Int, val slayerXp: Double,
                        val bonuses: Array<Int>) {

    constructor(other: NpcCombatDef) : this(other.hitpoints, other.combatLvl, other.attackLvl, other.strengthLvl,
            other.defenceLvl, other.magicLvl, other.rangedLvl, other.meleeMaxHit, other.magicMaxHit, other.rangedMaxHit,
            other.attackSpeed, other.aggressiveRadius, other.findTargetDelay, other.meleeAnimation, other.magicAnimation,
            other.rangedAnimation, other.deathAnimation, other.deathDelay, other.respawnDelay, other.poisonChance,
            other.poisonImmunity, other.venomImmunity, other.slayerReq, other.slayerXp, other.bonuses.copyOf())

    companion object {
        val DEFAULT = NpcCombatDef(hitpoints = 10, combatLvl = 1, attackLvl = 1, strengthLvl = 1, defenceLvl = 1,
                magicLvl = 1, rangedLvl = 1, meleeMaxHit = 1, magicMaxHit = 1, rangedMaxHit = 1, attackSpeed = 4,
                aggressiveRadius = 0, findTargetDelay = 0, meleeAnimation = 422, rangedAnimation = -1, magicAnimation = -1,
                deathAnimation = 836, deathDelay = 4, respawnDelay = 25, poisonChance = 0.0, poisonImmunity = false, venomImmunity = false,
                slayerReq = 1, slayerXp = 0.0, bonuses = Array(14) { 0 })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NpcCombatDef

        if (hitpoints != other.hitpoints) return false
        if (combatLvl != other.combatLvl) return false
        if (attackLvl != other.attackLvl) return false
        if (strengthLvl != other.strengthLvl) return false
        if (defenceLvl != other.defenceLvl) return false
        if (magicLvl != other.magicLvl) return false
        if (rangedLvl != other.rangedLvl) return false
        if (meleeMaxHit != other.meleeMaxHit) return false
        if (magicMaxHit != other.magicMaxHit) return false
        if (rangedMaxHit != other.rangedMaxHit) return false
        if (attackSpeed != other.attackSpeed) return false
        if (aggressiveRadius != other.aggressiveRadius) return false
        if (findTargetDelay != other.findTargetDelay) return false
        if (meleeAnimation != other.meleeAnimation) return false
        if (magicAnimation != other.magicAnimation) return false
        if (rangedAnimation != other.rangedAnimation) return false
        if (deathAnimation != other.deathAnimation) return false
        if (deathDelay != other.deathDelay) return false
        if (respawnDelay != other.respawnDelay) return false
        if (poisonChance != other.poisonChance) return false
        if (poisonImmunity != other.poisonImmunity) return false
        if (venomImmunity != other.venomImmunity) return false
        if (slayerReq != other.slayerReq) return false
        if (slayerXp != other.slayerXp) return false
        if (!bonuses.contentEquals(other.bonuses)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hitpoints
        result = 31 * result + combatLvl
        result = 31 * result + attackLvl
        result = 31 * result + strengthLvl
        result = 31 * result + defenceLvl
        result = 31 * result + magicLvl
        result = 31 * result + rangedLvl
        result = 31 * result + meleeMaxHit
        result = 31 * result + magicMaxHit
        result = 31 * result + rangedMaxHit
        result = 31 * result + attackSpeed
        result = 31 * result + aggressiveRadius
        result = 31 * result + findTargetDelay
        result = 31 * result + meleeAnimation
        result = 31 * result + magicAnimation
        result = 31 * result + rangedAnimation
        result = 31 * result + deathAnimation
        result = 31 * result + deathDelay
        result = 31 * result + respawnDelay
        result = 31 * result + poisonChance.hashCode()
        result = 31 * result + poisonImmunity.hashCode()
        result = 31 * result + venomImmunity.hashCode()
        result = 31 * result + slayerReq
        result = 31 * result + slayerXp.hashCode()
        result = 31 * result + bonuses.contentHashCode()
        return result
    }

}