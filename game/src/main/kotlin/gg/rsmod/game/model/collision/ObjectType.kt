package gg.rsmod.game.model.collision

/**
 * The type of an object, which affects specified behaviour (such as whether it displaces existing objects). TODO
 * complete this...
 *
 * @author Major
 * @author Scu11
 */
enum class ObjectType(val value: Int, val group: ObjectGroup) {

    /**
     * A wall that is presented lengthwise with respect to the tile.
     */
    LENGTHWISE_WALL(0, ObjectGroup.WALL),

    /**
     * A triangular object positioned in the corner of the tile.
     */
    TRIANGULAR_CORNER(1, ObjectGroup.WALL),

    /**
     * A corner for a wall, where the model is placed on two perpendicular edges of a single tile.
     */
    WALL_CORNER(2, ObjectGroup.WALL),

    /**
     * A rectangular object positioned in the corner of the tile.
     */
    RECTANGULAR_CORNER(3, ObjectGroup.WALL),

    /**
     * A wall joint that is presented diagonally with respect to the tile.
     */
    DIAGONAL_WALL(9, ObjectGroup.INTERACTABLE_OBJECT),

    /**
     * An object that can be interacted with by a player.
     */
    INTERACTABLE(10, ObjectGroup.INTERACTABLE_OBJECT),

    /**
     * An [.INTERACTABLE] object, rotated `pi / 2` radians.
     */
    DIAGONAL_INTERACTABLE(11, ObjectGroup.INTERACTABLE_OBJECT),

    /**
     * A decoration positioned on the floor.
     */
    FLOOR_DECORATION(22, ObjectGroup.GROUND_DECORATION)

}