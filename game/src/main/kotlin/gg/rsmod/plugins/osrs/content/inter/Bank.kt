package gg.rsmod.plugins.osrs.content.inter

import gg.rsmod.game.model.item.Item
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.getInteractingSlot
import gg.rsmod.plugins.player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Bank {

    private const val INV_INTERFACE_ID = 15
    private const val INV_INTERFAC_CHILD = 3

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindButton(parent = INV_INTERFACE_ID, child = INV_INTERFAC_CHILD) {
            val p = it.player()

            val slot = it.getInteractingSlot()
            val item = p.inventory[slot] ?: return@bindButton
            val copyItem = Item(item)

            if (p.inventory.remove(item.id, item.amount, beginSlot = slot).hasSucceeded()) {
                p.bank.add(copyItem.id, copyItem.amount).items.first().copyAttr(copyItem)
            }
        }
    }
}