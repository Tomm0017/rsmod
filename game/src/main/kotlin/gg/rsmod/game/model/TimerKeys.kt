package gg.rsmod.game.model

/**
 * @author Tom <rspsmods@gmail.com>
 */

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