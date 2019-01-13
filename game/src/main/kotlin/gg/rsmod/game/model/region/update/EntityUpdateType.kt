package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id if they can vary on revision (mainly looking for
 * differences in between osrs, rs2, rs3)
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    UPDATE_GROUND_ITEM(id = 0),
    SPAWN_PROJECTILE(id = 1),
    UNKNOWN(id = 2), // Player transform into object?
    TILE_GRAPHIC(id = 3),
    ANIMATE_OBJECT(id = 4),
    PLAY_TILE_SOUND(id = 5),
    REMOVE_OBJECT(id = 6),
    REMOVE_GROUND_ITEM(id = 7),
    SPAWN_GROUND_ITEM(id = 8),
    SPAWN_OBJECT(id = 9)
}