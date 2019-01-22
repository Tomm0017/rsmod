package gg.rsmod.plugins.osrs.content.combat.strategy.magic

import gg.rsmod.game.model.Graphic

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class CombatSpell(val id: Int, val castAnimation: Int, val castSound: Int, val castGfx: Graphic,
                       val projectile: Int, val impactGfx: Graphic, val autoCastId: Int) {

    WIND_STRIKE(id = 0, castAnimation = 711, castSound = 220, castGfx = Graphic(id = 90, height = 92),
            projectile = 91, impactGfx = Graphic(id = 92, height = 124), autoCastId = 1),

    WATER_STRIKE(id = 1, castAnimation = 711, castSound = 211, castGfx = Graphic(id = 93, height = 92),
            projectile = 94, impactGfx = Graphic(id = 95, height = 124), autoCastId = 2),

    EARTH_STRIKE(id = 2, castAnimation = 711, castSound = 132, castGfx = Graphic(id = 96, height = 92),
            projectile = 97, impactGfx = Graphic(id = 98, height = 124), autoCastId = 3),

    FIRE_STRIKE(id = 3, castAnimation = 711, castSound = 160, castGfx = Graphic(id = 99, height = 92),
            projectile = 100, impactGfx = Graphic(id = 101, height = 124), autoCastId = 4),


    WIND_BOLT(id = 5, castAnimation = 711, castSound = 218, castGfx = Graphic(id = 117, height = 92),
            projectile = 118, impactGfx = Graphic(id = 119, height = 124), autoCastId = 5),

    WATER_BOLT(id = 6, castAnimation = 711, castSound = 209, castGfx = Graphic(id = 120, height = 92),
            projectile = 121, impactGfx = Graphic(id = 122, height = 124), autoCastId = 6),

    EARTH_BOLT(id = 7, castAnimation = 711, castSound = 130, castGfx = Graphic(id = 123, height = 92),
            projectile = 124, impactGfx = Graphic(id = 125, height = 124), autoCastId = 7),

    FIRE_BOLT(id = 8, castAnimation = 711, castSound = 157, castGfx = Graphic(id = 126, height = 92),
            projectile = 127, impactGfx = Graphic(id = 128, height = 124), autoCastId = 8),


    WIND_BLAST(id = 9, castAnimation = 711, castSound = 216, castGfx = Graphic(id = 132, height = 92),
            projectile = 133, impactGfx = Graphic(id = 134, height = 124), autoCastId = 9),

    WATER_BLAST(id = 10, castAnimation = 711, castSound = 207, castGfx = Graphic(id = 135, height = 92),
            projectile = 136, impactGfx = Graphic(id = 137, height = 124), autoCastId = 10),

    EARTH_BLAST(id = 11, castAnimation = 711, castSound = 128, castGfx = Graphic(id = 138, height = 92),
            projectile = 139, impactGfx = Graphic(id = 140, height = 124), autoCastId = 11),

    FIRE_BLAST(id = 13, castAnimation = 711, castSound = 155, castGfx = Graphic(id = 129, height = 92),
            projectile = 130, impactGfx = Graphic(id = 131, height = 124), autoCastId = 12),


    WIND_WAVE(id = 14, castAnimation = 727, castSound = 222, castGfx = Graphic(id = 158, height = 92),
            projectile = 159, impactGfx = Graphic(id = 160, height = 124), autoCastId = 13),

    WATER_WAVE(id = 305, castAnimation = 727, castSound = 213, castGfx = Graphic(id = 161, height = 92),
            projectile = 162, impactGfx = Graphic(id = 163, height = 124), autoCastId = 14),

    EARTH_WAVE(id = 306, castAnimation = 727, castSound = 134, castGfx = Graphic(id = 164, height = 92),
            projectile = 165, impactGfx = Graphic(id = 166, height = 124), autoCastId = 15),

    FIRE_WAVE(id = 307, castAnimation = 727, castSound = 162, castGfx = Graphic(id = 155, height = 92),
            projectile = 156, impactGfx = Graphic(id = 157, height = 124), autoCastId = 16),


    WIND_SURGE(id = 1098, castAnimation = 7855, castSound = 4028, castGfx = Graphic(id = 1455, height = 92),
            projectile = 1456, impactGfx = Graphic(id = 1457, height = 124), autoCastId = 48),

    WATER_SURGE(id = 1099, castAnimation = 7855, castSound = 4030, castGfx = Graphic(id = 1458, height = 92),
            projectile = 1459, impactGfx = Graphic(id = 1460, height = 124), autoCastId = 49),

    EARTH_SURGE(id = 1100, castAnimation = 7855, castSound = 4025, castGfx = Graphic(id = 1461, height = 92),
            projectile = 1462, impactGfx = Graphic(id = 1463, height = 124), autoCastId = 50),

    FIRE_SURGE(id = 1751, castAnimation = 7855, castSound = 4032, castGfx = Graphic(id = 1464, height = 92),
            projectile = 1465, impactGfx = Graphic(id = 1466, height = 124), autoCastId = 51),
}