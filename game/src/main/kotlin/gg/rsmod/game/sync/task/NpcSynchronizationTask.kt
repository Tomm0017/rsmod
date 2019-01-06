package gg.rsmod.game.sync.task

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.SynchronizationSegment
import gg.rsmod.game.sync.SynchronizationTask
import gg.rsmod.game.sync.segment.*
import gg.rsmod.net.packet.GamePacketBuilder
import gg.rsmod.net.packet.PacketType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcSynchronizationTask(private val player: Player, private val worldNpcs: List<Npc?>) : SynchronizationTask {

    companion object {

        private const val MAX_LOCAL_NPCS = 255
        private const val MAX_NPC_ADDITIONS_PER_CYCLE = 25
    }

    private val largeScene = player.hasLargeViewport()

    override fun run() {
        val opcode = if (!largeScene) player.world.npcUpdateBlocks.updateOpcode
                        else player.world.npcUpdateBlocks.largeSceneUpdateOpcode

        val buf = GamePacketBuilder(opcode, PacketType.VARIABLE_SHORT)
        val maskBuf = GamePacketBuilder()

        buf.switchToBitAccess()

        val segments = getSegments()
        segments.forEach { segment ->
            segment.encode(if (segment is NpcUpdateBlockSegment) maskBuf else buf)
        }

        if (maskBuf.getBuffer().writerIndex() > 0) {
            maskBuf.putBits(15, 0x7FFF)
        }

        buf.switchToByteAccess()

        buf.putBytes(maskBuf.getBuffer())
        player.write(buf.toGamePacket())
    }

    private fun getSegments(): List<SynchronizationSegment> {
        val segments = arrayListOf<SynchronizationSegment>()

        val localNpcs = player.localNpcs
        val iterator = localNpcs.iterator()

        segments.add(NpcCountSegment(localNpcs.size))
        while (iterator.hasNext()) {
            val npc = iterator.next()
            if (shouldRemove(npc)) {
                segments.add(RemoveLocalNpcSegment())
                iterator.remove()
                continue
            }
            npc.setActive(true)

            val requiresBlockUpdate = npc.blockBuffer.isDirty()

            if (npc.teleport) {
                segments.add(NpcSkipSegment(skip = false))
                segments.add(NpcTeleportSegment())
            } else if (npc.steps != null) {
                segments.add(NpcSkipSegment(skip = false))
                segments.add(NpcWalkSegment(npc.steps!!.walkDirection!!.getNpcWalkIndex(),
                        npc.steps!!.runDirection?.getNpcWalkIndex() ?: -1, requiresBlockUpdate))
                if (requiresBlockUpdate) {
                    segments.add(NpcUpdateBlockSegment(npc))
                }
            } else if (requiresBlockUpdate) {
                segments.add(NpcSkipSegment(skip = false))
                segments.add(NpcNoMovementSegment())
                segments.add(NpcUpdateBlockSegment(npc))
            } else {
                segments.add(NpcSkipSegment(skip = true))
            }
        }

        var added = 0

        for (npc in worldNpcs) {
            if (added >= MAX_NPC_ADDITIONS_PER_CYCLE || player.localNpcs.size >= MAX_LOCAL_NPCS) {
                break
            }

            if (npc == null || !shouldAdd(npc) || player.localNpcs.contains(npc)) {
                continue
            }

            val requiresBlockUpdate = npc.blockBuffer.isDirty()
            segments.add(AddLocalNpcSegment(player, npc, requiresBlockUpdate, largeScene))
            if (requiresBlockUpdate) {
                segments.add(NpcUpdateBlockSegment(npc))
            }

            added++
            player.localNpcs.add(npc)
        }

        return segments
    }

    private fun shouldRemove(npc: Npc): Boolean = !npc.isSpawned() || !isWithinView(npc.tile)

    private fun shouldAdd(npc: Npc): Boolean = npc.isSpawned() && isWithinView(npc.tile)

    private fun isWithinView(tile: Tile): Boolean = tile.isWithinRadius(player.tile, if (largeScene) Player.LARGE_VIEW_DISTANCE else Player.NORMAL_VIEW_DISTANCE)
}