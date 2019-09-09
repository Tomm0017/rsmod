package gg.rsmod.plugins.content.objs.fairyrings

/**
 * @author DaGreenRs <dagreenrs@gmail.com>
 * https://oldschool.runescape.wiki/w/Fairy_rings
 */

for (objectid in 29496 until 29560) {
    on_obj_option(objectid, "Zanaris", lineOfSightDistance = 0) {
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
}
on_obj_option(Objs.FAIRY_RING_29561, "Configure") {
    if (checkFairyStaff(player)) {
        player.setVarbit(3985, 0)
        player.setVarbit(3986, 0)
        player.setVarbit(3987, 0)
        sendTravelLog(player)
        player.openInterface(398, InterfaceDestination.MAIN_SCREEN)
        player.openInterface(381, InterfaceDestination.TAB_AREA)
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

class Locations(val name: String, val component: Int, val tile: Tile, val dial1: Int, val dial2: Int, val dial3: Int)
val FairyRings = arrayOf(
        Locations("Mudskipper Point", 13, Tile(2996, 3114), 0, 0, 3),
        Locations("Island: Ardougne", 15, Tile(2700, 3247), 0, 0, 3),
        Locations("Dungeons: Dorgesh-Kaan", 21, Tile(2735, 5221), 0, 3, 3),
        Locations("Fremennik Slayer Cave", 23, Tile(2780, 3613), 0, 3, 2),
        Locations("Penguins", 25, Tile(2500, 3896), 0, 3, 1),
        Locations("Piscatoris", 29, Tile(2319, 3619), 0, 2,3),
        Locations("Feldip Hills", 33, Tile(2571, 2956), 0, 2, 1),
        Locations("Light House", 35, Tile(2503, 3636), 0, 1, 0),
        Locations("Haunted Woods", 37, Tile(3597, 3495), 0, 1, 3),
        Locations("Abyssal Area", 39, Tile(3059, 4875), 0, 1, 2),
        Locations("McGrubor's Wood", 41, Tile(2644, 3495), 0, 1, 1),
        Locations("River Slave", 43, Tile(3410, 3324), 3, 0, 0),
        Locations("Kalphite Hive", 45, Tile(3251, 3095), 3, 0, 3),
        Locations("Ardougne Zoo", 49, Tile(2635, 3266), 3, 0 , 1),
        Locations("Fisher King", 55, Tile(2650, 4730), 3, 3, 2),
        Locations("Zulrah Andra", 57, Tile(2150, 3070), 3, 3, 1),
        Locations("Castle Wars", 59, Tile(2385, 3035), 3, 2, 0),
        Locations("Enchanted Valley", 61, Tile(3041, 4532), 3, 2, 3),
        Locations("Mort Myre Swamp", 63, Tile(3469, 3431), 3, 2, 2),
        Locations("Zanaris", 65, Tile(2412, 4434), 3, 2, 1),
        Locations("TzHaar Area", 67, Tile(2437, 5126), 3, 1, 0),
        Locations("Legends Guild", 71, Tile(2740, 3351), 3, 1 , 2)
)

fairylocation.forEach {
    on_button(381, it.component) {
        player.setVarbit(3985, it.dial1)
        player.setVarbit(3986, it.dial2)
        player.setVarbit(3987, it.dial3)
        fairyRingTeleAction(player, it.tile)
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
        wait(5)
        player.closeInterfaces()
        wait(1)
        player.walkTo(player.getInteractingGameObj().tile, MovementQueue.StepType.FORCED_WALK, detectCollision = false)
        wait(1)
        player.playSound(1098)
        player.graphic(569)
        player.animate(3265, 30)
        wait(3)
        player.moveTo(Tile(endTile))
        player.animate(3266)
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
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2996, 3114, 0))
        //South-east of Ardougne
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 0 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2700, 3247, 0))
        //Dungeons: Cave south of Dorgesh-Kaan
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2735, 5221, 0))
        //Kandarin: Slayer cave south-east of Rellekka
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2780, 3613, 0))
        //Islands: Penguins near Miscellania.
        player.getVarbit(3985) == 0 && player.getVarbit(3986) == 3 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2500, 3896, 0))
        //Kandarin: Piscatoris Hunter area
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(2319, 3619, 0))
        //Feldip Hills: Jungle Hunter area
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2571, 2956, 0))
        //Fremennik Province: Lighthouse
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2503, 3636, 0))
        //Morytania: Haunted Woods
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3597, 3495,0))
        //Other Realms: Abyss
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3059, 4875, 0))
        //Kandarin: McGurbor's Wood
        player.getVarbit(3985) == 0 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2644, 3495, 0))
        //Islands: River Slave
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 0 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(3410, 3324, 0))
        //Kalhridian Desert: Near the Kalphite Hive
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 0 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3251, 3095, 0))
        //Kandarin: Ardougne Zoo unicorns
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 0 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2635, 3266, 0))
        //Other Realms: Fisher King
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 3 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2650, 4730, 0))
        //Islands: Near Zul-Andra
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 3 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2150, 3070, 0))
        //Feldip Hills: South of Castle Wars
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2385, 3035, 0))
        //Other Realms: Enchanted Valley
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 3 -> fairyRingTeleAction(player, Tile(3041, 4532,0))
        //Morytania: Mort Myre
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(3469, 3431,0))
        //Other Realms: Zanaris
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 2 && player.getVarbit(3987) == 1 -> fairyRingTeleAction(player, Tile(2412, 4434, 0))
        //Dungeons: TzHaar Area
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 0 -> fairyRingTeleAction(player, Tile(2437, 5126, 0))
        //Kandarin: Legends' Guild
        player.getVarbit(3985) == 3 && player.getVarbit(2986) == 1 && player.getVarbit(3987) == 2 -> fairyRingTeleAction(player, Tile(2740, 3351, 0))
    }
    player.closeInterfaces()
}
