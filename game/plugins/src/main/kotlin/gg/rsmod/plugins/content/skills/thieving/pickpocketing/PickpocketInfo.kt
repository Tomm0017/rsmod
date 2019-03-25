package gg.rsmod.plugins.content.skills.thieving.pickpocketing

import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.weight.WeightNode
import gg.rsmod.game.model.weight.WeightNodeSet
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Npcs

/**
 * @ids = an array of NPC ids for them to pickpocket
 * @exp = the amount of experience given per pickpocket
 * @lvl = the level requirement to pickpocket that npc
 * @npcName = the name of the NPC for the chat messages
 * @rewards = a 2d array of rewards
 */
enum class PickpocketInfo(val ids: IntArray, val exp: Double, val lvl: Int, val npcName: String,
                          val rewards: WeightNodeSet<Item>, val damage: IntRange, val stunTicks: Int) {
    MAN_WOMAN(
            ids = intArrayOf(
                    //Man NPC ID's
                    Npcs.MAN_3078, Npcs.MAN_3079, Npcs.MAN_3080, Npcs.MAN_3081, Npcs.MAN_3082,
                    Npcs.MAN_1118, Npcs.MAN_1138, Npcs.MAN_3014, Npcs.MAN_3101, Npcs.MAN_3109,
                    Npcs.MAN_3260, Npcs.MAN_3264, Npcs.MAN_3265, Npcs.MAN_3266, Npcs.MAN_3652,
                    Npcs.MAN_385, Npcs.MAN_4268, Npcs.MAN_4269, Npcs.MAN_4270, Npcs.MAN_4271,
                    Npcs.MAN_4272, Npcs.MAN_6776, Npcs.MAN_6987, Npcs.MAN_6988, Npcs.MAN_6989,
                    Npcs.MAN_7281, Npcs.MAN_7547, Npcs.MAN_7919, Npcs.MAN_7920,
                    //Woman NPC ID's
                    Npcs.WOMAN_1130, Npcs.WOMAN_1131, Npcs.WOMAN_1139, Npcs.WOMAN_1140, Npcs.WOMAN_1141,
                    Npcs.WOMAN_1142, Npcs.WOMAN_3015, Npcs.WOMAN_3083, Npcs.WOMAN_3084, Npcs.WOMAN_3085,
                    Npcs.WOMAN_3110, Npcs.WOMAN_3267, Npcs.WOMAN_3268, Npcs.WOMAN_4958, Npcs.WOMAN_6990,
                    Npcs.WOMAN_6991, Npcs.WOMAN_6992, Npcs.WOMAN_7921, Npcs.WOMAN_7922, Npcs.WOMAN
            ),
            exp = 8.0,
            lvl = 1,
            npcName = "Man/Woman",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 3))),
            damage = 1..1,
            stunTicks = 8
    ),
    FARMER(
            ids = intArrayOf(
                    Npcs.FARMER_3086, Npcs.FARMER_3087, Npcs.FARMER_3088, Npcs.FARMER_3089, Npcs.FARMER_3090,
                    Npcs.FARMER_3091, Npcs.FARMER_3672, Npcs.FARMER_6947, Npcs.FARMER_6948, Npcs.FARMER_6949,
                    Npcs.FARMER_6950, Npcs.FARMER_6951, Npcs.FARMER_6952, Npcs.FARMER_6959, Npcs.FARMER_6960,
                    Npcs.FARMER_6961
            ),
            exp = 14.5,
            lvl = 10,
            npcName = "Farmer",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COINS_995, 3)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.POTATO_SEED, 1))),
            damage = 2..2,
            stunTicks = 8
    ),
    HAM_FEMALE(
            ids = intArrayOf(Npcs.HAM_MEMBER_2541),
            exp = 18.5,
            lvl = 15,
            npcName = "H.A.M. Member",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_ARROW, (1..15).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_DAGGER, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BUTTONS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COINS_995, (1..21).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.FEATHER, (1..7).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.KNIFE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.LOGS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.NEEDLE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.RAW_ANCHOVIES, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.RAW_CHICKEN, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.THREAD, (2..10).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.TINDERBOX, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.UNCUT_OPAL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.LEATHER_BODY, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_BOOTS, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_CLOAK, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_GLOVES, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_HOOD, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_LOGO, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_SHIRT, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_ARROW, (1..13).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_DAGGER, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.CLUE_SCROLL_EASY, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COAL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COWHIDE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.DAMAGED_ARMOUR, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_GUAM_LEAF, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_MARRENTILL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_TARROMIN, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.IRON_ORE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.RUSTY_SWORD, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.UNCUT_JADE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.RAW_CHICKEN, 1))),
            damage = 1..3,
            stunTicks = 6
    ),
    HAM_MALE(
            ids = intArrayOf(Npcs.HAM_MEMBER),
            exp = 22.5,
            lvl = 20,
            npcName = "H.A.M. Member",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_ARROW, (1..15).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BRONZE_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_DAGGER, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.IRON_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BUTTONS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COINS_995, (1..21).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.FEATHER, (1..7).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.KNIFE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.LOGS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.NEEDLE, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.RAW_ANCHOVIES, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.RAW_CHICKEN, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.THREAD, (2..10).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.TINDERBOX, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.UNCUT_OPAL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.LEATHER_BODY, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_BOOTS, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_CLOAK, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_GLOVES, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_HOOD, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_LOGO, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.HAM_SHIRT, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_ARROW, (1..13).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_AXE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_DAGGER, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STEEL_PICKAXE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.CLUE_SCROLL_EASY, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COAL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COWHIDE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.DAMAGED_ARMOUR, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_GUAM_LEAF, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_MARRENTILL, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GRIMY_TARROMIN, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.IRON_ORE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.RUSTY_SWORD, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.UNCUT_JADE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.RAW_CHICKEN, 1))),
            damage = 1..3,
            stunTicks = 6
    ),
    ALKHARID_WARRIOR(
            ids = intArrayOf(Npcs.ALKHARID_WARRIOR, Npcs.WARRIOR_WOMAN_3100),
            exp = 26.0,
            lvl = 25,
            npcName = "Warrior",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 18))),
            damage = 2..2,
            stunTicks = 8
    ),
    ROGUE(
            ids = intArrayOf(Npcs.ROGUE_2884),
            exp = 35.5,
            lvl = 32,
            npcName = "Rogue",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COINS_995, (25..120).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.AIR_RUNE, 8)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.LOCKPICK, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.JUG_OF_WINE, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.GOLD_BAR, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.IRON_DAGGERP, 1))),
            damage = 2..2,
            stunTicks = 8
    ),
    CAVE_GOBLIN(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 40.0,
            lvl = 36,
            npcName = "Cave Goblin",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BAT_SHISH, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COATED_FROGS_LEGS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.FINGERS, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.FROGBURGER, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.FROGSPAWN_GUMBO, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.GREEN_GLOOP_SOUP, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COINS_995, (11..48).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.BULLSEYE_LANTERN, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.CAVE_GOBLIN_WIRE, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.IRON_ORE, (1..4).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.OIL_LANTERN, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.TINDERBOX, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.UNLIT_TORCH, 1))),
            damage = 1..1,
            stunTicks = 8
    ),
    MASTER_FARMER(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 43.0,
            lvl = 38,
            npcName = "Master Farmer",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.POTATO_SEED, (1..4).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.ONION_SEED, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.CABBAGE_SEED, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.TOMATO_SEED, (1..2).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.SWEETCORN_SEED, (1..2).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.STRAWBERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.WATERMELON_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BARLEY_SEED, (1..4).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.HAMMERSTONE_SEED, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.ASGARNIAN_SEED, (1..2).random())))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.JUTE_SEED, (1..3).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.YANILLIAN_SEED, (1..2).random())))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.KRANDORIAN_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.WILDBLOOD_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.MARIGOLD_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.NASTURTIUM_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.ROSEMARY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.WOAD_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.LIMPWURT_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.REDBERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.CADAVABERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.DWELLBERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.JANGERBERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.WHITEBERRY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.POISON_IVY_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.GUAM_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.MARRENTILL_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.TARROMIN_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.HARRALANDER_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.RANARR_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.TOADFLAX_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.IRIT_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.AVANTOE_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.KWUARM_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.SNAPDRAGON_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.CADANTINE_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.LANTADYME_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.DWARF_WEED_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.TORSTOL_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.MUSHROOM_SPORE, 1)))
                    .add(WeightNode<Item>(Rarity.RARE.ordinal, Item(Items.BELLADONNA_SEED, 1)))
                    .add(WeightNode<Item>(Rarity.VERY_RARE.ordinal, Item(Items.CACTUS_SEED, 1))),
            damage = 2..2,
            stunTicks = 8
    ),
    GUARD(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 46.8,
            lvl = 40,
            npcName = "Guard",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 30))),
            damage = 2..2,
            stunTicks = 8
    ),
    FREMENNIK_CITIZEN(
            ids = intArrayOf(-1 /*TODO: Find fremmy citizen id*/),
            exp = 65.0,
            lvl = 45,
            npcName = "Fremennik Citizen",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 30))),
            damage = 2..2,
            stunTicks = 8
    ),
    BEARDED_POLLNIVIAN_BANDIT(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 65.0,
            lvl = 45,
            npcName = "Pollnivian Bandit",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 40))),
            damage = 5..5,
            stunTicks = 8
    ),
    DESERT_BANDIT(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 79.5,
            lvl = 53,
            npcName = "Desert Bandit",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.COINS_995, 30)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.ANTIPOISON3, 30)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.LOCKPICK, 30))),
            damage = 3..3,
            stunTicks = 8
    ),
    KNIGHT(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 84.3,
            lvl = 55,
            npcName = "Knight",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 50))),
            damage = 3..3,
            stunTicks = 8
    ),
    POLLNIVIAN_BANDIT(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 84.3,
            lvl = 55,
            npcName = "Pollnivian Bandit",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 50))),
            damage = 5..5,
            stunTicks = 8
    ),
    YANILLE_WATCHMAN(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 137.5,
            lvl = 65,
            npcName = "Watchman",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COINS_995, 50)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.BREAD, 1))),
            damage = 3..3,
            stunTicks = 8
    ),
    MENAPHITE_THUG(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 137.5,
            lvl = 65,
            npcName = "Menaphite Thug",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.ALWAYS.ordinal, Item(Items.COINS_995, 60))),
            damage = 2..2,
            stunTicks = 8
    ),
    PALADIN(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 151.75,
            lvl = 70,
            npcName = "Paladin",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>(Rarity.UNCOMMON.ordinal, Item(Items.COINS_995, 80)))
                    .add(WeightNode<Item>(Rarity.COMMON.ordinal, Item(Items.CHAOS_RUNE, 2))),
            damage = 3..3,
            stunTicks = 8
    ),
    GNOME(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 198.5,
            lvl = 75,
            npcName = "Gnome",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.COINS_995, 300)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.EARTH_RUNE, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.GOLD_ORE, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.FIRE_ORB, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.SWAMP_TOAD, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.KING_WORM, 1))),
            damage = 1..1,
            stunTicks = 8
    ),
    HERO(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 275.0,
            lvl = 80,
            npcName = "Hero",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.COINS, (200..300).random())))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.DEATH_RUNE, 2)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.BLOOD_RUNE, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.GOLD_ORE, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.JUG_OF_WINE, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.FIRE_ORB, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.DIAMOND, 1))),
            damage = 4..4,
            stunTicks = 10
    ),
    ELF(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 353.0,
            lvl = 85,
            npcName = "Elf",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.COINS, (280..350).random())))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.DEATH_RUNE, 2)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.NATURE_RUNE, 3)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.GOLD_ORE, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.JUG_OF_WINE, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.FIRE_ORB, 1)))
                    .add(WeightNode<Item>( Rarity.UNCOMMON.ordinal,Item(Items.DIAMOND, 1))),
            damage = 5..5,
            stunTicks = 10
    ),
    TZHAAR_HUR(
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 103.5,
            lvl = 90,
            npcName = "Tzhaar Hur",
            rewards = WeightNodeSet<Item>()
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.TOKKUL, (3..14).random())))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.UNCUT_SAPPHIRE, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.UNCUT_EMERALD, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.UNCUT_RUBY, 1)))
                    .add(WeightNode<Item>( Rarity.COMMON.ordinal,Item(Items.UNCUT_DIAMOND, 1))),
            damage = 4..4,
            stunTicks = 8
    );

    enum class Rarity(value: Int) {
        ALWAYS(value = 0), COMMON(value = 1), UNCOMMON(value = 2), RARE(value = 3), VERY_RARE(value = 4);
    }
}