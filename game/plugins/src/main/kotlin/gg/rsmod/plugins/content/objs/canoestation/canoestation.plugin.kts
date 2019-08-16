package gg.rsmod.plugins.content.objs.canoestation

/**Thanks to Ingot
 * for helping me get the face direction
 * and for making the functions
 *
 **Thanks to ComradeNuzzles
 * for helping clean up the code
 * and for reworking the canoe stations & destinations
 *
 * To-Do
 * make the sinking canoes spawn
 * add in fading interface and blackout minimap
 * add proper sounds
 * make Champ guild work
 * */

import gg.rsmod.game.message.impl.CameraLookAtMessage
import gg.rsmod.game.message.impl.CameraMoveToMessage
import gg.rsmod.game.message.impl.CameraResetMessage

//player.playSound(id = 2734, volume = 1, delay = 0) //tree falling
//player.playSound(id = 2731, volume = 1, delay = 0) //canoe in water
//player.playSound(id = 2728, volume = 16, delay = 0) //rowing canoe scene

on_obj_option(Objs.CANOE_STATION, "Chop-down") {
    val obj = player.getInteractingGameObj()
    player.walkTo(obj.tile.x+2, obj.tile.z)
    player.lock()

    player.queue {
        wait(3)
        faceWest(player)
        player.animate(875)
        wait(3)
        if (player.tile.x == 3243 && player.tile.z == 3235) {
            player.setVarp(674, 9)
        }
        if (player.tile.x == 3202 && player.tile.z == 3343) {
            player.setVarp(674, 2304)
        }
        if (player.tile.x == 3112 && player.tile.z == 3409) {
            player.setVarp(674, 589824)
        }
        if (player.tile.x == 3132 && player.tile.z == 3508) {
            player.setVarp(674, 150994944)
        }
        player.animate(-1)

        world.queue {
            world.remove(obj)
            world.spawn(DynamicObject(obj, Objs.CANOE_STATION_12145))
            wait(3)
            world.remove(DynamicObject(obj, Objs.CANOE_STATION_12145))
            world.spawn(DynamicObject(obj))
        }

        if (player.tile.x == 3243 && player.tile.z == 3235) {
            player.setVarp(674, 10)
        }
        if (player.tile.x == 3202 && player.tile.z == 3343) {
            player.setVarp(674, 2560)
        }
        if (player.tile.x == 3112 && player.tile.z == 3409) {
            player.setVarp(674, 655360)
        }
        if (player.tile.x == 3132 && player.tile.z == 3508) {
            player.setVarp(674, 167772160)
        }
        player.unlock()
    }
}

on_obj_option(Objs.CANOE_STATION_12146, "Shape-Canoe") {
    val pushStart = Tile(player.tile)
    player.queue {
        player.moveTo(pushStart.x, pushStart.z+2)
        player.lock()
        wait(2)
        faceWest(player)
        player.openInterface(interfaceId = 52, dest = InterfaceDestination.MAIN_SCREEN)
        player.unlock()
    }
}

class shapeCanoe (val inter674varp: Int, val tile: Tile)
val log = arrayOf(
        shapeCanoe(1, Tile(3243, 3237)),
        shapeCanoe(256, Tile(3202, 3343)),
        shapeCanoe(65536, Tile(3112, 3411)),
        shapeCanoe(16777216, Tile(3132, 3510))
)
val dugout = arrayOf(
        shapeCanoe(2, Tile(3243, 3237)),
        shapeCanoe(512, Tile(3202, 3343)),
        shapeCanoe(131072, Tile(3112, 3411)),
        shapeCanoe(33554432, Tile(3132, 3510))
)
val stable_dugout = arrayOf(
        shapeCanoe(3, Tile(3243, 3237)),
        shapeCanoe(768, Tile(3202, 3343)),
        shapeCanoe(196608, Tile(3112, 3411)),
        shapeCanoe(50331648, Tile(3132, 3510))
)
val waka = arrayOf(
        shapeCanoe(4, Tile(3243, 3237)),
        shapeCanoe(1024, Tile(3202, 3343)),
        shapeCanoe(262144, Tile(3112, 3411)),
        shapeCanoe(67108864, Tile(3132, 3510))
)

