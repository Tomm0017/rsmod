package gg.rsmod.game.model.interf

import gg.rsmod.game.model.entity.Player
import org.apache.logging.log4j.LogManager

/**
 * Holds the data for interfaces that can be opened.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Interfaces(val player: Player, private val actionListener: InterfaceActionListener) {

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
     * The current [DisplayMode] being used.
     */
    private var displayMode = DisplayMode.FIXED

    fun getDisplayMode(): DisplayMode = displayMode

    fun setDisplayMode(newMode: DisplayMode) {
        if (displayMode != newMode) {
            actionListener.onDisplayChange(player, newMode)
        }
    }

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
     * @param type
     * Optional value. Can be used for different purposes, such as the flag for
     * OSRS interfaces.
     */
    fun open(parent: Int, child: Int, interfaceId: Int, type: Int = 0) {
        val paneHash = (parent shl 16) or child

        val replace = visible.remove(paneHash)
        if (replace != null) {
            closeByHash(paneHash)
        }
        visible[paneHash] = interfaceId
        actionListener.onOpen(player, parent, child, interfaceId, type)
    }

    /**
     * Closes the [interfaceId] if, and only if, it's currently visible.
     */
    fun close(interfaceId: Int) {
        val found = visible.filterValues { it == interfaceId }.keys.firstOrNull()
        if (found != null) {
            visible.remove(found)
            actionListener.onClose(player, interfaceId)
        } else {
            logger.warn("Interface {} is not visible and cannot be closed.", interfaceId)
        }
    }

    /**
     * Closes any interface that is currently being drawn on the [hash].
     *
     * @param hash
     * The bit-shifted value of the parent and child interface ids where an
     * interface may be drawn.
     */
    fun closeByHash(hash: Int) {
        val found = visible.remove(hash)
        if (found != null) {
            actionListener.onClose(player, hash shr 16)
        } else {
            logger.warn("No interface visible in pane ({}, {}).", hash shr 16, hash and 0xFFFF)
        }
    }

    /**
     * Checks if the [interfaceId] is currently visible on any interface.
     */
    fun isVisible(interfaceId: Int): Boolean = visible.values.contains(interfaceId)

}