package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.filterableMessage
import gg.rsmod.plugins.api.ext.playSound
import gg.rsmod.plugins.api.ext.player

/**
 * @author Lantern Web
 */
object Mining {

    data class Rock(val type: RockType, val obj: Int, val depletedOre: Int)

    suspend fun mineOre(it: QueueTask, obj: GameObject, rock: RockType, depletedOreId: Int) {
        val p = it.player

        if (!canMine(p, obj, rock)) {
            return
        }

        val oreName = p.world.definitions.get(ItemDef::class.java, rock.ore).name
        val pickaxe = PickAxeType.values().firstOrNull { p.getSkills().getMaxLevel(Skills.WOODCUTTING) >= it.level && (p.inventory.contains(it.item) || p.equipment.contains(it.item)) }!!

        p.filterableMessage("You swing your pickaxe at the rock.")
        while (true) {
            p.animate(pickaxe.animation)
            it.wait(2)

            if (!canMine(p, obj, rock)) {
                p.animate(-1)
                break
            }

            if (p.world.random(100) <= (100 - rock.level)) {
                p.filterableMessage("You get some ${oreName}s.")
                p.inventory.add(rock.ore)

                if (p.world.random(rock.depleteChance) == 0) {
                    p.playSound(3600)
                    p.animate(-1)
                    p.addXp(Skills.MINING, rock.xp)

                    if (depletedOreId != -1) {
                        val world = p.world
                        world.queue {
                            val trunk = DynamicObject(obj, depletedOreId)
                            world.remove(obj)
                            world.spawn(trunk)
                            wait(rock.respawnTime.random())
                            world.remove(trunk)
                            world.spawn(DynamicObject(obj))
                        }
                    }
                    break
                }
            }
            it.wait(2)
        }
    }

    private fun canMine(p: Player, obj: GameObject, ore: RockType): Boolean {
        if (!p.world.isSpawned(obj)) {
            return false
        }

        val pickaxe = PickAxeType.values().firstOrNull { p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.inventory.contains(it.item) || p.equipment.contains(it.item)) }
        if (pickaxe == null) {
            p.message("You need a pickaxe to mine this ore.")
            p.message("You do not have a pickaxe which you have the mining level to use.")
            return false
        }

        if (p.getSkills().getMaxLevel(Skills.MINING) < ore.level) {
            p.message("You need a Mining level of ${ore.level} to mine this ore.")
            return false
        }

        if (p.inventory.isFull()) {
            p.message("Your inventory is too full to hold any more ore.")
            return false
        }

        return true
    }
}