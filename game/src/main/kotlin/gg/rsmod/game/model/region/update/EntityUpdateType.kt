package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    REMOVE_GROUND_ITEM(id = 0),
    PLAY_TILE_SOUND(id = 1),
    UNKNOWN(id = 2),
    UPDATE_GROUND_ITEM(id = 3),
    SPAWN_PROJECTILE(id = 4),
    SPAWN_GROUND_ITEM(id = 5),
    SPAWN_OBJECT(id = 6),
    MAP_ANIM(id = 7),
    REMOVE_OBJECT(id = 8),
    ANIMATE_OBJECT(id = 9);
}