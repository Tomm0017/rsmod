package gg.rsmod.game.model

/**
 * Represents a movement step.
 *
 * @param primary
 * The walk direction of the step.
 *
 * @param secondary
 * The run direction of the step.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Step(val primary: Int, val secondary: Int)