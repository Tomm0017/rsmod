package gg.rsmod.game.model.timer

/**
 * A decoupled file that holds TimerKeys that require read-access from our
 * game module. Any timer keys that can be stored on the plugin classes themselves,
 * should do so. When storing them in a class, remember the TimerKey must be
 * a singleton, meaning it should only have a single state.
 *
 * @author Tom <rspsmods@gmail.com>
 */

/**
 * A timer for npcs to reset their pawn face attribute.
 */
internal val RESET_PAWN_FACING_TIMER = TimerKey()

/**
 * A timer for removing a skull icon.
 */
val SKULL_ICON_DURATION_TIMER = TimerKey()

/**
 * Timer key set when a pawn is attacked either in PvP or in PvM.
 */
val ACTIVE_COMBAT_TIMER = TimerKey()

/**
 * Timer key used to force a player disconnect, usually used so that if a
 * player's channel has been inactive (disconnected) for X amount of time,
 * we disconnect them so that they can play again.
 */
val FORCE_DISCONNECTION_TIMER = TimerKey()

/**
 * Timer key set when frozen.
 */
val FROZEN_TIMER = TimerKey()

/**
 * Timer key set when stunned.
 */
val STUN_TIMER = TimerKey()

/**
 * Timer key for poison ticks.
 */
val POISON_TIMER = TimerKey()

/**
 * Timer key for dragonfire protection ticking down.
 */
val ANTIFIRE_TIMER = TimerKey()

/**
 * Timer key for the delay in between a pawn's attack.
 */
val ATTACK_DELAY = TimerKey()

/**
 * Timer key for delay in between drinking potions.
 */
val POTION_DELAY = TimerKey()

/**
 * Timer key for delay in between eating food.
 */
val FOOD_DELAY = TimerKey()

/**
 * Timer key for delay in between eating "combo" food.
 */
val COMBO_FOOD_DELAY = TimerKey()