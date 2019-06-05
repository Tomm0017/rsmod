package gg.rsmod.plugins.content.combat.strategy.magic

import gg.rsmod.game.model.Graphic

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class CombatSpell(val id: Int, val maxHit: Int, val castAnimation: Int, val castSound: Int,
                       val castGfx: Graphic?, val projectile: Int, val projectilEndHeight: Int = -1,
                       val impactGfx: Graphic?, val autoCastId: Int, val baseXp: Double = 0.0) {

    /**
     * Standard.
     */
    WIND_STRIKE(id = 3273, maxHit = 2, castAnimation = 711, castSound = 220, castGfx = Graphic(id = 90, height = 92),
            projectile = 91, impactGfx = Graphic(id = 92, height = 124), autoCastId = 1, baseXp = 5.5),

    WATER_STRIKE(id = 3275, maxHit = 4, castAnimation = 711, castSound = 211, castGfx = Graphic(id = 93, height = 92),
            projectile = 94, impactGfx = Graphic(id = 95, height = 124), autoCastId = 2, baseXp = 7.5),

    EARTH_STRIKE(id = 3277, maxHit = 6, castAnimation = 711, castSound = 132, castGfx = Graphic(id = 96, height = 92),
            projectile = 97, impactGfx = Graphic(id = 98, height = 124), autoCastId = 3, baseXp = 9.5),

    FIRE_STRIKE(id = 3279, maxHit = 8, castAnimation = 711, castSound = 160, castGfx = Graphic(id = 99, height = 92),
            projectile = 100, impactGfx = Graphic(id = 101, height = 124), autoCastId = 4, baseXp = 11.5),


    WIND_BOLT(id = 3281, maxHit = 9, castAnimation = 711, castSound = 218, castGfx = Graphic(id = 117, height = 92),
            projectile = 118, impactGfx = Graphic(id = 119, height = 124), autoCastId = 5, baseXp = 13.5),

    WATER_BOLT(id = 3285, maxHit = 10, castAnimation = 711, castSound = 209, castGfx = Graphic(id = 120, height = 92),
            projectile = 121, impactGfx = Graphic(id = 122, height = 124), autoCastId = 6, baseXp = 16.5),

    EARTH_BOLT(id = 3288, maxHit = 11, castAnimation = 711, castSound = 130, castGfx = Graphic(id = 123, height = 92),
            projectile = 124, impactGfx = Graphic(id = 125, height = 124), autoCastId = 7, baseXp = 19.5),

    FIRE_BOLT(id = 3291, maxHit = 12, castAnimation = 711, castSound = 157, castGfx = Graphic(id = 126, height = 92),
            projectile = 127, impactGfx = Graphic(id = 128, height = 124), autoCastId = 8, baseXp = 22.5),


    WIND_BLAST(id = 3294, maxHit = 13, castAnimation = 711, castSound = 216, castGfx = Graphic(id = 132, height = 92),
            projectile = 133, impactGfx = Graphic(id = 134, height = 124), autoCastId = 9, baseXp = 25.5),

    WATER_BLAST(id = 3297, maxHit = 14, castAnimation = 711, castSound = 207, castGfx = Graphic(id = 135, height = 92),
            projectile = 136, impactGfx = Graphic(id = 137, height = 124), autoCastId = 10, baseXp = 28.5),

    EARTH_BLAST(id = 3302, maxHit = 15, castAnimation = 711, castSound = 128, castGfx = Graphic(id = 138, height = 92),
            projectile = 139, impactGfx = Graphic(id = 140, height = 124), autoCastId = 11, baseXp = 31.5),

    FIRE_BLAST(id = 3307, maxHit = 16, castAnimation = 711, castSound = 155, castGfx = Graphic(id = 129, height = 92),
            projectile = 130, impactGfx = Graphic(id = 131, height = 124), autoCastId = 12, baseXp = 34.5),


    WIND_WAVE(id = 3313, maxHit = 17, castAnimation = 727, castSound = 222, castGfx = Graphic(id = 158, height = 92),
            projectile = 159, impactGfx = Graphic(id = 160, height = 124), autoCastId = 13, baseXp = 36.0),

    WATER_WAVE(id = 3315, maxHit = 18, castAnimation = 727, castSound = 213, castGfx = Graphic(id = 161, height = 92),
            projectile = 162, impactGfx = Graphic(id = 163, height = 124), autoCastId = 14, baseXp = 37.5),

    EARTH_WAVE(id = 3319, maxHit = 19, castAnimation = 727, castSound = 134, castGfx = Graphic(id = 164, height = 92),
            projectile = 165, impactGfx = Graphic(id = 166, height = 124), autoCastId = 15, baseXp = 40.0),

    FIRE_WAVE(id = 3321, maxHit = 20, castAnimation = 727, castSound = 162, castGfx = Graphic(id = 155, height = 92),
            projectile = 156, impactGfx = Graphic(id = 157, height = 124), autoCastId = 16, baseXp = 42.5),


    WIND_SURGE(id = 21876, maxHit = 21, castAnimation = 7855, castSound = 4028, castGfx = Graphic(id = 1455, height = 92),
            projectile = 1456, impactGfx = Graphic(id = 1457, height = 124), autoCastId = 48, baseXp = 44.5),

    WATER_SURGE(id = 21877, maxHit = 22, castAnimation = 7855, castSound = 4030, castGfx = Graphic(id = 1458, height = 92),
            projectile = 1459, impactGfx = Graphic(id = 1460, height = 124), autoCastId = 49, baseXp = 46.5),

    EARTH_SURGE(id = 21878, maxHit = 23, castAnimation = 7855, castSound = 4025, castGfx = Graphic(id = 1461, height = 92),
            projectile = 1462, impactGfx = Graphic(id = 1463, height = 124), autoCastId = 50, baseXp = 48.5),

    FIRE_SURGE(id = 21879, maxHit = 24, castAnimation = 7855, castSound = 4032, castGfx = Graphic(id = 1464, height = 92),
            projectile = 1465, impactGfx = Graphic(id = 1466, height = 124), autoCastId = 51, baseXp = 50.5),

    /**
     * Ancient.
     */
    SMOKE_RUSH(id = 4629, maxHit = 14, castAnimation = 1978, castSound = 183, castGfx = null,
            projectile = 384, impactGfx = Graphic(id = 385, height = 0), autoCastId = 31, baseXp = 30.0),

    SHADOW_RUSH(id = 4630, maxHit = 15, castAnimation = 1978, castSound = 178, castGfx = null,
            projectile = 378, impactGfx = Graphic(id = 379, height = 0), autoCastId = 32, baseXp = 31.0),

    BLOOD_RUSH(id = 4632, maxHit = 16, castAnimation = 1978, castSound = 106, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 373, height = 0), autoCastId = 33, baseXp = 33.0),

    ICE_RUSH(id = 4633, maxHit = 17, castAnimation = 1978, castSound = 171, castGfx = null,
            projectile = 360, impactGfx = Graphic(id = 361, height = 0), autoCastId = 34,
            projectilEndHeight = 0, baseXp = 34.0),


    SMOKE_BURST(id = 4635, maxHit = 18, castAnimation = 1979, castSound = 183, castGfx = null,
            projectile = 388, impactGfx = Graphic(id = 389, height = 0), autoCastId = 35, baseXp = 36.0),

    SHADOW_BURST(id = 4636, maxHit = 19, castAnimation = 1979, castSound = 178, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 382, height = 0), autoCastId = 36, baseXp = 37.0),

    BLOOD_BURST(id = 4638, maxHit = 21, castAnimation = 1979, castSound = 469, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 376, height = 0), autoCastId = 37, baseXp = 39.0),

    ICE_BURST(id = 4639, maxHit = 22, castAnimation = 1979, castSound = 171, castGfx = null,
            projectile = 366, impactGfx = Graphic(id = 389, height = 0), autoCastId = 38,
            projectilEndHeight = 0, baseXp = 40.0),


    SMOKE_BLITZ(id = 4641, maxHit = 23, castAnimation = 1978, castSound = 183, castGfx = null,
            projectile = 386, impactGfx = Graphic(id = 387, height = 124), autoCastId = 39, baseXp = 42.0),

    SHADOW_BLITZ(id = 4642, maxHit = 24, castAnimation = 1978, castSound = 178, castGfx = null,
            projectile = 380, impactGfx = Graphic(id = 381, height = 0), autoCastId = 40,
            projectilEndHeight = 0, baseXp = 43.0),

    BLOOD_BLITZ(id = 4644, maxHit = 25, castAnimation = 1978, castSound = 106, castGfx = null,
            projectile = 374, impactGfx = Graphic(id = 375, height = 0), autoCastId = 41,
            projectilEndHeight = 0, baseXp = 45.0),

    ICE_BLITZ(id = 4645, maxHit = 26, castAnimation = 1978, castSound = 171, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 367, height = 0), autoCastId = 42, baseXp = 46.0),


    SMOKE_BARRAGE(id = 4647, maxHit = 27, castAnimation = 1979, castSound = 183, castGfx = null,
            projectile = 390, impactGfx = Graphic(id = 391, height = 124), autoCastId = 43, baseXp = 48.0),

    SHADOW_BARRAGE(id = 4648, maxHit = 28, castAnimation = 1979, castSound = 178, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 383, height = 0), autoCastId = 44, baseXp = 49.0),

    BLOOD_BARRAGE(id = 4650, maxHit = 29, castAnimation = 1979, castSound = 106, castGfx = null,
            projectile = 0, impactGfx = Graphic(id = 377, height = 0), autoCastId = 45, baseXp = 51.0),

    ICE_BARRAGE(id = 4651, maxHit = 30, castAnimation = 1979, castSound = 171, castGfx = null,
            projectile = 368, impactGfx = Graphic(id = 369, height = 0), autoCastId = 46,
            projectilEndHeight = 0, baseXp = 52.0),
    ;

    companion object {
        val values = enumValues<CombatSpell>()
    }
}