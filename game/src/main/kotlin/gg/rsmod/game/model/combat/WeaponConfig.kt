package gg.rsmod.game.model.combat

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class WeaponConfig(val item: Int, val type: Int, val animations: IntArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeaponConfig

        if (item != other.item) return false
        if (type != other.type) return false
        if (!animations.contentEquals(other.animations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = item
        result = 31 * result + type
        result = 31 * result + animations.contentHashCode()
        return result
    }
}