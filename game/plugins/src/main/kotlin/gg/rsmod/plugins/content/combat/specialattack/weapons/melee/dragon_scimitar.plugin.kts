package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.mechanics.prayer.Prayer
import gg.rsmod.plugins.content.mechanics.prayer.PrayerGroup
import gg.rsmod.plugins.content.mechanics.prayer.Prayers

val SPECIAL_REQUIREMENT = 55

SpecialAttacks.register(
        weapons = intArrayOf(Items.DRAGON_SCIMITAR),
        energy = SPECIAL_REQUIREMENT,
        excutionType = ExecutionType.EXECUTE_ON_ATTACK,
        attack = {
            player.animate(id = 1872)
            player.graphic(id = 347, height = 100)
            world.spawn(AreaSound(tile = player.tile, id = 2537, radius = 10, volume = 1))

            for (i in 0 until 1) {
                val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0)
                val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
                val landHit = accuracy >= world.randomDouble()
                val delay = if (target.entityType.isNpc) i + 1 else 1
                player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = delay)
                if (target is Player && landHit) {
                    Prayers.deactivate(target as Player, Prayer.PROTECT_FROM_MAGIC)
                    Prayers.deactivate(target as Player, Prayer.PROTECT_FROM_MISSILES)
                    Prayers.deactivate(target as Player, Prayer.PROTECT_FROM_MELEE)
                    Prayers.disableOverheads(target as Player, 12, PrayerGroup.OVERHEAD_PROTECTION)
                }
            }
        })