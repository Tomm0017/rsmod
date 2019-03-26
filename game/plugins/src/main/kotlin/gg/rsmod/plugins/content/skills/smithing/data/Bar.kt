package gg.rsmod.plugins.content.skills.smithing.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Triston Plummer ("Dread')
 *
 * Represents a bar that may be smelted, or smithed into an item
 *
 * @param id                The id of the bar
 * @param primaryOre        The primary ore of the bar
 * @param secondaryOre      The secondary ore of the bar
 * @param secondaryCount    The number of secondary ore required for the bar
 * @param level             The level required to smelt the bar
 * @param smeltXp           The amount of xp gained for smelting the bar
 * @param smithXp           The amount of xp gained per bar smithed
 */
enum class Bar(val id: Int, val prefix: String, val primaryOre: Int, val secondaryOre: Int = Items.COAL, val secondaryCount: Int = 1, val level: Int, val smeltXp: Double, val smithXp: Double = 0.0) {

    BRONZE(id = Items.BRONZE_BAR, prefix = "bronze", primaryOre = Items.TIN_ORE, secondaryOre = Items.COPPER_ORE, level = 1, smeltXp = 6.2, smithXp = 12.5),
    BLURITE(id = Items.BLURITE_BAR, prefix = "blurite", primaryOre = Items.BLURITE_ORE, secondaryCount = 0, level = 8, smeltXp = 8.0),
    IRON(id = Items.IRON_BAR, prefix = "iron", primaryOre = Items.IRON_ORE, secondaryCount = 0, level = 15, smeltXp = 12.5, smithXp = 25.0),
    SILVER(id = Items.SILVER_BAR, prefix = "silver", primaryOre = Items.SILVER_ORE, secondaryCount = 0, level = 20, smeltXp = 13.7),
    STEEL(id = Items.STEEL_BAR, prefix = "steel", primaryOre = Items.IRON_ORE, secondaryCount = 2, level = 30, smeltXp = 17.5, smithXp = 37.5),
    GOLD(id = Items.GOLD_BAR, prefix = "gold", primaryOre = Items.GOLD_ORE, secondaryCount = 0, level = 40, smeltXp = 22.5),
    LOVAKITE(id = Items.LOVAKITE_BAR, prefix = "shayzien", primaryOre = Items.LOVAKITE_ORE, secondaryCount = 2, level = 45, smeltXp = 20.0, smithXp = 10.0),
    MITHRIL(id = Items.MITHRIL_BAR, prefix = "mithril", primaryOre = Items.MITHRIL_ORE, secondaryCount = 4, level = 50, smeltXp = 30.0, smithXp = 50.0),
    ADAMANT(id = Items.ADAMANTITE_BAR, prefix = "adamant", primaryOre = Items.ADAMANTITE_ORE, secondaryCount = 6, level = 70, smeltXp = 37.5, smithXp = 62.5),
    RUNE(id = Items.RUNITE_BAR, prefix = "rune", primaryOre = Items.RUNITE_ORE, secondaryCount = 8, level = 85, smeltXp = 50.0, smithXp = 75.0);

    companion object {

        /**
         * The cached array of enum definitions
         */
        val values = enumValues<Bar>()

        /**
         * The map of bar ids to their definitions
         */
        val barDefinitions = values.associate { it.id to it }
    }
}