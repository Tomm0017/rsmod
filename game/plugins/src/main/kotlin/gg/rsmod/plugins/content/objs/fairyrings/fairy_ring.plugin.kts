package gg.rsmod.plugins.content.objs.fairyrings

/**
 * @author DaGreenRs <dagreenrs@gmail.com>
 * https://oldschool.runescape.wiki/w/Fairy_rings
 */

for (objectid in 29496 until 29560) {
    on_obj_option(objectid, "Zanaris") {
        if (checkFairyStaff(player)) {
            fairyRingTeleAction(player, Tile(2412, 4434, 0))
        }
    }
    on_obj_option(objectid, "Configure") {
        if (checkFairyStaff(player)) {
            player.setVarbit(3985, 0)
            player.setVarbit(3986, 0)
            player.setVarbit(3987, 0)
            sendTravelLog(player)
            player.openInterface(398, InterfaceDestination.MAIN_SCREEN)
            player.openInterface(381, InterfaceDestination.TAB_AREA)
        }
    }
    on_obj_option(objectid, 3) {
        if (checkFairyStaff(player)) {
            LastDesination(player)
        }
    }
}

for (ZanarisRing in 29561 until 29624) {
    on_obj_option(ZanarisRing, "Configure") {
        if (checkFairyStaff(player)) {
            player.setVarbit(3985, 0)
            player.setVarbit(3986, 0)
            player.setVarbit(3987, 0)
            sendTravelLog(player)
            player.openInterface(398, InterfaceDestination.MAIN_SCREEN)
            player.openInterface(381, InterfaceDestination.TAB_AREA)
        }
    }
    on_obj_option(ZanarisRing, 3) {
        if (checkFairyStaff(player)) {
            LastDesination(player)
        }
    }
}

on_button(398, 19) {
    player.setVarbit(3985, player.getVarbit(3985).plus(1))
}

on_button(398, 20) {
    player.setVarbit(3985, player.getVarbit(3985).minus(1))
}

on_button(398, 21) {
    player.setVarbit(3986, player.getVarbit(3986).plus(1))
}

on_button(398, 22) {
    player.setVarbit(3986, player.getVarbit(3986).minus(1))
}

on_button(398, 23) {
    player.setVarbit(3987, player.getVarbit(3987).plus(1))
}

on_button(398, 24) {
    player.setVarbit(3987, player.getVarbit(3987).minus(1))
}

on_button(398, 26) {
    teleportToFairyRingLocation(player)
}

class Locations(val name: String, val component: Int, val dial1: Int, val dial2: Int, val dial3: Int)
val FairyRings = arrayOf(
        Locations("Mudskipper Point", 13, 0, 0, 3),
        Locations("Island: Ardougne", 15, 0, 0, 2),
        Locations("Dungeons: Dorgesh-Kaan", 21, 0, 3, 3),
        Locations("Fremennik Slayer Cave", 23, 0, 3, 2),
        Locations("Penguins", 25, 0, 3, 1),
        Locations("Piscatoris", 29, 0, 2,3),
        Locations("Feldip Hills", 33, 0, 2, 1),
        Locations("Light House", 35, 0, 1, 0),
        Locations("Haunted Woods", 37, 0, 1, 3),
        Locations("Abyssal Area", 39, 0, 1, 2),
        Locations("McGrubor's Wood", 41, 0, 1, 1),
        Locations("River Slave", 43, 3, 0, 0),
        Locations("Kalphite Hive", 45, 3, 0, 3),
        Locations("Ardougne Zoo", 49, 3, 0 , 1),
        Locations("Fisher King", 55, 3, 3, 2),
        Locations("Zulrah Andra", 57, 3, 3, 1),
        Locations("Castle Wars", 59, 3, 2, 0),
        Locations("Enchanted Valley", 61, 3, 2, 3),
        Locations("Mort Myre Swamp", 63, 3, 2, 2),
        Locations("Zanaris", 65, 3, 2, 1),
        Locations("TzHaar Area", 67, 3, 1, 0),
        Locations("Legends Guild", 71, 3, 1 , 2),
        Locations("Miscellania", 75, 2,0,0),
        Locations("Yanille", 77, 2,0,3),
        Locations("Mount Karuulm", 79, 2,0,2),
        Locations("Arceuus Library", 81, 2,0,1),
        Locations("Sinclair Mansion", 87, 2,3,2),
        Locations("Cosmic Entity's plane", 91, 2,2,0),
        Locations("Tai Bwo Wannai Village", 95, 2,2,2),
        Locations("Canifis", 97, 2,2,1),
        Locations("Islands: Draynor", 99, 2,1,0),
        Locations("Ape Atoll", 103, 2,1,2),
        Locations("Hazelmere's Home", 105, 2,1,1),
        Locations("Abyssal Nexus", 107, 1,0,0),
        Locations("player Home", 109, 1,0,3),
        Locations("Gorak's Plane", 111, 1,0,2),
        Locations("Wizards' Tower", 113, 1,0,1),
        Locations("Tower of Life", 115, 1,3,0),
        Locations("Chasm of Fire", 119, 1,3,2),
        Locations("Musa Point", 123, 1,2,0),
        Locations("Edgeville", 127, 1,2,2),
        Locations("Polar Hunter area", 129, 1,2,1),
        Locations("Kahaidian Desert", 133, 1,1,3),
        Locations("Poison Waste", 135, 1,1,2),
        Locations("Myreque Hideout", 137, 1,1,1)
)

