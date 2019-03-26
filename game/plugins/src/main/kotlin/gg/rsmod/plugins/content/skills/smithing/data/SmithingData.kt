package gg.rsmod.plugins.content.skills.smithing.data

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.fs.def.ItemDef

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the various items that can be smithed
 *
 * @param defs  The definition set for the game world
 */
class SmithingData(private val defs: DefinitionSet) {

    /**
     * The enum definition containing the number of items that are produced
     * when smithing a specified item
     */
    private val producedCountEnum = defs.get(EnumDef::class.java, PRODUCED_COUNT_ENUM)

    /**
     * The enum definition containing the number of bars required to produce
     * each item
     */
    private val barCountEnum = defs.get(EnumDef::class.java, BARS_COUNT_ENUM)

    /**
     * The enum definition containing the levels required for each producible item
     */
    private val levelReqsEnum = defs.get(EnumDef::class.java, LEVEL_REQ_ENUM)

    /**
     * The enum definition containing the bars that may be smithed
     */
    val smithableBarsEnum = defs.get(EnumDef::class.java, SMITHABLE_BARS_ENUM)

    /**
     * A map of producible items to their names
     */
    private val itemNames = levelReqsEnum.values.map { it.key }.associate { it to defs.get(ItemDef::class.java, it).name.toLowerCase() }

    /**
     * Maps an item id to the meta data required to craft it
     */
    private val metaData : List<SmithingMetaData> = levelReqsEnum.values.map {
        (id, level) -> SmithingMetaData(
            id = id,
            name = itemNames.getValue(id),
            bar = getBar(itemNames.getValue(id)),
            barCount = barCountEnum.getInt(id),
            numProduced = producedCountEnum.getInt(id),
            level = level as Int)
    }

    /**
     * A map of bars to the smithing meta data they can be produced into
     */
    val barItemData  = metaData.groupBy { it.bar }

    /**
     * The map of bar item ids to their smithing index
     */
    val barIndices : Map<Int, Int> = smithableBarsEnum.values.entries.associate { it.value as Int to it.key }

    companion object {

        /**
         * The enum id that contains the number of items that are produced, to allow for
         * items such as Nails making 15 rather than 1.
         */
        private const val PRODUCED_COUNT_ENUM = 844

        /**
         * The enum id that contains the number of bars required to produce each item
         */
        private const val BARS_COUNT_ENUM = 845

        /**
         * The enum id that contains the Smithing level required to produce each item
         */
        private const val LEVEL_REQ_ENUM = 846

        /**
         * The enum id that contains the bars that may be smithed, and their index
         */
        private const val SMITHABLE_BARS_ENUM = 1253
    }
}