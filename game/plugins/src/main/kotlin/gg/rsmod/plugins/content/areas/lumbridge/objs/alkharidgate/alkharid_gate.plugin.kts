package gg.rsmod.plugins.content.areas.lumbridge.objs.alkharidgate
/**
 * Thanks to Tomm for the movement code
 * Thanks to Desetude for gate open and close code
*/

val GATES = intArrayOf(Objs.GATE_2883, Objs.GATE_2882)

GATES.forEach { obj ->
    on_obj_option(obj, "pay-toll(10gp)", lineOfSightDistance = 2) {
        if (player.inventory.getItemCount(Items.COINS_995) >= 10) {
            if (obj == 2883) {
                player.lock()
                if (player.tile.x == 3268 && player.tile.z == 3227) {
                    player.moveTo(3268, 3228, 0)
                }
                world.queue {
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_2882, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_2883)
                }
                val curr = player.tile
                val dst = if (curr.sameAs(3267, 3228)) Tile(3268, 3228) else if (curr.sameAs(3268, 3228)) Tile(3267, 3228) else return@on_obj_option
                player.inventory.remove(Items.COINS_995, 10)
                player.moveTo(dst)
                player.unlock()
            }

            if (obj == 2882) {
                player.lock()
                if (player.tile.x == 3268 && player.tile.z == 3228) {
                    player.moveTo(3268, 3227, 0)
                }
                world.queue {
                    world.openDoor(world.getObject(Tile(3268, 3228), 0)!!, 1574)
                    world.openDoor(world.getObject(Tile(3268, 3227), 0)!!, 1573, invertRot = true)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3267, 3227), 0)!!, Objs.GATE_2882, invertTransform = true, invertRot = true)
                    world.closeDoor(world.getObject(Tile(3267, 3228), 0)!!, Objs.GATE_2883)
                }
                val curr = player.tile
                val dst = if (curr.sameAs(3267, 3227)) Tile(3268, 3227) else if (curr.sameAs(3268, 3227)) Tile(3267, 3227) else return@on_obj_option
                player.inventory.remove(Items.COINS_995, 10)
                player.moveTo(dst)
                player.unlock()
            }
        } else
            player.queue { dialog(this) }
    }

    on_obj_option(obj, "open") {
        player.queue { dialog(this) }
    }
}


suspend fun dialog(it: QueueTask) {

    val guard = Npcs.BORDER_GUARD

    it.chatPlayer("Can I come through this gate?", animation = 588)
    it.chatNpc(npc = guard, message = "You must pay a toll of 10 gold coins to pass.", animation = 590)
    when (it.options("No thank you, I'll walk around.", "Who does my money go to?", "Yes, ok.")) {
        1 -> {
            it.chatPlayer("No thank you, I'll walk around.", animation = 554)
            it.chatNpc(npc = guard, message = "Ok suit yourself.", animation = 588)
        }
        2 -> {
            it.chatPlayer("Who does my money go to?", animation = 554)
            it.chatNpc(npc = guard, message = "The money goes to the city of Al-Kharid.", animation = 590)
        }
        3 -> {
                it.chatPlayer("Yes, ok.", animation = 554)

                /** TODO
                 * make different walking from multi tiles
                 * */
            }
        }
    }