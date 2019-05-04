package gg.rsmod.cache.struct

/**
 * Represents a single file inside a [Group].
 *
 * @param id the official id of the file.
 * @param nameHash a name identifier as a hash.
 * @param data the data for the file.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class GroupFile(val id: Int, val nameHash: Int, val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupFile

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