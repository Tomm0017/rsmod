package gg.rsmod.plugins.content.cmd

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.cmd.Commands_plugin.Command.tryWithUsage
import gg.rsmod.game.message.impl.OpenUrlMessage

on_command("script", Privilege.DEV_POWER){
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::script id args...</col>") { values ->
        val id = values[0].toInt()
        val clientArgs = MutableList<Any>(values.size-1) {}
        for(arg in 1 until values.size)
            clientArgs[arg-1] = values[arg].toIntOrNull() ?: values[arg]
        player.runClientScript(id, *clientArgs.toTypedArray())
        player.message("Executing <col=0000FF>cs_$id</col><col=801700>$clientArgs</col>")
    }
}

on_command("interface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
        val component = values[0].toInt()
        player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
        player.message("Opening interface <col=801700>$component</col>")
    }
}

on_command("openinterface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openinterface interfId parentId pChildId clickThrough isModal</col>") { values ->
        val component = values[0].toInt()
        val parent = values[1].toIntOrNull() ?: getDisplayComponentId(player.interfaces.displayMode)
        val child = values[2].toInt()
        var clickable = values[3].toIntOrNull() ?: 0
        clickable = if(clickable != 1) 0 else 1
        val modal = values[4].toIntOrNull() ?: 1 == 1
        player.openInterface(parent, child, component, clickable, isModal = modal)
        player.message("Opening interface <col=801700>$component</col> on <col=0000ff>$parent:$child</col>")
    }
}

on_command("closeinterface", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::closeinterface interfaceId</col>") { values ->
        val interfaceId = values[0].toInt()
        player.closeInterface(interfaceId)
        player.message("Closing interface <col=801700>$interfaceId</col>")
    }
}

on_command("hitme", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::hitme hitType amount</col>") { values ->
        val hitType = HitType.get(values[0].toInt())
        if(hitType?.name ?: "INVALID" == "INVALID"){
            throw IllegalArgumentException()
        }
        val damage = if(values.size==2 && values[1].matches(Regex("-?\\d+"))) values[1].toInt() else 0
        player.message("${hitType!!.name} hit for $damage")
        player.hit(damage, hitType)
    }
}

on_command("openurl", Privilege.DEV_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::openurl google.com</col>") { values ->
        val url = values[0]
        if(!url.startsWith("http://") || !url.startsWith("https://"))
            player.openUrl("https://$url") // not perfect by any means, but simple enough as fallback for easier command
        else
            player.openUrl(url)
    }
}