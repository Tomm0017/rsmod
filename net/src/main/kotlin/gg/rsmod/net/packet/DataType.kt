package gg.rsmod.net.packet

/**
 * Represents the different simple data types.
 *
 * @param bytes The number of bytes this type occupies.
 *
 * @author Graham
 */
enum class DataType(val bytes: Int) {

    /**
     * A byte.
     */
    BYTE(1),

    /**
     * A short.
     */
    SHORT(2),

    /**
     * A 'tri byte' - a group of three bytes.
     */
    TRI_BYTE(3),

    /**
     * An integer.
     */
    INT(4),

    /**
     * A long.
     */
    LONG(8),

    /**
     * A byte array.
     */
    BYTES(-1),

    SMART(-1),

    /**
     * A String.
     */
    STRING(-1),

}