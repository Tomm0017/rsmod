package gg.rsmod.game.sync.task

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.segment.AddLocalPlayerSegment
import gg.rsmod.game.sync.segment.PlayerLocationHashSegment
import gg.rsmod.game.sync.segment.PlayerSkipCountSegment
import gg.rsmod.game.sync.segment.PlayerTeleportSegment
import gg.rsmod.game.sync.segment.PlayerUpdateBlockSegment
import gg.rsmod.game.sync.segment.PlayerWalkSegment
import gg.rsmod.game.sync.segment.RemoveLocalPlayerSegment
import gg.rsmod.game.sync.segment.SetBitAccessSegment
import gg.rsmod.game.sync.segment.SetByteAccessSegment
import gg.rsmod.game.sync.segment.SignalPlayerUpdateBlockSegment
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.Misc

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerSynchronizationTask : SynchronizationTask<Player> {

    private const val MAX_LOCAL_PLAYERS = 255

    private const val MAX_PLAYER_ADDITIONS_PER_CYCLE = 40

    override fun run(pawn: Player) {
        val buf = GamePacketBuilder(pawn.world.playerUpdateBlocks.updateOpcode, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        val segments = getSegments(pawn)
        for (segment in segments) {
            segment.encode(if (segment is PlayerUpdateBlockSegment) maskBuf else buf)
        }

        buf.putBytes(maskBuf.byteBuf)
        pawn.write(buf.toGamePacket())

        pawn.gpiLocalCount = 0
        pawn.gpiExternalCount = 0
        for (i in 1 until 2048) {
            if (pawn.gpiLocalPlayers[i] != null) {
                pawn.gpiLocalIndexes[pawn.gpiLocalCount++] = i
            } else {
                pawn.gpiExternalIndexes[pawn.gpiExternalCount++] = i
            }
            pawn.gpiInactivityFlags[i] = pawn.gpiInactivityFlags[i] shr 1
        }
    }

    private fun getSegments(player: Player): List<SynchronizationSegment> {
        val segments = mutableListOf<SynchronizationSegment>()

        segments.add(SetBitAccessSegment())
        addLocalSegments(player, true, segments)
        segments.add(SetByteAccessSegment())

        segments.add(SetBitAccessSegment())
        addLocalSegments(player, false, segments)
        segments.add(SetByteAccessSegment())

        var added = 0

        segments.add(SetBitAccessSegment())
        added += addExternalSegments(player, true, added, segments)
        segments.add(SetByteAccessSegment())

        segments.add(SetBitAccessSegment())
        added += addExternalSegments(player, false, added, segments)
        segments.add(SetByteAccessSegment())

        return segments
    }

    private fun addLocalSegments(player: Player, initial: Boolean, segments: MutableList<SynchronizationSegment>) {
        var skipCount = 0

        for (i in 0 until player.gpiLocalCount) {
            val index = player.gpiLocalIndexes[i]
            val local = player.gpiLocalPlayers[index]

            val skip = when (initial) {
                true -> (player.gpiInactivityFlags[index] and 0x1) != 0
                else -> (player.gpiInactivityFlags[index] and 0x1) == 0
            }

            if (skip) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.gpiInactivityFlags[index] = player.gpiInactivityFlags[index] or 0x2
                continue
            }

            if (local != player && (local == null || shouldRemove(player, local))) {
                val lastTileHash = player.gpiTileHashMultipliers[index]
                val currTileHash = local?.tile?.asTileHashMultiplier ?: 0
                val updateTileHash = lastTileHash != currTileHash

                segments.add(RemoveLocalPlayerSegment(updateTileHash))
                if (updateTileHash) {
                    segments.add(PlayerLocationHashSegment(lastTileHash, currTileHash))
                }

                player.gpiLocalPlayers[index] = null
                player.gpiTileHashMultipliers[index] = currTileHash

                continue
            }

            val requiresBlockUpdate = local.blockBuffer.isDirty()
            if (requiresBlockUpdate) {
                segments.add(PlayerUpdateBlockSegment(other = local, newPlayer = false))
            }
            if (local.teleport) {
                segments.add(PlayerTeleportSegment(player = player, other = local, encodeUpdateBlocks = requiresBlockUpdate))
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
                for (j in i + 1 until player.gpiLocalCount) {
                    val nextIndex = player.gpiLocalIndexes[j]
                    val next = player.gpiLocalPlayers[nextIndex]
                    val skipNext = when (initial) {
                        true -> (player.gpiInactivityFlags[nextIndex] and 0x1) != 0
                        else -> (player.gpiInactivityFlags[nextIndex] and 0x1) == 0
                    }
                    if (skipNext) {
                        continue
                    }
                    if (next == null || next.blockBuffer.isDirty() || next.teleport || next.steps != null || next != player && shouldRemove(player, next)) {
                        break
                    }
                    skipCount++
                }
                segments.add(PlayerSkipCountSegment(skipCount))
                player.gpiInactivityFlags[index] = player.gpiInactivityFlags[index] or 0x2
            }
        }

        if (skipCount > 0) {
            throw RuntimeException()
        }
    }

    /**
     * @return
     * The total amount of external players that were added to the local player
     * list.
     */
    private fun addExternalSegments(player: Player, initial: Boolean, previouslyAdded: Int, segments: MutableList<SynchronizationSegment>): Int {
        var skipCount = 0
        var added = previouslyAdded

        for (i in 0 until player.gpiExternalCount) {
            val index = player.gpiExternalIndexes[i]

            val skip = when (initial) {
                true -> (player.gpiInactivityFlags[index] and 0x1) == 0
                else -> (player.gpiInactivityFlags[index] and 0x1) != 0
            }

            if (skip) {
                continue
            }

            if (skipCount > 0) {
                skipCount--
                player.gpiInactivityFlags[index] = player.gpiInactivityFlags[index] or 0x2
                continue
            }

            val nonLocal = if (index < player.world.players.capacity) player.world.players[index] else null

            if (nonLocal != null && added < MAX_PLAYER_ADDITIONS_PER_CYCLE && player.gpiLocalCount + added < MAX_LOCAL_PLAYERS
                    && shouldAdd(player, nonLocal)) {

                val oldTileHash = player.gpiTileHashMultipliers[index]
                val currTileHash = nonLocal.tile.asTileHashMultiplier

                val tileUpdateSegment = if (oldTileHash == currTileHash) null else PlayerLocationHashSegment(oldTileHash, currTileHash)

                segments.add(AddLocalPlayerSegment(other = nonLocal, locationSegment = tileUpdateSegment))
                segments.add(PlayerUpdateBlockSegment(other = nonLocal, newPlayer = true))

                player.gpiInactivityFlags[index] = player.gpiInactivityFlags[index] or 0x2
                player.gpiTileHashMultipliers[index] = currTileHash
                player.gpiLocalPlayers[index] = nonLocal

                added++
                continue
            }

            for (j in i + 1 until player.gpiExternalCount) {
                val nextIndex = player.gpiExternalIndexes[j]
                val skipNext = when (initial) {
                    true -> (player.gpiInactivityFlags[nextIndex] and 0x1) == 0
                    else -> (player.gpiInactivityFlags[nextIndex] and 0x1) != 0
                }
                if (skipNext) {
                    continue
                }
                val next = if (nextIndex < player.world.players.capacity) player.world.players[nextIndex] else null
                if (next != null && (shouldAdd(player, next) || next.tile.asTileHashMultiplier != player.gpiTileHashMultipliers[nextIndex])) {
                    break
                }
                skipCount++
            }
            segments.add(PlayerSkipCountSegment(count = skipCount))
            player.gpiInactivityFlags[index] = player.gpiInactivityFlags[index] or 0x2
        }

        if (skipCount > 0) {
            throw RuntimeException()
        }

        return added
    }

    private fun shouldAdd(player: Player, other: Player): Boolean = !other.invisible && other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE) && other != player

    private fun shouldRemove(player: Player, other: Player): Boolean = !other.isOnline || other.invisible || !other.tile.isWithinRadius(player.tile, Player.NORMAL_VIEW_DISTANCE)
}
