package gg.rsmod.cache.type

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ArchiveType(val idx: Int) {
    SKELETONS(0),
    SKINS(1),
    CONFIGS(2),
    INTERFACES(3),
    SOUND_EFFECTS(4),
    REGIONS(5),
    TRACKS(6),
    MODELS(7),
    SPRITES(8),
    TEXTURES(9),
    BINARY(10),
    TRACKS2(11),
    CLIENTSCRIPT(12),
    FONTS(13),
    VORBIS(14),
    INSTRUMENTS(15),
    UNDERMINED(16),
    DEFAULTS(17);

    companion object {
        val values = enumValues<ArchiveType>()
    }
}