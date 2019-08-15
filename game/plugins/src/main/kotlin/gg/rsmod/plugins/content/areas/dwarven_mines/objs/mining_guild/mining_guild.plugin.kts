package gg.rsmod.plugins.content.areas.dwarven_mines.objs.mining_guild

on_obj_option(Objs.DOOR_30364, option = "open") {
    if (player.getSkills().getCurrentLevel(Skills.MINING) < 60) {
        player.queue {
            chatNpc("Sorry, but you're not experienced enough to go in<br>there.", npc = 7712)
            messageBox("You need a ${Skills.getSkillName(player.world, Skills.MINING)} level of 60 to access the Mining Guild.")
        }
    } else if (player.tile.x == 3046 && player.tile.z == 9757) {
            player.queue {
                player.lock()
                player.moveTo(3046, 9756)
                world.queue {
                    world.openDoor(world.getObject(Tile(3046, 9756), 0)!!, 1539)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3046, 9757), 0)!!, 30364)
                }
                wait(2)
                player.unlock()
            }
        }
        if (player.tile.x == 3046 && player.tile.z == 9756) {
            player.queue {
                player.lock()
                player.moveTo(3046, 9757)
                world.queue {
                    world.openDoor(world.getObject(Tile(3046, 9756), 0)!!, 1539)
                    wait(3)
                    world.closeDoor(world.getObject(Tile(3046, 9757), 0)!!, 30364)
                }
                wait(2)
                player.unlock()
            }
        }
}
