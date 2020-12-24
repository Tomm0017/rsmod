package gg.rsmod.plugins.content.inter.bank

import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Player.openBank() {
    Bank.open(this)
}