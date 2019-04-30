package gg.rsmod.plugins.content.areas.edgeville.objs

val OPEN_SFX = 53
val CLOSE_SFX = 54

on_obj_option(obj = Objs.COFFIN_398, option = "open") {
    open(player, player.getInteractingGameObj())
}

on_obj_option(obj = Objs.COFFIN_3577, option = "close") {
    close(player, player.getInteractingGameObj())
}

fun open(p: Player, obj: GameObject) {
    p.playSound(OPEN_SFX)
    p.message("The coffin creaks open...")
    world.spawn(DynamicObject(obj, Objs.COFFIN_3577))
}

fun close(p: Player, obj: GameObject) {
    p.playSound(CLOSE_SFX)
    p.message("You close the coffin.")
    world.spawn(DynamicObject(obj, Objs.COFFIN_398))
}