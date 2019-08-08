package gg.rsmod.plugins.content.skills.firemaking

Logs.values().forEach { log ->
    on_item_on_item(Items.TINDERBOX, log.log) {
        player.queue { Firemaking.burnLog(this, log) }
    }
}
