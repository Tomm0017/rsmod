package gg.rsmod.plugins.content.areas.alkharid.chat

    on_npc_option(npc = Npcs.ELLIS, option = "talk-to") {
        player.queue { dialog(this) }
    }

    on_npc_option(npc = Npcs.ELLIS, option = "trade") {
        player.setInterfaceUnderlay(-1, -2)
        player.openInterface(324, InterfaceDestination.MAIN_SCREEN)
    }

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Greetings friend. I am a manufacturer of leather.", animation = 567)
    if (it.player.inventory.getItemCount(Items.COWHIDE) >= 1) {
        it.chatNpc("I see you have brought me a hide. Would you like me to tan it for you?", animation = 567)
        when (it.options("Yes please.", "No thanks.")) {
            1 -> {
                it.chatPlayer("Yes please.", animation = 567)
                it.player.setInterfaceUnderlay(-1, -2)
                it.player.openInterface(324, InterfaceDestination.MAIN_SCREEN)
            }
            2 -> {
                it.chatPlayer("No thanks.", animation = 567)
                it.chatNpc("Very well, sir, as you wish.", animation = 567)
            }
        }
    } else
    when (it.options("Can I buy some leather then?", "Leather is rather weak stuff.")) {
        1 -> {
            it.chatNpc("Can I buy some leather then?", animation = 567)
            it.chatNpc("I make leather from animal hides. bring me some cowhides and one gold coin per hide, and I'll tan them into soft leather for you.", animation = 567)
        }
        2 -> {
            it.chatPlayer("Leather is rather weak stuff.", animation = 567)
            it.chatNpc("Normal leather may be quite weak, but it's very cheap I make it from cowhides for only 1 gp per hide and it's so easy to craft that anyone can work with it.", animation = 567)
            it.chatNpc("Alternatively you could try hard leather. It's not so easy to craft, but I only charge 3 gp per cowhide to prepare it, and it makes much sturdier armor.", animation = 567)
            it.chatNpc("I can also tan snake hides and dragonhides, suitable for crafting into highest quality armor for rangers.", animation = 567)
            it.chatPlayer("Thanks, I'll bear it in mind.", animation = 567)
        }
    }
}