package gg.rsmod.game.model

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Appearance(val looks: IntArray, val colors: IntArray, val gender: Gender) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appearance

        if (!looks.contentEquals(other.looks)) return false
        if (!colors.contentEquals(other.colors)) return false
        if (gender != other.gender) return false

        return true
    }

    override fun hashCode(): Int {
        var result = looks.contentHashCode()
        result = 31 * result + colors.contentHashCode()
        result = 31 * result + gender.hashCode()
        return result
    }

    companion object {

        private val DEFAULT_LOOKS = intArrayOf(9, 14, 109, 26, 33, 36, 42)

        private val DEFAULT_COLORS = intArrayOf(0, 3, 2, 0, 0)

        val DEFAULT = Appearance(DEFAULT_LOOKS, DEFAULT_COLORS, Gender.MALE)
    }
}