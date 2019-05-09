package gg.rsmod.cache.type

/**
 * Represents valid compression types in the cache.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class CompressionType(val value: Int) {
    /**
     * No compression type.
     */
    NONE(0),

    /**
     * BZIP2 compression type.
     */
    BZIP2(1),

    /**
     * GZIP compression type.
     */
    GZIP(2),

    /**
     * Lempel-Ziv-Marok compression type.
     */
    LZMA(3);

    companion object {
        val values = enumValues<CompressionType>()
    }
}