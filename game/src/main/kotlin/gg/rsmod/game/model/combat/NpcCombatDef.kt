package gg.rsmod.game.model.combat

/**
 * Represents the combat definition for an npc.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class NpcCombatDef(
        val hitpoints: Int, val stats: List<Int>, val attackSpeed: Int, val attackAnimation: Int,
        val blockAnimation: Int, val deathAnimation: List<Int>, val attackSound: Int,
        val blockSound: Int, val deathSound: Int, val respawnDelay: Int,
        val aggressiveRadius: Int, val aggroTargetDelay: Int, val aggressiveTimer: Int,
        val poisonChance: Double, val venomChance: Double, val poisonImmunity: Boolean,
        val venomImmunity: Boolean, val slayerReq: Int, val slayerXp: Double,
        val bonuses: List<Int>, val species: Set<Any>) {

    companion object {

        private const val DEFAULT_HITPOINTS = 10
        private const val DEFAULT_ATTACK_SPEED = 4
        private const val DEFAULT_RESPAWN_DELAY = 25
        private const val DEFAULT_ATTACK_ANIMATION = 422
        private const val DEFAULT_BLOCK_ANIMATION = 424
        private const val DEFAULT_DEATH_ANIMATION = 836
        private const val DEFAULT_ATTACK_SOUND = 2566
        private const val DEFAULT_BLOCK_SOUND = 513
        private const val DEFAULT_DEATH_SOUND = 512

        val DEFAULT = NpcCombatDef(
                hitpoints = DEFAULT_HITPOINTS, stats = listOf(1, 1, 1, 1, 1),
                attackSpeed = DEFAULT_ATTACK_SPEED, aggressiveRadius = 0,
                aggroTargetDelay = 0, aggressiveTimer = 0,
                attackAnimation = DEFAULT_ATTACK_ANIMATION,
                blockAnimation = DEFAULT_BLOCK_ANIMATION,
                deathAnimation = listOf(DEFAULT_DEATH_ANIMATION),
                attackSound = DEFAULT_ATTACK_SOUND,
                blockSound = DEFAULT_BLOCK_SOUND,
                deathSound = DEFAULT_DEATH_SOUND,
                respawnDelay = DEFAULT_RESPAWN_DELAY, poisonChance = 0.0,
                venomChance = 0.0, poisonImmunity = false, venomImmunity = false,
                slayerReq = 1, slayerXp = 0.0, bonuses = emptyList(), species = emptySet())
    }
}