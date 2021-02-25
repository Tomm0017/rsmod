package gg.rsmod.plugins.content.areas.motherlode.objs

import gg.rsmod.game.model.Direction.*

/**
 * Enter cave but exit tunnel lol. ¯\_(ヅ)_/¯
 */

on_obj_option(Objs.CAVE_26654, 1){
    tunnelMotherlodeMine(player, SOUTH, Tile(3728, 5694))
}
on_obj_option(Objs.TUNNEL_26655, 1){
    tunnelMotherlodeMine(player, NORTH, Tile(3060, 9764))
}

on_obj_option(Objs.CAVE_30374, 1){
    tunnelMotherlodeMine(player, EAST, Tile(3716, 5678))
}
on_obj_option(Objs.TUNNEL_30375, 1){
    tunnelMotherlodeMine(player, WEST, Tile(3056, 9744))
}

fun tunnelMotherlodeMine(player: Player, direction: Direction, exit: Tile) {
    player.queue {
        player.crawlTunnel(direction)
        wait(4)
        player.moveTo(exit)
        player.crawlTunnel(direction)
        wait(4)
        player.animate(id = -1)
        player.playSong(426)
    }
}

fun Player.crawlTunnel(direction: Direction) {
    queue {
        player.playSound(2454)
        player.animate(id = 2796)
        forceMove(this, ForcedMovement.of(player.tile, player.tile.step(direction, 2), 10, 60, direction.angle))
    }
}