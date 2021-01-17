package gg.rsmod.plugins.content.areas.kharid.objs

import kotlin.random.Random

/**
 * Beer glass giving shelves in Sorceress' house
 */
on_obj_option(21794, "Search") {
    while(player.inventory.hasSpace){
        suspend {
            searchForBeerGlass(player)
        }
    }
}

fun searchForBeerGlass(player: Player){
    player.message("You search the shelves...")
    player.queue {
        repeat(Random.nextInt(1,3)){
            player.animate(2270)
            player.playSound(59)
        }
        player.message("...and among the strange paraphernalia, you find an empty beer glass.")
        player.animate(536)
        player.playSound(89)
        player.inventory.add(Items.BEER_GLASS)
    }
}