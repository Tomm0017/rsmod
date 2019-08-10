package gg.rsmod.plugins.content.objs.canoestation

/**Thanks to Ingot
 * for helping me get the face direction
 * and for making the functions
 *
 * To-Do
 * make the sinking canoes spawn
 * add in fading interface and blackout minimap
 * add proper sounds
 * make all stations work
 * */

import gg.rsmod.game.message.impl.CameraLookAtMessage
import gg.rsmod.game.message.impl.CameraMoveToMessage
import gg.rsmod.game.message.impl.CameraResetMessage

//player.playSound(id = 2728, volume = 16, delay = 0)
//sound = 2734, volume = 1, delay = 0)

/*Sinking canoes
* 12159 log
* 12160 dugout
* 12161 stable
* 12162 waka
* */

//station start
on_obj_option(Objs.CANOE_STATION, "Chop-down") {
    val obj = player.getInteractingGameObj()

    //player.moveTo(3243, 3235, 0)
    player.lock()

    player.queue {
        wait(2)
        faceWest(player)
        player.animate(875)
        wait(3)
        player.setVarp(674, 9)
        player.animate(-1)

            world.queue {
                world.remove(obj)
                world.spawn(DynamicObject(obj, Objs.CANOE_STATION_12145))
                wait(3)
                world.remove(DynamicObject(obj, Objs.CANOE_STATION_12145))
                world.spawn(DynamicObject(obj))
            }
        player.setVarp(674, 10)
        player.unlock()
    }
}

//station laying down
on_obj_option(Objs.CANOE_STATION_12146, "Shape-Canoe") {
    player.queue {
        player.lock()
        //player.moveTo(3243, 3237, 0)
        wait(2)
        faceWest(player)
        player.openInterface(interfaceId = 52, dest = InterfaceDestination.MAIN_SCREEN)
        player.unlock()
    }
}

on_interface_close(interfaceId = 52) {
    player.animate(3289)
}

//interface 52 buttons
on_button(52, 24) {
    player.message("log")
    player.setVarp(674, 1)
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 37) {
    player.message("dugout")
    player.setVarp(674, 2)
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 38) {
    player.message("stable dugout")
    player.setVarp(674, 3)
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 39) {
    player.message("waka")
    player.setVarp(674, 4)
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}

//float different canoes
on_obj_option(Objs.CANOE_STATION_12147, "float log") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12151)
    player.message("float log")
    player.setVarp(674, 11)
}
on_obj_option(Objs.CANOE_STATION_12148, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12152)
    player.message("float dugout")
    player.setVarp(674, 12)
}
on_obj_option(Objs.CANOE_STATION_12149, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12153)
    player.message("float stable dugout")
    player.setVarp(674, 13)
}
on_obj_option(Objs.CANOE_STATION_12150, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12154)
    player.message("float waka")
    player.setVarp(674, 14)
}

//paddle different canoes
on_obj_option(Objs.CANOE_STATION_12155, "Paddle Canoe") {
    player.openInterface(interfaceId = 57, dest = InterfaceDestination.MAIN_SCREEN)
}
on_obj_option(Objs.CANOE_STATION_12156, "Paddle Canoe") {
    player.openInterface(interfaceId = 57, dest = InterfaceDestination.MAIN_SCREEN)
}
on_obj_option(Objs.CANOE_STATION_12157, "Paddle Canoe") {
    player.openInterface(interfaceId = 57, dest = InterfaceDestination.MAIN_SCREEN)
}
on_obj_option(Objs.CANOE_STATION_12158, "Paddle Canoe") {
    player.openInterface(interfaceId = 57, dest = InterfaceDestination.MAIN_SCREEN)
}

//interface 57 buttons
    on_button(57, 32) {
        player.message("lumbridge")
        doCanoeAction(player, Tile(3240, 3242))
    }
    on_button(57, 33) {
        player.message("champions guild")
        doCanoeAction(player,Tile(3199, 3344))
    }
    on_button(57, 34) {
        player.message("barbvillage")
        doCanoeAction(player,Tile(3109, 3415))
    }
    on_button(57, 39) {
        player.message("edgeville")
        doCanoeAction(player, Tile(3128, 3503))
    }
    on_button(57, 35) {
        player.message("wilderness pond")
        doCanoeAction(player, Tile(3141, 3796))
    }

fun floatCanoe(player: Player, canoe: Int) {
    val obj = player.getInteractingGameObj()

    player.queue {
        player.animate(3301)

        world.queue {
            world.remove(obj)
            world.spawn(DynamicObject(obj, canoe))
            wait(4)
            world.remove(DynamicObject(obj, canoe))
            world.spawn(DynamicObject(obj))
        }
    }
}

fun doCanoeAction(player: Player, finalMoveLocation: Tile) {
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
    player.moveTo(1817, 4515)
    player.animate(3302)
    player.queue {
        faceWest(player)
        wait(2)

        if (player.tile.x == 1817 && player.tile.z == 4515) {
            player.lock()
            player.setVarp(id = 1021, value = 128)
            player.setVarp(id = 1021, value = 192)
            player.setVarp(id = 1021, value = 16576)
            player.write(CameraMoveToMessage(44, 51, 2500, 100, 100))
            player.write(CameraMoveToMessage(44, 51, 255, 100, 100))
            player.write(CameraLookAtMessage(49, 51, 225, 100, 100))
            player.queue {
                wait(20)
                player.setVarp(674, 0)
                player.setVarp(id = 1021, value = 0)
                player.animate(-1)
                player.moveTo(finalMoveLocation)
                player.write(CameraResetMessage())
                player.unlock()
            }
        }
    }
}

fun faceWest(player: Player) {
    val direction : Direction = Direction.WEST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
}
