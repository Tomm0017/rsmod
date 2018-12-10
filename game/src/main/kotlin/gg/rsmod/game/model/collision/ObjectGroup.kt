package gg.rsmod.game.model.collision

import java.util.*

/**
 * The group of an object, which indicates its general class (e.g. if it's a wall, or a floor decoration).
 *
 * @author Major
 * @author Scu11
 */
enum class ObjectGroup(val value: Int) {

    /**
     * The wall object group, which may block a tile.
     */
    WALL(0),

    /**
     * The wall decoration object group, which never blocks a tile.
     */
    WALL_DECORATION(1),

    /**
     * The interactable object group, for objects that can be clicked and interacted with.
     */
    INTERACTABLE_OBJECT(2),

    /**
     * The ground decoration object group, which may block a tile.
     */
    GROUND_DECORATION(3);


    companion object {

        /**
         * Attempts to find the ObjectGroup with the specified integer value.
         *
         * @param value The integer value of the ObjectGroup.
         * @return The [Optional] possibly containing the ObjectGroup, if found.
         */
        fun valueOf(value: Int): Optional<ObjectGroup> {
            return Optional.ofNullable(values().firstOrNull { it.value == value })
        }
    }

}