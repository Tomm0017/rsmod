package gg.rsmod.game.system

import com.google.common.primitives.Ints
import gg.rsmod.net.codec.filestore.FilestoreRequest
import gg.rsmod.net.codec.filestore.FilestoreResponse
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import mu.KLogging
import net.runelite.cache.fs.Container
import net.runelite.cache.fs.Store
import net.runelite.cache.fs.jagex.CompressionType
import net.runelite.cache.fs.jagex.DiskStorage

/**
 * A [ServerSystem] responsible for sending decoded [filestore] data to the
 * [channel].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class FilestoreSystem(channel: Channel, private val filestore: Store) : ServerSystem(channel) {

    /**
     * TODO(Tom): the logic for encoding the data should be handled
     * by a pipeline defined in the [net] module instead. This [ServerSystem]
     * should only be responsible for informing the pipeline that a request
     * was sent by the client.
     */

    override fun receiveMessage(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is FilestoreRequest) {
            if (msg.index == 255) {
                encodeIndexData(ctx, msg)
            } else {
                encodeFileData(ctx, msg)
            }
        }
    }

    override fun terminate() {
    }

    private fun encodeIndexData(ctx: ChannelHandlerContext, req: FilestoreRequest) {
        val data: ByteArray

        if (req.archive == 255) {
            if (cachedIndexData == null) {
                val buf = ctx.alloc().heapBuffer(filestore.indexes.size * 8)

                filestore.indexes.forEach { index ->
                    buf.writeInt(index.crc)
                    buf.writeInt(index.revision)
                }

                val container = Container(CompressionType.NONE, -1)
                container.compress(buf.array().copyOf(buf.readableBytes()), null)
                cachedIndexData = container.data
                buf.release()
            }
            data = cachedIndexData!!
        } else {
            val storage = filestore.storage as DiskStorage
            data = storage.readIndex(req.archive)
        }

        val response = FilestoreResponse(index = req.index, archive = req.archive, data = data)
        ctx.writeAndFlush(response)
    }

    private fun encodeFileData(ctx: ChannelHandlerContext, req: FilestoreRequest) {
        val index = filestore.findIndex(req.index)!!
        val archive = index.getArchive(req.archive)!!
        var data = filestore.storage.loadArchive(archive)

        if (data != null) {
            val compression = data[0]
            val length = Ints.fromBytes(data[1], data[2], data[3], data[4])
            val expectedLength = length + (if (compression.toInt() != CompressionType.NONE) 9 else 5)
            if (expectedLength != length && data.size - expectedLength == 2) {
                data = data.copyOf(data.size - 2)
            }

            val response = FilestoreResponse(index = req.index, archive = req.archive, data = data)
            ctx.writeAndFlush(response)
        } else {
            logger.warn("Data is missing from archive. index={}, archive={}", req.index, req.archive)
        }
    }

    companion object : KLogging() {
        private var cachedIndexData: ByteArray? = null
    }
}