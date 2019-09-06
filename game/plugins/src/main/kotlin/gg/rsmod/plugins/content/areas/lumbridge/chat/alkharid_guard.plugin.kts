package gg.rsmod.plugins.content.areas.lumbridge.chat
/**
 * Thanks to Tomm for the movement code
 * Thanks to Desetude for gate open and close code
 */

spawn_npc(Npcs.BORDER_GUARD, 3267,3226,0,0, Direction.WEST)
spawn_npc(Npcs.BORDER_GUARD, 3267,3229,0, 0, Direction.WEST)
spawn_npc(Npcs.BORDER_GUARD_4288, 3268,3226,0, 0, Direction.EAST)
spawn_npc(Npcs.BORDER_GUARD_4288, 3268,3229,0, 0, Direction.EAST)


on_npc_option(npc = Npcs.BORDER_GUARD, option = "talk-to") {
        player.queue { dialog(this) }
    }

on_npc_option(npc = Npcs.BORDER_GUARD_4288, option = "talk-to") {
    player.queue { dialog2(this) }
}

suspend fun dialog(it: QueueTask) {

    it.chatPlayer("Can I come through this gate?", animation = 588)
    it.chatNpc("You must pay a toll of 10 gold coins to pass.", animation = 590)
    when (it.options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
        1 -> {
            it.chatPlayer("No thank you, I'll walk around.", animation = 554)
            it.chatNpc("Ok suit yourself.", animation = 588)
        }
        2 -> {
            it.chatPlayer("Who does my money go to?", animation = 554)
            it.chatNpc("The money goes to the city of Al-Kharid.", animation = 590)
        }
        3 -> {
            it.chatPlayer("Yes, ok.", animation = 554)

            val inventory = it.player.inventory
            if (inventory.getItemCount(Items.COINS_995) >= 10) {
                
                it.player.lock()
                it.player.moveTo(3267, 3227, 0)
                world.queue {
                    wait(2)
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_2882, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_2883)
                }
                it.player.inventory.remove(Items.COINS_995, 10)
                it.wait(2)
                it.player.moveTo(3268, 3227, 0)
                it.wait(2)
                it.player.unlock()
            } else
                it.chatPlayer("Oh dear, I don't actually seem to have enough money.", animation = 554)
        }
    }
}

suspend fun dialog2(it: QueueTask) {

    it.chatPlayer("Can I come through this gate?", animation = 588)
    it.chatNpc("You must pay a toll of 10 gold coins to pass.", animation = 590)
    when (it.options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
        1 -> {
            it.chatPlayer("No thank you, I'll walk around.", animation = 554)
            it.chatNpc("Ok suit yourself.", animation = 588)
        }
        2 -> {
            it.chatPlayer("Who does my money go to?", animation = 554)
            it.chatNpc("The money goes to the city of Al-Kharid.", animation = 590)
        }
        3 -> {
            it.chatPlayer("Yes, ok.", animation = 554)

            val inventory = it.player.inventory
            if (inventory.getItemCount(Items.COINS_995) >= 10) {
                it.player.lock()
                it.player.moveTo(3268, 3227, 0)
                world.queue {
                    wait(2)
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_2882, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_2883)
                }
                it.player.inventory.remove(Items.COINS_995, 10)
                it.wait(2)
                it.player.moveTo(3267, 3227, 0)
                it.wait(2)
                it.player.unlock()
            } else
                it.chatPlayer("Oh dear, I don't actually seem to have enough money.", animation = 554)
        }
    }
}