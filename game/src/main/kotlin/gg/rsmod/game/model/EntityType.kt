package gg.rsmod.game.model

/**
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

    PROJECTILE;

    fun isHumanControlled(): Boolean = this == CLIENT
}