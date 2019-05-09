package gg.rsmod.cache.type

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ConfigType(private val idx: Int, private val archive: ArchiveType) : GroupType {
    OBJ(10, ArchiveType.CONFIGS)
    ;

    override val archiveType: ArchiveType
        get() = archive

    override val id: Int
        get() = idx
}