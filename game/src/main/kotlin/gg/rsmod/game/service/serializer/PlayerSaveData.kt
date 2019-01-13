package gg.rsmod.game.service.serializer

import gg.rsmod.game.model.TimerSystem
import gg.rsmod.game.model.Varp
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.service.serializer.json.JsonPlayerSerializer

/**
 * The data that will be decoded/encoded by the [PlayerSerializerService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class PlayerSaveData(val passwordHash: String, val username: String, val displayName: String,
                          val x: Int, val z: Int, val height: Int, val privilege: Int,
                          val displayMode: Int, val runEnergy: Double, val skills: List<JsonPlayerSerializer.PersistentSkill>,
                          val attributes: Map<String, Any>, val timers: List<TimerSystem.PersistentTimer>,
                          val inventory: Map<Int, Item>, val equipment: Map<Int, Item>,
                          val bank: Map<Int, Item>, val varps: List<Varp>)