package gg.rsmod.plugins.service.worldlist.io

import gg.rsmod.plugins.service.worldlist.model.WorldEntry
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * @author Triston Plummer ("Dread")
 *
 * @param list  The world list instance
 */
@ChannelHandler.Sharable
class WorldListChannelHandler(val list: List<WorldEntry>) : ChannelInboundHandlerAdapter() {

    /**
     * When a message is received from the client, send the [List] of [WorldEntry]s, in
     * the form of a HTTP response with the content as the encoded buffer.
     *
     * @param ctx   The channel handler context
     * @param msg   The inbound message
     */
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        ctx?.writeAndFlush(list)
    }

    /**
     * Gets executed when an exception is thrown in the pipeline
     *
     * @param ctx   The [ChannelHandlerContext] instance
     * @param cause The exception thrown
     */
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        ctx?.close()
    }
}