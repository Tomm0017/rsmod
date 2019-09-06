package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(Npcs.DOOMSAYER, 3232,3223,0, 0, Direction.EAST)

on_npc_option(npc = Npcs.DOOMSAYER, option = "talk to") {
    player.queue { dialog() }
}

suspend fun QueueTask.dialog() {
    chatNpc("Dooooom!", animation = 588)
    chatPlayer("Where?")
    chatNpc("All around us! I can feel it in the air, hear it on the<br><br>wind, smell it... also in the air!")
    chatPlayer("Is there anything we can do about this doom?")
    chatNpc("There is nothing you need to do my firend! I am the<br>Doomsayer, although my real title could be something<br>like the Danger Tutor.")
    chatPlayer("Danger Tutor?")
    chatNpc("Yes! I roam the world sensing danger.")
    chatNpc("if I find a dangerous area, then I put up warning<br>signs that will tell you what is so dangerous about that<br>area.")
    chatNpc("If you see the signs often enough, then you can turn<br>them off; by that time you likely know what the area<br> has in store for you.")
    chatPlayer("But what if I want to see the warnings again?")
    chatNpc("That's why im waiting here!")
    chatNpc("If you want to see the warning messages again, I can<br><br>turn them back on for you.")
    /*if (player.warning == 0) {
        chatPlayer("Thanks, I'll remember that if I see any warning<br><br>messages.")
        chatNpc("You're welcome!")
    } else */
    chatNpc("Do you need to turn on any warnings right now?")
    when (options("Yes, I do.", "Not right now.")) {
        1 -> {
            chatPlayer("Yes, I do.")
            player.openInterface(interfaceId = 583, dest = InterfaceDestination.MAIN_SCREEN)
        }
        2 -> {
            chatPlayer("Not right now.")
            chatNpc("Ok, keep an eye out for the messages though!")
            chatPlayer("I will.")
        }
    }
}

/*0 starting 7 Disabled 6 Enabled
if 6 "You have toggled this warning screen on. You will see this interface again."
if 7 "You have toggles this warning screen off. You will no longer see this warning screen"
Varbits value to what it does
3851 is Dagannoth Kings' ladder
3852 is Contact Dungeon Ladder
3853 is Falador Mole Lair
3854 is Stronghold of Security Ladders
3855 is Player-Owned Houses
3856 is Dropped Items in Random Events
3857 is Wilderness Ditch
3858 is Trollheim Wilderness Entrance
3859 is Observatory Stairs
3860 is Shantay Pass
3861 is Icy Path Area
3862 is Watchtower Shaman Cave
3863 is Lumbridge Swamp Cave Rope
3864 is H.A.M Tunnel from Mill
3865 is Fairy Ring to Dorgesh-Raan
3866 is Lumbridge Cellar
3867 is Elid Genie Cave
3868 is Dorgesh-kaan Tunnel to Kalphites
3869 is Dorgesh-kaan City Exit
3870 is Mort-Myre
3871 is Ranging Guild Towner
3872 is Death Plateau*/