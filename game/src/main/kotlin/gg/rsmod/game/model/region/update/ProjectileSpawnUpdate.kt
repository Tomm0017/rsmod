package gg.rsmod.game.model.region.update

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.SpawnProjectileMessage
import gg.rsmod.game.model.entity.Projectile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ProjectileSpawnUpdate(override val type: EntityUpdateType,
                            override val entity: Projectile) : EntityUpdate<Projectile>(type, entity) {

    override fun toMessage(): Message {
        if (entity.targetPawn != null) {
            return SpawnProjectileMessage(
                    start = ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7),
                    pawnTargetIndex = entity.targetPawn.index, offsetX = 0, offsetZ = 0,
                    gfx = entity.gfx, startHeight = entity.startHeight, endHeight = entity.endHeight,
                    delay = entity.delay, lifespan = entity.lifespan, angle = entity.angle, steepness = entity.steepness)
        }
        val target = entity.targetTile!!
        return SpawnProjectileMessage(
                start = ((entity.tile.x and 0x7) shl 4) or (entity.tile.z and 0x7),
                pawnTargetIndex = 0, offsetX = target.x - entity.tile.x, offsetZ = target.z - entity.tile.z,
                gfx = entity.gfx, startHeight = entity.startHeight, endHeight = entity.endHeight, delay = entity.delay,
                lifespan = entity.lifespan, angle = entity.angle, steepness = entity.steepness)
    }
}