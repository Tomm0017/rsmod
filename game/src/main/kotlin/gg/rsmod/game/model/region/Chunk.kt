package gg.rsmod.game.model.region

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.message.impl.UpdateZonePartialEnclosedMessage
import gg.rsmod.game.message.impl.UpdateZonePartialFollowsMessage
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.*
import gg.rsmod.game.model.region.update.*
import gg.rsmod.game.service.GameService

/**
 * Represents an 8x8 tile in the game map.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(val coords: ChunkCoords, val heights: Int) {

    companion object {
        /**
         * The size of a chunk, in tiles.
         */
        const val CHUNK_SIZE = 8

        /**
         * The size of the viewport a [gg.rsmod.game.model.entity.Player] can
         * 'see' at a time, in tiles.
         */
        const val MAX_VIEWPORT = CHUNK_SIZE * 13

        /**
         * The amount of [Chunk]s that can be viewed at a time by default.
         */
        const val CHUNK_VIEW_RADIUS = 3
    }

    /**
     * The collision matrices of 8x8 tiles in [heights] different height levels.
     */
    private val matrices = CollisionMatrix.createMatrices(heights, CHUNK_SIZE, CHUNK_SIZE)

    /**
     * The [Entity]s that are currently registered to the [Tile] key. This is
     * not used for [gg.rsmod.game.model.entity.Pawn], but rather [Entity]s
     * that do not regularly change [Tile]s.
     */
    private val entities: Multimap<Tile, Entity> = HashMultimap.create()

    /**
     * A list of [EntityUpdate]s that will be sent to players who have just entered
     * a region that has this chunk as viewable.
     */
    private val updates = arrayListOf<EntityUpdate<*>>()

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun setMatrix(height: Int, matrix: CollisionMatrix) {
        matrices[height] = matrix
    }

    fun contains(tile: Tile): Boolean = coords == tile.toChunkCoords()

    fun isBlocked(tile: Tile, direction: Direction, projectile: Boolean): Boolean = matrices[tile.height].isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)

    fun isClipped(tile: Tile): Boolean = matrices[tile.height].isClipped(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE)

    fun addEntity(world: World, entity: Entity, tile: Tile) {
        /**
         * Objects will affect the collision map.
         */
        if (entity.getType().isObject()) {
            world.collision.applyCollision(entity as GameObject, CollisionUpdate.Type.ADD)
        }

        /**
         * Transient entities will <strong>not</strong> be registered to one of
         * our [Chunk]'s tiles.
         */
        if (!entity.getType().isTransient()) {
            entities.put(tile, entity)
        }

        /**
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = true)
        if (update != null) {
            /**
             * [EntityType.STATIC_OBJECT]s shouldn't be sent to local players
             * for them to view since the client is already aware of them as
             * they are loaded from the game resources (cache).
             */
            if (entity.getType() != EntityType.STATIC_OBJECT) {
                /**
                 * [EntityType]s marked as transient will only be sent to local
                 * players who are currently in the viewport, but will now be
                 * sent to players who enter the region later on.
                 */
                if (!entity.getType().isTransient()) {
                    updates.add(update)
                }
                /**
                 * Send the update to all players in viewport.
                 */
                sendUpdate(world, update)
            }
        }
    }

    fun removeEntity(world: World, entity: Entity, tile: Tile) {
        /**
         * Transient entities do not get added to our [Chunk]'s tiles, so no use
         * in trying to remove it.
         */
        check(!entity.getType().isTransient()) { "Transient entities cannot be removed from chunks." }

        /**
         * [EntityType]s that are considered objects will be removed from our
         * collision map.
         */
        if (entity.getType().isObject()) {
            world.collision.applyCollision(entity as GameObject, CollisionUpdate.Type.REMOVE)
        }

        entities.remove(tile, entity)

        /**
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = false)
        if (update != null) {

            /**
             * If the entity is an [EntityType.STATIC_OBJECT], we want to cache
             * an [EntityUpdate] that will remove the entity when new players come
             * into this [Chunk]'s viewport.
             *
             * This is done because the client will always load [EntityType.STATIC_OBJECT]
             * through the game resources and have to be removed manually by our server.
             */
            if (entity.getType() == EntityType.STATIC_OBJECT) {
                updates.add(update)
            }

            updates.removeIf { it.entity == entity }

            /**
             * Send the update to all players in viewport.
             */
            sendUpdate(world, update)
        }
    }

    fun updateGroundItem(world: World, item: GroundItem, oldAmount: Int, newAmount: Int) {
        if (item.getType().isGroundItem()) {
            val update = ObjCountUpdate(EntityUpdateType.UPDATE_GROUND_ITEM, item, oldAmount, newAmount)
            sendUpdate(world, update)

            if (updates.removeIf { it.entity == item }) {
                updates.add(createUpdateFor(item, spawn = true)!!)
            }
        }
    }

    private fun sendUpdate(world: World, update: EntityUpdate<*>) {
        val surrounding = coords.getSurroundingCoords()

        for (coords in surrounding) {
            val chunk = world.chunks.get(coords, createIfNeeded = false) ?: continue
            val clients = chunk.getEntities<Client>(EntityType.CLIENT)
            for (client in clients) {
                if (!canBeViewed(client, update.entity)) {
                    continue
                }
                val local = client.lastKnownRegionBase!!.toLocal(update.entity.tile)
                client.write(UpdateZonePartialFollowsMessage(local.x, local.z))
                client.write(update.toMessage())
            }
        }
    }

    fun sendUpdates(p: Player, gameService: GameService) {
        val messages = arrayListOf<EntityGroupMessage>()

        updates.forEach { update ->
            val message = EntityGroupMessage(update.type.id, update.toMessage())
            messages.add(message)
        }

        if (messages.isNotEmpty()) {
            val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
            p.write(UpdateZonePartialEnclosedMessage(local.x, local.z, gameService.messageEncoders, gameService.messageStructures, *messages.toTypedArray()))
        }
    }

    private fun canBeViewed(p: Player, entity: Entity): Boolean {
        if (entity.getType().isGroundItem()) {
            val item = entity as GroundItem
            return item.isPublic() || item.isOwnedBy(p)
        }
        return true
    }

    private fun <T: Entity> createUpdateFor(entity: T, spawn: Boolean): EntityUpdate<*>? = when (entity.getType()) {

        EntityType.DYNAMIC_OBJECT, EntityType.STATIC_OBJECT ->
            if (spawn) LocAddChangeUpdate(EntityUpdateType.SPAWN_OBJECT, entity as GameObject)
            else LocDelUpdate(EntityUpdateType.REMOVE_OBJECT, entity as GameObject)

        EntityType.GROUND_ITEM ->
            if (spawn) ObjAddUpdate(EntityUpdateType.SPAWN_GROUND_ITEM, entity as GroundItem)
            else ObjDelUpdate(EntityUpdateType.REMOVE_GROUND_ITEM, entity as GroundItem)

        EntityType.PROJECTILE ->
            if (spawn) MapProjAnimUpdate(EntityUpdateType.SPAWN_PROJECTILE, entity as Projectile)
            else throw RuntimeException("${entity.getType()} can only be spawned, not removed!")

        EntityType.AREA_SOUND ->
            if (spawn) SoundAreaUpdate(EntityUpdateType.PLAY_TILE_SOUND, entity as AreaSound)
            else throw RuntimeException("${entity.getType()} can only be spawned, not removed!")

        else -> null
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities.get(tile).filter { it.getType() in types } as List<T>
}