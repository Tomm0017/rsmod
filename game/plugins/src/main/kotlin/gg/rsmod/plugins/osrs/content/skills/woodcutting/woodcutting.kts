import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.plugins.getInteractingGameObj
import gg.rsmod.plugins.osrs.content.skills.woodcutting.Woodcutting
import gg.rsmod.plugins.osrs.content.skills.woodcutting.WoodcuttingTree

/**
 * @author Tom <rspsmods@gmail.com>
 */

registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1278, trunk = 1342)
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1276, trunk = 1342)
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1286, trunk = 1351) // Dead tree
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1282, trunk = 1347) // Dead tree
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1383, trunk = 1358) // Dead tree
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 1289, trunk = 1353) // Dead tree
registerTree(r, tree = WoodcuttingTree.TREE, treeObj = 2091, trunk = 1342) // Evergreen

registerTree(r, tree = WoodcuttingTree.REDWOOD, treeObj = 29668, trunk = 29669)
registerTree(r, tree = WoodcuttingTree.REDWOOD, treeObj = 29670, trunk = 29671)

fun registerTree(r: PluginRepository, tree: WoodcuttingTree, treeObj: Int, trunk: Int) {
    r.bindObject(treeObj, 1) {
        val obj = it.getInteractingGameObj()
        it.suspendable {
            Woodcutting.chopDownTree(it, obj, tree, trunk)
        }
    }
}