package gg.rsmod.game.model.region

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
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

/**
 * Represents an 8x8 tile in the game map.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(val coords: ChunkCoords, val heights: Int) {

    constructor(other: Chunk) : this(other.coords, other.heights) {
        copyMatrices(other)
    }

    /**
     * The array of matrices of 8x8 tiles. Each index representing a height.
     */
    private val matrices: Array<CollisionMatrix> = CollisionMatrix.createMatrices(Tile.TOTAL_HEIGHT_LEVELS, CHUNK_SIZE, CHUNK_SIZE)

    internal val blockedTiles = ObjectOpenHashSet<Tile>()

    /**
     * The [Entity]s that are currently registered to the [Tile] key. This is
     * not used for [gg.rsmod.game.model.entity.Pawn], but rather [Entity]s
     * that do not regularly change [Tile]s.
     */
    private lateinit var entities: MutableMap<Tile, MutableList<Entity>>

    /**
     * A list of [EntityUpdate]s that will be sent to players who have just entered
     * a region that has this chunk as viewable.
     */
    private lateinit var updates: MutableList<EntityUpdate<*>>

    /**
     * Create the collections used for [Entity]s and [EntityUpdate]s.
     * @see entities
     * @see updates
     */
    fun createEntityContainers() {
        entities = Object2ObjectOpenHashMap()
        updates = ObjectArrayList()
    }

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun setMatrix(height: Int, matrix: CollisionMatrix) {
        matrices[height] = matrix
    }

    private fun copyMatrices(other: Chunk) {
        other.matrices.forEachIndexed { index, matrix ->
            matrices[index] = CollisionMatrix(matrix)
        }
    }

    /**
     * Check if [tile] belongs to this chunk.
     */
    fun contains(tile: Tile): Boolean = coords == tile.chunkCoords

    fun isBlocked(tile: Tile, direction: Direction, projectile: Boolean): Boolean = matrices[tile.height].isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)

    fun isClipped(tile: Tile): Boolean = matrices[tile.height].isClipped(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE)

    fun addEntity(world: World, entity: Entity, tile: Tile) {
        /*
         * Objects will affect the collision map.
         */
        if (entity.getType().isObject()) {
            world.collision.applyCollision(world.definitions, entity as GameObject, CollisionUpdate.Type.ADD)
        }

        /*
         * Transient entities will <strong>not</strong> be registered to one of
         * our [Chunk]'s tiles.
         */
        if (!entity.getType().isTransient()) {
            val list = entities[tile] ?: ObjectArrayList(1)
            list.add(entity)
            entities[tile] = list
        }

        /*
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = true)
        if (update != null) {
            /*
             * [EntityType.STATIC_OBJECT]s shouldn't be sent to local players
             * for them to view since the client is already aware of them as
             * they are loaded from the game resources (cache).
             */
            if (entity.getType() != EntityType.STATIC_OBJECT) {
                /*
                 * [EntityType]s marked as transient will only be sent to local
                 * players who are currently in the viewport, but will now be
                 * sent to players who enter the region later on.
                 */
                if (!entity.getType().isTransient()) {
                    updates.add(update)
                }
                /*
                 * Send the update to all players in viewport.
                 */
                sendUpdate(world, update)
            }
        }
    }

    fun removeEntity(world: World, entity: Entity, tile: Tile) {
        /*
         * Transient entities do not get added to our [Chunk]'s tiles, so no use
         * in trying to remove it.
         */
        check(!entity.getType().isTransient()) { "Transient entities cannot be removed from chunks." }

        /*
         * [EntityType]s that are considered objects will be removed from our
         * collision map.
         */
        if (entity.getType().isObject()) {
            world.collision.applyCollision(world.definitions, entity as GameObject, CollisionUpdate.Type.REMOVE)
        }

        entities[tile]?.remove(entity)

        /*
         * Create an [EntityUpdate] for our local players to receive and view.
         */
        val update = createUpdateFor(entity, spawn = false)
        if (update != null) {

            /*
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

            /*
             * Send the update to all players in viewport.
             */
            sendUpdate(world, update)
        }
    }

    /**
     * Update the item amount of an existing [GroundItem] in [entities].
     */
    fun updateGroundItem(world: World, item: GroundItem, oldAmount: Int, newAmount: Int) {
        val update = ObjCountUpdate(EntityUpdateType.UPDATE_GROUND_ITEM, item, oldAmount, newAmount)
        sendUpdate(world, update)

        if (updates.removeIf { it.entity == item }) {
            updates.add(createUpdateFor(item, spawn = true)!!)
        }
    }

    /**
     * Send the [update] to any [Client] entities that are within view distance
     * of this chunk.
     */
    private fun sendUpdate(world: World, update: EntityUpdate<*>) {
        val surrounding = coords.getSurroundingCoords()

        for (coords in surrounding) {
            val chunk = world.chunks.get(coords, createIfNeeded = false) ?: continue
            val clients = chunk.getEntities<Client>(EntityType.CLIENT)
            for (client in clients) {
                if (!canBeViewed(client, update.entity)) {
                    continue
                }
                val local = client.lastKnownRegionBase!!.toLocal(this.coords.toTile())
                client.write(UpdateZonePartialFollowsMessage(local.x, local.z))
                client.write(update.toMessage())
            }
        }
    }

    /**
     * Sends all [updates] from this chunk to the player [p].
     *
     * @param gameService
     * Game service is required to get the XTEA service.
     */
    fun sendUpdates(p: Player, gameService: GameService) {
        val messages = ObjectArrayList<EntityGroupMessage>()

        updates.forEach { update ->
            val message = EntityGroupMessage(update.type.id, update.toMessage())
            if (canBeViewed(p, update.entity)) {
                messages.add(message)
            }
        }

        if (messages.isNotEmpty()) {
            val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
            p.write(UpdateZonePartialEnclosedMessage(local.x, local.z, gameService.messageEncoders, gameService.messageStructures, *messages.toTypedArray()))
        }
    }

    /**
     * Checks to see if player [p] is able to view [entity].
     */
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
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values.flatten().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities[tile]?.filter { it.getType() in types } as? List<T> ?: emptyList()

    companion object {
        /**
         * The size of a chunk, in tiles.
         */
        const val CHUNK_SIZE = 8

        /**
         * The amount of chunks in a region.
         */
        const val CHUNKS_PER_REGION = 13

        /**
         * The amount of [Chunk]s that can be viewed at a time by a player.
         */
        const val CHUNK_VIEW_RADIUS = 3

        /**
         * The size of a region, in tiles.
         */
        const val REGION_SIZE = CHUNK_SIZE * CHUNK_SIZE

        /**
         * The size of the viewport a [gg.rsmod.game.model.entity.Player] can
         * 'see' at a time, in tiles.
         */
        const val MAX_VIEWPORT = CHUNK_SIZE * CHUNKS_PER_REGION
    }
}