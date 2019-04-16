package gg.rsmod.plugins.content.skills.thieving.pickpocket

import gg.rsmod.game.model.weight.impl.WeightItem
import gg.rsmod.game.model.weight.impl.WeightItemSet
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Npcs

//Rarity weighting
private const val ALWAYS = 0
private const val COMMON = 256
private const val UNCOMMON = 32
private const val RARE = 8
private const val VERY_RARE = 1

/**
 * @property npcIds an array of NPC npcIds for them to pickpocket
 * @property experience the amount of experienceerience given per pickpocket
 * @property reqLevel the level requirement to pickpocket that npc
 * @property rewards a weighted set of possible item rewards
 * @property damage damage range when getting stunned
 * @property stunTicks the amount of time that the npc stuns the player for
 */
enum class PickpocketNpc(val npcIds: IntArray, val experience: Double, val reqLevel: Int, val npcName: String? = null,
                         val rewards: Array<WeightItem>, val damage: IntRange, val stunTicks: Int) {
    MAN_WOMAN(
            npcIds = intArrayOf(
                    //Man NPC npcIds
                    Npcs.MAN_3014, Npcs.MAN_3078, Npcs.MAN_3079, Npcs.MAN_3080, Npcs.MAN_3081,
                    Npcs.MAN_3082, Npcs.MAN_3101, Npcs.MAN_3109, Npcs.MAN_3260, Npcs.MAN_3264,
                    Npcs.MAN_3265, Npcs.MAN_3266, Npcs.MAN_3652, Npcs.MAN_6987, Npcs.MAN_6988,
                    Npcs.MAN_6989, Npcs.MAN_7547,

                    //Woman NPC Ids
                    Npcs.WOMAN_3015, Npcs.WOMAN_3083, Npcs.WOMAN_3084, Npcs.WOMAN_3085, Npcs.WOMAN_3110,
                    Npcs.WOMAN_3268, Npcs.WOMAN_6990, Npcs.WOMAN_6991, Npcs.WOMAN_6992
            ),
            experience = 8.0,
            reqLevel = 1,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 3, weight = ALWAYS)
            ),
            damage = 1..1,
            stunTicks = 8
    ),
    FARMER(
            npcIds = intArrayOf(
                    Npcs.FARMER_3086, Npcs.FARMER_3087, Npcs.FARMER_3088
            ),
            experience = 14.5,
            reqLevel = 10,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 3, weight = COMMON),
                    WeightItem(item = Items.POTATO_SEED, amount = 1, weight = RARE)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    HAM_FEMALE(
            npcIds = intArrayOf(Npcs.HAM_MEMBER_2541),
            experience = 18.5,
            reqLevel = 15,
            rewards = arrayOf(
                    WeightItem(item = Items.BRONZE_ARROW, amount = 1..15, weight = COMMON),
                    WeightItem(item = Items.BRONZE_AXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.BRONZE_PICKAXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_AXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_DAGGER, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_PICKAXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.BUTTONS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.COINS_995, amount = 1..21, weight = COMMON),
                    WeightItem(item = Items.FEATHER, amount = 1..7, weight = COMMON),
                    WeightItem(item = Items.KNIFE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.LOGS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.NEEDLE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.RAW_ANCHOVIES, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = COMMON),
                    WeightItem(item = Items.THREAD, amount = 2..10, weight = COMMON),
                    WeightItem(item = Items.TINDERBOX, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNCUT_OPAL, amount = 1, weight = COMMON),
                    WeightItem(item = Items.LEATHER_BODY, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_BOOTS, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_CLOAK, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_GLOVES, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_HOOD, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_LOGO, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_SHIRT, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_ARROW, amount = 1..13, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_AXE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_DAGGER, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_PICKAXE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.CLUE_SCROLL_EASY, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.COAL, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.COWHIDE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.DAMAGED_ARMOUR, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_GUAM_LEAF, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_MARRENTILL, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_TARROMIN, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.IRON_ORE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.RUSTY_SWORD, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.UNCUT_JADE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = UNCOMMON)
            ),
            damage = 1..3,
            stunTicks = 6
    ),
    HAM_MALE(
            npcIds = intArrayOf(Npcs.HAM_MEMBER),
            experience = 22.5,
            reqLevel = 20,
            rewards = arrayOf(
                    WeightItem(item = Items.BRONZE_ARROW, amount = 1..15, weight = COMMON),
                    WeightItem(item = Items.BRONZE_AXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.BRONZE_PICKAXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_AXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_DAGGER, amount = 1, weight = COMMON),
                    WeightItem(item = Items.IRON_PICKAXE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.BUTTONS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.COINS_995, amount = 1..21, weight = COMMON),
                    WeightItem(item = Items.FEATHER, amount = 1..7, weight = COMMON),
                    WeightItem(item = Items.KNIFE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.LOGS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.NEEDLE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.RAW_ANCHOVIES, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = COMMON),
                    WeightItem(item = Items.THREAD, amount = 2..10, weight = COMMON),
                    WeightItem(item = Items.TINDERBOX, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNCUT_OPAL, amount = 1, weight = COMMON),
                    WeightItem(item = Items.LEATHER_BODY, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_BOOTS, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_CLOAK, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_GLOVES, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_HOOD, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_LOGO, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.HAM_SHIRT, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_ARROW, amount = 1..13, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_AXE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_DAGGER, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.STEEL_PICKAXE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.CLUE_SCROLL_EASY, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.COAL, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.COWHIDE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.DAMAGED_ARMOUR, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_GUAM_LEAF, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_MARRENTILL, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GRIMY_TARROMIN, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.IRON_ORE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.RUSTY_SWORD, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.UNCUT_JADE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.RAW_CHICKEN, amount = 1, weight = UNCOMMON)
            ),
            damage = 1..3,
            stunTicks = 6
    ),
    ALKHARID_WARRIOR(
            npcIds = intArrayOf(
                    Npcs.ALKHARID_WARRIOR, Npcs.WARRIOR_WOMAN_3100
            ),
            experience = 26.0,
            reqLevel = 25,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 18, weight = ALWAYS)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    ROGUE(
            npcIds = intArrayOf(
                    Npcs.ROGUE_2884
            ),
            experience = 35.5,
            reqLevel = 32,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 25..120, weight = COMMON),
                    WeightItem(item = Items.AIR_RUNE, amount = 8, weight = COMMON),
                    WeightItem(item = Items.LOCKPICK, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GOLD_BAR, amount = 1, weight = RARE),
                    WeightItem(item = Items.IRON_DAGGERP, amount = 1, weight = RARE)
            ),
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
            experience = 40.0,
            reqLevel = 36,
            rewards = arrayOf(
                    WeightItem(item = Items.BAT_SHISH, amount = 1, weight = COMMON),
                    WeightItem(item = Items.COATED_FROGS_LEGS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.FINGERS, amount = 1, weight = COMMON),
                    WeightItem(item = Items.FROGBURGER, amount = 1, weight = COMMON),
                    WeightItem(item = Items.FROGSPAWN_GUMBO, amount = 1, weight = COMMON),
                    WeightItem(item = Items.GREEN_GLOOP_SOUP, amount = 1, weight = COMMON),
                    WeightItem(item = Items.COINS_995, amount = 11..48, weight = UNCOMMON),
                    WeightItem(item = Items.BULLSEYE_LANTERN, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.CAVE_GOBLIN_WIRE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.IRON_ORE, amount = 1..4, weight = UNCOMMON),
                    WeightItem(item = Items.OIL_LANTERN, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.TINDERBOX, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNLIT_TORCH, amount = 1, weight = RARE)
            ),
            damage = 1..1,
            stunTicks = 8
    ),
    MASTER_FARMER(
            npcIds = intArrayOf(
                    Npcs.MASTER_FARMER_3257, Npcs.MASTER_FARMER_3258, Npcs.MARTIN_THE_MASTER_GARDENER
            ),
            experience = 43.0,
            reqLevel = 38,
            rewards = arrayOf(
                    WeightItem(item = Items.POTATO_SEED, amount = 1..4, weight = COMMON),
                    WeightItem(item = Items.ONION_SEED, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.CABBAGE_SEED, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.TOMATO_SEED, amount = 1..2, weight = COMMON),
                    WeightItem(item = Items.SWEETCORN_SEED, amount = 1..2, weight = UNCOMMON),
                    WeightItem(item = Items.STRAWBERRY_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.WATERMELON_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.BARLEY_SEED, amount = 1..4, weight = COMMON),
                    WeightItem(item = Items.HAMMERSTONE_SEED, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.ASGARNIAN_SEED, amount = 1..2, weight = COMMON),
                    WeightItem(item = Items.JUTE_SEED, amount = 1..3, weight = COMMON),
                    WeightItem(item = Items.YANILLIAN_SEED, amount = 1..2, weight = UNCOMMON),
                    WeightItem(item = Items.KRANDORIAN_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.WILDBLOOD_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.MARIGOLD_SEED, amount = 1, weight = COMMON),
                    WeightItem(item = Items.NASTURTIUM_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.ROSEMARY_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.WOAD_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.LIMPWURT_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.REDBERRY_SEED, amount = 1, weight = COMMON),
                    WeightItem(item = Items.CADAVABERRY_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.DWELLBERRY_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.JANGERBERRY_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.WHITEBERRY_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.POISON_IVY_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.GUAM_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.MARRENTILL_SEED, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.TARROMIN_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.HARRALANDER_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.RANARR_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.TOADFLAX_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.IRIT_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.AVANTOE_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.KWUARM_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.SNAPDRAGON_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.CADANTINE_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.LANTADYME_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.DWARF_WEED_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.TORSTOL_SEED, amount = 1, weight = VERY_RARE),
                    WeightItem(item = Items.MUSHROOM_SPORE, amount = 1, weight = RARE),
                    WeightItem(item = Items.BELLADONNA_SEED, amount = 1, weight = RARE),
                    WeightItem(item = Items.CACTUS_SEED, amount = 1, weight = VERY_RARE)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    GUARD(
            npcIds = intArrayOf(
                    Npcs.GUARD_3094
            ),
            experience = 46.8,
            reqLevel = 40,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 30, weight = ALWAYS)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    FREMENNIK_CITIZEN(
            npcIds = intArrayOf(
                    Npcs.AGNAR, Npcs.FREIDIR, Npcs.BORROKAR, Npcs.LANZIG,
                    Npcs.PONTAK, Npcs.FREYGERD_3942, Npcs.LENSA_3943, Npcs.JENNELLA,
                    Npcs.SASSILIK_3945, Npcs.INGA
            ),
            experience = 65.0,
            reqLevel = 45,
            npcName = "Fremennik",
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 30, weight = ALWAYS)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    BEARDED_POLLNIVIAN_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_736, Npcs.BANDIT_737
            ),
            experience = 65.0,
            reqLevel = 45,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 40, weight = ALWAYS)
            ),
            damage = 5..5,
            stunTicks = 8
    ),
    DESERT_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_690, Npcs.BANDIT_695
            ),
            experience = 79.5,
            reqLevel = 53,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 30, weight = COMMON),
                    WeightItem(item = Items.ANTIPOISON3, amount = 30, weight = COMMON),
                    WeightItem(item = Items.LOCKPICK, amount = 30, weight = COMMON)
            ),

            damage = 3..3,
            stunTicks = 8
    ),
    KNIGHT(
            npcIds = intArrayOf(
                    Npcs.KNIGHT_OF_ARDOUGNE_3108, Npcs.KNIGHT_OF_ARDOUGNE_3111
            ),
            experience = 84.3,
            reqLevel = 55,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 50, weight = ALWAYS)
            ),
            damage = 3..3,
            stunTicks = 8
    ),
    POLLNIVIAN_BANDIT(
            npcIds = intArrayOf(
                    Npcs.BANDIT_734, Npcs.BANDIT_735
            ),
            experience = 84.3,
            reqLevel = 55,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 50, weight = ALWAYS)
            ),
            damage = 5..5,
            stunTicks = 8
    ),
    YANILLE_WATCHMAN(
            npcIds = intArrayOf(
                    Npcs.WATCHMAN_3251
            ),
            experience = 137.5,
            reqLevel = 65,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 50, weight = UNCOMMON),
                    WeightItem(item = Items.BREAD, amount = 1, weight = COMMON)
            ),
            damage = 3..3,
            stunTicks = 8
    ),
    MENAPHITE_THUG(
            npcIds = intArrayOf(
                    Npcs.MENAPHITE_THUG_3550
            ),
            experience = 137.5,
            reqLevel = 65,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 60, weight = ALWAYS)
            ),
            damage = 2..2,
            stunTicks = 8
    ),
    PALADIN(
            npcIds = intArrayOf(
                    Npcs.PALADIN_3104, Npcs.PALADIN_3105
            ),
            experience = 151.75,
            reqLevel = 70,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 80, weight = UNCOMMON),
                    WeightItem(item = Items.CHAOS_RUNE, amount = 2, weight = COMMON)
            ),
            damage = 3..3,
            stunTicks = 8
    ),
    GNOME(
            npcIds = intArrayOf(
                    Npcs.GNOME_5130, Npcs.GNOME_CHILD, Npcs.GNOME_CHILD_6078, Npcs.GNOME_CHILD_6079,
                    Npcs.GNOME_WOMAN, Npcs.GNOME_WOMAN_6087, Npcs.GNOME_6094, Npcs.GNOME_6095, Npcs.GNOME_6096

            ),
            experience = 198.5,
            reqLevel = 75,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 300, weight = COMMON),
                    WeightItem(item = Items.EARTH_RUNE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.GOLD_ORE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.FIRE_ORB, amount = 1, weight = COMMON),
                    WeightItem(item = Items.SWAMP_TOAD, amount = 1, weight = COMMON),
                    WeightItem(item = Items.KING_WORM, amount = 1, weight = COMMON)
            ),
            damage = 1..1,
            stunTicks = 8
    ),
    HERO(
            npcIds = intArrayOf(Npcs.HERO_3106),
            experience = 275.0,
            reqLevel = 80,
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 200..300, weight = COMMON),
                    WeightItem(item = Items.DEATH_RUNE, amount = 2, weight = UNCOMMON),
                    WeightItem(item = Items.BLOOD_RUNE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.GOLD_ORE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.FIRE_ORB, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.DIAMOND, amount = 1, weight = UNCOMMON)
            ),
            damage = 4..4,
            stunTicks = 10
    ),
    ELF(
            npcIds = intArrayOf(
                    Npcs.GOREU, Npcs.YSGAWYN, Npcs.ARVEL, Npcs.MAWRTH, Npcs.KELYN, Npcs.SIGMUND_5322, Npcs.SANDY
            ),
            experience = 353.0,
            reqLevel = 85,
            npcName = "Elf",
            rewards = arrayOf(
                    WeightItem(item = Items.COINS_995, amount = 280..350, weight = COMMON),
                    WeightItem(item = Items.DEATH_RUNE, amount = 2, weight = UNCOMMON),
                    WeightItem(item = Items.NATURE_RUNE, amount = 3, weight = UNCOMMON),
                    WeightItem(item = Items.GOLD_ORE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.JUG_OF_WINE, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.FIRE_ORB, amount = 1, weight = UNCOMMON),
                    WeightItem(item = Items.DIAMOND, amount = 1, weight = UNCOMMON)
            ),
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
            experience = 103.5,
            reqLevel = 90,
            rewards = arrayOf(
                    WeightItem(item = Items.TOKKUL, amount = 3..14, weight = COMMON),
                    WeightItem(item = Items.UNCUT_SAPPHIRE, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNCUT_EMERALD, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNCUT_RUBY, amount = 1, weight = COMMON),
                    WeightItem(item = Items.UNCUT_DIAMOND, amount = 1, weight = COMMON)
            ),
            damage = 4..4,
            stunTicks = 8

    );

    val rewardSet = WeightItemSet().apply { rewards.forEach { reward -> add(reward) } }

    companion object {
        val values = enumValues<PickpocketNpc>()
    }
}