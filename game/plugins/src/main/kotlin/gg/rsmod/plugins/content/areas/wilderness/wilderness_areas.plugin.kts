package gg.rsmod.plugins.content.areas.wilderness

val CHECK_TIMER = TimerKey()
val WAS_IN_WILD = AttributeKey<Boolean>()

val INTERFACE_ID = 90

/**
 * The component that draws a 'cross' (or x) through the skull interface
 * that signifies the player is in a pvp-area but cannot engage in pvp.
 */
val SKULL_CROSSOUT_COMPONENT = 63
val SKULL_COMPONENT = 62

on_login {
    player.timers[CHECK_TIMER] = 1
}

on_timer(CHECK_TIMER) {
    player.timers[CHECK_TIMER] = 1

    if (in_wilderness(player.tile)) {
        val inWild = player.attr[WAS_IN_WILD] ?: false
        if (!inWild) {
            set_in_wild(player, true)
        }
    } else {
        val inWild = player.attr[WAS_IN_WILD] ?: false
        if (inWild) {
            set_in_wild(player, false)
        }
    }
}

fun set_in_wild(player: Player, inWilderness: Boolean) {
    player.attr[WAS_IN_WILD] = inWilderness
    if (inWilderness) {
        player.openInterface(dest = InterfaceDestination.PVP_OVERLAY, interfaceId = INTERFACE_ID)
        player.sendOption("Attack", 2)
    } else {
        player.closeInterface(dest = InterfaceDestination.PVP_OVERLAY)
        player.removeOption(2)
    }
    player.setComponentHidden(interfaceId = INTERFACE_ID, component = SKULL_CROSSOUT_COMPONENT, hidden = inWilderness)
}

fun in_wilderness(tile: Tile): Boolean = tile.getWildernessLevel() > 0