package gg.rsmod.game.model.region.update

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    PLAY_TILE_SOUND(0),
    SPAWN_PROJECTILE(1),
    TILE_GRAPHIC(2),
    SPAWN_GROUND_ITEM(3),
    REMOVE_GROUND_ITEM(4),
    ANIMATE_OBJECT(6),
    UPDATE_GROUND_ITEM(7),
    SPAWN_OBJECT(8),
    REMOVE_OBJECT(9)
}