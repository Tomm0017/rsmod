package gg.rsmod.plugins.content.skills.thieving.pickpocketing

import gg.rsmod.game.model.weight.impl.WeightItem
import gg.rsmod.game.model.weight.impl.WeightItemSet
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Npcs

/**
 * @npcIds = an array of NPC npcIds for them to pickpocket
 * @exp = the amount of experience given per pickpocket
 * @lvl = the level requirement to pickpocket that npc
 * @npcName = the name of the NPC for the chat messages
 * @rewards = a weighted set of possible item rewards
 * @damage = damage range when getting stunned
 * @stunTicks = the amount of time that the npc stuns the player for
 */
enum class PickpocketNpcs(val npcIds: IntArray, val exp: Double, val lvl: Int, val npcName: String,
                          val rewards: WeightItemSet, val damage: IntRange, val stunTicks: Int) {
    MAN_WOMAN(
            npcIds = intArrayOf(
                    //Man NPC npcIds
                    Npcs.MAN_3014, Npcs.MAN_3078, Npcs.MAN_3079, Npcs.MAN_3080, Npcs.MAN_3081,
                    Npcs.MAN_3082, Npcs.MAN_3101, Npcs.MAN_3109, Npcs.MAN_3260, Npcs.MAN_3264,
                    Npcs.MAN_3265, Npcs.MAN_3266, Npcs.MAN_3652, Npcs.MAN_6987, Npcs.MAN_6988,
                    Npcs.MAN_6989, Npcs.MAN_7547,

                    //Woman NPC Ids
                    Npcs.WOMAN_3015, Npcs.WOMAN_3083, Npcs.WOMAN_3084, Npcs.WOMAN_3085, Npcs.WOMAN_3110,
                    Npcs.WOMAN_3267, Npcs.WOMAN_3268, Npcs.WOMAN_6990, Npcs.WOMAN_6991, Npcs.WOMAN_6992
            ),
            exp = 8.0,
            lvl = 1,
            npcName = "Man/Woman",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 3, weight = Rarity.ALWAYS.weight)),
            damage = 1..1,
            stunTicks = 8
    ),
    FARMER(
            npcIds = intArrayOf(
                    Npcs.FARMER_3086, Npcs.FARMER_3087, Npcs.FARMER_3088
            ),
            exp = 14.5,
            lvl = 10,
            npcName = "Farmer",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.POTATO_SEED, amount = 1, weight = Rarity.RARE.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    HAM_FEMALE(
            npcIds = intArrayOf(Npcs.HAM_MEMBER_2541),
            exp = 18.5,
            lvl = 15,
            npcName = "H.A.M. Member",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.BRONZE_ARROW, amount = 1..15, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BRONZE_AXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BRONZE_PICKAXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_AXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_DAGGER, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_PICKAXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BUTTONS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.COINS_995, amount = 1..21, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FEATHER, amount = 1..7, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.KNIFE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LOGS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.NEEDLE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.RAW_ANCHOVIES, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.THREAD, amount = 2..10, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.TINDERBOX, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_OPAL, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LEATHER_BODY, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_BOOTS, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_CLOAK, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_GLOVES, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_HOOD, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_LOGO, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_SHIRT, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_ARROW, amount = 1..13, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_AXE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_DAGGER, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_PICKAXE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.CLUE_SCROLL_EASY, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.COAL, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.COWHIDE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.DAMAGED_ARMOUR, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_GUAM_LEAF, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_MARRENTILL, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_TARROMIN, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.IRON_ORE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.RUSTY_SWORD, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_JADE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = Rarity.UNCOMMON.weight)),
            damage = 1..3,
            stunTicks = 6
    ),
    HAM_MALE(
            npcIds = intArrayOf(Npcs.HAM_MEMBER),
            exp = 22.5,
            lvl = 20,
            npcName = "H.A.M. Member",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.BRONZE_ARROW, amount = 1..15, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BRONZE_AXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BRONZE_PICKAXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_AXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_DAGGER, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.IRON_PICKAXE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.BUTTONS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.COINS_995, amount = 1..21, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FEATHER, amount = 1..7, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.KNIFE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LOGS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.NEEDLE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.RAW_ANCHOVIES, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.THREAD, amount = 2..10, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.TINDERBOX, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_OPAL, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LEATHER_BODY, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_BOOTS, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_CLOAK, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_GLOVES, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_HOOD, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_LOGO, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.HAM_SHIRT, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_ARROW, amount = 1..13, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_AXE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_DAGGER, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STEEL_PICKAXE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.CLUE_SCROLL_EASY, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.COAL, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.COWHIDE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.DAMAGED_ARMOUR, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_GUAM_LEAF, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_MARRENTILL, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GRIMY_TARROMIN, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.IRON_ORE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.RUSTY_SWORD, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_JADE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = Rarity.UNCOMMON.weight)),
            damage = 1..3,
            stunTicks = 6
    ),
    ALKHARID_WARRIOR(
            npcIds = intArrayOf(
                    Npcs.ALKHARID_WARRIOR, Npcs.WARRIOR_WOMAN_3100
            ),
            exp = 26.0,
            lvl = 25,
            npcName = "Warrior",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 18, weight = Rarity.ALWAYS.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    ROGUE(
            npcIds = intArrayOf(
                    Npcs.ROGUE_2884
            ),
            exp = 35.5,
            lvl = 32,
            npcName = "Rogue",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 25..120, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.AIR_RUNE, amount = 8, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LOCKPICK, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GOLD_BAR, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.IRON_DAGGERP, amount = 1, weight = Rarity.RARE.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    CAVE_GOBLIN(
            npcIds = intArrayOf(
                    Npcs.CAVE_GOBLIN_2268, Npcs.CAVE_GOBLIN_2269, Npcs.CAVE_GOBLIN_2270, Npcs.CAVE_GOBLIN_2271,
                    Npcs.CAVE_GOBLIN_2272, Npcs.CAVE_GOBLIN_2273, Npcs.CAVE_GOBLIN_2274, Npcs.CAVE_GOBLIN_2275,
                    Npcs.CAVE_GOBLIN_2276, Npcs.CAVE_GOBLIN_2277, Npcs.CAVE_GOBLIN_2278, Npcs.CAVE_GOBLIN_2279,
                    Npcs.CAVE_GOBLIN_2280, Npcs.CAVE_GOBLIN_2281, Npcs.CAVE_GOBLIN_2282, Npcs.CAVE_GOBLIN_2283,
                    Npcs.CAVE_GOBLIN_2284, Npcs.CAVE_GOBLIN_2285
            ),
            exp = 40.0,
            lvl = 36,
            npcName = "Cave Goblin",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.BAT_SHISH, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.COATED_FROGS_LEGS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FINGERS, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FROGBURGER, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FROGSPAWN_GUMBO, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.GREEN_GLOOP_SOUP, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.COINS_995, amount = 11..48, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.BULLSEYE_LANTERN, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.CAVE_GOBLIN_WIRE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.IRON_ORE, amount = 1..4, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.OIL_LANTERN, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.TINDERBOX, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNLIT_TORCH, amount = 1, weight = Rarity.RARE.weight)),
            damage = 1..1,
            stunTicks = 8
    ),
    MASTER_FARMER(
            npcIds = intArrayOf(
                    Npcs.MASTER_FARMER_3257, Npcs.MASTER_FARMER_3258, Npcs.MARTIN_THE_MASTER_GARDENER
            ),
            exp = 43.0,
            lvl = 38,
            npcName = "Master Farmer",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.POTATO_SEED, amount = 1..4, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.ONION_SEED, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.CABBAGE_SEED, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.TOMATO_SEED, amount = 1..2, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.SWEETCORN_SEED, amount = 1..2, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.STRAWBERRY_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.WATERMELON_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.BARLEY_SEED, amount = 1..4, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.HAMMERSTONE_SEED, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.ASGARNIAN_SEED, amount = 1..2, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.JUTE_SEED, amount = 1..3, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.YANILLIAN_SEED, amount = 1..2, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.KRANDORIAN_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.WILDBLOOD_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.MARIGOLD_SEED, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.NASTURTIUM_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.ROSEMARY_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.WOAD_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.LIMPWURT_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.REDBERRY_SEED, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.CADAVABERRY_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.DWELLBERRY_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.JANGERBERRY_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.WHITEBERRY_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.POISON_IVY_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.GUAM_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.MARRENTILL_SEED, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.TARROMIN_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.HARRALANDER_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.RANARR_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.TOADFLAX_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.IRIT_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.AVANTOE_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.KWUARM_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.SNAPDRAGON_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.CADANTINE_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.LANTADYME_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.DWARF_WEED_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.TORSTOL_SEED, amount = 1, weight = Rarity.VERY_RARE.weight))
                    .add(WeightItem(item = Items.MUSHROOM_SPORE, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.BELLADONNA_SEED, amount = 1, weight = Rarity.RARE.weight))
                    .add(WeightItem(item = Items.CACTUS_SEED, amount = 1, weight = Rarity.VERY_RARE.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    GUARD(
            npcIds = intArrayOf(
                    Npcs.GUARD_3094
            ),
            exp = 46.8,
            lvl = 40,
            npcName = "Guard",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 30, weight = Rarity.ALWAYS.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    FREMENNIK_CITIZEN(
            npcIds = intArrayOf(
                    Npcs.AGNAR, Npcs.FREIDIR, Npcs.BORROKAR, Npcs.LANZIG,
                    Npcs.PONTAK, Npcs.FREYGERD_3942, Npcs.LENSA_3943, Npcs.JENNELLA,
                    Npcs.SASSILIK_3945, Npcs.INGA
            ),
            exp = 65.0,
            lvl = 45,
            npcName = "Fremennik Citizen",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 30, weight = Rarity.ALWAYS.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    BEARDED_POLLNIVIAN_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_736, Npcs.BANDIT_737
            ),
            exp = 65.0,
            lvl = 45,
            npcName = "Pollnivian Bandit",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 40, weight = Rarity.ALWAYS.weight)),
            damage = 5..5,
            stunTicks = 8
    ),
    DESERT_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_690, Npcs.BANDIT_695
            ),
            exp = 79.5,
            lvl = 53,
            npcName = "Desert Bandit",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 30, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.ANTIPOISON3, amount = 30, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.LOCKPICK, amount = 30, weight = Rarity.COMMON.weight)),

            damage = 3..3,
            stunTicks = 8
    ),
    KNIGHT(
            npcIds = intArrayOf(
                    Npcs.KNIGHT_OF_ARDOUGNE_3108, Npcs.KNIGHT_OF_ARDOUGNE_3111
            ),
            exp = 84.3,
            lvl = 55,
            npcName = "Knight",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 50, weight = Rarity.ALWAYS.weight)),
            damage = 3..3,
            stunTicks = 8
    ),
    POLLNIVIAN_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_734, Npcs.BANDIT_735
            ),
            exp = 84.3,
            lvl = 55,
            npcName = "Pollnivian Bandit",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 50, weight = Rarity.ALWAYS.weight)),
            damage = 5..5,
            stunTicks = 8
    ),
    YANILLE_WATCHMAN(
            npcIds = intArrayOf(
                    Npcs.WATCHMAN_3251
            ),
            exp = 137.5,
            lvl = 65,
            npcName = "Watchman",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 50, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.BREAD, amount = 1, weight = Rarity.COMMON.weight)),
            damage = 3..3,
            stunTicks = 8
    ),
    MENAPHITE_THUG(
            npcIds = intArrayOf(
                    Npcs.MENAPHITE_THUG_3550
            ),
            exp = 137.5,
            lvl = 65,
            npcName = "Menaphite Thug",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 60, weight = Rarity.ALWAYS.weight)),
            damage = 2..2,
            stunTicks = 8
    ),
    PALADIN(
            npcIds = intArrayOf(
                    Npcs.PALADIN_3104, Npcs.PALADIN_3105
            ),
            exp = 151.75,
            lvl = 70,
            npcName = "Paladin",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 80, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.CHAOS_RUNE, amount = 2, weight = Rarity.COMMON.weight)),
            damage = 3..3,
            stunTicks = 8
    ),
    GNOME(
            npcIds = intArrayOf(
                    Npcs.GNOME_5130, Npcs.GNOME_CHILD, Npcs.GNOME_CHILD_6078, Npcs.GNOME_CHILD_6079,
                    Npcs.GNOME_WOMAN, Npcs.GNOME_WOMAN_6087, Npcs.GNOME_6094, Npcs.GNOME_6095, Npcs.GNOME_6096

            ),
            exp = 198.5,
            lvl = 75,
            npcName = "Gnome",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 300, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.EARTH_RUNE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.GOLD_ORE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.FIRE_ORB, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.SWAMP_TOAD, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.KING_WORM, amount = 1, weight = Rarity.COMMON.weight)),
            damage = 1..1,
            stunTicks = 8
    ),
    HERO(
            npcIds = intArrayOf(Npcs.HERO_3106),
            exp = 275.0,
            lvl = 80,
            npcName = "Hero",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 200..300, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.DEATH_RUNE, amount = 2, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.BLOOD_RUNE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GOLD_ORE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.FIRE_ORB, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.DIAMOND, amount = 1, weight = Rarity.UNCOMMON.weight)),
            damage = 4..4,
            stunTicks = 10
    ),
    ELF(
            npcIds = intArrayOf(
                    Npcs.GOREU, Npcs.YSGAWYN, Npcs.ARVEL, Npcs.MAWRTH, Npcs.KELYN, Npcs.SIGMUND_5322, Npcs.SANDY
            ),
            exp = 353.0,
            lvl = 85,
            npcName = "Elf",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.COINS_995, amount = 280..350, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.DEATH_RUNE, amount = 2, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.NATURE_RUNE, amount = 3, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.GOLD_ORE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.FIRE_ORB, amount = 1, weight = Rarity.UNCOMMON.weight))
                    .add(WeightItem(item = Items.DIAMOND, amount = 1, weight = Rarity.UNCOMMON.weight)),
            damage = 5..5,
            stunTicks = 10
    ),
    TZHAAR_HUR(
            npcIds = intArrayOf(
                    Npcs.TZHAARHUR_7682,
                    Npcs.TZHAARHUR_7683,
                    Npcs.TZHAARHUR_7684,
                    Npcs.TZHAARHUR_7685,
                    Npcs.TZHAARHUR_7686,
                    Npcs.TZHAARHUR_7687
            ),
            exp = 103.5,
            lvl = 90,
            npcName = "Tzhaar Hur",
            rewards = WeightItemSet()
                    .add(WeightItem(item = Items.TOKKUL, amount = 3..14, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_SAPPHIRE, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_EMERALD, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_RUBY, amount = 1, weight = Rarity.COMMON.weight))
                    .add(WeightItem(item = Items.UNCUT_DIAMOND, amount = 1, weight = Rarity.COMMON.weight)),
            damage = 4..4,
            stunTicks = 8
    );

    //Enum used for item weighting in the weighted item set
    enum class Rarity(val weight: Int) {
        ALWAYS(weight = 0), COMMON(weight = 256), UNCOMMON(weight = 32), RARE(weight = 8), VERY_RARE(weight = 1);
    }
}