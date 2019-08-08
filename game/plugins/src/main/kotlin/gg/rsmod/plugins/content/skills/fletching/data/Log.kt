package gg.rsmod.plugins.content.skills.fletching.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author momof513
 *
 * Represents a log that can be whittled into items
 *
 * @param id                The id of the log
 * @param whittleOptions  An array of WhittleItems that can be made from the log
 */
enum class Log(val id: Int, val whittleOptions: Array<WhittleItem>) {
    LOGS(id=Items.LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_15, WhittleItem.JAVELIN_SHAFT, WhittleItem.SHORTBOW_U, WhittleItem.LONGBOW_U, WhittleItem.WOODEN_STOCK)),
    OAK(id=Items.OAK_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_30, WhittleItem.OAK_SHORTBOW_U, WhittleItem.OAK_LONGBOW_U, WhittleItem.OAK_STOCK, WhittleItem.OAK_SHIELD)),
    ACHEY(id=Items.ACHEY_TREE_LOGS, whittleOptions = arrayOf(WhittleItem.OGRE_ARROW_SHAFT, WhittleItem.UNSTRUNG_COMP_BOW)), // This does not include ogre arrow shafts
    WILLOW(id=Items.WILLOW_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_45, WhittleItem.WILLOW_SHORTBOW_U, WhittleItem.WILLOW_LONGBOW_U, WhittleItem.WILLOW_STOCK, WhittleItem.WILLOW_SHIELD)),
    TEAK(id=Items.TEAK_LOGS, whittleOptions = arrayOf(WhittleItem.TEAK_STOCK)),
    MAPLE(id=Items.MAPLE_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_60, WhittleItem.MAPLE_SHORTBOW_U, WhittleItem.MAPLE_LONGBOW_U, WhittleItem.MAPLE_STOCK, WhittleItem.MAPLE_SHIELD)),
    MAHOGANY(id=Items.MAHOGANY_LOGS, whittleOptions = arrayOf(WhittleItem.MAHOGANY_STOCK)),
    YEW(id=Items.YEW_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_75, WhittleItem.YEW_SHORTBOW_U, WhittleItem.YEW_LONGBOW_U, WhittleItem.YEW_STOCK, WhittleItem.YEW_SHIELD)),
    MAGIC(id=Items.MAGIC_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_90, WhittleItem.MAGIC_SHORTBOW_U, WhittleItem.MAGIC_LONGBOW_U, WhittleItem.MAGIC_STOCK, WhittleItem.MAGIC_SHIELD)),
    REDWOOD(id=Items.REDWOOD_LOGS, whittleOptions = arrayOf(WhittleItem.ARROW_SHAFT_105, WhittleItem.REDWOOD_SHIELD));

    companion object {
        /**
         * The map of log ids to a map of whittleItem ids to their definitions
         */
        val logDefinitions = values().associate { log ->
            log.id to log.whittleOptions.associate { whittleOption ->
                whittleOption.id to whittleOption
            }
        }
    }
}