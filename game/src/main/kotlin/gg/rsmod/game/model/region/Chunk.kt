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
         * The client contains an array of entity update packets.
         */
        private const val MULTI_SPAWN_OBJ_INDEX = 8

        /**
         * The client contains an array of entity update packets.
         */
        private const val MULTI_REMOVE_OBJ_INDEX = 9
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

    private val objects: Multimap<Tile, GameObject> = HashMultimap.create()

    private val removedObjects = hashSetOf<StaticObject>()

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

    fun getSurroundingCoords(chunkRadius: Int = CHUNK_VIEW_RADIUS): MutableSet<ChunkCoords> {
        val surrounding = hashSetOf<ChunkCoords>()

        val radius = chunkRadius - 1

        for (x in -radius .. radius) {
            for (z in -radius .. radius) {
                surrounding.add(ChunkCoords(coords.x + x, coords.z + z))
            }
        }

        return surrounding
    }

    fun spawnAll(p: Player, gameService: GameService) {
        val messages = arrayListOf<EntityGroupMessage>()

        val objSpawn = objects.values()//getEntities<DynamicObject>(EntityType.DYNAMIC_OBJECT)
        val objRemove = removedObjects

        objSpawn.forEach { obj ->
            if (p.tile.height == obj.tile.height) {
                val message = SpawnObjectMessage(obj.id, obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
                messages.add(EntityGroupMessage(MULTI_SPAWN_OBJ_INDEX, message))
            }
        }

        objRemove.forEach { obj ->
            if (p.tile.height == obj.tile.height) {
                val message = RemoveObjectMessage(obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
                messages.add(EntityGroupMessage(MULTI_REMOVE_OBJ_INDEX, message))
            }
        }

        if (messages.isNotEmpty()) {
            val local = p.lastKnownRegionBase!!.toLocal(coords.toTile())
            p.write(SpawnEntityGroupsMessage(local.x, local.z, gameService.messageEncoders, gameService.messageStructures, *messages.toTypedArray()))
        }
    }

    private fun submit(world: World, entity: Entity, tile: Tile, updateType: ChunkUpdateType) {
        when (updateType) {
            ChunkUpdateType.ADD -> {
                entities.put(tile, entity)

                if (entity.getType() == EntityType.DYNAMIC_OBJECT) {
                    val obj = entity as DynamicObject
                    getSurroundingCoords().forEach { coords ->
                        val chunk = world.chunks.getOrCreate(coords, false) ?: return@forEach
                        val spawn = SpawnObjectMessage(obj.id, obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
                        chunk.getEntities<Client>(EntityType.CLIENT).forEach { client ->
                            val local = client.lastKnownRegionBase!!.toLocal(entity.tile)
                            client.write(SetChunkToRegionOffset(local.x, local.z))
                            client.write(spawn)
                        }
                    }
                }

                if (entity.getType() == EntityType.DYNAMIC_OBJECT) {
                    objects.put(tile, entity as GameObject)
                }
            }
            ChunkUpdateType.REMOVE -> {
                entities.remove(tile, entity)

                if (entity.getType() == EntityType.STATIC_OBJECT) {
                    val obj = entity as StaticObject

                    removedObjects.add(obj)

                    getSurroundingCoords().forEach { coords ->
                        val chunk = world.chunks.getOrCreate(coords, false) ?: return@forEach
                        val message = RemoveObjectMessage(obj.settings.toInt(), ((obj.tile.x and 0x7) shl 4) or (obj.tile.z and 0x7))
                        chunk.getEntities<Client>(EntityType.CLIENT).forEach { client ->
                            val local = client.lastKnownRegionBase!!.toLocal(entity.tile)
                            client.write(SetChunkToRegionOffset(local.x, local.z))
                            client.write(message)
                        }
                    }
                }

                if (entity.getType() == EntityType.DYNAMIC_OBJECT) {
                    objects.remove(tile, entity as GameObject)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(vararg types: EntityType): List<T> = entities.values().filter { it.getType() in types } as List<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> getEntities(tile: Tile, vararg types: EntityType): List<T> = entities.get(tile).filter { it.getType() in types } as List<T>
}