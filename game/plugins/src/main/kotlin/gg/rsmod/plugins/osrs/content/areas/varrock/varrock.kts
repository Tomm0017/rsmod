
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.Tile
import gg.rsmod.plugins.osrs.api.helper.getInteractingGameObj
import gg.rsmod.plugins.osrs.api.helper.isPrivilegeEligible
import gg.rsmod.plugins.osrs.api.helper.player

/**
 * Museum staircase to basement.
 */
r.bindCustomPathingObject(24428) {
    val p = it.player()
    val obj = it.getInteractingGameObj()
    it.suspendable {
        val validTiles = arrayOf(Tile(3257, 3450), Tile(3258, 3451), Tile(3258, 3452), Tile(3258, 3453), Tile(3257, 3454))
        p.walkTo(obj.tile, MovementQueue.StepType.NORMAL, validSurroundingTiles = validTiles)
        while (p.tile !in validTiles) {
            it.wait(1)
        }
        it.wait(1)
        if (p.isPrivilegeEligible(Privilege.DEV_POWER)) {
            p.message("Unhandled staircase - varrock script.")
        }
    }
}