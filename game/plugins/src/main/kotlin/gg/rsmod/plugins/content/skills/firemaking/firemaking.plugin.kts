package gg.rsmod.plugins.content.skills.firemaking

import gg.rsmod.plugins.content.skills.firemaking.Firemaking.DEFAULT_FIRE
import gg.rsmod.plugins.content.skills.firemaking.Firemaking.DEFAULT_TINDERBOX
import gg.rsmod.plugins.content.skills.firemaking.Firemaking.Log

private val logs = setOf(
        Log(LogType.NORMAL, item = Items.LOGS, fire = DEFAULT_FIRE),
        Log(LogType.ACHEY, item = Items.ACHEY_TREE_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.OAK, item = Items.OAK_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.WILLOW, item = Items.WILLOW_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.TEAK, item = Items.TEAK_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.MAPLE, item = Items.MAPLE_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.MAHOGANY, item = Items.MAHOGANY_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.YEW, item = Items.YEW_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.MAGIC, item = Items.MAGIC_LOGS, fire = DEFAULT_FIRE),
        Log(LogType.REDWOOD, item = Items.REDWOOD_LOGS, fire = DEFAULT_FIRE)
)

logs.forEach { log ->

    on_item_on_item(item1 = log.item, item2 = DEFAULT_TINDERBOX){
        val logItem = player.getInteractingItemPair().toList().find { it.getName(player.world.definitions).contains("log", true) }?:return@on_item_on_item
        player.queue {
            Firemaking.lightLog(this, logItem, log.type, log.fire)
        }
    }

    on_ground_item_option(item = log.item, option = "Light") {
        val groundItem = player.getInteractingGroundItem()
        player.queue {
            Firemaking.lightLogOnGround(this, groundItem, log.type, log.fire)
        }
    }
}