package gg.rsmod.net.codec.filestore

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class FilestoreResponse(val index: Int, val archive: Int, val data: ByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilestoreResponse

        if (index != other.index) return false
        if (archive != other.archive) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = index
        result = 31 * result + archive
        result = 31 * result + data.contentHashCode()
        return result
    }
}