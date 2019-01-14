
import gg.rsmod.plugins.osrs.api.CombatClass
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.api.helper.getInteractingGameObj
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.combat.Combat

r.bindCustomPathingObject(1) {
    val p = it.player()
    val obj = it.getInteractingGameObj()

    val projectile = Combat.createProjectile(source = p, target = obj.tile,
            gfx = 1384, type = ProjectileType.ARROW)
    val hitDelay = Combat.calculateHitDelay(start = p.calculateCentreTile(), target = obj.tile,
            combatClass = CombatClass.RANGED)
    p.world.spawn(projectile)
    p.world.executePlugin {
        it.suspendable {
            it.wait(hitDelay)
            println("hit now!")
        }
    }
}