package gg.rsmod.cache.file

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class RSFile(val id: Int, val nameHash: Int, val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RSFile

        if (id != other.id) return false
        if (nameHash != other.nameHash) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nameHash
        result = 31 * result + data.contentHashCode()
        return result
    }
}