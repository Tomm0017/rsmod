package gg.rsmod.game.service.serializer

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.Varp

/**
 * The data that will be decoded/encoded by the [PlayerSerializerService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class PlayerSaveData(val passwordHash: String, val username: String, val displayName: String,
                          val tile: Tile, val privilege: Int, val attributes: HashMap<String, Any>,
                          val varps: List<Varp>)