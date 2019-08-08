package gg.rsmod.game.model.droptable

import org.json.JSONObject

class DropTable(json: String): JSONObject(json) {
    val percentage: Double = this.optDouble("percentage")
    val items: List<ItemDrop>? = this.optJSONArray("items")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
            ?.map { ItemDrop(it.toString()) }

    fun DropTable.getPercentage(): Double {
        return percentage
    }

    fun DropTable.getItems(): List<ItemDrop>? {
        return items
    }
}