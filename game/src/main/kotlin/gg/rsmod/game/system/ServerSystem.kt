package gg.rsmod.game.system

import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext

/**
 * A [ServerSystem] handles any message received by the [channel] while it's alive
 * and readable.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class ServerSystem(open val channel: Channel) {

    /**
     * Called when a message is received from the [channel].
     */
    abstract fun receiveMessage(ctx: ChannelHandlerContext, msg: Any)

    /**
     * Called when the channel is disconnected.
     */
    abstract fun terminate()
}