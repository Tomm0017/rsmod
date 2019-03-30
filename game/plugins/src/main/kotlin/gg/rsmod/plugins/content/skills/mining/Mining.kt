package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.ChatMessageType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*

/**
 * @author Misterbaho <MisterBaho#6447>
 */
object Mining {

    data class Rock(val type: RockType, val obj: Int)

    private const val emptyRock = Objs.ROCKS_7469

    suspend fun mining(it: QueueTask, obj: GameObject, rock: RockType) {
        val p = it.player

        if (!canMine(p, obj, rock)) {
            return
        }

        val oreName = p.world.definitions.get(ItemDef::class.java, rock.ore).name
        val pickaxe = PickaxeType.values.firstOrNull { p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.equipment.contains(it.item) || p.inventory.contains(it.item)) }!!

        p.filterableMessage("You swing your pick at the rock.")
        while (true) {
            p.animate(pickaxe.animation)
            it.wait(2)

            if (!canMine(p, obj, rock)) {
                p.animate(-1)
                break
            }

            val level = p.getSkills().getCurrentLevel(Skills.MINING)
            if (level.interpolate(minChance = 30, maxChance = 100, minLvl = 1, maxLvl = 99, cap = 2555)) {
                p.filterableMessage("You manage to mine some ${oreName}.")
                p.playSound(3600)
                p.inventory.add(rock.ore)
                p.addXp(Skills.MINING, rock.xp)

                if (p.world.random(rock.depleteChance) == 0 && rock.depleteChance != 999) {
                    p.animate(-1)

                    if (emptyRock != -1) {
                        val world = p.world
                        world.queue {
                            val emptyRock = DynamicObject(obj, emptyRock)
                            world.remove(obj)
                            world.spawn(emptyRock)
                            wait(rock.respawnTime)
                            world.remove(emptyRock)
                            world.spawn(DynamicObject(obj))
                        }
                    }
                    break
                }
            }
            it.wait(1)
        }
    }

    suspend fun prospect(it: QueueTask, rock: RockType) {
        val player = it.player
        val oreName = player.world.definitions.get(ItemDef::class.java, rock.ore).name.toLowerCase()
        player.lock()
        player.message("You examine the rock for ores...")
        it.wait(3)
        player.message("... this rock contains $oreName.")
        player.unlock()

    }

    private fun canMine(p: Player, obj: GameObject, rock: RockType): Boolean {
        if (!p.world.isSpawned(obj)) {
            return false
        }

        val pickaxe = PickaxeType.values.firstOrNull { p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.equipment.contains(it.item) || p.inventory.contains(it.item)) }
        if (pickaxe == null) {
            p.queue { messageBox("You need a pickaxe to mine this rock. You do not have a pickaxe which you have the mining level to use.") }
            return false
        }

        if (p.getSkills().getMaxLevel(Skills.MINING) < rock.level) {
            p.queue { messageBox("You need a mining level of ${rock.level} to ming this rock.") }
            return false
        }

        if (p.inventory.isFull) {
            p.queue { messageBox("Your inventory is too full to hold any more ${p.world.definitions.get(ItemDef::class.java, rock.ore).name.toLowerCase()}.") }
            return false
        }

        return true
    }
}