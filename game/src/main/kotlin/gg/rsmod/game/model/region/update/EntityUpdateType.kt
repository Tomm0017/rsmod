package gg.rsmod.game.model.region.update

/**
 *
 * TODO(Tom): externalize the id as it changes every revision
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class EntityUpdateType(val id: Int) {
    LEGACY_REMOVE_GROUND_ITEM(id = 0), // OBJ_DEL_LEGACY
    SPAWN_GROUND_ITEM(id = 1), // OBJ_ADD
    PLAY_TILE_SOUND(id = 2), // AREA_SOUND
    ANIMATE_OBJECT(id = 3), // LOC_ANIM
    UPDATE_GROUND_ITEM(id = 4), // OBJ_COUNT
    PREFETCH_GAMEOBJECTS(id = 5), // PREFETCH_GAMEOBJECTS
    REMOVE_OBJECT(id = 6), // LOC_DEL
    SPAWN_OBJECT(id = 7), // LOC_ADD_CHANGE
    REMOVE_GROUND_ITEM(id = 8), // OBJ_DEL
    MAP_ANIM(id = 9), // MAP_ANIM
    SPAWN_PROJECTILE(id = 10), //  MAPPROJ_ANIM
}