package gg.rsmod.game.model.combat

/**
 * Represents the combat definition for an npc.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class NpcCombatDef private constructor(
        val hitpoints: Int, val attackSpeed: Int, val attackAnimation: Int,
        val blockAnimation: Int, val deathAnimation: List<Int>, val respawnDelay: Int,
        val aggressiveRadius: Int, val aggroTargetDelay: Int, val poisonChance: Double,
        val poisonImmunity: Boolean, val venomImmunity: Boolean, val slayerReq: Int, val slayerXp: Double,
        val bonuses: IntArray = IntArray(14) { 0 }) {

    fun isDemon(): Boolean = false

    fun isShade(): Boolean = false

    fun isKalphite(): Boolean = false

    fun isScarab(): Boolean = false

    fun isDragon(): Boolean = false

    fun isBasicDragon(): Boolean = false

    fun isBrutalDragon(): Boolean = false

    fun isFiery(): Boolean = false

    fun isUndead(): Boolean = false

    companion object {
        private const val DEFAULT_HITPOINTS = 10
        private const val DEFAULT_ATTACK_SPEED = 4
        private const val DEFAULT_RESPAWN_DELAY = 25

        private const val ATTACK_STAB_BONUS = 0
        private const val ATTACK_SLASH_BONUS = 1
        private const val ATTACK_CRUSH_BONUS = 2
        private const val ATTACK_MAGIC_BONUS = 3
        private const val ATTACK_RANGED_BONUS = 4

        private const val DEFENCE_STAB_BONUS = 5
        private const val DEFENCE_SLASH_BONUS = 6
        private const val DEFENCE_CRUSH_BONUS = 7
        private const val DEFENCE_MAGIC_BONUS = 8
        private const val DEFENCE_RANGED_BONUS = 9

        private const val ATTACK_BONUS = 10
        private const val STRENGTH_BONUS = 11
        private const val RANGED_BONUS = 12
        private const val MAGIC_BONUS = 13

        val DEFAULT = NpcCombatDef(
                hitpoints = DEFAULT_HITPOINTS, attackSpeed = DEFAULT_ATTACK_SPEED,
                aggressiveRadius = 0, aggroTargetDelay = 0, attackAnimation = 422,
                blockAnimation = 424, deathAnimation = listOf(836),
                respawnDelay = DEFAULT_RESPAWN_DELAY, poisonChance = 0.0,
                poisonImmunity = false, venomImmunity = false, slayerReq = 1,
                slayerXp = 0.0, bonuses = IntArray(14) { 0 })
    }

    class Builder {

        private var hitpoints = -1

        private var attackLvl = -1

        private var strengthLvl = -1

        private var defenceLvl = -1

        private var magicLvl = -1

        private var rangedLvl = -1

        private var attackSpeed = -1

        private var attackAnimation = -1

        private var blockAnimation = -1

        private var deathAnimation = arrayListOf<Int>()

        private var respawnDelay = -1

        private var aggressiveRadius = -1

        private var aggroTargetDelay = -1

        private var poisonChance = -1.0

        private var poisonImmunity = false

        private var venomImmunity = false

        private var slayerReq = -1

        private var slayerXp = -1.0

        private val bonuses = IntArray(14) { 0 }

        fun build(): NpcCombatDef {
            check(aggressiveRadius == -1 || aggroTargetDelay == -1 || aggressiveRadius != -1 && aggroTargetDelay != -1) {
                "Can't set aggressive radius without aggressive target delay and vise-versa."
            }

            if (hitpoints == -1) {
                hitpoints = DEFAULT_HITPOINTS
            }

            if (attackLvl == -1) {
                attackLvl = 1
            }

            if (strengthLvl == -1) {
                strengthLvl = 1
            }

            if (defenceLvl == -1) {
                defenceLvl = 1
            }

            if (magicLvl == -1) {
                magicLvl = 1
            }

            if (rangedLvl == -1) {
                rangedLvl = 1
            }

            if (attackSpeed == -1) {
                attackSpeed = DEFAULT_ATTACK_SPEED
            }

            if (respawnDelay == -1) {
                respawnDelay = DEFAULT_RESPAWN_DELAY
            }

            if (aggressiveRadius == -1) {
                aggressiveRadius = 0
            }

            if (aggroTargetDelay == -1) {
                aggroTargetDelay = 0
            }

            if (poisonChance == -1.0) {
                poisonChance = 0.0
            }

            if (slayerReq == -1) {
                slayerReq = 0
            }

            if (slayerXp == -1.0) {
                slayerXp = 0.0
            }

            return NpcCombatDef(
                    hitpoints = hitpoints, attackSpeed = attackSpeed, aggressiveRadius = aggressiveRadius,
                    aggroTargetDelay = aggroTargetDelay, attackAnimation = attackAnimation,
                    blockAnimation = blockAnimation, deathAnimation = deathAnimation, respawnDelay = respawnDelay,
                    poisonChance = poisonChance, poisonImmunity = poisonImmunity, venomImmunity = venomImmunity,
                    slayerReq = slayerReq, slayerXp = slayerXp, bonuses = bonuses.copyOf())
        }

        fun setHitpoints(hitpoints: Int): Builder {
            this.hitpoints = hitpoints
            return this
        }

        fun setAttackLvl(attackLvl: Int): Builder {
            this.attackLvl = attackLvl
            return this
        }

        fun setStrengthLvl(strengthLvl: Int): Builder {
            this.strengthLvl = strengthLvl
            return this
        }

        fun setDefenceLvl(defenceLvl: Int): Builder {
            this.defenceLvl = defenceLvl
            return this
        }

        fun setMagicLvl(magicLvl: Int): Builder {
            this.magicLvl = magicLvl
            return this
        }

        fun setRangedLvl(rangedLvl: Int): Builder {
            this.rangedLvl = rangedLvl
            return this
        }

        fun setAttackSpeed(attackSpeed: Int): Builder {
            this.attackSpeed = attackSpeed
            return this
        }

        fun setAttackAnimation(attackAnimation: Int): Builder {
            this.attackAnimation = attackAnimation
            return this
        }

        fun setBlockAnimation(blockAnimation: Int): Builder {
            this.blockAnimation = blockAnimation
            return this
        }

        fun setDeathAnimation(deathAnimation: Int): Builder {
            this.deathAnimation = arrayListOf(deathAnimation)
            return this
        }

        fun addDeathAnimation(deathAnimation: Int): Builder {
            this.deathAnimation.add(deathAnimation)
            return this
        }

        fun setRespawnDelay(respawnDelay: Int): Builder {
            this.respawnDelay = respawnDelay
            return this
        }

        fun setAggressiveRadius(aggressiveRadius: Int): Builder {
            this.aggressiveRadius = aggressiveRadius
            return this
        }

        fun setFindAggroTargetDelay(aggroTargetDelay: Int): Builder {
            this.aggroTargetDelay = aggroTargetDelay
            return this
        }

        fun setPoisonChance(poisonChance: Double): Builder {
            this.poisonChance = poisonChance
            return this
        }

        fun setPoisonImmunity(): Builder {
            this.poisonImmunity = true
            return this
        }

        fun setVenomImmunity(): Builder {
            this.venomImmunity = true
            return this
        }

        fun setSlayerReq(slayerReq: Int): Builder {
            this.slayerReq = slayerReq
            return this
        }

        fun setSlayerXp(slayerXp: Double): Builder {
            this.slayerXp = slayerXp
            return this
        }

        fun setAttackStabBonus(bonus: Int): Builder {
            bonuses[ATTACK_STAB_BONUS] = bonus
            return this
        }

        fun setAttackSlashBonus(bonus: Int): Builder {
            bonuses[ATTACK_SLASH_BONUS] = bonus
            return this
        }

        fun setAttackCrushBonus(bonus: Int): Builder {
            bonuses[ATTACK_CRUSH_BONUS] = bonus
            return this
        }

        fun setAttackMagicBonus(bonus: Int): Builder {
            bonuses[ATTACK_MAGIC_BONUS] = bonus
            return this
        }

        fun setAttackRangedBonus(bonus: Int): Builder {
            bonuses[ATTACK_RANGED_BONUS] = bonus
            return this
        }

        fun setDefenceStabBonus(bonus: Int): Builder {
            bonuses[DEFENCE_STAB_BONUS] = bonus
            return this
        }

        fun setDefenceSlashBonus(bonus: Int): Builder {
            bonuses[DEFENCE_SLASH_BONUS] = bonus
            return this
        }

        fun setDefenceCrushBonus(bonus: Int): Builder {
            bonuses[DEFENCE_CRUSH_BONUS] = bonus
            return this
        }

        fun setDefenceMagicBonus(bonus: Int): Builder {
            bonuses[DEFENCE_MAGIC_BONUS] = bonus
            return this
        }

        fun setDefenceRangedBonus(bonus: Int): Builder {
            bonuses[DEFENCE_RANGED_BONUS] = bonus
            return this
        }

        fun setAttackBonus(bonus: Int): Builder {
            bonuses[ATTACK_BONUS] = bonus
            return this
        }

        fun setStrengthBonus(bonus: Int): Builder {
            bonuses[STRENGTH_BONUS] = bonus
            return this
        }

        fun setRangedBonus(bonus: Int): Builder {
            bonuses[RANGED_BONUS] = bonus
            return this
        }

        fun setMagicBonus(bonus: Int): Builder {
            bonuses[MAGIC_BONUS] = bonus
            return this
        }
    }
}