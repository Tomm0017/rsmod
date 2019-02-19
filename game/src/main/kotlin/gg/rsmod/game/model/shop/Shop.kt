package gg.rsmod.game.model.shop

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Shop(@JsonProperty("name") val name: String,
                @JsonProperty("stock_type") val stockType: StockType,
                @JsonProperty("stock") val items: Array<ShopItem?>) {

    companion object {
        const val DEFAULT_STOCK_SIZE = 28
        const val DEFAULT_RESUPPLY_AMOUNT = 1
        const val DEFAULT_RESUPPLY_CYCLES = 25
    }

    val viewers = hashSetOf<Any>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shop

        if (name != other.name) return false
        if (stockType != other.stockType) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + stockType.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}