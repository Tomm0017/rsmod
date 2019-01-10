package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.INTERACTING_ITEM_SLOT
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.hasEquipped
import gg.rsmod.plugins.osrs.api.Equipment
import gg.rsmod.plugins.osrs.api.Items
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.playSound
import gg.rsmod.plugins.player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Foods {

    private val FOOD_DELAY = TimerKey()
    private val COMBO_FOOD_DELAY = TimerKey()

    private const val EAT_FOOD_ANIM = 829
    private const val EAT_FOOD_ON_SLED_ANIM = 1469
    private const val EAT_FOOD_SOUND = 2393

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        Food.values().forEach { food ->
            r.bindItem(food.item, 1) {
                val p = it.player()

                if (!canEat(p, food)) {
                    return@bindItem
                }

                val inventorySlot = it.player().attr[INTERACTING_ITEM_SLOT]
                if (p.inventory.remove(id = food.item, beginSlot = inventorySlot).hasSucceeded()) {
                    eat(p, food)
                    if (food.replacement != -1) {
                        p.inventory.add(id = food.replacement, beginSlot = inventorySlot)
                    }
                }
            }
        }
    }

    fun canEat(p: Player, food: Food): Boolean = !p.timers.has(if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY)

    fun eat(p: Player, food: Food) {
        val delay = if (food.comboFood) COMBO_FOOD_DELAY else FOOD_DELAY
        val anim = if (p.hasEquipped(slot = Equipment.WEAPON, item = 1469)) EAT_FOOD_ON_SLED_ANIM else EAT_FOOD_ANIM

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

    enum class Food(val item: Int, val heal: Int = 0, val overheal: Boolean = false,
                    val replacement: Int = -1, val tickDelay: Int = 3,
                    val comboFood: Boolean = false) {

        /**
         * Sea food.
         */
        SHRIMP(item = Items.SHRIMPS, heal = 3),
        SARDINE(item = Items.SARDINE, heal = 4),
        HERRING(item = Items.HERRING, heal = 5),
        MACKEREL(item = Items.MACKEREL, heal = 6),
        TROUT(item = Items.TROUT, heal = 7),
        SALMON(item = Items.SALMON, heal = 9),
        TUNA(item = Items.TUNA, heal = 10),
        LOBSTER(item = Items.LOBSTER, heal = 12),
        BASS(item = Items.BASS, heal = 13),
        SWORDFISH(item = Items.SWORDFISH, heal = 14),
        MONKFISH(item = Items.MONKFISH, heal = 16),
        KARAMBWAN(item = Items.COOKED_KARAMBWAN, heal = 18, comboFood = true),
        SHARK(item = Items.SHARK, heal = 20),
        MANTA_RAY(item = Items.MANTA_RAY, heal = 21),
        DARK_CRAB(item = Items.DARK_CRAB, heal = 22),
        ANGLERFISH(item = Items.ANGLERFISH, overheal = true),

        /**
         * Meat.
         */
        CHICKEN(item = Items.COOKED_CHICKEN, heal = 4),
        MEAT(item = Items.COOKED_MEAT, heal = 4),

        /**
         * Pastries.
         */
        BREAD(item = Items.BREAD, heal = 5),
    }
}