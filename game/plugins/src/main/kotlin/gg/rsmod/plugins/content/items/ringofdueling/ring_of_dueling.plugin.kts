package gg.rsmod.plugins.content.items.ringofdueling

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.teleport

val RING_OF_DUELING = intArrayOf(
        Items.RING_OF_DUELING8, Items.RING_OF_DUELING7, Items.RING_OF_DUELING6,
        Items.RING_OF_DUELING5, Items.RING_OF_DUELING4, Items.RING_OF_DUELING3,
        Items.RING_OF_DUELING2, Items.RING_OF_DUELING1
)

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 5
private val SOUNDAREA_VOLUME = 1

private val LOCATIONS = mapOf(
        "Duel Arena" to Tile(3308, 3234, 0),
        "Castle Wars" to Tile(2440, 3089, 0),
        "Ferox Enclave" to Tile(3370, 3161, 0)
)

RING_OF_DUELING.forEach { duel ->
    LOCATIONS.forEach { location, endTile ->
        on_equipment_option(duel, option = location) {
            player.queue(TaskPriority.STRONG) {
                player.teleport(endTile)
            }
        }
    }
}

fun Player.teleport(endTile : Tile) {
    if (canTeleport(TeleportType.MODERN) && hasEquipped(EquipmentType.RING, *RING_OF_DUELING)) {
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
        equipment[EquipmentType.RING.id] = getRingReplacement()
        teleport(endTile, TeleportType.MODERN)
        message(getRingChargeMessage())
    }
}

fun Player.getRingReplacement(): Item ? {
    return when {
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING8) -> Item(Items.RING_OF_DUELING7)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING7) -> Item(Items.RING_OF_DUELING6)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING6) -> Item(Items.RING_OF_DUELING5)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING5) -> Item(Items.RING_OF_DUELING4)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING4) -> Item(Items.RING_OF_DUELING3)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING3) -> Item(Items.RING_OF_DUELING2)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING2) -> Item(Items.RING_OF_DUELING1)
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING1) -> null
        else -> null
    }
}

fun Player.getRingChargeMessage(): String {
    return when {
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING7) -> "<col=7f007f>Your ring of dueling has seven uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING6) -> "<col=7f007f>Your ring of dueling has six uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING5) -> "<col=7f007f>Your ring of dueling has five uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING4) -> "<col=7f007f>Your ring of dueling has four uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING3) -> "<col=7f007f>Your ring of dueling has three uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING2) -> "<col=7f007f>Your ring of dueling has two uses left.</col>"
        hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING1) -> "<col=7f007f>Your ring of dueling has one use left.</col>"
        else -> "<col=7f007f>Your ring of dueling crumbles to dust.</col>"
    }
}
