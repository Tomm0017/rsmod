package gg.rsmod.plugins.content.events.xmas2020

on_obj_option(40930, 6, 3) {
    player.message("What a pretty tree!")
    if(player.inventory.hasSpace) player.inventory.add(Items.COINS_995, 1000000)
}




// enter caves
on_obj_option(40887, 1) {
    player.animate(0)
    player.forceMove(ForcedMovement.of(player.tile, player.tile.step(Direction.WEST, 2), 33, 60, Direction.WEST.angle))
}

// leave caves
on_obj_option(40888, 1) {
    player
}

// take sled
on_obj_option(40892, 1) {

}

// collecting snowballs
on_obj_option(19030, 1) {
    player.filterableMessage("You attempt to make some snowballs...")
    player.queue(TaskPriority.WEAK){
        while(true) {
            player.animate(5067)
            wait(2)
            player.playSound(3287)
            player.inventory.add(10501, 3)
            wait(2)
        }
    }
}