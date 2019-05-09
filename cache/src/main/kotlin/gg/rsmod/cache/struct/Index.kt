package gg.rsmod.cache.struct

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Index(val protocol: Int, val revision: Int, val nameHash: Boolean, val archiveData: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Index

        if (protocol != other.protocol) return false
        if (revision != other.revision) return false
        if (nameHash != other.nameHash) return false
        if (!archiveData.contentEquals(other.archiveData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = protocol
        result = 31 * result + revision
        result = 31 * result + nameHash.hashCode()
        result = 31 * result + archiveData.contentHashCode()
        return result
    }
}