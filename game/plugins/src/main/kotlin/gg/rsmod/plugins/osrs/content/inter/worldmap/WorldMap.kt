package gg.rsmod.plugins.osrs.content.inter.worldmap

import gg.rsmod.game.model.AttributeKey
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerKey

/**
 * @author Tom <rspsmods@gmail.com>
 */
object WorldMap {

    val UPDATE_TIMER = TimerKey()
    val LAST_TILE = AttributeKey<Tile>()

    const val COMPONENT_ID = 595
    const val FULLSCREEN_COMPONENT_ID = 594
}