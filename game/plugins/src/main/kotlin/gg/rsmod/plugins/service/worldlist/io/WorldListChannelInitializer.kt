package gg.rsmod.plugins.service.worldlist.io

import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder

/**
 * @author Triston Plummer ("Dread")
 *
 * Handles the initialisation of a [SocketChannel] that has connected to this HTTP Server, which
 * is responsible for serving the world list data
 *
 * @param handler   The inbound channel handler
 */
class WorldListChannelInitializer(val handler: ChannelInboundHandlerAdapter) : ChannelInitializer<SocketChannel>() {

    /**
     * Initialises the [SocketChannel] pipeline
     *
     * @param ch    The inbound channel
     */
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        pipeline.addLast(HttpRequestDecoder())
        pipeline.addLast(HttpResponseEncoder())
        pipeline.addLast(WorldListEncoder())
        pipeline.addLast(handler)
    }

}