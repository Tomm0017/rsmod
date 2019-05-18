package gg.rsmod.plugins.content.items.teletabs

import gg.rsmod.plugins.content.magic.TeleportType
import gg.rsmod.plugins.content.magic.canTeleport
import gg.rsmod.plugins.content.magic.prepareForTeleport

private val LOCATIONS = mapOf(
        Items.VARROCK_TELEPORT to Area(3210, 3423, 3216, 3425),
        Items.FALADOR_TELEPORT to Area(2961, 3376, 2969, 3385),
        Items.LUMBRIDGE_TELEPORT to Area(3221, 3218, 3222, 3219),
        Items.CAMELOT_TELEPORT to Area(2756, 3476, 2758, 3480),
        Items.ARDOUGNE_TELEPORT to Area(2659, 3300, 2665, 3310),
        Items.WATCHTOWER_TELEPORT to Area(2551, 3113, 2553, 3116),
        Items.RIMMINGTON_TELEPORT to Area(2953, 3222, 2956, 3226),
        Items.TAVERLEY_TELEPORT to Area(2893, 3463, 2894, 3467),
        Items.POLLNIVNEACH_TELEPORT to Area(3338, 3003, 3342, 3004),
        Items.HOSIDIUS_TELEPORT to Area(1742, 3515, 1743, 3518),
        Items.RELLEKKA_TELEPORT to Area(2668, 3631, 2671, 3632),
        Items.BRIMHAVEN_TELEPORT to Area(2757, 3176, 2758, 3179),
        Items.YANILLE_TELEPORT to Area(2542, 3095, 2545, 3096),
        Items.TROLLHEIM_TELEPORT to Area(2888, 3678, 2893, 3681)
)

LOCATIONS.forEach { item, endTile ->
    on_item_option(item = item, option = "break") {
        player.queue(TaskPriority.STRONG) {
            player.teleport(this, endTile, item)
        }
    }
}

suspend fun Player.teleport(it : QueueTask, endArea : Area, tab : Int) {
    if (canTeleport(TeleportType.MODERN) && inventory.contains(tab)) {
        inventory.remove(item = tab)
        prepareForTeleport()
        lock = LockState.FULL_WITH_DAMAGE_IMMUNITY
        animate(id = 4069, delay = 16)
        playSound(id = 965, volume = 1, delay = 15)
        it.wait(cycles = 3)
        graphic(id = 678)
        animate(id = 4071)
        it.wait(cycles = 2)
        animate(id = -1)
        unlock()
        moveTo(tile = endArea.randomTile)
    }
}