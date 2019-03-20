package gg.rsmod.game.model.instance

/**
 * Represents valid attributes that an [InstancedMap] can have.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class InstancedMapAttribute {
    /**
     * The instanced map will de-allocate if its owner logs out in the map area.
     * Requires an owner for the instanced map.
     */
    DEALLOCATE_ON_LOGOUT,

    /**
     * The instanced map will de-allocate if its owner dies while in the map area.
     * Requires an owner for the instanced map.
     */
    DEALLOCATE_ON_DEATH
}