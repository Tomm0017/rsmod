package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(Npcs.CRAFTING_TUTOR, 3211,3212,1,1, Direction.WEST)

on_npc_option(npc = Npcs.CRAFTING_TUTOR, option = "talk-to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatPlayer("Can you teach me the basics of crafting please?", animation = 554)
    chatNpc("Firstly, you should know that not all places associated<br>with crafting will be marked on your minimap. Some<br>take quite a bit of hunting down to find, don't lose<br>heart!", animation = 591)
    chatPlayer("I see... so where should I start?", animation = 554)
    chatNpc("I suggest you help Farmer Fred out with his sheep<br>shearing, you can find him northwest of Lumbridge at<br>his sheep farm, this will give you experience using the<br>spinning wheel here in this room.", animation = 570)
    chatNpc("It's fairly easy, simply gather your wool from sheep in<br>the field near Lumbridge using the shears sold in<br>general stores, then click on the spinning wheel and you<br>will be given a choice of what to spin. Right-clicking on", animation = 570)
    chatNpc("the choices will allow you to make multiple spins and<br>therefore save you time.", animation = 568)
    chatNpc("When you have a full inventory, take it to the bank,<br>you can find it on the roof of this very castle.", animation = 568)
    itemMessageBox("To find a bank, look for this symbol on your minimap<br>after climbing the stairs of the Lumbridge Castle to the<br>top. There are banks all over the world with this symbol.", item = 5080, amountOrZoom = 400)
}
