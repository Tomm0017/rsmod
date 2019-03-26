package gg.rsmod.plugins.content.skills.smithing.data

/**
 * @author Triston Plummer ("Dread")
 *
 * @param id            The item id
 * @param name          The name of the item
 * @param bar           The bar that is required to create the smithing item
 * @param barCount      The number of bars required to create the item
 * @param numProduced   The number of items produced
 * @param level         The level required to produce this item
 */
data class SmithingMetaData(val id: Int, val name: String, val bar: Bar?, val barCount: Int, val numProduced: Int, val level: Int)