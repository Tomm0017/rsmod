package gg.rsmod.game.model.interf

import gg.rsmod.game.model.entity.Player
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import mu.KotlinLogging

/**
 * Stores interfaces that are visible.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class InterfaceSet(val player: Player) {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    /**
     * A map of currently visible interfaces.
     *
     * Key: bit-shifted value of the parent and child id.
     * Value: Sub-child interface id that will be drawn on the child.
     */
    private val visible = Int2IntOpenHashMap()

    /**
     * The main screen is allowed to have one 'main' interface opened.
     * When a client closes a main interface, they will send a message
     * ([gg.rsmod.game.message.impl.CloseModalMessage]) and we have to make
     * sure the interface is removed from our [visible] map.
     */
    private var currentModal = -1

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
        val hash = (parent shl 16) or child
        if (visible.containsKey(hash)) {
            closeByHash(hash)
        }
        visible[hash] = interfaceId
        // TODO(Tom): remove player val and start using listeners instead.
        player.world.plugins.executeInterfaceOpen(player, interfaceId)
    }

    /**
     * Closes the [parent] if, and only if, it's currently visible.
     *
     * @return The hash key of the visible interface, [-1] if not found.
     *
     * @note
     * This method by itself will not visually 'close' an interface for the client,
     * you will have to define a specific method or function that will call this
     * method and also send a [gg.rsmod.game.message.Message] to signal the client
     * to close the interface.
     */
    fun close(parent: Int): Int {
        val found = visible.filterValues { it == parent }.keys.firstOrNull()
        if (found != null) {
            closeByHash(found)
            return found
        }
        logger.warn("Interface {} is not visible and cannot be closed.", parent)
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
     *
     * @return
     * The interface associated with the hash, otherwise -1
     */
    fun close(parent: Int, child: Int): Int = closeByHash((parent shl 16) or child)

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
     *
     * @return
     * The interface associated with the hash, otherwise -1
     */
    private fun closeByHash(hash: Int): Int {
        val found = visible.remove(hash)
        if (found != visible.defaultReturnValue()) {
            // TODO(Tom): remove player val and start using listeners instead.
            player.world.plugins.executeInterfaceClose(player, found)
            return found
        }
        logger.warn("No interface visible in pane ({}, {}).", hash shr 16, hash and 0xFFFF)
        return -1
    }

    /**
     * Calls the [open] method, but also sets the [currentModal]
     * to [component].
     */
    fun openMain(parent: Int, child: Int, component: Int) {
        open(parent, child, component)
        currentModal = component
    }

    /**
     * Calls the [close] method for [currentModal].
     */
    fun closeMain() {
        if (currentModal != -1) {
            close(currentModal)
            currentModal = -1
        }
    }

    /**
     * Checks if an interface id was placed on interface ([parent], [child]).
     */
    fun isOccupied(parent: Int, child: Int): Boolean = visible.containsKey((parent shl 16) or child)

    /**
     * Checks if the [component] is currently visible on any interface.
     */
    fun isVisible(component: Int): Boolean = visible.values.contains(component)

    /**
     * Set an interface as being visible. This should be reserved for settings
     * interfaces such as display mode as visible.
     */
    fun setVisible(parent: Int, child: Int, visible: Boolean) {
        val hash = (parent shl 16) or child
        if (visible) {
            this.visible[hash] = parent
        } else {
            this.visible.remove(hash)
        }
    }
}