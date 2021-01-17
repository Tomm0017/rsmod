package gg.rsmod.plugins.content.skills.herblore.pots

Pots.values().forEach { p ->
    on_item_on_item(p.pot.secondary, p.pot.unfinished) {
        val max = player.maxPossible(p.pot.secondary, p.pot.unfinished)
        player.produceItemBoxMessage(p.pot.finished[2], max = max, growingDelay = true){
            player.queue {
                p.pot.make(player)
            }
        }
    }
}
