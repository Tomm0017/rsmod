package gg.rsmod.game.protocol

import gg.rsmod.game.model.World
import gg.rsmod.game.system.FilestoreSystem
import gg.rsmod.game.system.LoginSystem
import gg.rsmod.game.system.ServerSystem
import gg.rsmod.net.codec.handshake.HandshakeMessage
import gg.rsmod.net.codec.handshake.HandshakeType
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.ReadTimeoutException
import io.netty.util.AttributeKey
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager

/**
 * A [ChannelInboundHandlerAdapter] implementation that is responsible for intercepting
 * messages received by the [io.netty.channel.Channel].
 *
 * @author Tom <rspsmods@gmail.com>
 */
@ChannelHandler.Sharable
class GameHandler(private val filestore: Store, private val world: World) : ChannelInboundHandlerAdapter() {

    companion object {
        private val logger = LogManager.getLogger(GameHandler::class.java)

        /**
         * A mutable [AttributeKey] that stores the current [ServerSystem] that
         * will intercept the messages sent by the [io.netty.channel.Channel].
         */
        val SYSTEM_KEY: AttributeKey<ServerSystem> = AttributeKey.valueOf("system")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val session = ctx.channel().attr(SYSTEM_KEY).andRemove
        session?.terminate()
        ctx.channel().close()
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val attribute = ctx.channel().attr(SYSTEM_KEY)
            val system = attribute.get()
            if (system != null) {
                system.receiveMessage(ctx, msg)
            } else if (msg is HandshakeMessage) {
                /**
                 * The [HandshakeMessage] is sent in the [HandshakeDecoder], after the pipelines
                 * have been configured to meet the requirements of the handshake.
                 */
                when (msg.id) {
                    HandshakeType.FILESTORE.id -> attribute.set(FilestoreSystem(ctx.channel(), filestore))
                    HandshakeType.LOGIN.id -> attribute.set(LoginSystem(ctx.channel(), world))
                }
            }
        } catch (e: Exception) {
            logger.error("Error reading message $msg from channel ${ctx.channel()}.", e)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause.stackTrace.isEmpty() || cause.stackTrace[0].methodName != "read0") {
            if (cause is ReadTimeoutException) {
                logger.info("Channel disconnected due to read timeout: {}", ctx.channel())
            } else {
                logger.error("Channel threw an exception: ${ctx.channel()}", cause)
            }
        }
        ctx.channel().close()
    }
}