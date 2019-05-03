package gg.rsmod.plugins.content.items.ringofdueling

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport

val RING_OF_DUELING = arrayOf(
        Items.RING_OF_DUELING8, Items.RING_OF_DUELING7, Items.RING_OF_DUELING6,
        Items.RING_OF_DUELING5, Items.RING_OF_DUELING4, Items.RING_OF_DUELING3,
        Items.RING_OF_DUELING2, Items.RING_OF_DUELING1
)

RING_OF_DUELING.forEach { duel ->
    on_equipment_option(duel, option = "Duel Arena") {
        player.queue {
            teleport(Tile(x = 3308, z = 3234, height = 0)) //Coordinates of duel arena
        }
    }

    on_equipment_option(duel, option = "Castle Wars") {
        player.queue {
            teleport(Tile(x = 2440, z = 3089, height = 0)) //Coordinates of castle wars
        }
    }

    on_equipment_option(duel, option = "Clan Wars") {
        player.queue {
            teleport(Tile(x = 3370, z = 3161, height = 0)) //Coordinates of clan wars
        }
    }
}

fun QueueTask.teleport(tile : Tile) {
    if (player.canTeleport(TeleportType.MODERN)) {
        world.spawn(AreaSound(tile, id = 200, radius = 10, volume = 1))
        player.teleport(tile, TeleportType.MODERN)
        val delete = player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING1)
        if (delete) {
            player.equipment.remove(Items.RING_OF_DUELING1)
            player.message("<col=7f007f>Your ring has no charges left.</col>")
            return
        }
        player.equipment[EquipmentType.RING.id] = Item(set(player))
        player.message(message(player))
    }
}

fun set(player: Player): Int {
    return when {
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING8) -> Items.RING_OF_DUELING7
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING7) -> Items.RING_OF_DUELING6
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING6) -> Items.RING_OF_DUELING5
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING5) -> Items.RING_OF_DUELING4
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING4) -> Items.RING_OF_DUELING3
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING3) -> Items.RING_OF_DUELING2
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING2) -> Items.RING_OF_DUELING1
        else -> Items.RING_OF_DUELING1
    }
}

fun message(player: Player): String {
    return when {
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING8) -> "<col=7f007f>Your ring has seven charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING7) -> "<col=7f007f>Your ring has seven charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING6) -> "<col=7f007f>Your ring has fix charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING5) -> "<col=7f007f>Your ring has five charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING4) -> "<col=7f007f>Your ring has four charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING3) -> "<col=7f007f>Your ring has three charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING2) -> "<col=7f007f>Your ring has two charges left.</col>"
        player.hasEquipped(EquipmentType.RING, Items.RING_OF_DUELING1) -> "<col=7f007f>Your ring has one charge left.</col>"
        else -> "<col=7f007f>Your ring has no charges left.</col>"
    }
}

fun Pawn.teleport(tile : Tile, teleportType: TeleportType) {
    queue(TaskPriority.STRONG) {
        resetInteractions()
        clearHits()

        lock = LockState.FULL_WITH_DAMAGE_IMMUNITY

        animate(teleportType.animation)
        teleportType.graphic?.let {
            graphic(it)
        }

        wait(teleportType.teleportDelay)

        moveTo(tile)

        teleportType.endAnimation?.let {
            animate(it)
        }

        teleportType.endGraphic?.let {
            graphic(it)
        }

        teleportType.endAnimation?.let {
            val def = world.definitions.get(AnimDef::class.java, it)
            wait(def.cycleLength)
        }

        animate(-1)
        unlock()
    }
}
