/**
 * Copyright (c) 2010-2011 Graham Edgecombe
 * Copyright (c) 2011-2016 Major <major.emrs@gmail.com> and other apollo contributors
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package gg.rsmod.game.model.collision

/**
 * The type of an object, which affects specified behaviour (such as whether it displaces existing objects).
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
     * An object placed on a wall that can be interacted with by a player.
     */
    INTERACTABLE_WALL_DECORATION(4, ObjectGroup.WALL),

    /**
     * A wall that you can interact with.
     */
    INTERACTABLE_WALL(5, ObjectGroup.WALL),

    /**
     * A wall joint that is presented diagonally with respect to the tile.
     */
    DIAGONAL_WALL(9, ObjectGroup.WALL),

    /**
     * An object that can be interacted with by a player.
     */
    INTERACTABLE(10, ObjectGroup.INTERACTABLE_OBJECT),

    /**
     * An [INTERACTABLE] object, rotated `pi / 2` radians.
     */
    DIAGONAL_INTERACTABLE(11, ObjectGroup.INTERACTABLE_OBJECT),

    /**
     * A decoration positioned on the floor.
     */
    FLOOR_DECORATION(22, ObjectGroup.GROUND_DECORATION);

    companion object {
        val values = enumValues<ObjectType>()
    }
}