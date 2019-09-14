package gg.rsmod.plugins.content.objs.spirittree

/**
 * @author DaGreenRs <dagreenrs@gmail.com>
 */

val SPIRIT_TREES = arrayOf(Objs.SPIRIT_TREE_26260, Objs.SPIRIT_TREE_26261, Objs.SPIRIT_TREE_26263, Objs.SPIRIT_TREE_35950)
val TALKING_TREES = arrayOf(Objs.SPIRIT_TREE_26259, Objs.SPIRIT_TREE_26262, Objs.SPIRIT_TREE_35949)

on_login {
    //Unlock grand trees
    player.setVarp(111, 9)//Quest Tree Gnome Village
    player.setVarp(150, 160)//Quest The Grand Tree
    player.setVarbit(598, 2)
}

TALKING_TREES.forEach { treeTalk ->
    on_obj_option(treeTalk, option = "talk-to") {
        if (treeTalk == 26259) {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Need Npc Chat", npc = 4982)
            }
        } else {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Need Npc Chat", npc = 4981)
            }
        }
    }
}

SPIRIT_TREES.forEach { tree ->
    on_obj_option(tree, "Travel") {
        TreeTele(player)
    }
    on_obj_option(tree, option = "talk-to") {
        if (tree == 26260 || tree == 26261) {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Hello gnome friend. Where would you like to go?", npc = 4982)
                TreeTele(player)
            }
        } else {
            player.queue(TaskPriority.STRONG) {
                chatNpc("Hello gnome friend. Where would you like to go?", npc = 4981)
                TreeTele(player)
            }
        }
    }
}

fun spiritTreeTele(player: Player, endTile : Tile) {
    player.queue(TaskPriority.STRONG) {
        player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        player.lock = LockState.DELAY_ACTIONS
        itemMessage(message = "You place your hands on the dry tough bark of the<br>spirit tree, and feel a surge of energy run through<br>your veins.", item = 6063, amountOrZoom = 400)
        player.animate(id = 828)
        wait(1)
        player.moveTo(endTile)
        wait(1)
        player.unlock()
        itemMessageBox(message = "You place your hands on the dry tough bark of the<br>spirit tree, and feel a surge of energy run through<br>your veins.", item = 6063, amountOrZoom = 400)
    }
}

fun TreeTele (player: Player) {
    player.queue(TaskPriority.STRONG) {
        when (interfaceOptions("Tree Gnome Village", "Gnome Stronghold", "Battlefield of Khazard", "Grand Exchange", "Feldip Hills", "Prifddinas", "<col=777777>Port Sarim</col>", "<col=777777>Etceteria</col>", "<col=777777>Brimhaven</col>", "<col=777777>Hosidius</col>", "<col=777777>Farming Guild</col>", "<col=777777>Your house</col>", "Cancel", title = "Spirit Tree Locations")) {
            0 -> spiritTreeTele(player, Tile(2542, 3169, 0))
            1 -> spiritTreeTele(player, Tile(2461, 3444, 0))
            2 -> spiritTreeTele(player, Tile(2557, 3260, 0))
            3 -> spiritTreeTele(player, Tile(3183, 3508, 0))
            4 -> spiritTreeTele(player, Tile(2488, 2850, 0))
            5 -> spiritTreeTele(player, Tile(3274, 6123, 0))
            12 -> player.closeInterface(InterfaceDestination.MAIN_SCREEN)
        }
    }
}