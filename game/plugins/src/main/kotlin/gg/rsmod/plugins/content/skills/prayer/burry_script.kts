package gg.rsmod.plugins.content.skills.prayer

/**
 * @author Misterbaho <MisterBaho#6447>
 */

Bones.values.forEach { bones ->
    on_item_option(item = bones.item, option = "bury") {
        player.queue {
            Prayer.burying(this, bones)
        }
    }
}