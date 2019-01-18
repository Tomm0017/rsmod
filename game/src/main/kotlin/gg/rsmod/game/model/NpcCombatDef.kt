package gg.rsmod.game.model

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class NpcCombatDef(val hitpoints: Int, val combatLvl: Int, val attackLvl: Int, val strengthLvl: Int,
                        val defenceLvl: Int, val magicLvl: Int, val rangedLvl: Int, val meleeMaxHit: Int,
                        val magicMaxHit: Int, val rangedMaxHit: Int, val aggressive: Boolean,
                        val poisonChance: Double, val poisonImmunity: Boolean, val venomImmunity: Boolean,
                        val slayerReq: Int, val slayerXp: Double, val stats: Array<Int>, val bonuses: Array<Int>) {

    companion object {
        val DEFAULT = NpcCombatDef(hitpoints = 10, combatLvl = 1, attackLvl = 1, strengthLvl = 1, defenceLvl = 1,
                magicLvl = 1, rangedLvl = 1, meleeMaxHit = 1, magicMaxHit = 1, rangedMaxHit = 1,
                aggressive = false, poisonChance = 0.0, poisonImmunity = false, venomImmunity = false,
                slayerReq = 1, slayerXp = 0.0, stats = Array(5) { 0 }, bonuses = Array(14) { 0 })
    }

    constructor(other: NpcCombatDef) : this(other.hitpoints, other.combatLvl, other.attackLvl, other.strengthLvl,
            other.defenceLvl, other.magicLvl, other.rangedLvl, other.meleeMaxHit, other.magicMaxHit, other.rangedMaxHit,
            other.aggressive, other.poisonChance, other.poisonImmunity, other.venomImmunity,
            other.slayerReq, other.slayerXp, other.stats.copyOf(), other.bonuses.copyOf())

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
        if (aggressive != other.aggressive) return false
        if (poisonChance != other.poisonChance) return false
        if (poisonImmunity != other.poisonImmunity) return false
        if (venomImmunity != other.venomImmunity) return false
        if (slayerReq != other.slayerReq) return false
        if (slayerXp != other.slayerXp) return false
        if (!stats.contentEquals(other.stats)) return false
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
        result = 31 * result + aggressive.hashCode()
        result = 31 * result + poisonChance.hashCode()
        result = 31 * result + poisonImmunity.hashCode()
        result = 31 * result + venomImmunity.hashCode()
        result = 31 * result + slayerReq
        result = 31 * result + slayerXp.hashCode()
        result = 31 * result + stats.contentHashCode()
        result = 31 * result + bonuses.contentHashCode()
        return result
    }

}