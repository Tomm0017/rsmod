package gg.rsmod.plugins.osrs.content.skills.woodcutting

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.api.Skills

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Woodcutting {

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1278, trunk = 1342)
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1276, trunk = 1342)
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1286, trunk = 1351) // Dead tree
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1282, trunk = 1347) // Dead tree
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1383, trunk = 1358) // Dead tree
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1289, trunk = 1353) // Dead tree
        registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 2091, trunk = 1342) // Evergreen

        registerTree(r, tree = WoodcuttingTree.REDWOOD, treeObj = 29668, trunk = 29669)
        registerTree(r, tree = WoodcuttingTree.REDWOOD, treeObj = 29670, trunk = 29671)
    }

    private fun registerTree(r: PluginRepository, tree: WoodcuttingTree, treeObj: Int, trunk: Int) {
        r.bindObject(treeObj, 1) {
            val obj = it.getInteractingGameObj()
            it.suspendable {
                chopDownTree(it, obj, tree, trunk)
            }
        }
    }

    private suspend fun chopDownTree(it: Plugin, obj: GameObject, tree: WoodcuttingTree, trunkId: Int) {
        val p = it.player()

        if (!canChop(p, obj, tree)) {
            return
        }

        val logName = p.world.definitions.get(ItemDef::class.java, tree.log).name
        val axe = WoodcuttingAxe.values().firstOrNull { p.getSkills().getMaxLevel(Skills.WOODCUTTING) >= it.level && p.inventory.hasItem(it.item) }!!

        p.filterableMessage("You swing your axe at the tree.")
        while (true) {
            p.animate(axe.animation)
            it.wait(2)

            if (!canChop(p, obj, tree)) {
                p.animate(-1)
                break
            }

            if (p.world.random(100) <= (100 - tree.level)) {
                p.filterableMessage("You get some ${logName}s.")
                p.inventory.add(tree.log)

                if (p.world.random(tree.depleteChance) == 0) {
                    p.playSound(3600)
                    p.animate(-1)
                    it.player().addXp(Skills.WOODCUTTING, tree.xp)

                    if (trunkId != -1) {
                        val world = p.world
                        world.executePlugin {
                            it.suspendable {
                                val trunk = DynamicObject(trunkId, obj)

                                world.remove(obj)
                                world.spawn(trunk)
                                it.wait(tree.respawnTime.random())
                                world.remove(trunk)
                                world.spawn(DynamicObject(obj))
                            }
                        }
                    }
                    break
                }
            }
            it.wait(2)
        }
    }

    private fun canChop(p: Player, obj: GameObject, tree: WoodcuttingTree): Boolean {
        if (!p.world.isSpawned(obj)) {
            return false
        }

        val axe = WoodcuttingAxe.values().firstOrNull { p.getSkills().getMaxLevel(Skills.WOODCUTTING) >= it.level && p.inventory.hasItem(it.item) }
        if (axe == null) {
            p.playSound(2277)
            p.message("You do not have an axe which you have the woodcutting level to use.")
            return false
        }

        if (p.getSkills().getMaxLevel(Skills.WOODCUTTING) < tree.level) {
            p.playSound(2277)
            p.message("You need a Woodcutting level of ${tree.level} to chop down this tree.")
            return false
        }

        if (p.inventory.isFull()) {
            p.playSound(2277)
            p.message("Your inventory is too full to hold any more logs.")
            return false
        }

        return true
    }
}