FairyRings.forEach {
    on_button(381, it.component) {
        player.setVarbit(3985, it.dial1)
        player.setVarbit(3986, it.dial2)
        player.setVarbit(3987, it.dial3)
    }
}

fun checkFairyStaff(player: Player) : Boolean {
    if (!player.equipment.contains(Items.DRAMEN_STAFF) || player.equipment.contains(Items.LUNAR_STAFF)) {
        player.message("The fairy ring only works for those who wield fairy magic.")
        return false
    }
    return true
}

fun fairyRingTeleAction(player: Player, endTile : Tile) {
    player.queue(TaskPriority.STRONG) {
        player.lock = LockState.DELAY_ACTIONS
        player.walkTo(player.getInteractingGameObj().tile, MovementQueue.StepType.FORCED_WALK, detectCollision = false)
        wait(2)
        player.closeInterfaces()
        player.playSound(1098)
        player.graphic(569)
        player.animate(3265, 30)
        wait(3)
        player.moveTo(Tile(endTile))
        player.animate(3266)
        wait(2)
        player.walkTo(player.tile.x, player.tile.z -1, MovementQueue.StepType.FORCED_WALK, detectCollision = false)
        player.unlock()
    }
}

fun Player.closeInterfaces() {
    closeInterface(InterfaceDestination.MAIN_SCREEN)
    closeInterface(InterfaceDestination.TAB_AREA)
}

