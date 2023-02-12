package gg.rsmod.plugins.content.skills

val buttons = intArrayOf(5, 11, 12, 13, 14, 15)

buttons.forEach {
    println("instantiating button plugin for interface 374 button $it")
    on_button(374, it) {
        player.message("interface 374 button $it clicked")
    }
}
