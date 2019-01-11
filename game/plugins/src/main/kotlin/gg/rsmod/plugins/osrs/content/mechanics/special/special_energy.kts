import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.content.mechanics.special.SpecialEnergy
import gg.rsmod.plugins.osrs.api.player
import gg.rsmod.plugins.osrs.api.setVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */

r.bindEquipSlot(EquipmentType.WEAPON.id) {
    it.player().setVarp(SpecialEnergy.ENABLED_VARP, 0)
}