fun sendTravelLog(player: Player) {
    player.setComponentText(381,13, "<br>Asgarnia: Mudskipper Point")
    player.setComponentText(381,15, "<br>Islands: South-east of Ardougne")
    player.setComponentText(381,21, "<br>Dungeons: Cave south of Dorgesh-Kaan")
    player.setComponentText(381,23, "<br>Fremennik Province: Fremennik Slayer Cave")
    player.setComponentText(381,25, "<br>Islands: Penguins")
    player.setComponentText(381,29, "<br>Kandarin: Piscatoris Hunter area")
    player.setComponentText(381,33, "<br>Feldip Hills: Jungle Hunter area")
    player.setComponentText(381,35, "<br>Fremennik Province: Lighthouse")
    player.setComponentText(381,37, "<br>Morytania: Haunted Woods")
    player.setComponentText(381,39, "<br>Other Realms: Abyss")
    player.setComponentText(381,41, "<br>Kandarin: McGurbor's Wood")
    player.setComponentText(381,43, "<br>Islands: River Salve")
    player.setComponentText(381,45, "<br>Kahridian Desert: Near the Kalphite Hive")
    player.setComponentText(381,49, "<br>Kandarin: Ardougne Zoo unicorns")
    player.setComponentText(381,55, "<br>Other Realms: Fisher King")
    player.setComponentText(381,57, "<br>Islands: Near Zul-Andra")
    player.setComponentText(381,59, "<br>Feldip Hills: South of Castle Wars")
    player.setComponentText(381,61, "<br>Other Realms: Enchanted Valley")
    player.setComponentText(381,63, "<br>Morytania: Mort Myre")
    player.setComponentText(381,65, "<br>Other Realms: Zanaris")
    player.setComponentText(381,67, "<br>Dungeons: TzHarr area")
    player.setComponentText(381,71, "<br>Kandarin: Legends' Guild")
    player.setComponentText(381,75, "<br>Islands: Miscellania")
    player.setComponentText(381,77, "<br>Kandarin: North-west of Yanille")
    player.setComponentText(381,79, "<br>Kebos Lowlands: South of Mount Karuulm")
    player.setComponentText(381,81, "<br>Great Kourend: Arceuus Library")
    player.setComponentText(381,87, "<br>Kandarin: Sinclair Mansion")
    player.setComponentText(381,91, "<br>Other Realms: Cosmic Entity's plane")
    player.setComponentText(381,95, "<br>Karamja: Tai Bwo Wannai Village")
    player.setComponentText(381,97, "<br>Morytania: Canifis")
    player.setComponentText(381,99, "<br>Islands: South of Draynor Village")
    player.setComponentText(381,103, "<br>Islands: Ape Atoll")
    player.setComponentText(381,105, "<br>Kandarin: Hazelmere's home")
    player.setComponentText(381,107, "<br>Other Realms: Abyssal Nexus")
    player.setComponentText(381,109, "<br>Player-owned house Superior Garden")
    player.setComponentText(381,111, "<br>Other Realms: Goraks' plane")
    player.setComponentText(381,113, "<br>Misthalin: Wizards' Tower")
    player.setComponentText(381,115, "<br>Kandarin: Tower of Life")
    player.setComponentText(381,119, "<br>Great Kourend: Chasm of Fire")
    player.setComponentText(381,123, "<br>Karamja: Gnome Glider")
    player.setComponentText(381,127, "<br>Mistalin: Edgeville")
    player.setComponentText(381,129, "<br>Fremennik Province: Polar Hunter area")
    player.setComponentText(381,133, "<br>Kharidian Desert: North of Nardah")
    player.setComponentText(381,135, "<br>Islands: Poison Waste")
    player.setComponentText(381,137, "<br>Dungeons: Myreque hideout under The Hollows")
    player.setComponentText(381,139, "<col=ffffff>AIR</col> DLR DJQ AJS<br><col=ff981f>Fairy Queen's Hideout</col>")
    /*liked travel locations
    player.setComponentText(381,140, "")
    player.setComponentText(381,144, "")
    player.setComponentText(381,141, "")
    player.setComponentText(381,145, "")
    player.setComponentText(381,142, "")
    player.setComponentText(381,146, "")
    player.setComponentText(381,143, "")
    player.setComponentText(381,147, "")*/
}

