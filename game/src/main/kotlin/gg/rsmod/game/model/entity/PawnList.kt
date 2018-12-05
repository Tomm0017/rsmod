package gg.rsmod.game.model.entity

/**
 * A custom list of [Pawn]s backed by an [Array].
 *
 * @param capacity The maximum amount of [Pawn]s that can be stored at a time.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class PawnList<T : Pawn>(val capacity: Int) {

    /**
     * The [Array] of [Pawn]s registered on this list.
     */
    private val pawns = arrayOfNulls<Pawn>(capacity)

    /**
     * The total count of non-null [Pawn]s in the [pawns] array.
     */
    private var count = 0

    @Suppress("UNCHECKED_CAST")
    fun get(index: Int): T? = pawns[index] as? T

    fun contains(pawn: T): Boolean = pawns[pawn.index] == pawn

    fun count(): Int = count

    @Suppress("UNCHECKED_CAST")
    fun getBackedArray(): Array<T?> = pawns as Array<T?>

    @Suppress("UNCHECKED_CAST")
    fun forEach(action: (T) -> Unit) {
        for (element in pawns) {
            if (element != null) {
                action(element as T)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun any(predicate: (T) -> Boolean): Boolean {
        for (element in pawns) {
            if (predicate(element as T)) {
                return true
            }
        }
        return false
    }

    @Suppress("UNCHECKED_CAST")
    fun count(predicate: (T) -> Boolean): Int {
        var count = 0
        for (element in pawns) {
            if (predicate(element as T)) {
                count++
            }
        }
        return count
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

    @Suppress("UNCHECKED_CAST")
    fun remove(index: Int): T? {
        if (pawns[index] != null) {
            val pawn = pawns[index]
            pawns[index] = null
            count--
            return pawn as T
        }
        return null
    }
}