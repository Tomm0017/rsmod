package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id if they can vary on revision (mainly looking for
 * differences in between osrs, rs2, rs3)
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    PLAY_TILE_SOUND(id = 0),
    SPAWN_PROJECTILE(id = 1),
    TILE_GRAPHIC(id = 2),
    SPAWN_GROUND_ITEM(id = 3),
    REMOVE_GROUND_ITEM(id = 4),
    ANIMATE_OBJECT(id = 6),
    UPDATE_GROUND_ITEM(id = 7),
    SPAWN_OBJECT(id = 8),
    REMOVE_OBJECT(id = 9)
}