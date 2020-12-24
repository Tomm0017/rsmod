package gg.rsmod.plugins.content.inter.follower

import gg.rsmod.plugins.api.EquipmentType.Companion.EQUIPMENT_INTERFACE_ID

on_button(interfaceId = EQUIPMENT_INTERFACE_ID, component = 7) {
    player.message("You do not have a follower.")
}