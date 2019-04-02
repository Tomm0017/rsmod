package gg.rsmod.plugins.service.worldlist.io

import gg.rsmod.plugins.service.worldlist.model.WorldEntry
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE

/**
 * @author Triston Plummer ("Dread")
 *
 * Encodes an outbound [List] of [WorldEntry]s into a [HttpResponse]
 */
class WorldListEncoder : MessageToMessageEncoder<List<WorldEntry>>() {

    /**
     * Encodes the outgoing world list
     *
     * @param ctx   The [ChannelHandlerContext] instance
     * @param msg   The [List] of [WorldEntry]s
     * @param out   The outgoing messages
     */
    override fun encode(ctx: ChannelHandlerContext, msg: List<WorldEntry>, out: MutableList<Any>) {

        // The world list buffer
        val worldList = encodeWorldList(msg)

        // The outgoing buffer
        val buf = Unpooled.buffer()
        buf.writeInt(worldList.readableBytes())
        buf.writeBytes(worldList)

        // The outgoing HTTP Response
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf)

        // Set the response headers
        val headers = response.headers()
        headers.set(CONTENT_TYPE, LIST_CONTENT_TYPE)
        headers.set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes())
        headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE)

        // Add the outgoing message
        out.add(response)
    }

    /**
     * Encodes the world list
     *
     * @param list  The list of worlds
     * @return      The encoded buffer
     */
    private fun encodeWorldList(list: List<WorldEntry>) : ByteBuf {
        val buf = Unpooled.buffer()

        buf.writeShort(list.size)

        list.forEach {

            var mask = 0
            it.types.forEach { type -> mask = mask or type.mask }

            buf.writeShort(it.id)
            buf.writeInt(mask)
            buf.writeString(it.address)
            buf.writeString(it.activity)
            buf.writeByte(it.location.id)
            buf.writeShort(it.players)
        }

        return buf
    }

    /**
     * An extension function used for writing a string to the buffer
     *
     * @param value The string to write
     */
    private fun ByteBuf.writeString(value: String) {
        val bytes = value.toByteArray()
        writeBytes(bytes).writeByte(0)
    }

    companion object {

        /**
         * The content type of the world list, which is just arbitrary binary data
         */
        private const val LIST_CONTENT_TYPE = "application/octet-stream"
    }
}