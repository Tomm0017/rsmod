package gg.rsmod.game.model.combat

/**
 * Represents configurations for a valid weapon in the game.
 *
 * @param item
 * The item id of the weapon.
 *
 * @param type
 * The type of weapon, this can vary depending on the revision between OSRS,
 * RS2 and RS3.
 *
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