package gg.rsmod.game.model.entity

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.World

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Npc(override val world: World) : Pawn(world) {

    override fun isDead(): Boolean = false

    override fun isRunning(): Boolean = false

    override fun cycle() {

    }

    override fun getType(): EntityType = EntityType.NPC
}