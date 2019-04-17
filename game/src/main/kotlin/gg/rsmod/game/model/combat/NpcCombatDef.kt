package gg.rsmod.game.model.combat

/**
 * Represents the combat definition for an npc.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class NpcCombatDef(
        val hitpoints: Int, val stats: List<Int>, val attackSpeed: Int, val attackAnimation: Int,
        val blockAnimation: Int, val deathAnimation: List<Int>, val respawnDelay: Int,
        val aggressiveRadius: Int, val aggroTargetDelay: Int, val poisonChance: Double,
        val poisonImmunity: Boolean, val venomImmunity: Boolean, val slayerReq: Int, val slayerXp: Double,
        val bonuses: List<Int>, val species: Set<Any>) {

    companion object {
        private const val DEFAULT_HITPOINTS = 10
        private const val DEFAULT_ATTACK_SPEED = 4
        private const val DEFAULT_RESPAWN_DELAY = 25

        val DEFAULT = NpcCombatDef(
                hitpoints = DEFAULT_HITPOINTS, stats = listOf(1, 1, 1, 1, 1),
                attackSpeed = DEFAULT_ATTACK_SPEED, aggressiveRadius = 0,
                aggroTargetDelay = 0, attackAnimation = 422, blockAnimation = 424,
                deathAnimation = listOf(836), respawnDelay = DEFAULT_RESPAWN_DELAY,
                poisonChance = 0.0, poisonImmunity = false, venomImmunity = false,
                slayerReq = 1, slayerXp = 0.0, bonuses = emptyList(), species = emptySet())
    }
}