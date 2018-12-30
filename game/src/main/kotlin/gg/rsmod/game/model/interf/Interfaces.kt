package gg.rsmod.game.model.interf

import gg.rsmod.game.model.entity.Player
import org.apache.logging.log4j.LogManager

/**
 * Holds the data for interfaces that can be opened.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Interfaces(val player: Player) {

    companion object {
        private val logger = LogManager.getLogger(Interfaces::class.java)
    }

    /**
     * A map of currently visible interfaces.
     *
     * Key: bit-shifted value of the parent and child id.
     * Value: Sub-child interface id that will be drawn on the child.
     */
    private val visible = hashMapOf<Int, Int>()

    /**
     * The current [DisplayMode] being used by the client.
     */
    var displayMode = DisplayMode.FIXED

    /**
     * Registers the [interfaceId] as being opened on the pane correspondent to
     * the [parent] and [child] id.
     *
     * @param parent
     * The interface id of where [interfaceId] will be drawn.
     *
     * @param child
     * The child of the [parent] interface, where [interfaceId] will be drawn.
     *
     * @param interfaceId
     * The interface that will be drawn on the [child] interface inside the [parent] interface.
     *
     * @note
     * This method by itself will not visually 'open' an interface for the client,
     * you will have to define a specific method or function that will call this
     * method and also send a [gg.rsmod.game.message.Message] to signal the client
     * to draw the interface.
     */
    fun open(parent: Int, child: Int, interfaceId: Int) {
        val paneHash = (parent shl 16) or child

        if (visible.containsKey(paneHash)) {
            closeByHash(paneHash)
        }
        visible[paneHash] = interfaceId
    }

    /**
     * Closes the [interfaceId] if, and only if, it's currently visible.
     *
     * @return The hash key of the visible interface, [-1] if not found.
     *
     * @note
     * This method by itself will not visually 'close' an interface for the client,
     * you will have to define a specific method or function that will call this
     * method and also send a [gg.rsmod.game.message.Message] to signal the client
     * to close the interface.
     */
    fun close(interfaceId: Int): Int {
        val found = visible.filterValues { it == interfaceId }.keys.firstOrNull()
        if (found != null) {
            visible.remove(found)
            return found
        }
        logger.warn("Interface {} is not visible and cannot be closed.", interfaceId)
        return -1
    }

    /**
     * Close the [child] interface on its [parent] interface.
     *
     * @note
     * This method by itself will not visually 'close' an interface for the client,
     * you will have to define a specific method or function that will call this
     * method and also send a [gg.rsmod.game.message.Message] to signal the client
     * to close the interface.
     */
    fun close(parent: Int, child: Int) {
        closeByHash((parent shl 16) or child)
    }

    /**
     * Closes any interface that is currently being drawn on the [hash].
     *
     * @param hash
     * The bit-shifted value of the parent and child interface ids where an
     * interface may be drawn.
     *
     * @note
     * This method by itself will not visually 'close' an interface for the client,
     * you will have to define a specific method or function that will call this
     * method and also send a [gg.rsmod.game.message.Message] to signal the client
     * to close the interface.
     */
    private fun closeByHash(hash: Int) {
        val found = visible.remove(hash)
        println("close by hash: ${hash shr 16}, ${hash and 0xFFFF}")
        if (found == null) {
            logger.warn("No interface visible in pane ({}, {}).", hash shr 16, hash and 0xFFFF)
        }
    }

    /**
     * Checks if the [interfaceId] is currently visible on any interface.
     */
    fun isVisible(interfaceId: Int): Boolean = visible.values.contains(interfaceId)

    /**
     * Set an interface as being visible. This should be reserved for settings
     * interfaces such as display mode as visible.
     */
    fun setVisible(interfaceId: Int, isVisible: Boolean) {
        if (isVisible) {
            visible[interfaceId shl 16] = interfaceId
        } else {
            visible.remove(interfaceId shl 16)
        }
    }
}