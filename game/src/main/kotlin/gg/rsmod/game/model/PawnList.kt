package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Pawn

/**
 * A custom list of [Pawn]s backed by an [Array].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PawnList<T: Pawn>(private val pawns: Array<T?>) {

    val capacity = pawns.size

    /**
     * The total count of non-null [Pawn]s in the [pawns] array.
     */
    private var count = 0

    fun getRaw(): Array<T?> = pawns

    fun get(index: Int): T? = pawns[index]

    fun contains(pawn: T): Boolean = pawns[pawn.index] == pawn

    fun count(): Int = count

    fun forEach(action: (T) -> Unit) {
        for (element in pawns) {
            if (element != null) {
                action(element)
            }
        }
    }

    fun any(predicate: (T) -> Boolean): Boolean {
        for (element in pawns) {
            if (element != null && predicate(element)) {
                return true
            }
        }
        return false
    }

    fun count(predicate: (T) -> Boolean): Int {
        var count = 0
        for (element in pawns) {
            if (element != null && predicate(element)) {
                count++
            }
        }
        return count
    }

    fun firstOrNull(predicate: (T) -> Boolean): T? {
        for (element in pawns) {
            if (element != null && predicate(element)) {
                return element
            }
        }
        return null
    }

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

    fun remove(pawn: T): Boolean {
        /**
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