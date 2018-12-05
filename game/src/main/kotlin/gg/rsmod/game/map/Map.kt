package gg.rsmod.game.map

import com.google.common.base.Stopwatch
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Player
import org.apache.logging.log4j.LogManager
import java.util.concurrent.TimeUnit

/**
 * Holds all active [Region]s in the [World].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Map {

    companion object {
        private val logger = LogManager.getLogger(Map::class.java)

        /**
         * The amount of ticks that have to go by before every region is checked
         * for players. If the region does not have any players, it will be
         * removed from active regions.
         */
        private const val TICKS_FOR_REGION_ACTIVITY_CHECK = 50

        /**
         * The maximum amount of time, in milliseconds, that the [Map.pulse]
         * method should take.
         */
        private const val MAX_COMPUTATION_TIME = 50

        /**
         * The maximum amount of tiles a [Chunk] occupies.
         * This number should not be changed without underlying modifications.
         * These modifications include the changing of bit shifting values from
         * three (shifting three bits to the right to get the chunk coordinates).
         */
        private const val MAX_CHUNK_SIZE = 8

        /**
         * The maximum amount of [Chunk]s in a [Region].
         * This number should not be changed without underlying modifications.
         * These modifications include the changing of bit shifting values from
         * six (shifting six bits to the right to get the region coordinates).
         */
        private const val MAX_REGION_CHUNKS = 8
    }

    private var activityCheckTick = 0

    private val chunks = hashMapOf<Int, Chunk>()

    private val regions = hashMapOf<Int, Region>()

    private val chunkPlayers = hashMapOf<Chunk, MutableList<Player>>()

    fun pulse(world: World) {
        val stopwatch = Stopwatch.createStarted()

        var removed = 0
        if (activityCheckTick++ >= TICKS_FOR_REGION_ACTIVITY_CHECK) {
            /**
             * TODO(Tom): this logic can be parallelized via a
             * [java.util.concurrent.Phaser] if need be. Just make sure to
             * have a concurrency-supported collections to add any regions
             * that must be removed.
             */
            val iterator = regions.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                val region = entry.value

                val players = getPlayersInRegion(region)
                if (players == null || players.isEmpty()) {
                    iterator.remove()
                    removed++
                }
            }
            activityCheckTick = 0
        }

        world.players.forEach { player ->
            val chunkX = (player.tile.x shr 3) * MAX_CHUNK_SIZE
            val chunkZ = (player.tile.z shr 3) * MAX_CHUNK_SIZE
            val chunk = (chunkX shl 16) or chunkZ

            var activeChunk = chunks[chunk]

            /**
             * If player is already registered to the [activeChunk], we do not
             * want to execute any further logic.
             */
            if (player.mapChunk != null && player.mapChunk == activeChunk) {
                return@forEach
            }

            /**
             * If [Chunk] is not active, we want to register it as active.
             */
            if (activeChunk == null) {
                activeChunk = Chunk(Tile(chunkX, chunkZ))
                chunks[chunk] = activeChunk
            }

            /**
             * Get the [Region] based on the [activeChunk].
             */
            var activeRegion = getActiveRegionForChunk(activeChunk)
            if (activeRegion == null) {
                /**
                 * If [newRegion] is not active, we want to register it as active
                 * and set the [activeChunk] as one of its [Chunk]s.
                 */
                val chunkIndex = getIndexForChunk(activeChunk)
                val chunks = arrayOfNulls<Chunk>(MAX_REGION_CHUNKS)
                chunks[chunkIndex] = activeChunk

                val regionX = (activeChunk.base.x shr 6) * MAX_CHUNK_SIZE * MAX_REGION_CHUNKS
                val regionZ = (activeChunk.base.z shr 6) * MAX_CHUNK_SIZE * MAX_REGION_CHUNKS

                activeRegion = Region(Tile(regionX, regionZ), chunks)
                activeRegion.chunks[chunkIndex] = activeChunk
                regions[activeRegion.toInteger()] = activeRegion

                logger.debug("New region {} has been registered as active.", activeRegion.toTile())
                logger.debug("New chunk {} registered to region {}.", activeChunk.toTile(), activeRegion.toTile())
            } else {
                val chunkIndex = getIndexForChunk(activeChunk)
                if (activeRegion.chunks[chunkIndex] != activeChunk) {
                    /**
                     * If chunk is not active in its [Region], we register it.
                     */
                    activeRegion.chunks[chunkIndex] = activeChunk
                    logger.debug("New chunk {} registered to region {}.", activeChunk.toTile(), activeRegion.toTile())
                }
            }

            /**
             * Remove player from their old [Chunk] if they had one.
             */
            if (player.mapChunk != null) {
                if (getActiveRegionForChunk(player.mapChunk!!) == null) {
                    logger.warn("Player '{}' was registered to an inactive region {}.", player.username, player.mapChunk!!.toTile())
                }
                removePlayerFromChunk(player, player.mapChunk!!)
            }

            /**
             * Handle region change for [player] if necessary.
             */
            if (player.mapRegion != activeRegion) {
                if (player.mapRegion != null) {
                    removePlayerFromRegion(player, player.mapRegion!!)
                }
                addPlayerToRegion(player, activeRegion)
                player.mapRegion = activeRegion
            }

            /**
             * Now we add [player] to the player list associated with [activeChunk].
             */
            var players = chunkPlayers[activeChunk]
            if (players == null) {
                players = arrayListOf()
                chunkPlayers[activeChunk] = players
            }
            players.add(player)
            player.mapChunk = activeChunk
            addPlayerToChunk(player, activeChunk)
        }

        val elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS)
        if (removed > 0 || elapsed >= MAX_COMPUTATION_TIME) {
            if (elapsed < MAX_COMPUTATION_TIME) {
                logger.info("[Active regions: {}] [Regions removed: {}] [Time to compute: {}ms]", regions.size, removed, elapsed)
            } else {
                logger.fatal("[Active regions: {}] [Regions removed: {}] [Time to compute: {}ms / {}ms!]", regions.size, removed, elapsed, MAX_COMPUTATION_TIME)
            }
        }
    }

    /**
     * Completely removes the player from any [Chunk] they're in.
     */
    fun removePlayer(p: Player) {
        val activeChunk = getChunkForTile(p.tile)
        val registeredChunk = p.mapChunk

        if (activeChunk == null && registeredChunk == null) {
            return
        }

        if (activeChunk != null) {
            if (registeredChunk == null) {
                logger.warn("Active chunk {} for '{}' was not registered as their chunk!", activeChunk.toTile(), p.username)
            }
            val region = getActiveRegionForChunk(activeChunk)
            removePlayerFromChunk(p, activeChunk)
            if (region != null) {
                removePlayerFromRegion(p, region)
            }
        } else if (registeredChunk != null) {
            logger.warn("Registered chunk {} for '{}' is not active!", registeredChunk.toTile(), p.username)
        }
    }

    fun getChunkForTile(tile: Tile): Chunk? {
        val chunkX = (tile.x shr 3) * MAX_CHUNK_SIZE
        val chunkZ = (tile.z shr 3) * MAX_CHUNK_SIZE
        val chunk = (chunkX shl 16) or chunkZ
        return chunks[chunk]
    }

    fun getActiveRegionForChunk(chunk: Chunk): Region? {
        val regionX = (chunk.base.x shr 6) * (MAX_CHUNK_SIZE * MAX_REGION_CHUNKS)
        val regionZ = (chunk.base.z shr 6) * (MAX_CHUNK_SIZE * MAX_REGION_CHUNKS)
        val region = (regionX shl 16) or regionZ
        return regions[region]
    }

    /**
     * Get the surrounding [Chunk]s that are within [chunkRadius] of [center].
     *
     * @param center
     * The tile that belongs to the center [Chunk]. Can be any tile inside the
     * chunk. Does not have to be on relative coordinates of 0,0 on the chunk.
     *
     * @param chunkRadius
     * The radius, relative to [MAX_CHUNK_SIZE], of the search.
     *
     * @param activeOnly
     * If [true], only chunks that are currently active in our [Map] will be
     * returned.
     *
     * @param centerFirst
     * If [true], [center] will be inserted into our [Chunk] list as the head.
     *
     * @param prioritized
     * If [true], we use a separate algorithm that will insert the [Chunk]s in
     * a prioritized manner, where the search starts from the center and expands
     * outwards. Otherwise, the search starts from the bottom-left and continues
     * until it reaches the top-right ([center] being inserted first if [centerFirst]
     * is [true]).
     */
    fun getSurroundingChunks(center: Tile, chunkRadius: Int, activeOnly: Boolean, centerFirst: Boolean = true,
                             prioritized: Boolean = false): MutableList<Chunk> {
        val chunks = arrayListOf<Chunk>()

        if (centerFirst) {
            val tile = Tile(center)
            val chunk = getChunkForTile(tile)
            if (chunk != null) {
                chunks.add(chunk)
            } else if (!activeOnly) {
                chunks.add(Chunk(Tile(tile)))
            }
        }

        if (prioritized) {
            for (i in 0 until chunkRadius) {
                val requiredSpace = i + 1

                for (x in -requiredSpace..requiredSpace) {
                    for (z in -requiredSpace..requiredSpace) {
                        if (x == 0 && z == 0 && centerFirst) {
                            continue
                        }
                        val tile = center.transform(x * MAX_CHUNK_SIZE, z * MAX_CHUNK_SIZE)
                        val chunk = getChunkForTile(tile)
                        if (chunk != null) {
                            chunks.add(chunk)
                        } else if (!activeOnly) {
                            chunks.add(Chunk(Tile(tile)))
                        }
                    }
                }
            }
        } else {
            for (x in -chunkRadius..chunkRadius) {
                for (z in -chunkRadius..chunkRadius) {
                    if (x == 0 && z == 0 && centerFirst) {
                        continue
                    }
                    val tile = center.transform(x * MAX_CHUNK_SIZE, z * MAX_CHUNK_SIZE)
                    val chunk = getChunkForTile(tile)
                    if (chunk != null) {
                        chunks.add(chunk)
                    } else if (!activeOnly) {
                        chunks.add(Chunk(Tile(tile)))
                    }
                }
            }
        }

        return chunks
    }

    fun getPlayersInChunk(chunk: Chunk): MutableList<Player>? = chunkPlayers[chunk]

    fun getPlayersInRegion(region: Region): List<Player>? {
        val foundRegion = regions[region.toInteger()]
        if (foundRegion != region) {
            return null
        }
        return foundRegion.chunks.filterNotNull().map { getPlayersInChunk(it)!! }.flatten()
    }

    private fun getIndexForChunk(chunk: Chunk): Int {
        val chunkX = (chunk.base.x shr 3)
        val chunkZ = (chunk.base.z shr 3)

        val regionX = (chunk.base.x shr 6) * MAX_CHUNK_SIZE
        val regionZ = (chunk.base.z shr 6) * MAX_CHUNK_SIZE

        val row = chunkX - regionX
        val col = chunkZ - regionZ

        return (row * MAX_REGION_CHUNKS + col) / MAX_CHUNK_SIZE
    }

    private fun addPlayerToChunk(p: Player, chunk: Chunk): Boolean {
        logger.debug("Registered player {} to chunk {}.", p.username, chunk.toTile())
        // TODO: signal player addition to chunk -> execute scripts here
        return true
    }

    private fun removePlayerFromChunk(p: Player, chunk: Chunk): Boolean {
        val active = chunkPlayers[chunk]
        if (active != null) {
            logger.debug("Removed player {} from chunk {}.", p.username, chunk.toTile())
            val removed = active.remove(p)
            // TODO: signal player removal from chunk -> execute scripts here
            return removed
        }
        logger.warn("Player '{}' could not be removed from chunk {} as they have not been registered to it.", p.username, chunk.toTile())
        return false
    }

    private fun addPlayerToRegion(p: Player, region: Region): Boolean {
        logger.debug("Registered player {} to region {}.", p.username, region.toTile())
        p.world.server.getPlugins().executeRegionEnter(p, p.tile.toRegionId())
        return true
    }

    private fun removePlayerFromRegion(p: Player, region: Region) {
        logger.debug("Removed player {} from region {}.", p.username, region.toTile())
        p.world.server.getPlugins().executeRegionExit(p, p.tile.toRegionId())
    }
}