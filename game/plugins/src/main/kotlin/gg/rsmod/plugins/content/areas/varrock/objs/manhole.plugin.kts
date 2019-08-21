package gg.rsmod.plugins.content.areas.varrock.objs

val OPEN_SFX = 54

on_obj_option(obj = Objs.MANHOLE, option = "open") {
    open(player, player.getInteractingGameObj())
}

on_obj_option(obj = Objs.MANHOLE_882, option = "close") {
    close(player, player.getInteractingGameObj())
}

on_obj_option(obj = Objs.MANHOLE_882, option = "climb-down") {
    val direction : Direction = Direction.SOUTH
    player.faceTile(player.tile.transform(direction.getDeltaX(), direction.getDeltaZ()))
    player.moveTo(3237, 9858)
}

fun open(p: Player, obj: GameObject) {
    p.playSound(OPEN_SFX)
    p.filterableMessage("You pull back the cover from over the manhole.")
    world.spawn(DynamicObject(obj, Objs.MANHOLE_882))
}

fun close(p: Player, obj: GameObject) {
    p.filterableMessage("You place the cover back over the manhole.")
    world.spawn(DynamicObject(obj, Objs.MANHOLE))
}