package gg.rsmod.game.model.region

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.collision.CollisionMatrix
import gg.rsmod.game.model.collision.CollisionUpdate
import gg.rsmod.game.model.entity.*
import gg.rsmod.game.service.GameService

/**
 * Represents an 8x8 tile in the game map.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Chunk(private val coords: ChunkCoords, private val heights: Int) {

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

        /**
         * These constants represent the index of each type of grouped entity
         * update in the client.
         */
        private const val MULTI_SPAWN_OBJ_INDEX = 8
        private const val MULTI_REMOVE_OBJ_INDEX = 9

        /**
         * Handles spawning (and removing) all entities in the [surrounding]
         * chunks for [p].
         */
        fun spawnAll(p: Player, surrounding: Set<ChunkCoords>) {
            val gameService = p.world.getService(GameService::class.java, false).orElse(null)!!
            surrounding.forEach { coords ->
                val chunk = p.world.chunks.get(coords) ?: return@forEach
                chunk.spawnAll(p, gameService)
            }
        }
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

    private val removedObjects = hashSetOf<GameObject>()

    fun getMatrix(height: Int): CollisionMatrix = matrices[height]

    fun contains(tile: Tile): Boolean = coords == tile.toChunkCoords()

    fun canTraverse(tile: Tile, direction: Direction, projectile: Boolean): Boolean {
        val matrix = matrices[tile.height]
        return !matrix.isBlocked(tile.x % CHUNK_SIZE, tile.z % CHUNK_SIZE, direction, projectile)
    }

    fun addEntity(world: World, entity: Entity, tile: Tile) {
        submit(world, entity, tile, ChunkUpdateType.ADD)
        world.collision.submit(entity, CollisionUpdate.Type.ADD)
    }

    fun removeEntity(world: World, entity: Entity, tile: Tile) {
        submit(world, entity, tile, ChunkUpdateType.REMOVE)
        world.collision.submit(entity, CollisionUpdate.Type.REMOVE)
    }

    fun spawnAll(p: Player, gameService: GameService) {
        val groupMessages = arrayListOf<EntityGroupMessage>()

        getEntities<DynamicObject>(EntityType.DYNAMIC_OBJECT).forEach { obj ->
            val message = SpawnObjectMessage(obj.id, obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
            groupMessages.add(EntityGroupMessage(MULTI_SPAWN_OBJ_INDEX, message))
        }

        removedObjects.forEach { obj ->
            val message = RemoveObjectMessage(obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
            groupMessages.add(EntityGroupMessage(MULTI_REMOVE_OBJ_INDEX, message))
        }

        if (groupMessages.isNotEmpty()) {
            val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
            p.write(SpawnEntityGroupsMessage(local.x, local.z, gameService.messageEncoders, gameService.messageStructures, *groupMessages.toTypedArray()))
        }
    }

    fun getSurroundingCoords(chunkRadius: Int = CHUNK_VIEW_RADIUS): MutableSet<ChunkCoords> {
        val surrounding = hashSetOf<ChunkCoords>()

        for (x in -chunkRadius .. chunkRadius) {
            for (z in -chunkRadius .. chunkRadius) {
                surrounding.add(ChunkCoords(coords.x + x, coords.z + z))
            }
        }

        return surrounding
    }

    fun forSurrounding(world: World, chunkRadius: Int = CHUNK_VIEW_RADIUS, action: (Chunk) -> Unit) {
        getSurroundingCoords(chunkRadius).forEach { coords ->
            val chunk = world.chunks.get(coords) ?: return@forEach
            action.invoke(chunk)
        }
    }

    private fun submit(world: World, entity: Entity, tile: Tile, updateType: ChunkUpdateType) {
        when (updateType) {
            ChunkUpdateType.ADD -> {
                entities.put(tile, entity)

                if (entity.getType() == EntityType.DYNAMIC_OBJECT) {
                    forSurrounding(world) { chunk ->
                        chunk.getEntities<Client>(EntityType.CLIENT).forEach { player ->
                            val local = player.lastKnownRegionBase!!.toLocal(entity.tile)
                            val obj = entity as GameObject
                            player.write(SetChunkToRegionOffset(local.x, local.z))
                            player.write(SpawnObjectMessage(obj.id, obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7)))
                        }
                    }
                }
            }
            ChunkUpdateType.REMOVE -> {
                entities.remove(tile, entity)

                if (entity.getType().isObject()) {
                    val obj = entity as GameObject
                    if (entity.getType() == EntityType.STATIC_OBJECT) {
                        removedObjects.add(obj)
                    }
                    forSurrounding(world) { chunk ->
                        chunk.getEntities<Client>(EntityType.CLIENT).forEach { player ->
                            val local = player.lastKnownRegionBase!!.toLocal(entity.tile)
                            player.write(SetChunkToRegionOffset(local.x, local.z))
                            player.write(RemoveObjectMessage(obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7)))
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities.get(tile).filter { it.getType() in types } as List<T>
}