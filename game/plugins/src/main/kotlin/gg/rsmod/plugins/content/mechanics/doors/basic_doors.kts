package gg.rsmod.plugins.content.mechanics.doors

import gg.rsmod.plugins.api.ext.closeDoor
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.openDoor
import gg.rsmod.plugins.api.ext.player

on_obj_option(obj = 1536, option = "close") {
    it.player().world.closeDoor(it.getInteractingGameObj())
}

on_obj_option(obj = 1535, option = "open") {
    it.player().world.openDoor(it.getInteractingGameObj())
}