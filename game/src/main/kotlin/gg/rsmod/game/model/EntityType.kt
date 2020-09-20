package gg.rsmod.game.model

/**
 * Represents a type of entity that can be spawned in the world.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityType {

    /**
     * A player in our world, which does not necessarily need to be controlled
     * by a human.
     */
    PLAYER,

    /**
     * A human-controlled [PLAYER].
     */
    CLIENT,

    /**
     * A non-playable character.
     */
    NPC,

    /**
     * A game object that is part of the static terrain (loaded from game resources).
     */
    STATIC_OBJECT,

    /**
     * A game object that is spawned in by the [gg.rsmod.game.model.World].
     */
    DYNAMIC_OBJECT,

    /**
     * A graphic in the map.
     */
    MAP_ANIM,

    /**
     * An item on the floor.
     */
    GROUND_ITEM,

    /**
     * A projectile.
     */
    PROJECTILE,

    /**
     * An area sound effect.
     */
    AREA_SOUND;

    val isHumanControlled: Boolean
        get() = this == CLIENT

    val isPlayer: Boolean
        get() = this == CLIENT || this == PLAYER

    val isNpc: Boolean
        get() = this == NPC

    val isObject: Boolean
        get() = this == STATIC_OBJECT || this == DYNAMIC_OBJECT

    val isProjectile: Boolean
        get() = this == PROJECTILE

    val isGroundItem: Boolean
        get() = this == GROUND_ITEM

    val isTransient: Boolean
        get() = this == PROJECTILE || this == AREA_SOUND || this == MAP_ANIM
}