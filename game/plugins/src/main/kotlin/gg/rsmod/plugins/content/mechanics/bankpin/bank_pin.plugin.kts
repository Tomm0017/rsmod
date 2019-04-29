package gg.rsmod.plugins.content.mechanics.bankpin

val INTERFACE_ID = 14

on_interface_open(INTERFACE_ID) {
    open_settings(player)
}

fun open_settings(p: Player) {
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 0, hidden = false)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 28, hidden = true)
    p.setComponentText(interfaceId = INTERFACE_ID, component = 6, text = "No PIN set")
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 18, hidden = false)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 21, hidden = true)
    p.setComponentHidden(interfaceId = INTERFACE_ID, component = 26, hidden = true)
    p.setComponentText(interfaceId = INTERFACE_ID, component = 8, text = "7 days")
    p.setComponentText(interfaceId = INTERFACE_ID, component = 10, text = "Always lock")
    p.setComponentText(interfaceId = INTERFACE_ID, component = 14, text = "Customers are reminded that they should NEVER tell anyone their Bank PINs or passwords, nor should they ever enter their PINs on any website form.")
}