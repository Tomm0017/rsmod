package gg.rsmod.game.model.instance

import gg.rsmod.game.model.*
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.Chunk
import mu.KLogging
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMapAllocator {

    companion object : KLogging() {

        /**
         * 07 identifies instanced maps by having an X-axis of 6400 or above. They
         * use this for some client scripts, such as a Theatre of Blood cs2 that
         * will change depending on if you're inside the Theatre (in an instance)
         * or in the lobby.
         *
         * Current stats (as of 17/03/2018):
         * Area size: 3200x6400
         * Instance support: 5000 [50 in x-axis * 100 in z-axis]
         *
         * [Instance support]: the amount of instances the world can support at a time,
         * assuming every map is 64x64 in size, which isn't always the case.
         */
        private val VALID_AREA = Area(bottomLeftX = 6400, bottomLeftZ = 0, topRightX = 9600, topRightZ = 6400)

        /**
         * The amount of game cycles that must go by before scanning the active
         * [maps] for any [InstancedMap] eligible to be de-allocated.
         */
        private const val SCAN_MAPS_CYCLES = 25
    }

    private val maps = arrayListOf<InstancedMap>()

    private var deallocationScanCycle = 0

    /**
     * Allocate a new [InstancedMap] given [chunks].
     *
     * @param world
     * The [World] that the instanced map is apart of.
     *
     * @param chunks
     * The [InstancedChunkSet] that holds all the [InstancedChunk]s that will make
     * up the newly constructed [InstancedMap], if applicable.
     */
    fun allocate(world: World, chunks: InstancedChunkSet, exitTile: Tile, configs: Configuration? = null): InstancedMap? {
        val area = VALID_AREA
        val step = 64

        /**
         * The total amount of tiles that the new [InstancedMap] will occupy.
         */
        val totalTiles = chunks.regionSize * Chunk.REGION_SIZE

        for (x in area.bottomLeftX until area.topRightX step step) {
            for (z in area.bottomLeftZ until area.topRightZ step step) {

                /**
                 * If a map is already allocated in [x,z], we move on.
                 */
                if (maps.any { it.area.contains(x, z) || it.area.contains(x + totalTiles - 1, z + totalTiles - 1) }) {
                    continue
                }

                val map = allocate(x, z, chunks, exitTile, configs)
                applyCollision(world, map, configs?.bypassObjectChunkBounds ?: false)
                maps.add(map)
                return map
            }
        }

        return null
    }

    private fun allocate(x: Int, z: Int, chunks: InstancedChunkSet, exitTile: Tile, configs: Configuration?): InstancedMap =
            InstancedMap(Area(x, z, x + chunks.regionSize * Chunk.REGION_SIZE, z + chunks.regionSize * Chunk.REGION_SIZE), chunks, exitTile,
                    configs?.owner, configs?.attributes ?: EnumSet.noneOf(InstancedMapAttribute::class.java))

    private fun deallocate(world: World, map: InstancedMap) {
        if (maps.remove(map)) {
            removeCollision(world, map)
            world.removeAll(map.area)

            world.players.forEach { player ->
                if (map.area.contains(player.tile)) {
                    player.teleport(map.exitTile)
                }
            }
        }
    }

    internal fun logout(player: Player) {
        val world = player.world

        getMap(player.tile)?.let { map ->
            player.teleport(map.exitTile)

            if (map.attr.contains(InstancedMapAttribute.DEALLOCATE_ON_LOGOUT)) {
                val mapOwner = map.owner!! // If map has this attribute, they should also set an owner.
                if (player.uid == mapOwner) {
                    deallocate(world, map)
                }
            }
        }
    }

    internal fun death(player: Player) {
        val world = player.world

        getMap(player.tile)?.let { map ->
            if (map.attr.contains(InstancedMapAttribute.DEALLOCATE_ON_DEATH)) {
                val mapOwner = map.owner!! // If map has this attribute, they should also set an owner.
                if (player.uid == mapOwner) {
                    deallocate(world, map)
                }
            }
        }
    }

    internal fun cycle(world: World) {
        if (deallocationScanCycle++ == SCAN_MAPS_CYCLES) {

            var deallocated = 0

            for (i in 0 until maps.size) {
                val map = maps[i]

                if (world.players.none { map.area.contains(it.tile) }) {
                    deallocate(world, map)
                    deallocated++
                }
            }

            deallocationScanCycle = 0

            if (deallocated > 0) {
                logger.info { "De-allocated $deallocated instanced map${if (deallocated != 1) "s" else ""}." }
            }
        }
    }

    fun getMap(tile: Tile): InstancedMap? = maps.find { it.area.contains(tile) }

    private fun applyCollision(world: World, map: InstancedMap, bypassObjectChunkBounds: Boolean) {
        val bounds = Chunk.CHUNKS_PER_REGION * map.chunks.regionSize
        val heights = Tile.TOTAL_HEIGHT_LEVELS

        val chunks = map.chunks.values

        for (height in 0 until heights) {
            for (x in 0 until bounds) {
                for (z in 0 until bounds) {
                    val coords = InstancedChunkSet.getCoordinates(x, z, height)
                    val chunk = chunks[coords]

                    val chunkH = (coords shr 28) and 0x3
                    val chunkX = (coords shr 14) and 0x3FF
                    val chunkZ = coords and 0x7FF

                    val baseTile = map.area.bottomLeft.transform(chunkX shl 3, chunkZ shl 3, chunkH)
                    val newChunk = world.chunks.getOrCreate(baseTile)

                    if (chunk != null) {
                        val copyTile = Tile.fromRotatedHash(chunk.packed)
                        val copyChunk = world.chunks.get(copyTile.chunkCoords, createIfNeeded = true)!!

                        copyChunk.getEntities<StaticObject>(EntityType.STATIC_OBJECT).forEach { obj ->
                            if (obj.tile.height == chunkH && obj.tile.isInSameChunk(copyTile)) {
                                val def = obj.getDef(world.definitions)
                                val width = def.getRotatedWidth(obj)
                                val length = def.getRotatedLength(obj)

                                val localX = obj.tile.x % 8
                                val localZ = obj.tile.z % 8

                                val newObj = DynamicObject(obj.id, obj.type, (obj.rot + chunk.rot) and 0x3, baseTile.transformAndRotate(localX, localZ, chunk.rot, width, length))
                                val insideChunk = newObj.tile.isInSameChunk(baseTile)

                                if (insideChunk) {
                                    newChunk.addEntity(world, newObj, newObj.tile)
                                } else if (!bypassObjectChunkBounds) {
                                    throw IllegalStateException("Could not copy object due to its size and rotation outcome (object rotation + chunk rotation). " +
                                            "The object would, otherwise, be spawned out of bounds of its original chunk. [obj=$obj, copy=$newObj]")
                                }
                            }
                        }

                        copyChunk.blockedTiles.forEach { tile ->
                            if (tile.height == chunkH && tile.isInSameChunk(copyTile)) {
                                val localX = tile.x % 8
                                val localZ = tile.z % 8
                                val local = baseTile.transformAndRotate(localX, localZ, chunk.rot)
                                newChunk.getMatrix(chunkH).block(local.x % 8, local.z % 8, impenetrable = true)
                            }
                        }
                    } else {
                        for (lx in 0 until Chunk.CHUNK_SIZE) {
                            for (lz in 0 until Chunk.CHUNK_SIZE) {
                                newChunk.getMatrix(chunkH).block(lx, lz, impenetrable = true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun removeCollision(world: World, map: InstancedMap) {
        val regionCount = map.chunks.regionSize
        val chunks = world.chunks

        for (i in 0 until regionCount) {
            val tile = map.area.bottomLeft.transform(i * Chunk.REGION_SIZE, i * Chunk.REGION_SIZE)
            chunks.remove(tile.chunkCoords)
        }
    }

    class Configuration {

        internal var owner: PlayerUID? = null

        internal val attributes = EnumSet.noneOf(InstancedMapAttribute::class.java)

        /**
         * If true, objects that are found to exceed the bounds of its [Chunk] will
         * not throw an error - however the object will not be applied to the [world]'s
         * [gg.rsmod.game.model.region.ChunkSet], so this flag should be used with
         * that caveat in mind.
         *
         * Explanation:
         * In certain scenarios, an object's tile can overextend its original [Chunk]
         * where it would be placed in the [InstancedMap]; this can occur in any object
         * who's width or length is greater than 1 (one).
         *
         * Example:
         * - 2x2 object is in the local tile of 2,7 (in respect to its [Chunk])
         * - The [InstancedChunk.rot] is set to 2 (two)
         * - The outcome local tile would be 2,-1
         *
         * The outcome local tile would be out-of-bounds in its [Chunk] and would
         * lead to undesired behaviour.
         */
        internal var bypassObjectChunkBounds: Boolean = false

        /**
         * Verify that the [Configuration] is in an acceptable state for usage.
         */
        fun verify(): Configuration {
            val ownerRequired = EnumSet.of(InstancedMapAttribute.DEALLOCATE_ON_LOGOUT, InstancedMapAttribute.DEALLOCATE_ON_DEATH)
            check(owner != null || attributes.none { it in ownerRequired }) { "One or more attributes require an owner to be set." }
            return this
        }

        fun setOwner(owner: PlayerUID): Configuration {
            this.owner = owner
            return this
        }

        fun addAttribute(attribute: InstancedMapAttribute, vararg others: InstancedMapAttribute): Configuration {
            attributes.add(attribute)
            attributes.addAll(others)
            return this
        }

        /**
         * @see bypassObjectChunkBounds
         */
        fun setBypassObjectChunkBounds(bypassObjectChunkBounds: Boolean): Configuration {
            this.bypassObjectChunkBounds = bypassObjectChunkBounds
            return this
        }
    }
}