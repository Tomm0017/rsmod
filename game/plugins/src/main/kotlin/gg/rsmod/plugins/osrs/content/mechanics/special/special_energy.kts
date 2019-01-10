import gg.rsmod.plugins.osrs.api.Equipment
import gg.rsmod.plugins.osrs.content.mechanics.special.SpecialEnergy
import gg.rsmod.plugins.player
import gg.rsmod.plugins.setVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */

r.bindEquipSlot(Equipment.WEAPON.id) {
    it.player().setVarp(SpecialEnergy.ENABLED_VARP, 0)
}