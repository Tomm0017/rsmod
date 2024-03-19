package gg.rsmod.game.model.weightedTableBuilder

enum class TableTypes {
    ALWAYS,
    PRE_ROLL,
    MAIN,
    TERTIARY
}
data class itemDrop(
    val itemId: Int,
    val min: Int,
    val max: Int,
    val weight: Int?,
    val description: String? = null,
    val block: Unit.() -> Boolean = { true }
)

data class tableDrops(
    val tableType: TableTypes,
    val tableWeight: Int,
    val tableRolls: Int,
    val drops: Set<itemDrop>
)


