package gg.rsmod.game.model.entity

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityType {
    PLAYER,
    CLIENT;

    fun isHumanControlled(): Boolean = this == CLIENT
}