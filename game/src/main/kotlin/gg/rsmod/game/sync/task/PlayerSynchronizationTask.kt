package gg.rsmod.game.sync.task

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.segment.*
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.Misc

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PlayerSynchronizationTask(val player: Player) : SynchronizationTask {

    companion object {
        private const val MAX_LOCAL_PLAYERS = 255
        private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 40
    }

    override fun run() {
        val buf = GamePacketBuilder(player.world.playerUpdateBlocks.updateOpcode, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        val segments = getSegments()
        for (segment in segments) {
            segment.encode(if (segment is PlayerUpdateBlockSegment) maskBuf else buf)
        }

        buf.putBytes(maskBuf.getBuffer())
        player.write(buf.toGamePacket())

        player.localPlayerCount = 0
        player.externalPlayerCount = 0
        for (i in 1 until 2048) {
            if (player.localPlayers[i] != null) {
                player.localPlayerIndices[player.localPlayerCount++] = i
            } else {
                player.externalPlayerIndices[player.externalPlayerCount++] = i
            }
            player.inactivityPlayerFlags[i] = player.inactivityPlayerFlags[i] shr 1
        }
    }

    private fun getPrioritizedWorldPlayers(): List<Player> {
        val players = arrayListOf<Player>()

        mainLoop@ for (radius in 0..Player.NORMAL_VIEW_DISTANCE) {
            for (x in -radius..radius) {
                for (z in -radius..radius) {
                    val tile = player.tile.transform(x, z)
                    val chunk = player.world.chunks.get(tile.toChunkCoords(), createIfNeeded = false) ?: continue
                    chunk.getEntities<Player>(tile, EntityType.PLAYER, EntityType.CLIENT).forEach { p ->
                        if (p != player && players.size < MAX_LOCAL_PLAYERS) {
                            players.add(p)
                        }
                    }
                    if (players.size >= MAX_LOCAL_PLAYERS) {
                        break@mainLoop
                    }
                }
            }
        }

        return players
    }

    private fun getSegments(): List<SynchronizationSegment> {
        val segments = arrayListOf<SynchronizationSegment>()

        var skipCount = 0

        segments.add(SetBitAccessSegment())
        for (i in 0 until player.localPlayerCount) {
            val index = player.localPlayerIndices[i]
            val local = player.localPlayers[index]

            if ((player.inactivityPlayerFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                continue
            }

            if (local != player && (local == null || shouldRemove(local))) {
                player.localPlayers[index] = null
                player.otherPlayerTiles[index] = 0
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
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.playerWalkValue]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.playerWalkValue]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.playerWalkValue]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.playerWalkValue]
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
                for (j in i + 1 until player.localPlayerCount) {
                    val nextIndex = player.localPlayerIndices[j]
                    val next = player.localPlayers[nextIndex]
                    if ((player.inactivityPlayerFlags[nextIndex] and 0x1) != 0) {
                        continue
                    }
                    if (next == null || next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
            }
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())
        for (i in 0 until player.localPlayerCount) {
            val index = player.localPlayerIndices[i]
            val local = player.localPlayers[index]

            if ((player.inactivityPlayerFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                continue
            }

            if (local != player && (local == null || shouldRemove(local))) {
                player.localPlayers[index] = null
                player.otherPlayerTiles[index] = 0
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
                var dx = Misc.DIRECTION_DELTA_X[local.steps!!.walkDirection!!.playerWalkValue]
                var dz = Misc.DIRECTION_DELTA_Z[local.steps!!.walkDirection!!.playerWalkValue]
                var running = local.steps!!.runDirection != null

                var direction = 0
                if (running) {
                    dx += Misc.DIRECTION_DELTA_X[local.steps!!.runDirection!!.playerWalkValue]
                    dz += Misc.DIRECTION_DELTA_Z[local.steps!!.runDirection!!.playerWalkValue]
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
                for (j in i + 1 until player.localPlayerCount) {
                    val nextIndex = player.localPlayerIndices[j]
                    val next = player.localPlayers[nextIndex]
                    if ((player.inactivityPlayerFlags[nextIndex] and 0x1) == 0) {
                        continue
                    }
                    if (next == null || next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
            }
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())

        var added = 0
        for (i in 0 until player.externalPlayerCount) {
            val index = player.externalPlayerIndices[i]

            if ((player.inactivityPlayerFlags[index] and 0x1) == 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayerCount + added < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash
                player.localPlayers[index] = nonLocal

                added++
                continue
            }

            for (j in i + 1 until player.externalPlayerCount) {
                val nextIndex = player.externalPlayerIndices[j]
                if ((player.inactivityPlayerFlags[nextIndex] and 0x1) == 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        segments.add(SetBitAccessSegment())

        for (i in 0 until player.externalPlayerCount) {
            val index = player.externalPlayerIndices[i]

            if ((player.inactivityPlayerFlags[index] and 0x1) != 0) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players.get(index) else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE
                    && player.localPlayerCount + added < MAX_LOCAL_PLAYERS && shouldAdd(nonLocal)) {
                val tileHash = nonLocal.tile.to30BitInteger()
                segments.add(AddLocalPlayerSegment(player = player, other = nonLocal, index = index, tileHash = tileHash))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
                player.otherPlayerTiles[index] = tileHash
                player.localPlayers[index] = nonLocal

                added++
                continue
            }

            for (j in i + 1 until player.externalPlayerCount) {
                val nextIndex = player.externalPlayerIndices[j]
                if ((player.inactivityPlayerFlags[nextIndex] and 0x1) != 0) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players.get(nextIndex) else null
                if (next != null && shouldAdd(next)) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.inactivityPlayerFlags[index] = player.inactivityPlayerFlags[index] or 0x2
        }
        segments.add(SetByteAccessSegment())

        if (skipCount > 0) {
            throw RuntimeException()
        }

        return segments
    }

    private fun shouldAdd(other: Player): Boolean = !other.invisible && other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE) && other != player

    private fun shouldRemove(other: Player): Boolean = !other.isOnline() || other.invisible || !other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE)
}