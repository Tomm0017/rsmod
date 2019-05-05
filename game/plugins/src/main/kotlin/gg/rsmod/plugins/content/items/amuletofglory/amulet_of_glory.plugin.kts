package gg.rsmod.plugins.content.items.amuletofglory

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.teleport

val GLORY = intArrayOf(
        Items.AMULET_OF_GLORY1, Items.AMULET_OF_GLORY2, Items.AMULET_OF_GLORY3,
        Items.AMULET_OF_GLORY4, Items.AMULET_OF_GLORY5, Items.AMULET_OF_GLORY6
)

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 5
private val SOUNDAREA_VOLUME = 1

private val LOCATIONS = mapOf(
        "Edgeville" to Tile(3086, 3500),
        "Karamja" to Tile(2917, 3176),
        "Draynor Village" to Tile(3103, 3249),
        "Al kharid" to Tile(3292, 3165)
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
    if (canTeleport(TeleportType.GLORY) && hasEquipped(EquipmentType.AMULET, *GLORY)) {
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
        equipment[EquipmentType.AMULET.id] = getAmuletReplacement()
        message(getAmuletChargeMessage())
        teleport(endTile, TeleportType.GLORY)
    }
}

fun Player.getAmuletReplacement(): Item ? {
    return when {
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY6) -> Item(Items.AMULET_OF_GLORY5)
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> Item(Items.AMULET_OF_GLORY4)
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> Item(Items.AMULET_OF_GLORY3)
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> Item(Items.AMULET_OF_GLORY2)
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> Item(Items.AMULET_OF_GLORY1)
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> Item(Items.AMULET_OF_GLORY)
        else -> null
    }
}

fun Player.getAmuletChargeMessage(): String {
    return when {
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY5) -> "<col=7f007f>Your amulet has five charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY4) -> "<col=7f007f>Your amulet has four charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY3) -> "<col=7f007f>Your amulet has three charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY2) -> "<col=7f007f>Your amulet has two charges left.</col>"
        hasEquipped(EquipmentType.AMULET, Items.AMULET_OF_GLORY1) -> "<col=7f007f>Your amulet has one charge left.</col>"
        else -> "<col=7f007f>You use your amulet's last charge.</col>"
    }
}
