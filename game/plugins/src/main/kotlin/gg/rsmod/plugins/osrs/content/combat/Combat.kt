package gg.rsmod.plugins.osrs.content.combat

import gg.rsmod.game.model.COMBAT_TARGET_FOCUS
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.entity.Projectile
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.api.WeaponType
import gg.rsmod.plugins.osrs.api.helper.getEquipment
import gg.rsmod.plugins.osrs.api.helper.hasWeaponType
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.MeleeCombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Combat {

    private var cachedItemStats: ItemStatsService? = null

    private const val DEFAULT_ATTACK_SPEED = 4

    private const val MIN_ATTACK_SPEED = 1

    val ATTACK_DELAY = TimerKey()

    fun reset(pawn: Pawn) {
        pawn.attr.remove(COMBAT_TARGET_FOCUS)
    }

    fun getCombatStrategy(pawn: Pawn): CombatStrategy {
        /**
         * TODO(Tom): add a way to specify what combat class npcs are currently using,
         * and also add a way to load in a custom combat script for npcs to use instead
         * of this script.
         */
        if (pawn !is Player) {
            return MeleeCombatStrategy
        }

        if (pawn.hasWeaponType(WeaponType.BOW, WeaponType.CHINCHOMPA, WeaponType.CROSSBOW, WeaponType.THROWN)) {
            return RangedCombatStrategy
        }

        return MeleeCombatStrategy
    }

    fun getAttackDelay(pawn: Pawn): Int {
        if (pawn is Npc) {
            // TODO: get attack delay from npc combat defs
            return 3
        }
        if (pawn is Player) {
            val weapon = pawn.getEquipment(EquipmentType.WEAPON) ?: return DEFAULT_ATTACK_SPEED
            val stats = getItemStats(pawn.world) ?: return DEFAULT_ATTACK_SPEED
            val weaponStats = stats.get(weapon.id) ?: return DEFAULT_ATTACK_SPEED
            return Math.max(MIN_ATTACK_SPEED, weaponStats.attackSpeed)
        }
        return DEFAULT_ATTACK_SPEED
    }

    fun createProjectile(source: Pawn, target: Tile, gfx: Int, type: ProjectileType): Projectile {
        val distance = source.calculateCentreTile().getDistance(target)

        val builder = Projectile.Builder()
                .setTiles(start = source.calculateCentreTile(), target = target)
                .setGfx(gfx = gfx)
                .setHeights(startHeight = type.startHeight, endHeight = type.endHeight)
                .setSlope(angle = type.angle, steepness = type.steepness)
                .setTimes(delay = type.delay, lifespan = type.delay + type.calculateLife(distance))

        return builder.build()
    }

    fun createProjectile(source: Pawn, target: Pawn, gfx: Int, type: ProjectileType): Projectile {
        val distance = source.calculateCentreTile().getDistance(target.calculateCentreTile())
        val builder = Projectile.Builder()
                .setTiles(start = source.calculateCentreTile(), target = target)
                .setGfx(gfx = gfx)
                .setHeights(startHeight = type.startHeight, endHeight = type.endHeight)
                .setSlope(angle = type.angle, steepness = type.steepness)
                .setTimes(delay = type.delay, lifespan = type.delay + type.calculateLife(distance))

        return builder.build()
    }

    private fun getItemStats(world: World): ItemStatsService? {
        if (cachedItemStats == null) {
            cachedItemStats = world.getService(ItemStatsService::class.java, searchSubclasses = false).orElse(null)
        }
        return cachedItemStats
    }
}