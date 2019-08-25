package gg.rsmod.game.model.droptable

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import org.json.JSONObject
import kotlin.random.Random

class ItemDrop(json: String): JSONObject(json) {
    var itemId: Int = this.optInt("itemId")
    var quanityMin: Int = this.optInt("quanityMin")
    var quanityMax: Int = this.optInt("quanityMax")

}
/**Thanks to Nbness2*/
fun ItemDrop.createItem(): Item = Item(itemId, Random.nextInt(quanityMin, quanityMax + 1))
fun Item.toGroundItem(tile: Tile, owner: Player? = null) = GroundItem(item = this, tile = tile, owner = owner)