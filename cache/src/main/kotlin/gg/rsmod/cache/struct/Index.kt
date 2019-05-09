package gg.rsmod.cache.struct

import gg.rsmod.cache.type.ArchiveVersionType

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Index(val version: Int, val versionType: ArchiveVersionType, val nameHash: Boolean, var archiveData: ByteArray?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Index

        if (version != other.version) return false
        if (versionType != other.versionType) return false
        if (nameHash != other.nameHash) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + versionType.hashCode()
        result = 31 * result + nameHash.hashCode()
        return result
    }
}