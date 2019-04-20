package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Pawn

/**
 * A custom list of [Pawn]s backed by an [Array].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PawnList<T : Pawn>(private val pawns: Array<T?>) {

    /**
     * Get the backing array for our pawn list.
     */
    val entries: Array<T?> = pawns

    val capacity = pawns.size

    /**
     * The total count of non-null [Pawn]s in the [pawns] array.
     */
    private var count = 0

    operator fun get(index: Int): T? = pawns[index]

    fun contains(pawn: T): Boolean = pawns[pawn.index] == pawn

    fun count(): Int = count

    /**
     * Invoke [action] for each <strong>non-null</strong> pawn in our list.
     */
    fun forEach(action: (T) -> Unit) {
        for (element in pawns) {
            if (element != null) {
                action(element)
            }
        }
    }

    /**
     * @return
     * True if any of <strong>non-null</strong> pawns in our list pass
     * the [predicate] test.
     */
    fun any(predicate: (T) -> Boolean): Boolean {
        for (element in pawns) {
            if (element != null && predicate(element)) {
                return true
            }
        }
        return false
    }

    /**
     * @return
     * False if any of <strong>non-null</strong> pawns in our list pass
     * the [predicate] test.
     */
    fun none(predicate: (T) -> Boolean): Boolean {
        for (element in pawns) {
            if (element != null && predicate(element)) {
                return false
            }
        }
        return true
    }

    /**
     * @return
     * The amount of <strong>non-null</strong> pawns in our list who
     * pass the [predicate] test.
     */
    fun count(predicate: (T) -> Boolean): Int {
        var count = 0
        for (element in pawns) {
            if (element != null && predicate(element)) {
                count++
            }
        }
        return count
    }

    /**
     * Get the first <strong>non-null</strong> pawn in our list who passes the
     * [predicate] test.
     */
    fun firstOrNull(predicate: (T) -> Boolean): T? {
        for (element in pawns) {
            if (element != null && predicate(element)) {
                return element
            }
        }
        return null
    }

    /**
     * Add [pawn] to our list.
     *
     * Note: this will set the [Pawn.index] for [pawn].
     */
    fun add(pawn: T): Boolean {
        for (i in 1 until pawns.size) {
            if (pawns[i] == null) {
                pawns[i] = pawn
                pawn.index = i
                count++
                return true
            }
        }
        return false
    }

    /**
     * Remove [pawn] from our list.
     *
     * Note: this will set the [Pawn.index] for [pawn].
     *
     * @return
     * True if the pawn was registered on our list, false otherwise.
     */
    fun remove(pawn: T): Boolean {
        /*
         * Only able to remove the pawn if it's registered in our array.
         */
        if (pawns[pawn.index] == pawn) {
            pawns[pawn.index] = null
            pawn.index = -1
            count--
            return true
        }
        return false
    }

    fun remove(index: Int): T? {
        if (pawns[index] != null) {
            val pawn = pawns[index]
            pawns[index] = null
            count--
            return pawn
        }
        return null
    }
}