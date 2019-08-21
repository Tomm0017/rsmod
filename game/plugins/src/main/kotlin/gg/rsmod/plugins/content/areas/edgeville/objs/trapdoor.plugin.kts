package gg.rsmod.plugins.content.areas.edgeville.objs

val OPEN_SFX = 62
val CLOSE_SFX = 60

on_obj_option(obj = Objs.TRAPDOOR_1579, option = "open") {
    open(player, player.getInteractingGameObj())
}

on_obj_option(obj = Objs.TRAPDOOR_1581, option = "close") {
    close(player, player.getInteractingGameObj())
}

on_obj_option(obj = Objs.TRAPDOOR_1581, option = "climb-down") {
    player.moveTo(3096, 9867)
}

fun open(p: Player, obj: GameObject) {
    p.playSound(OPEN_SFX)
    p.filterableMessage("You close the trapdoor.")
    world.spawn(DynamicObject(obj, Objs.TRAPDOOR_1581))
}

fun close(p: Player, obj: GameObject) {
    p.playSound(CLOSE_SFX)
    p.filterableMessage("The trapdoor opens...")
    world.spawn(DynamicObject(obj, Objs.TRAPDOOR_1579))
}