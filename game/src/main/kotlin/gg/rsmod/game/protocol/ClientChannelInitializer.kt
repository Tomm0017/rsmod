package gg.rsmod.game.protocol

import gg.rsmod.game.model.World
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import io.netty.handler.traffic.ChannelTrafficShapingHandler
import io.netty.handler.traffic.GlobalTrafficShapingHandler
import net.runelite.cache.fs.Store
import java.math.BigInteger
import java.util.concurrent.Executors

/**
 * Initializes a channel and appends any required pipelines.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class ClientChannelInitializer(private val revision: Int, private val rsaExponent: BigInteger?, private val rsaModulus: BigInteger?,
                               private val filestore: Store, world: World) : ChannelInitializer<SocketChannel>() {

    /**
     * A global traffic handler that limits the amount of bandwidth all channels
     * can take up at once.
     */
    private val globalTrafficHandler = GlobalTrafficShapingHandler(Executors.newSingleThreadScheduledExecutor(), 0, 0, 1000)

    /**
     * The [io.netty.channel.ChannelHandler.Sharable] channel inbound adapter that
     * handles the messages sent and received from [SocketChannel]s.
     */
    private val handler = GameHandler(filestore, world)

    override fun initChannel(ch: SocketChannel) {
        val p = ch.pipeline()
        val crcs = filestore.indexes.map { it.crc }.toIntArray()

        p.addLast("global_traffic", globalTrafficHandler)
        p.addLast("channel_traffic", ChannelTrafficShapingHandler(0, 1024 * 5, 1000))
        p.addLast("timeout", IdleStateHandler(30, 0, 0))
        p.addLast("handshake_encoder", HandshakeEncoder())
        p.addLast("handshake_decoder", HandshakeDecoder(revision = revision, cacheCrcs = crcs, rsaExponent = rsaExponent, rsaModulus = rsaModulus))
        p.addLast("handler", handler)
    }
}