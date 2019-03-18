package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.service.xtea.XteaKeyService

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class RebuildRegionMessage(val x: Int, val z: Int, val forceLoad: Int, val coordinates: IntArray, val xteaKeyService: XteaKeyService?) : Message {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RebuildRegionMessage

        if (x != other.x) return false
        if (z != other.z) return false
        if (forceLoad != other.forceLoad) return false
        if (!coordinates.contentEquals(other.coordinates)) return false
        if (xteaKeyService != other.xteaKeyService) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + z
        result = 31 * result + forceLoad
        result = 31 * result + coordinates.contentHashCode()
        result = 31 * result + (xteaKeyService?.hashCode() ?: 0)
        return result
    }
}