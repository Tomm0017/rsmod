package gg.rsmod.cache.struct

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Archive(val id: Int, val nameHash: Int?, val crc: Int, val revision: Int) {

    val groupIds = mutableMapOf<Int, Int>()

    val groupNameHashes = mutableMapOf<Int, Int>()
}