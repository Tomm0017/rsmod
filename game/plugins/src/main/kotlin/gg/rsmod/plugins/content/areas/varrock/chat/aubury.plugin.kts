package gg.rsmod.plugins.content.areas.varrock.chat

spawn_npc(Npcs.AUBURY, 3253, 3402, 0, 2)

arrayOf(Npcs.AUBURY).forEach { aubury ->
    on_npc_option(npc = aubury, option = "trade", lineOfSightDistance = 1) {
        open_shop(player)
    }
    on_npc_option(npc = aubury, option = "talk-to", lineOfSightDistance = 1) {
        player.queue {
            dialog(this)
        }
    }
    on_npc_option(npc = aubury, option = "teleport", lineOfSightDistance = 1) {

        player.queue {
            val npc = player.getInteractingNpc()
            player.lock = LockState.FULL
            npc.forceChat("Senventior Disthine Molenko")
            npc.graphic(108, 10)
            wait(3)
            player.graphic(110, 125)
            wait(2)
            player.moveTo(2912, 4833, 0)
            player.lock = LockState.NONE
        }
    }
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Do you want to buy some runes?")
    when (options(it)) {
        1 -> about_your_cape(it)
        2 -> open_shop(it.player)
        3 -> no_thank_you(it)
        4 -> teleport_me(it)
    }
}

suspend fun options(it: QueueTask): Int = it.options("Can you tell me about your cape?", "Yes please!", "Oh, it's a rune shop. No thank you then.", "Can you teleport me to the Rune Essence?")

suspend fun about_your_cape(it: QueueTask) {
    it.chatNpc("Certainly! Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.", animation = 568)
    it.chatNpc("The Cape of Runescape has been upgraded with each talisman, allowing you to access all Runecrafting altars. Is there anything else I can help you with?", animation = 554)
}

fun open_shop(p: Player) {
    p.openShop("Aubury's Rune Shop.")
}

suspend fun no_thank_you(it: QueueTask) {
    it.chatPlayer("Oh, it's a rune shop. No thank you, then.", animation = 568)
    it.chatNpc("Well, if you find someone who does want runes, please send them my way.", animation = 554)
}

suspend fun teleport_me(it: QueueTask) {
    it.chatPlayer("Can you teleport me to the Rune Essence?", animation = 568)
}