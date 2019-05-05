package gg.rsmod.plugins.content.magic

import gg.rsmod.game.model.Graphic

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class TeleportType(val teleportDelay: Int, val animation: Int, val endAnimation: Int? = null, val graphic: Graphic? = null,
                        val endGraphic: Graphic? = null, val wildLvlRestriction: Int = 20) {
    MODERN(teleportDelay = 4, animation = 714, graphic = Graphic(111, 92)),
    GLORY(teleportDelay = 4, animation = 714, graphic = Graphic(111, 92), wildLvlRestriction = 30),
    ANCIENT(teleportDelay = 5, animation = 1979, graphic = Graphic(392, 0)),
    LUNAR(teleportDelay = 4, animation = 1816, graphic = Graphic(747, 120)),
    ARCEUUS(teleportDelay = 4, animation = 1816, graphic = Graphic(747, 120))
}