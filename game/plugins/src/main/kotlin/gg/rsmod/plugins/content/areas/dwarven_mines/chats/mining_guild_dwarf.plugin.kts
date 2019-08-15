package gg.rsmod.plugins.content.areas.dwarven_mines.chats

spawn_npc(npc = Npcs.DWARF_7712, x = 3045, z = 9758, walkRadius = 3)

on_npc_option(Npcs.DWARF_7712, option = "talk-to") {
    player.queue { dialog(this) }
}

suspend fun dialog(it: QueueTask) {
    it.chatNpc("Welcome to the Mining Guild.<br><br>Can I help you with anything?")
    when (it.options("What have you got in the Guild?", "What do you dwarves do with the ore you mine?", "No thanks, I'm fine.")) {
        1 -> whatsInThere(it)
        2 -> oreYouMine(it)
        3 -> it.chatPlayer("No thanks, I'm fine.")
    }
}

suspend fun whatsInThere (it: QueueTask) {
    it.chatPlayer("What have you got in the guild?")
    it.chatNpc("All sorts of things!<br>There's plenty of coal rocks along with some iron,<br>mithril and adamantite as well.")
    it.chatNpc("There's no better mining site anywhere!")
    when (it.options("What do you dwarves do with the ore you mine?", "No thanks, I'm fine.")) {
        1 -> oreYouMine(it)
        2 -> it.chatPlayer("No thanks, I'm fine.")
    }
}

suspend fun oreYouMine (it: QueueTask) {
    it.chatPlayer("What do you dwarves do with the ore you mine?")
    it.chatNpc("What do you think? We smelt it into bars, smith the<br>metal to make armour and weapons, then we exchange<br>them for goods and services.")
    it.chatPlayer("I don't see many dwarves<br><br>selling armour or weapons here.")
    it.chatNpc("No, this is only a mining outpost. We dwarves don't<br>much like to settle in human cities. Most of the ore is<br>carted off to keldagrim, the great dwarven city.<br>They've got a special blast furnace up there - it makes")
    it.chatNpc("smelting the ore so much easier. There are plenty of<br>dwarven traders working in keldagrim. Anyway, can I<br>help you with anything else?")
    when (it.options("What have you go in the guild?", "No thanks, I'm fine")) {
        1 -> whatsInThere(it)
        2 -> it.chatPlayer("No thanks, I'm fine.")
    }
}