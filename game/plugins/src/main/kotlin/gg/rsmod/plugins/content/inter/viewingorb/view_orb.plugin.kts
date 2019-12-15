package gg.rsmod.plugins.content.inter.viewingorb

val VIEWING = AttributeKey<ViewArea>("viewing")
val VIEW_ORB_INTERFACE = 374
on_obj_option(obj = Objs.VIEWING_ORB_26741, option = "use") {
    use(player)
}

fun use(player: Player) {
    player.queue() {
        this.chatNpc("Which game would you like to spectate?", npc = 7316, animation = 567)
        when (this.options("Forest", "Snow", "Desert")) {
            1 -> player.attr[VIEWING] = ViewArea.FOREST
            2 -> player.attr[VIEWING] = ViewArea.SNOW
            3 -> player.attr[VIEWING] = ViewArea.DESERT
        }
        spectate(player)
    }
}

fun spectate(player: Player) {
    player.lock()
    player.invisible = true
    player.openInterface(dest = InterfaceDestination.TAB_AREA, interfaceId = 374)
}

on_button(interfaceId = 374, component = 5) {
    player.closeInterface(dest = InterfaceDestination.TAB_AREA)
    player.moveTo(1247, 3160)
    player.invisible = false
    player.unlock()
}

on_button(interfaceId = VIEW_ORB_INTERFACE, component = 11) { player.attr[VIEWING]?.center?.let { player.moveTo(it) } }

on_button(interfaceId = VIEW_ORB_INTERFACE, component = 12) { player.attr[VIEWING]?.northWest?.let { player.moveTo(it) } }

on_button(interfaceId = VIEW_ORB_INTERFACE, component = 13) { player.attr[VIEWING]?.northEast?.let { player.moveTo(it) } }

on_button(interfaceId = VIEW_ORB_INTERFACE, component = 14) { player.attr[VIEWING]?.southEast?.let { player.moveTo(it) } }

on_button(interfaceId = VIEW_ORB_INTERFACE, component = 15) { player.attr[VIEWING]?.southWest?.let { player.moveTo(it) } }