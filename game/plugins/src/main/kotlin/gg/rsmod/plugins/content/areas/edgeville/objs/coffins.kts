package gg.rsmod.plugins.content.areas.edgeville.objs

import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.playSound
import gg.rsmod.plugins.api.ext.player

val OPEN_SFX = 53
val CLOSE_SFX = 54

on_obj_option(obj = Objs.COFFIN_398, option = "open") {
    open(it.player(), it.getInteractingGameObj())
}

on_obj_option(obj = Objs.COFFIN_3577, option = "close") {
    close(it.player(), it.getInteractingGameObj())
}

fun open(p: Player, obj: GameObject) {
    p.playSound(OPEN_SFX)
    p.message("The coffin creaks open...")
    p.world.spawn(DynamicObject(Objs.COFFIN_3577, obj))
}

fun close(p: Player, obj: GameObject) {
    p.playSound(CLOSE_SFX)
    p.message("You close the coffin.")
    p.world.spawn(DynamicObject(Objs.COFFIN_398, obj))
}