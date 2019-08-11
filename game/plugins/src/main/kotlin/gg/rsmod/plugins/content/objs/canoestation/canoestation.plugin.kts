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

//player.playSound(id = 2734, volume = 1, delay = 0) //tree falling
//player.playSound(id = 2731, volume = 1, delay = 0) //canoe in water
//player.playSound(id = 2728, volume = 16, delay = 0) //rowing canoe scene


//station start
on_obj_option(Objs.CANOE_STATION, "Chop-down") {
    val obj = player.getInteractingGameObj()
    player.walkTo(obj.tile.x+2, obj.tile.z)
    player.lock()

    player.queue {
        wait(2)
        faceWest(player)
        player.animate(875)
        wait(3)
        if (player.tile.x == 3243 && player.tile.z == 3235) {
            player.setVarp(674, 9)
        }
        /*if (player.tile.x == 3204 && player.tile.z == 3343) {
            player.setVarp(674, 2304)
        }*/
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
        //lumbridge
        if (player.tile.x == 3243 && player.tile.z == 3235) {
            player.setVarp(674, 10)
        }
        //Champs guild
        if (player.tile.x == 3204 && player.tile.z == 3343) {
            player.setVarp(674, 2560)
        }
        //barbvillage
        if (player.tile.x == 3112 && player.tile.z == 3409) {
            player.setVarp(674, 655360)
        }
        //edgeville
        if (player.tile.x == 3132 && player.tile.z == 3508) {
            player.setVarp(674, 167772160)
        }
        player.unlock()
    }
}

//station laying down
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

on_interface_close(interfaceId = 52) {
    player.animate(3289)
}

//interface 52 buttons
on_button(52, 24) {
    //lumbrige
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.message("log")
        player.setVarp(674, 1)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.message("log")
        player.setVarp(674, 256)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.message("log")
        player.setVarp(674, 65536)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.message("log")
        player.setVarp(674, 16777216)
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 37) {
    //lumbridge
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.message("dugout")
        player.setVarp(674, 2)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.message("dugout")
        player.setVarp(674, 512)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.message("log")
        player.setVarp(674, 131072)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.message("log")
        player.setVarp(674, 33554432)
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 38) {
    //lumbridge
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.message("stable dugout")
        player.setVarp(674, 3)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.message("stable dugout")
        player.setVarp(674, 768)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.message("log")
        player.setVarp(674, 196608)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.message("log")
        player.setVarp(674, 50331648)
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}
on_button(52, 39) {
    //lumbridge
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.message("waka")
        player.setVarp(674, 4)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.message("stable dugout")
        player.setVarp(674, 1024)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.message("log")
        player.setVarp(674, 262144)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.message("log")
        player.setVarp(674, 67108864)
    }
    player.closeInterface(dest = InterfaceDestination.MAIN_SCREEN)
}

//float different canoes
on_obj_option(Objs.CANOE_STATION_12147, "float log") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12151)
    player.message("float log")
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.setVarp(674, 11)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.setVarp(674, 2816)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.setVarp(674, 720896)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.setVarp(674, 184549376)
    }
}
on_obj_option(Objs.CANOE_STATION_12148, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12152)
    player.message("float dugout")
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.setVarp(674, 12)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.setVarp(674, 3328)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.setVarp(674, 851968)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.setVarp(674, 218103808)
    }
}
on_obj_option(Objs.CANOE_STATION_12149, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12153)
    player.message("float stable dugout")
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.setVarp(674, 13)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.setVarp(674, 851968)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.setVarp(674, 786432)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.setVarp(674, 201326592)
    }
}
on_obj_option(Objs.CANOE_STATION_12150, "float canoe") {
    faceWest(player)
    floatCanoe(player, Objs.CANOE_STATION_12154)
    player.message("float waka")
    if (player.tile.x == 3243 && player.tile.z == 3237) {
        player.setVarp(674, 14)
    }
    //champs guild
    if (player.tile.x == 3202 && player.tile.z == 3343) {
        player.setVarp(674, 3584)
    }
    //barbarian village
    if (player.tile.x == 3112 && player.tile.z == 3411) {
        player.setVarp(674, 917504)
    }
    //edgeville
    if (player.tile.x == 3132 && player.tile.z == 3510) {
        player.setVarp(674, 234881024)
    }
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