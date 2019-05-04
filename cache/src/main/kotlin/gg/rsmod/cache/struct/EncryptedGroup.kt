package gg.rsmod.cache.struct

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class EncryptedGroup(val xteaKeys: IntArray, val crc: Int, val revision: Int,
                          val compression: Int, val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedGroup

        if (!xteaKeys.contentEquals(other.xteaKeys)) return false
        if (crc != other.crc) return false
        if (revision != other.revision) return false
        if (compression != other.compression) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xteaKeys.contentHashCode()
        result = 31 * result + crc
        result = 31 * result + revision
        result = 31 * result + compression
        result = 31 * result + data.contentHashCode()
        return result
    }
}