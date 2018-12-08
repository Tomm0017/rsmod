package gg.rsmod.game.model

import gg.rsmod.game.message.impl.LoginRegionMessage
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.service.xtea.XteaKeyService
import gg.rsmod.net.packet.DataType
import gg.rsmod.net.packet.GamePacketBuilder

/**
 * TODO: please delete this file, just using it temporarily until we have a
 * proper region/viewport system.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object PlayerGpi {

    var xteaKeys: XteaKeyService? = null

    const val MAP_SIZE = 104

    fun init(client: Client) {
        if (xteaKeys == null) {
            xteaKeys = client.world.server.getService(XteaKeyService::class.java, false).get()
        }
        val gpiBuf = GamePacketBuilder()

        /**
         * Encode the GPI.
         */
        client.localPlayers.add(client)

        gpiBuf.switchToBitAccess()
        gpiBuf.putBits(30, client.tile.toInteger())
        for (i in 1 until 2048) {
            if (i != client.index) {
                gpiBuf.putBits(18, 0)
            }
        }
        gpiBuf.switchToByteAccess()

        /**
         * Encode the xteas.
         */
        val xteaBuf = GamePacketBuilder()
        val chunkX = client.tile.x / 8
        val chunkZ = client.tile.z / 8

        var forceSend = false
        if ((chunkX / 8 == 48 || chunkX / 8 == 49) && chunkZ / 8 == 48) {
            forceSend = true
        }
        if (chunkX / 8 == 48 && chunkZ / 8 == 148) {
            forceSend = true
        }

        val lx = (chunkX - (MAP_SIZE shr 4)) / 8
        val rx = (chunkX + (MAP_SIZE shr 4)) / 8
        val lz = (chunkZ - (MAP_SIZE shr 4)) / 8
        val rz = (chunkZ + (MAP_SIZE shr 4)) / 8

        var count = 0
        for (x in lx..rx) {
            for (z in lz..rz) {
                if (!forceSend || z != 49 && z != 149 && z != 147 && x != 50 && (x != 49 || x != 47)) {
                    val region = z + (x shl 8)
                    val keys = xteaKeys!!.get(region)
                    for (key in keys) {
                        xteaBuf.put(DataType.INT, key)
                    }
                }
                count++
            }
        }

        val xteas = ByteArray(xteaBuf.getBuffer().readableBytes())
        val gpi = ByteArray(gpiBuf.getBuffer().readableBytes())
        xteaBuf.getBuffer().readBytes(xteas)
        gpiBuf.getBuffer().readBytes(gpi)

        client.write(LoginRegionMessage(chunkX, chunkZ, count, xteas, gpi))
    }
}