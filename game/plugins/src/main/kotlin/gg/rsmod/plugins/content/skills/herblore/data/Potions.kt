package gg.rsmod.plugins.content.skills.herblore.data

import gg.rsmod.plugins.api.cfg.Items

var Tarromin = Items.TARROMIN

enum class Potions(val potion: Int, val unfinished: Int, val prefix: String, val firstingreident: Int, val secondingreident: Int, val level: Int, val herbloreXp: Double = 0.0) {

    ATTACK_POTION(potion = Items.ATTACK_POTION3, unfinished = Items.GUAM_POTION_UNF, prefix = "attack potion", firstingreident = Items.GUAM_LEAF, secondingreident = Items.EYE_OF_NEWT, level = 3, herbloreXp = 25.0),
    ANTI_POISON(potion = Items.ANTIPOISON3, unfinished = Items.MARRENTILL_POTION_UNF, prefix = "antipoison potion", firstingreident = Items.MARRENTILL, secondingreident = Items.UNICORN_HORN_DUST, level = 5, herbloreXp = 37.5),
    RELICYMS_BALM(potion = Items.RELICYMS_BALM3, unfinished = Items.UNFINISHED_POTION_4840, prefix = "relicym's balm", firstingreident = Items.ROGUES_PURSE, secondingreident = Items.SNAKE_WEED, level = 8, herbloreXp = 40.0),
    STRENGTH_POTION(potion = Items.STRENGTH_POTION3, unfinished = Items.TARROMIN_POTION_UNF, prefix = "strength potion", firstingreident = Tarromin, secondingreident = Items.LIMPWURT_ROOT, level = 12, herbloreXp = 50.0),
    //SERUM_207(potion = Items.SERUM_207_3, unfinished = Items.TARROMIN_POTION_UNF, prefix = "serum 207", firstingreident = Tarromin, secondingreident = Items.ASHES, level = 15, herbloreXp = 50.0),
    COMPOST_POTION(potion = Items.COMPOST_POTION3, unfinished = Items.HARRALANDER_POTION_UNF, prefix = "compost potion", firstingreident = Items.HARRALANDER, secondingreident = Items.VOLCANIC_ASH, level = 22, herbloreXp = 60.0),
    //RESTORE_POTION(potion = Items.RESTORE_POTION3, unfinished = Items.HARRALANDER_POTION_UNF, prefix = "restore potion", firstingreident = Items.HARRALANDER, secondingreident = Items.RED_SPIDERS_EGGS, level = 22, herbloreXp = 62.5),
    GUTHIX_BALANCE(potion = Items.GUTHIX_BALANCE3, unfinished = Items.RESTORE_POTION3, prefix = "guthix balance", firstingreident = Items.GARLIC, secondingreident = Items.SILVER_DUST, level = 22, herbloreXp = 50.0),
    //BLAMISH_OIL(potion = Items.BLAMISH_OIL, unfinished = Items.HARRALANDER_POTION_UNF, prefix = "blamish oil", firstingreident = Items.HARRALANDER, secondingreident = Items.BLAMISH_SNAIL_SLIME, level = 25, herbloreXp = 80.0),
    //ENERGY_POTION(potion = Items.ENERGY_POTION3, unfinished = Items.HARRALANDER_POTION_UNF, prefix = "energy potion", firstingreident = Items.HARRALANDER, secondingreident = Items.CHOCOLATE_DUST, level = 26, herbloreXp = 67.5),
    DEFENCE_POTION(potion = Items.DEFENCE_POTION3, unfinished = Items.RANARR_POTION_UNF, prefix = "defence potion", firstingreident = Items.RANARR_WEED, secondingreident = Items.WHITE_BERRIES, level = 30, herbloreXp = 75.0),
    AGILITY_POTION(potion = Items.AGILITY_POTION3, unfinished = Items.TOADFLAX_POTION_UNF, prefix = "agility potion", firstingreident = Items.TOADFLAX, secondingreident = Items.TOADS_LEGS, level = 34, herbloreXp = 80.0),
    //COMBAT_POTION(potion = Items.COMBAT_POTION3, unfinished = Items.HARRALANDER_POTION_UNF, prefix = "combat potion", firstingreident = Items.HARRALANDER, secondingreident = Items.GOAT_HORN_DUST, level = 36, herbloreXp = 84.0),
    //PRAYER_POTION(potion = Items.PRAYER_POTION3, unfinished = Items.RANARR_POTION_UNF, prefix = "prayer potion", firstingreident = Items.RANARR_WEED, secondingreident = Items.SNAPE_GRASS, level = 38, herbloreXp = 87.5),
    SUPER_ATTACK(potion = Items.SUPER_ATTACK3, unfinished = Items.IRIT_POTION_UNF, prefix = "super attack potion", firstingreident = Items.IRIT_LEAF, secondingreident = Items.EYE_OF_NEWT, level = 45, herbloreXp = 100.0),
    //SUPER_ANTIPOISON(potion = Items.SUPERANTIPOISON3, unfinished = Items.IRIT_POTION_UNF, prefix = "super antipoison potion", firstingreident = Items.IRIT_LEAF, secondingreident = Items.UNICORN_HORN_DUST, level = 48, herbloreXp = 106.3),
    FISHING_POTION(potion = Items.FISHING_POTION3, unfinished = Items.AVANTOE_POTION_UNF, prefix = "fishing potion", firstingreident = Items.AVANTOE, secondingreident = Items.SNAPE_GRASS, level = 50, herbloreXp = 112.5),
    //SUPER_ENERGY(potion = Items.SUPER_ENERGY3, unfinished = Items.AVANTOE_POTION_UNF, prefix = "super energy potion", firstingreident = Items.AVANTOE, secondingreident = Items.MORT_MYRE_FUNGUS, level = 52, herbloreXp = 117.5),
    //SHRINK_ME_QUICK(potion = Items.SHRINKMEQUICK, unfinished = Items.TARROMIN_POTION_UNF, prefix = "shrink me quick", firstingreident = Items.TARROMIN, secondingreident = Items.SHRUNK_OGLEROOT, level = 52, herbloreXp = 6.0),
    //HUNTER_POTION(potion = Items.HUNTER_POTION3, unfinished = Items.AVANTOE_POTION_UNF, prefix = "hunter potion", firstingreident = Items.AVANTOE, secondingreident = Items.KEBBIT_TEETH_DUST, level = 53, herbloreXp = 120.0),
    SUPER_STRENGTH(potion = Items.SUPER_STRENGTH3, unfinished = Items.KWUARM_POTION_UNF, prefix = "super strength potion", firstingreident = Items.KWUARM, secondingreident = Items.LIMPWURT_ROOT, level = 55, herbloreXp = 125.0),
    MAGIC_ESSENCE(potion = Items.MAGIC_ESSENCE3, unfinished = Items.MAGIC_ESSENCE_UNF, prefix = "magic essence potion", firstingreident = Items.STAR_FLOWER, secondingreident = Items.GORAK_CLAW_POWDER, level = 57, herbloreXp = 130.0),
    //WEAPON_POISON(potion = Items.WEAPON_POISON, unfinished = Items.KWUARM_POTION_UNF, prefix = "weapon poison", firstingreident = Items.KWUARM, secondingreident = Items.DRAGON_SCALE_DUST, level = 60, herbloreXp = 137.5),
    SUPER_RESTORE(potion = Items.SUPER_RESTORE3, unfinished = Items.SNAPDRAGON_POTION_UNF, prefix = "super restore potion", firstingreident = Items.SNAPDRAGON, secondingreident = Items.RED_SPIDERS_EGGS, level = 63, herbloreXp = 142.5),
    SUPER_DEFENCE(potion = Items.SUPER_DEFENCE3, unfinished = Items.CADANTINE_POTION_UNF, prefix = "super defence potion", firstingreident = Items.CADANTINE, secondingreident = Items.WHITE_BERRIES, level = 66, herbloreXp = 150.0),
    //ANTIDOTE_PLUS(potion = Items.ANTIDOTE3_5954, unfinished = Items.TOADFLAX_POTION_UNF, prefix = "antidote+", firstingreident = Items.TOADFLAX, secondingreident = Items.YEW_ROOTS, level = 68, herbloreXp = 155.0),
    ANTIFIRE_POTION(potion = Items.ANTIFIRE_POTION3, unfinished = Items.LANTADYME_POTION_UNF, prefix = "antifire potion", firstingreident = Items.LANTADYME, secondingreident = Items.DRAGON_SCALE_DUST, level = 69, herbloreXp = 157.5),
    RANGING_POTION(potion = Items.RANGING_POTION3, unfinished = Items.DWARF_WEED_POTION_UNF, prefix = "ranging potion", firstingreident = Items.DWARF_WEED, secondingreident = Items.WINE_OF_ZAMORAK, level = 72, herbloreXp = 162.5),
    WEAPON_POISON_PLUS(potion = Items.WEAPON_POISON_5937, unfinished = Items.COCONUT_MILK, prefix = "weapon poison+", firstingreident = Items.CACTUS_SPINE, secondingreident = Items.RED_SPIDERS_EGGS, level = 73, herbloreXp = 165.0),
    //MAGIC_POTION(potion = Items.MAGIC_POTION3, unfinished = Items.LANTADYME_POTION_UNF, prefix = "magic potion", firstingreident = Items.LANTADYME, secondingreident = Items.POTATO_CACTUS, level = 76, herbloreXp = 172.5),
    ZAMORAK_BREW(potion = Items.ZAMORAK_BREW3, unfinished = Items.TORSTOL_POTION_UNF, prefix = "zamorak brew", firstingreident = Items.TORSTOL, secondingreident = Items.JANGERBERRIES, level = 78, herbloreXp = 175.0),
    //ANTIDOTE_PLUSS(potion = Items.ANTIDOTE3, unfinished = Items.IRIT_POTION_UNF, prefix = "antidote potion", firstingreident = Items.IRIT_LEAF, secondingreident = Items.MAGIC_ROOTS, level = 79, herbloreXp = 177.5),
    //BASTION_POTION(potion = Items.BASTION_POTION3, unfinished = Items.CADANTINE_BLOOD_POTION_UNF, prefix = "bastion potion", firstingreident = Items.CADANTINE, secondingreident = Items.WINE_OF_ZAMORAK, level = 80, herbloreXp = 155.0),
    //BATTLEMAGE_POTION(potion = Items.BATTLEMAGE_POTION3, unfinished = Items.CADANTINE_BLOOD_POTION_UNF, prefix = "battlemage potion", firstingreident = Items.CADANTINE, secondingreident = Items.POTATO_CACTUS, level = 80, herbloreXp = 155.0),
    //SARADOMIN_BREW(potion = Items.SARADOMIN_BREW3, unfinished = Items.TOADFLAX_POTION_UNF, prefix = "saradomin brew", firstingreident = Items.TOADFLAX, secondingreident = Items.CRUSHED_NEST, level = 81, herbloreXp = 180.0),
    WEAPON_POISON_PLUSS(potion = Items.WEAPON_POISON_5940, unfinished = Items.COCONUT_MILK, prefix = "weapon poison++", firstingreident = Items.CAVE_NIGHTSHADE, secondingreident = Items.POISON_IVY_BERRIES, level = 82, herbloreXp = 190.0);

    companion object {
        /**
         * The cached array of enum definitions
         */
        val values = enumValues<Grimy>()
    }
}