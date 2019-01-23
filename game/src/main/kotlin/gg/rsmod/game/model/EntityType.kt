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

    fun isHumanControlled(): Boolean = this == CLIENT

    fun isPlayer(): Boolean = this == CLIENT || this == PLAYER

    fun isNpc(): Boolean = this == NPC

    fun isObject(): Boolean = this == STATIC_OBJECT || this == DYNAMIC_OBJECT

    fun isProjectile(): Boolean = this == PROJECTILE

    fun isGroundItem(): Boolean = this == GROUND_ITEM

    fun isTransient(): Boolean = this == PROJECTILE || this == AREA_SOUND
}