fun teleportToFairyRingLocation(player: Player) {
    when {
        //MUDSKIPPER POINT
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2996, 3114))
        //South-east of Ardougne
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2700, 3247))
        //Dungeons: Cave south of Dorgesh-Kaan
        //player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2735, 5221))
        //Kandarin: Slayer cave south-east of Rellekka
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2780, 3613))
        //Islands: Penguins near Miscellania.
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2500, 3896))
        //Kandarin: Piscatoris Hunter area
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2319, 3619))
        //Feldip Hills: Jungle Hunter area
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2571, 2956))
        //Fremennik Province: Lighthouse
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2503, 3636))
        //Morytania: Haunted Woods
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3597, 3495))
        //Other Realms: Abyss
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3059, 4875))
        //Kandarin: McGurbor's Wood
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2644, 3495))
        //Islands: River Slave
        //player.getVarbit(3985) == 3 && player.getVarbit(2986) == 0 && player.getVarbit(3987) == 0 && player.getVarbit(5374) == 3 -> fairyRingTeleAction(player, Tile(3410, 3324))
        //Kalhridian Desert: Near the Kalphite Hive
        //player.getVarbit(3985) == 3 && player.getVarbit(2986) == 0 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3251, 3095))
        //Kandarin: Ardougne Zoo unicorns
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2635, 3266))
        //Other Realms: Fisher King
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2650,4730))
        //Islands: Near Zul-Andra
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2150,3070))
        //Feldip Hills: South of Castle Wars
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2385,3035))
        //Other Realms: Enchanted Valley
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3041,4532))
        //Morytania: Mort Myre
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3469,3431))
        //Other Realms: Zanaris
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2412, 4434))
        //Dungeons: TzHaar Area
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2437, 5126))
        //Kandarin: Legends' Guild
        player.getVarbit(3985) == 3 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2740, 3351))
        //Islands: Miscellania
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2513, 3884))
        //Kandarin: North-west of Yanille
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2528, 3127))
        //Kebos Lowlands: South of Mount Karuulm
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(1302, 3762))
        //Arceuss library
        //player.getVarbit(3985) == 2 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(1639, 3868))
        //Kandarin: Sinclair Mansion
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2705, 3576))
        //Other Realms: Cosmic entity's plane
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2075, 4848))
        //Karamja: South of Tai Bwo Wannai Village
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2801, 3003))
        //Morytania: Canifis
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(3447, 3470))
        //Islands: South of Draynor Village
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(3082, 3206))
        //Islands: Ape Atoll
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2740, 2738))
        //Islands: Hazelmere's home
        player.getVarbit(3985) == 2 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2682, 3081))
        //Other Realms: Abyssal Nexus
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(3037, 4763))
        //Other Realms: Gorak's Plane
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3038, 5348))
        //Misthalin: Wizards' Tower
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(3108, 3149))
        //Kandarin: Tower of Life
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2658, 3230))
        //Great Kourend: Chasm of Fire
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(1455, 3658))
        //Karamja: South of Musa Point
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2900, 3111))
        //Misthalin: Edgeville
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3129, 3496))
        //Kandarin: Polar Hunter area
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2744, 3719))
        //Kahridian Desert: North of Nardah
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3423, 3016))
        //Islands: Poison Waste South of Isafdar
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2213, 3099))
        //Dungeons: Myreque hideout under The Hollows
        player.getVarbit(3985) == 1 && player.getVarbit(3986) == 1 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(3501, 9821, 3))
    }
}

fun LastDesination(player: Player) {
    when {
        player.getVarbit(5374) == 2 -> fairyRingTeleAction(player, Tile(2700, 3247))
        player.getVarbit(5374) == 3 -> fairyRingTeleAction(player, Tile(2996, 3114))
        player.getVarbit(5374) == 4 -> fairyRingTeleAction(player, Tile(2503, 3636))
        player.getVarbit(5374) == 5 -> fairyRingTeleAction(player, Tile(2644, 3495))
        player.getVarbit(5374) == 6 -> fairyRingTeleAction(player, Tile(3059, 4875))
        player.getVarbit(5374) == 7 -> fairyRingTeleAction(player, Tile(3597, 3495))
        player.getVarbit(5374) == 9 -> fairyRingTeleAction(player, Tile(2571, 2956))
        player.getVarbit(5374) == 11 -> fairyRingTeleAction(player, Tile(2319, 3619))
        player.getVarbit(5374) == 13 -> fairyRingTeleAction(player, Tile(2500, 3896))
        player.getVarbit(5374) == 14 -> fairyRingTeleAction(player, Tile(2780, 3613))
        player.getVarbit(5374) == 30 -> fairyRingTeleAction(player, Tile(1455, 3658))
    }
}