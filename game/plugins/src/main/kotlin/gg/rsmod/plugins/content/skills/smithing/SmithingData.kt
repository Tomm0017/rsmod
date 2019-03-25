package gg.rsmod.plugins.content.skills.smithing

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.plugins.api.cfg.Items

class SmithingData(private val defs: DefinitionSet) {

    private val smithItemAmounts = defs.get(EnumDef::class.java, 844)

    private val smithItemRequirements = defs.get(EnumDef::class.java, 845)

    private val smithLevelRequirements = defs.get(EnumDef::class.java, 846)

    private val smithingMetaData = HashMap<Int, SmithingMetaData>()

    fun load() {

        // Loop through the amounts that are produced for each item
        smithItemAmounts.values.forEach { key, value ->

            // The definition of the item
            val def = defs.get(ItemDef::class.java, key)

            // The bar used to create this item, and the number of this item that is produced
            val bar = getBar(def.name.toLowerCase())
            val count = value as Int

            // The level requirement for the item
            val level = smithLevelRequirements.getInt(key)

            // The number of bars required for the bar
            val barsRequired = smithItemRequirements.getInt(key)

            // Store the metadata in the map
            smithingMetaData[key] = SmithingMetaData(item = key, bar = bar, numItemsProduced = count, numBarsRequired = barsRequired, level = level)
        }

        smithingMetaData.values.filter { meta -> meta.bar == Items.RUNITE_BAR }.forEach { println(it) }
    }

    fun getBar(name: String) : Int = when {
        name.startsWith("bronze") -> Items.BRONZE_BAR
        name.startsWith("steel") -> Items.STEEL_BAR
        name.startsWith("mithril") -> Items.MITHRIL_BAR
        name.startsWith("adamant") -> Items.ADAMANTITE_BAR
        name.startsWith("rune") -> Items.RUNITE_BAR
        name.startsWith("shayzien") -> Items.LOVAKITE_BAR
        else -> Items.NULL
    }
}