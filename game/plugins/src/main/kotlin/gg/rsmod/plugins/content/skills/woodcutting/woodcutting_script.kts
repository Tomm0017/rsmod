package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.content.skills.woodcutting.Woodcutting.Tree

private val TREES = setOf(
        Tree(TreeType.TREE, obj = 1278, trunk = 1342),
        Tree(TreeType.TREE, obj = 1276, trunk = 1342),
        Tree(TreeType.TREE, obj = 1286, trunk = 1351), // Dead tree
        Tree(TreeType.TREE, obj = 1282, trunk = 1347), // Dead tree
        Tree(TreeType.TREE, obj = 1383, trunk = 1358), // Dead tree
        Tree(TreeType.TREE, obj = 1289, trunk = 1353), // Dead tree
        Tree(TreeType.TREE, obj = 2091, trunk = 1342), // Evergreen

        Tree(TreeType.YEW, obj = 1753, trunk = 9714),
        Tree(TreeType.YEW, obj = 1754, trunk = 9714),

        Tree(TreeType.REDWOOD, obj = 29668, trunk = 29669),
        Tree(TreeType.REDWOOD, obj = 29670, trunk = 29671)
)

TREES.forEach { tree ->
    on_obj_option(obj = tree.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue {
            Woodcutting.chopDownTree(this, obj, tree.type, tree.trunk)
        }
    }
}