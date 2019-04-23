package gg.rsmod.plugins.content.inter.displayname

/**
 * A plugin for changing display name.
 *
 * @author HolyRSPS <dagreenrs@gmail.com>
 */
on_button(interfaceId = 261, component = 75) {
    player.queue {
        player.openInterface(interfaceId = 589, dest = InterfaceDestination.TAB_AREA)
        player.sendTempVarbit(5605, 2)
        player.sendTempVarbit(5606, 0)
        player.runClientScript(635, "")
        player.setComponentText(interfaceId = 589, component = 6, text = "Next free change:")
        player.setComponentText(interfaceId = 589, component = 7, text = "---")
        player.setComponentText(interfaceId = 589, component = 8, text = "Extra changes: <col=ffb83f>0</col>")
        player.setComponentText(interfaceId = 589, component = 15, text = "Loading...")

        wait(2)
        player.sendTempVarbit(5605, 1)
        player.sendTempVarbit(5606, 2)
        player.setComponentText(interfaceId = 589, component = 6, text = "Next free change:")
        player.setComponentText(interfaceId = 589, component = 7, text = "Now!")
        player.setComponentText(interfaceId = 589, component = 8, text = "Extra changes: <col=ffb83f>0</col>")
        player.setComponentText(interfaceId = 589, component = 15, text = "")

    }
}

/**
 * The display name.
 */
lateinit var name : String

on_button(interfaceId = 589, component = 18) {
    player.queue {
        name = inputString(description = "What name would you like to check (maximum of 12 characters)?")
        player.runClientScript(635, name)
        player.setComponentText(interfaceId = 589, component = 15, text = name)
        wait(cycles = 3)
        //TODO: TODO: Display names separated from login users. MYSQL or some sort of database would be required.
        player.sendTempVarbit(5607, 0)
        player.sendTempVarbit(5605, 4)
        player.setComponentText(interfaceId = 589, component = 15, text = "<col=00ff00>Not taken</col>")
    }
}

on_button(interfaceId = 589, component = 19) {
    player.queue {
        player.closeInterface(dest = InterfaceDestination.TAB_AREA)
        wait(cycles = 2)
        messageBox(message = "Your display name request was accepted. It may take a minute for the game's chat system to receive the update.")
        player.username = name
    }
}

on_button(interfaceId = 589, component = 20) {
    if (!player.lock.canInterfaceInteract()) {
        return@on_button
    }
    player.setInterfaceUnderlay(color = -1, transparency = -1)
    player.openInterface(interfaceId = 65, dest = InterfaceDestination.MAIN_SCREEN)
    player.runClientScript(2276, 2)
    player.runClientScript(733, 0, 0, 0, 0, 0, 0, 0, 0)
}