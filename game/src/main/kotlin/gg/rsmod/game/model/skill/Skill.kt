package gg.rsmod.game.model.skill

/**
 * Represents a trainable skill for a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Skill(val id: Int, var xp: Double = 0.0, var currentLevel: Int = 1)