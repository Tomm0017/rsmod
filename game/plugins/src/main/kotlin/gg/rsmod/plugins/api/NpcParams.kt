package gg.rsmod.plugins.api

import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.plugins.api.ext.enumSetOf

/**
 * @author Tom <rspsmods@gmail.com>
 */
object NpcSkills {
    const val ATTACK = 0
    const val STRENGTH = 1
    const val DEFENCE = 2
    const val MAGIC = 3
    const val RANGED = 4
}

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class NpcSpecies {
    DEMON,
    SHADE,
    KALPHITE,
    SCARAB,
    DRAGON,
    BASIC_DRAGON,
    BRUTAL_DRAGON,
    FIERY,
    UNDEAD
}

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcCombatBuilder {

    private var maxHealth = -1

    private var attackSpeed = -1

    private var stats = Array(5) { -1 }

    private var defaultAttackAnim = -1

    private var defaultBlockAnim = -1

    private val deathAnimList = mutableListOf<Int>()

    private var respawnDelay = -1

    private var aggroRadius = -1

    private var aggroTargetDelay = -1

    private var aggroTimer = -1

    private var poisonChance = -1.0

    private var venomChance = -1.0

    private var poisonImmunity = false

    private var venomImmunity = false

    private var slayerReq = -1

    private var slayerXp = -1.0

    private val bonuses = Array(BONUS_COUNT) { 0 }

    private val speciesSet = enumSetOf<NpcSpecies>()

    fun build(): NpcCombatDef {
        check(maxHealth != -1) { "Max health must be set." }
        check(attackSpeed != -1) { "Attack speed must be set." }
        check(deathAnimList.isNotEmpty()) { "A death animation must be set." }
        check(respawnDelay != -1) { "Respawn delay must be set." }

        stats.forEachIndexed { index, level ->
            stats[index] = Math.max(1, level)
        }
        poisonChance = Math.max(0.0, poisonChance)
        venomChance = Math.max(0.0, venomChance)
        slayerReq = Math.max(1, slayerReq)
        slayerXp = Math.max(0.0, slayerXp)

        if (aggroTimer == -1) {
            aggroTimer = DEFAULT_AGGRO_TIMER
        }

        return NpcCombatDef(
                maxHealth, stats.toList(), attackSpeed, defaultAttackAnim,
                defaultBlockAnim, deathAnimList, respawnDelay, aggroRadius,
                aggroTargetDelay, aggroTimer, poisonChance, venomChance,
                poisonImmunity, venomImmunity, slayerReq, slayerXp,
                bonuses.toList(), speciesSet)
    }

    fun setHitpoints(health: Int): NpcCombatBuilder {
        check(maxHealth == -1) { "Max health already set." }
        maxHealth = health
        return this
    }

    /**
     * @param speed the attack speed, in cycles.
     */
    fun setAttackSpeed(speed: Int): NpcCombatBuilder {
        check(attackSpeed == -1) { "Attack speed already set." }
        attackSpeed = speed
        return this
    }

    fun setAttackLevel(level: Int): NpcCombatBuilder {
        check(stats[NpcSkills.ATTACK] == -1) { "Attack level already set." }
        stats[NpcSkills.ATTACK] = level
        return this
    }

    fun setStrengthLevel(level: Int): NpcCombatBuilder {
        check(stats[NpcSkills.STRENGTH] == -1) { "Strength level already set." }
        stats[NpcSkills.STRENGTH] = level
        return this
    }

    fun setDefenceLevel(level: Int): NpcCombatBuilder {
        check(stats[NpcSkills.DEFENCE] == -1) { "Defence level already set." }
        stats[NpcSkills.DEFENCE] = level
        return this
    }

    fun setMagicLevel(level: Int): NpcCombatBuilder {
        check(stats[NpcSkills.MAGIC] == -1) { "Magic level already set." }
        stats[NpcSkills.MAGIC] = level
        return this
    }

    fun setRangedLevel(level: Int): NpcCombatBuilder {
        check(stats[NpcSkills.RANGED] == -1) { "Ranged level already set." }
        stats[NpcSkills.RANGED] = level
        return this
    }

    fun setLevel(index: Int, level: Int): NpcCombatBuilder {
        check(stats[index] == -1) { "Level [$index] already set." }
        stats[index] = level
        return this
    }

    fun setLevels(attack: Int, strength: Int, defence: Int, magic: Int, ranged: Int): NpcCombatBuilder {
        setAttackLevel(attack)
        setDefenceLevel(defence)
        setStrengthLevel(strength)
        setMagicLevel(magic)
        setRangedLevel(ranged)
        return this
    }

    fun setDefaultAttackAnimation(animation: Int): NpcCombatBuilder {
        check(defaultAttackAnim == -1) { "Default attack animation already set." }
        defaultAttackAnim = animation
        return this
    }

    fun setDefaultBlockAnimation(animation: Int): NpcCombatBuilder {
        check(defaultBlockAnim == -1) { "Default block animation already set." }
        defaultBlockAnim = animation
        return this
    }

    fun setCombatAnimations(attackAnimation: Int, blockAnimation: Int): NpcCombatBuilder {
        setDefaultAttackAnimation(attackAnimation)
        setDefaultBlockAnimation(blockAnimation)
        return this
    }

    fun setDeathAnimation(vararg anims: Int): NpcCombatBuilder {
        check(anims.isNotEmpty()) { "Must specify at least one animation." }
        check(deathAnimList.isEmpty()) { "Death animation(s) already set." }
        anims.forEach { deathAnimList.add(it) }
        return this
    }

    fun setRespawnDelay(cycles: Int): NpcCombatBuilder {
        check(respawnDelay == -1) { "Respawn delay already set." }
        respawnDelay = cycles
        return this
    }

    fun setAggroRadius(radius: Int): NpcCombatBuilder {
        check(aggroRadius == -1) { "Aggro radius already set." }
        aggroRadius = radius
        return this
    }

    fun setFindAggroTargetDelay(delay: Int): NpcCombatBuilder {
        check(aggroTargetDelay == -1) { "Aggro target delay already set." }
        aggroTargetDelay = delay
        return this
    }

    fun setAggroTimer(timer: Int): NpcCombatBuilder {
        check(aggroTimer == -1) { "Aggro timer already set." }
        aggroTimer = timer
        return this
    }

    fun setPoisonChance(chance: Double): NpcCombatBuilder {
        check(poisonChance == -1.0) { "Poison chance already set." }
        poisonChance = chance
        return this
    }

    fun setVenomChance(chance: Double): NpcCombatBuilder {
        check(venomChance == -1.0) { "Venom chance already set." }
        venomChance = chance
        return this
    }

    fun setPoisonImmunity(): NpcCombatBuilder {
        check(!poisonImmunity) { "Poison immunity already set." }
        poisonImmunity = true
        return this
    }

    fun setVenomImmunity(): NpcCombatBuilder {
        check(!venomImmunity) { "Venom immunity already set." }
        venomImmunity = true
        return this
    }

    fun setSlayerRequirement(levelReq: Int): NpcCombatBuilder {
        check(slayerReq == -1) { "Slayer requirement already set." }
        slayerReq = levelReq
        return this
    }

    fun setSlayerXp(xp: Double): NpcCombatBuilder {
        check(slayerXp == -1.0) { "Slayer xp already set." }
        slayerXp = xp
        return this
    }

    fun setSlayerParams(levelReq: Int, xp: Double): NpcCombatBuilder {
        setSlayerRequirement(levelReq)
        setSlayerXp(xp)
        return this
    }

    fun setBonus(index: Int, value: Int): NpcCombatBuilder {
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setAttackStabBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.ATTACK_STAB.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setAttackSlashBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.ATTACK_SLASH.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setAttackCrushBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.ATTACK_CRUSH.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setAttackMagicBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.ATTACK_MAGIC.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setAttackRangedBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.ATTACK_RANGED.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setDefenceStabBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.DEFENCE_STAB.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setDefenceSlashBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.DEFENCE_SLASH.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setDefenceCrushBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.DEFENCE_CRUSH.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setDefenceMagicBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.DEFENCE_MAGIC.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun setDefenceRangedBonus(value: Int): NpcCombatBuilder {
        val index = BonusSlot.DEFENCE_RANGED.id
        check(bonuses[index] == 0) { "Bonus [$index] already set." }
        bonuses[index] = value
        return this
    }

    fun addSpecies(species: NpcSpecies): NpcCombatBuilder {
        speciesSet.add(species)
        return this
    }

    fun setSpecies(vararg species: NpcSpecies): NpcCombatBuilder {
        check(speciesSet.isEmpty()) { "Species already set." }
        speciesSet.addAll(species)
        return this
    }

    companion object {
        private const val BONUS_COUNT = 14
        private const val DEFAULT_AGGRO_TIMER = 1000 // 10 minutes
    }
}