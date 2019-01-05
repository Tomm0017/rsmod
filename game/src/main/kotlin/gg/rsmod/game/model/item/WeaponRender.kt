package gg.rsmod.game.model.item

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class WeaponRender(val item: Int, val animations: IntArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeaponRender

        if (item != other.item) return false
        if (!animations.contentEquals(other.animations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = item
        result = 31 * result + animations.contentHashCode()
        return result
    }
}