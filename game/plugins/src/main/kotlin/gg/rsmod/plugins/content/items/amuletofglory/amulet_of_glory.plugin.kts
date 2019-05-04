package gg.rsmod.plugins.content.items.amuletofglory

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.teleport

val GLORY = intArrayOf(
        Items.AMULET_OF_GLORY1, Items.AMULET_OF_GLORY2, Items.AMULET_OF_GLORY3,
        Items.AMULET_OF_GLORY4, Items.AMULET_OF_GLORY5, Items.AMULET_OF_GLORY6
)

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 10
private val SOUNDAREA_VOLUME = 1

private val LOCATIONS = mapOf(
        "Edgeville" to Tile(3086, 3500, 0),
        "Karamja" to Tile(2917, 3176, 0),
        "Draynor Village" to Tile(3103, 3249, 0),
        "Al kharid" to Tile(3292, 3165, 0)
)

GLORY.forEach { glory ->
    LOCATIONS.forEach { location, tile ->
        on_equipment_option(glory, option = location) {
            player.queue(TaskPriority.STRONG) {
                player.teleport(tile)
            }
        }
    }
}


fun Player.teleport(endTile : Tile) {
    if (canTeleport(TeleportType.GLORY)) {
        if (hasEquipped(EquipmentType.AMULET, *GLORY)) {
            world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
            equipment[EquipmentType.AMULET.id] = Item(set())
            message(message())
            teleport(endTile, TeleportType.GLORY)
        }
    }
}

fun Player.set(): Int {
    return when {
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY6) -> Items.AMULET_OF_GLORY5
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> Items.AMULET_OF_GLORY4
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> Items.AMULET_OF_GLORY3
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> Items.AMULET_OF_GLORY2
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> Items.AMULET_OF_GLORY1
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> Items.AMULET_OF_GLORY
        else -> Items.AMULET_OF_GLORY
    }
}

fun Player.message(): String {
    return when {
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> "<col=7f007f>Your amulet has five charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> "<col=7f007f>Your amulet has four charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> "<col=7f007f>Your amulet has three charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> "<col=7f007f>Your amulet has two charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> "<col=7f007f>Your amulet has one charge left.</col>"
        else -> "<col=7f007f>You use your amulet's last charge.</col>"
    }
}
