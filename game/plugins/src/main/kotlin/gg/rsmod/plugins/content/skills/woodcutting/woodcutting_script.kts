package gg.rsmod.plugins.content.skills.woodcutting

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.content.skills.woodcutting.Woodcutting.Tree

val trees = ImmutableSet.of(
        Tree(TreeType.TREE, obj = 3648, trunk = 3649), //Dying Tree
        Tree(TreeType.TREE, obj = 1278, trunk = 1342), //Normal
        Tree(TreeType.TREE, obj = 1279, trunk = 1342), //Normal
        Tree(TreeType.TREE, obj = 1276, trunk = 1342), //Normal
        Tree(TreeType.TREE, obj = 1286, trunk = 1351), // Dead tree
        Tree(TreeType.TREE, obj = 1282, trunk = 1347), // Dead tree
        Tree(TreeType.TREE, obj = 1383, trunk = 1358), // Dead tree
        Tree(TreeType.TREE, obj = 1289, trunk = 1353), // Dead tree
        Tree(TreeType.TREE, obj = 2091, trunk = 1342), // Evergreen
        Tree(TreeType.ACHEY, obj = 2023, trunk = 3371), //Achey
        Tree(TreeType.OAK, obj = 1751, trunk = 1356), //Oak
        Tree(TreeType.WILLOW, obj = 1750, trunk = 9177), //Willow
        Tree(TreeType.WILLOW, obj = 1756, trunk = 9471), //Willow
        Tree(TreeType.WILLOW, obj = 1758, trunk = 9471), //Willow
        Tree(TreeType.WILLOW, obj = 1760, trunk = 9471), //Willow
        Tree(TreeType.TEAK, obj = 9036, trunk = 9037), //Teak
        Tree(TreeType.MAPLE, obj = 1759, trunk = 9712), //Maple
        Tree(TreeType.HOLLOW, obj = 1752, trunk = 4061), //Hollow
        Tree(TreeType.MAHOGANY, obj = 9034, trunk = 9035), //Mahogany
        Tree(TreeType.YEW, obj = 1753, trunk = 9714), //Yew
        Tree(TreeType.YEW, obj = 1754, trunk = 9714), //Yew
        Tree(TreeType.MAGIC, obj = 1761, trunk = 9713), //Magic

        Tree(TreeType.YEW, obj = 1753, trunk = 9714),
        Tree(TreeType.YEW, obj = 1754, trunk = 9714),

        Tree(TreeType.REDWOOD, obj = 29668, trunk = 29669),
        Tree(TreeType.REDWOOD, obj = 29670, trunk = 29671)
)!!

trees.forEach { tree ->
    on_obj_option(obj = tree.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue { Woodcutting.chopDownTree(this, obj, tree.type, tree.trunk) }
    }
}