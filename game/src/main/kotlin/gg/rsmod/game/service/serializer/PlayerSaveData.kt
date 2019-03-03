package gg.rsmod.game.service.serializer

import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.timer.TimerSystem
import gg.rsmod.game.model.varp.Varp
import gg.rsmod.game.service.serializer.json.JsonPlayerSerializer

/**
 * The data that will be decoded/encoded by the [PlayerSerializerService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class PlayerSaveData(val passwordHash: String, val username: String, val displayName: String,
                          val previousXteas: IntArray, val x: Int, val z: Int, val height: Int, val privilege: Int,
                          val displayMode: Int, val runEnergy: Double, val skills: List<JsonPlayerSerializer.PersistentSkill>,
                          val attributes: Map<String, Any>, val timers: List<TimerSystem.PersistentTimer>,
                          val inventory: Map<Int, Item>, val equipment: Map<Int, Item>,
                          val bank: Map<Int, Item>, val varps: List<Varp>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerSaveData

        if (passwordHash != other.passwordHash) return false
        if (username != other.username) return false
        if (displayName != other.displayName) return false
        if (!previousXteas.contentEquals(other.previousXteas)) return false
        if (x != other.x) return false
        if (z != other.z) return false
        if (height != other.height) return false
        if (privilege != other.privilege) return false
        if (displayMode != other.displayMode) return false
        if (runEnergy != other.runEnergy) return false
        if (skills != other.skills) return false
        if (attributes != other.attributes) return false
        if (timers != other.timers) return false
        if (inventory != other.inventory) return false
        if (equipment != other.equipment) return false
        if (bank != other.bank) return false
        if (varps != other.varps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = passwordHash.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + previousXteas.contentHashCode()
        result = 31 * result + x
        result = 31 * result + z
        result = 31 * result + height
        result = 31 * result + privilege
        result = 31 * result + displayMode
        result = 31 * result + runEnergy.hashCode()
        result = 31 * result + skills.hashCode()
        result = 31 * result + attributes.hashCode()
        result = 31 * result + timers.hashCode()
        result = 31 * result + inventory.hashCode()
        result = 31 * result + equipment.hashCode()
        result = 31 * result + bank.hashCode()
        result = 31 * result + varps.hashCode()
        return result
    }
}