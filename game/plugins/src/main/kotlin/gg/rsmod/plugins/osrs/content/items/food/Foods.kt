package gg.rsmod.plugins.osrs.content.items.food

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.helper.hasEquipped
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.content.items.potion.Potions
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.api.helper.playSound

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Foods {

    private val FOOD_DELAY = TimerKey()
    private val COMBO_FOOD_DELAY = TimerKey()

    private const val EAT_FOOD_ANIM = 829
    private const val EAT_FOOD_ON_SLED_ANIM = 1469
    private const val EAT_FOOD_SOUND = 2393

    fun canEat(p: Player, food: Food): Boolean = !p.timers.has(if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY)

    fun eat(p: Player, food: Food) {
        val delay = if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY
        val anim = if (p.hasEquipped(slot = EquipmentType.WEAPON, item = 1469)) EAT_FOOD_ON_SLED_ANIM else EAT_FOOD_ANIM

        val heal = when (food) {
            Food.ANGLERFISH -> {
                val c = when (p.getSkills().getMaxLevel(Skills.HITPOINTS)) {
                    in 25..49 -> 4
                    in 50..74 -> 6
                    in 75..92 -> 8
                    in 93..99 -> 13
                    else -> 2
                }
                Math.floor(p.getSkills().getMaxLevel(Skills.HITPOINTS) / 10.0).toInt() + c
            }
            else -> food.heal
        }

        val oldHp = p.getSkills().getCurrentLevel(Skills.HITPOINTS)
        val foodName = p.world.definitions.get(ItemDef::class.java, food.item).name

        p.animate(anim)
        p.playSound(EAT_FOOD_SOUND)
        if (heal > 0) {
            p.heal(heal, if (food.overheal) heal else 0)
        }

        p.timers[delay] = food.tickDelay
        p.timers[Combat.ATTACK_DELAY] = 5

        if (food == Food.KARAMBWAN) {
            // Eating Karambwans also blocks drinking potions.
            p.timers[Potions.POTION_DELAY] = 3
        }

        when (food) {
            else -> {
                p.message("You eat the ${foodName.toLowerCase()}.")
                if (p.getSkills().getCurrentLevel(Skills.HITPOINTS) > oldHp) {
                    p.message("It heals some health.")
                }
            }
        }
    }

}