on_button(52, 24){
    player.message("log")
    log.forEach {
        if (player.tile == (it.tile)) {
            player.setVarp(674, it.inter674varp)
        }
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 37) {
    player.message("dugout")
    dugout.forEach {
        if (player.tile == (it.tile)) {
            player.setVarp(674, it.inter674varp)
        }
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 38) {
    player.message("stable dugout")
    stable_dugout.forEach {
        if (player.tile == (it.tile)) {
            player.setVarp(674, it.inter674varp)
        }
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 39) {
    player.message("waka")
    waka.forEach {
        if (player.tile == (it.tile)) {
            player.setVarp(674, it.inter674varp)
        }
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}

on_interface_close(interfaceId = 52) {
    player.animate(3289)
}

class FloatCanoes (val inter674varp: Int, val tile: Tile)
val floatLog = arrayOf (
        FloatCanoes(11, Tile(3243, 3237)),
        FloatCanoes(2816, Tile(3202, 3343)),
        FloatCanoes(720896, Tile(3112, 3411)),
        FloatCanoes(184549376, Tile(3132, 3510))
)
val floatDugout = arrayOf (
        FloatCanoes(12, Tile(3243, 3237)),
        FloatCanoes(3072, Tile(3202, 3343)),
        FloatCanoes(786432, Tile(3112, 3411)),
        FloatCanoes(201326592, Tile(3132, 3510))
)
val floatStable = arrayOf (
        FloatCanoes(13, Tile(3243, 3237)),
        FloatCanoes(3328, Tile(3202, 3343)),
        FloatCanoes(851968, Tile(3112, 3411)),
        FloatCanoes(218103808, Tile(3132, 3510))
)
val floatWaka = arrayOf (
        FloatCanoes(14, Tile(3243, 3237)),
        FloatCanoes(3584, Tile(3202, 3343)),
        FloatCanoes(917504, Tile(3112, 3411)),
        FloatCanoes(234881024, Tile(3132, 3510))
)

on_obj_option(Objs.CANOE_STATION_12147, "float log") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12151)
    player.message("float log")
    floatLog.forEach {
        if (player.tile == (it.tile)) {
            player.setVarbit(1843, 1)
            player.setVarp(674, it.inter674varp)
        }
    }
}
on_obj_option(Objs.CANOE_STATION_12148, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12152)
    player.message("float dugout")
    floatDugout.forEach {
        if (player.tile == (it.tile)) {
            player.setVarbit(1843, 2)
            player.setVarp(674, it.inter674varp)
        }
    }
}
on_obj_option(Objs.CANOE_STATION_12149, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12153)
    player.message("float stable dugout")
    floatStable.forEach {
        if (player.tile == (it.tile)) {
            player.setVarbit(1843, 3)
            player.setVarp(674, it.inter674varp)
        }
    }
}
on_obj_option(Objs.CANOE_STATION_12150, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12154)
    player.message("float waka")
    floatWaka.forEach {
        if (player.tile == (it.tile)) {
            player.setVarbit(1843, 4)
            player.setVarp(674, it.inter674varp)
        }
    }
}

//interface 57 buttons
class Paddle (val paddleObj: Int)
class Destination(val name: String, val component: Int, val tile: Tile)
val paddle = arrayOf(
        Paddle(Objs.CANOE_STATION_12155),
        Paddle(Objs.CANOE_STATION_12156),
        Paddle(Objs.CANOE_STATION_12157),
        Paddle(Objs.CANOE_STATION_12158)
)
val destinations = arrayOf(
        Destination("lumbridge", 32, Tile(3240, 3242)),
        Destination("champions guild", 33, Tile(3199, 3344)),
        Destination("barbvillage", 34, Tile(3109, 3415)),
        Destination("edgeville", 39, Tile(3128, 3503)),
        Destination("wilderness pond", 35, Tile(3141, 3796))
)

paddle.forEach {
    on_obj_option(it.paddleObj, "Paddle Canoe") {
        player.openInterface(57, InterfaceDestination.MAIN_SCREEN)
    }
}
destinations.forEach{
    on_button(57, it.component){
        player.message(it.name)
        doCanoeAction(player, it.tile)
    }
}

fun faceWest(player: Player) {
    val direction : Direction = Direction.WEST
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
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

on_logout { player.setVarp(id = 674, value = 0) }