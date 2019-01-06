package gg.rsmod.game.sync.task

import gg.rsmod.game.model.INDEX_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.segment.*
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.Misc
import org.apache.logging.log4j.LogManager

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerSynchronizationTask(val player: Player) : SynchronizationTask {

    companion object {
        private val logger = LogManager.getLogger(PlayerSynchronizationTask::class.java)

        private const val MAX_LOCAL_PLAYERS = 255
        private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 15
    }

    private val nonLocalIndices = arrayListOf<Int>().apply { addAll(1..2047) }

    override fun run() {
        val buf = GamePacketBuilder(player.world.playerUpdateBlocks.updateOpcode, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        val segments = getSegments()
        segments.forEach { segment ->
            segment.encode(if (segment is PlayerUpdateBlockSegment) maskBuf else buf)
        }

        buf.putBytes(maskBuf.getBuffer())
        player.write(buf.toGamePacket())

        for (i in 1 until 2048) {
            player.otherPlayerSkipFlags[i] = player.otherPlayerSkipFlags[i] shr 1
        }
    }

    private fun getSegments(): List<SynchronizationSegment> {
        val segments = arrayListOf<SynchronizationSegment>()

        val localsToRemove = arrayListOf<Player>()
        val nonLocalsToRemove = arrayListOf<Int>()

        var skipCount = 0
        val locals = player.localPlayers

        segments.add(SetBitAccessSegment())
        for (i in 0 until locals.size) {
            val local = locals[i]
            val index = local.attr[INDEX_ATTR]

            nonLocalIndices.remove(index)

            if ((player.otherPlayerSkipFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            if (local != player && shouldRemove(local)) {
                player.otherPlayerTiles[index] = 0
                localsToRemove.add(local)
                segments.add(RemoveLocalPlayerSegment())
                continue
            }

            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
            }
            if (local.teleport) {
                segments.add(PlayerTeleportSegment(player = player, other = local,
                        index = index, encodeUpdateBlocks = requiresBlockUpdate))
            } else if (local.steps != null) {
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    direction = Misc.getPlayerRunningDirection(dx, dz)
                    running = direction != -1
                }
                if (!running) {
                    direction = Misc.getPlayerWalkingDirection(dx, dz)
                }

                segments.add(PlayerWalkSegment(encodeUpdateBlocks = requiresBlockUpdate, running = running,
                        direction = direction))

                if (!requiresBlockUpdate && running) {
                    segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
                }
            } else if (requiresBlockUpdate) {
                segments.add(SignalPlayerUpdateBlockSegment())
            } else {
                for (j in i + 1 until locals.size) {
                    val next = locals[j]
                    val nextIndex = next.attr[INDEX_ATTR]
                    if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) != 0) {
                        continue
                    }
                    if (next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
            }
        }
        segments.add(SetByteAccessSegment())

        locals.removeAll(localsToRemove)

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())
        for (i in 0 until locals.size) {
            val local = locals[i]
            val index = local.attr[INDEX_ATTR]

            if ((player.otherPlayerSkipFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            if (local != player && shouldRemove(local)) {
                player.otherPlayerTiles[index] = 0
                localsToRemove.add(local)
                segments.add(RemoveLocalPlayerSegment())
                continue
            }

            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
            }
            if (local.teleport) {
                segments.add(PlayerTeleportSegment(player = player, other = local,
                        index = index, encodeUpdateBlocks = requiresBlockUpdate))
            } else if (local.steps != null) {
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.getPlayerWalkIndex()]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.getPlayerWalkIndex()]
                    direction = Misc.getPlayerRunningDirection(dx, dz)
                    running = direction != -1
                }
                if (!running) {
                    direction = Misc.getPlayerWalkingDirection(dx, dz)
                }

                segments.add(PlayerWalkSegment(encodeUpdateBlocks = requiresBlockUpdate, running = running,
                        direction = direction))

                if (!requiresBlockUpdate && running) {
                    segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
                }
            } else if (requiresBlockUpdate) {
                segments.add(SignalPlayerUpdateBlockSegment())
            } else {
                for (j in i + 1 until locals.size) {
                    val next = locals[j]
                    val nextIndex = next.attr[INDEX_ATTR]
                    if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) == 0) {
                        continue
                    }
                    if (next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
            }
        }
        segments.add(SetByteAccessSegment())

        locals.removeAll(localsToRemove)

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())

        var added = 0
        for (i in 0 until nonLocalIndices.size) {
            val index = nonLocalIndices[i]

            if ((player.otherPlayerSkipFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash

                player.localPlayers.add(nonLocal)
                player.localPlayers.sortBy { it.index }
                added++
                nonLocalsToRemove.add(index)
                continue
            }

            for (j in i + 1 until nonLocalIndices.size) {
                val nextIndex = nonLocalIndices[j]
                if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) == 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())

        nonLocalIndices.removeAll(nonLocalsToRemove)
        for (i in 0 until nonLocalIndices.size) {
            val index = nonLocalIndices[i]

            if ((player.otherPlayerSkipFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayers.size < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash

                player.localPlayers.add(nonLocal)
                player.localPlayers.sortBy { it.index }
                added++
                continue
            }

            for (j in i + 1 until nonLocalIndices.size) {
                val nextIndex = nonLocalIndices[j]
                if ((player.otherPlayerSkipFlags[nextIndex] and 0x1) != 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.otherPlayerSkipFlags[index] = player.otherPlayerSkipFlags[index] or 0x2
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        return segments
    }

    private fun shouldAdd(other: Player): Boolean = other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE) && !player.localPlayers.contains(other) && other != player

    private fun shouldRemove(other: Player): Boolean = !other.isOnline() || !other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE)
}