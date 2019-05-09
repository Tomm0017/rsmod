package gg.rsmod.cache.type

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ArchiveVersionType(val opcode: Int) {
    NONE(5),
    SHORT(6),
    SMART(7);

    companion object {
        val values = enumValues<ArchiveVersionType>()
    }
}