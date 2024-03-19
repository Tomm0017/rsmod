package gg.rsmod.game.model.appearance

import gg.rsmod.util.Misc.toArray

object Looks {
    val MALE_HEADS = arrayOf(*(0..9).toArray(), *(129..134).toArray(), 151, *(144..150).toArray()) // 0-9, 129-134, 151, 144-150
    val FEMALE_HEADS = arrayOf(*(45..55).toArray(), *(118..128).toArray(), 152, 143) // 45-55, 118-128, 152, 143

    fun getHeads(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_HEADS
            Gender.FEMALE -> FEMALE_HEADS
        }
    }

    val MALE_JAWS = arrayOf(*(10..13).toArray(), *(15..17).toArray(), *(111..117).toArray(), 14) // 10-13, 15-17, 111-117, 14
    val FEMALE_JAWS = arrayOf(-1) // no bearded ladies

    /**
     * Allows for [Gender.FEMALE] however this shouldn't be utilized
     * in that capacity unless bearded ladies are desired.
     */
    fun getJaws(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_JAWS
            Gender.FEMALE -> FEMALE_JAWS
        }
    }

    val MALE_TORSOS = arrayOf(*(18..25).toArray(), *(105..110).toArray())
    val FEMALE_TORSOS = arrayOf(*(56..60).toArray(), *(89..94).toArray())

    fun getTorsos(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_TORSOS
            Gender.FEMALE -> FEMALE_TORSOS
        }
    }

    val MALE_ARMS = arrayOf(*(26..32).toArray(), *(84..88).toArray())
    val FEMALE_ARMS = arrayOf(*(61..66).toArray(), *(95..99).toArray())

    fun getArms(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_ARMS
            Gender.FEMALE -> FEMALE_ARMS
        }
    }

    val MALE_HANDS = arrayOf(33, 34)
    val FEMALE_HANDS = arrayOf(67, 68)

    fun getHands(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_HANDS
            Gender.FEMALE -> FEMALE_HANDS
        }
    }

    val MALE_LEGS = arrayOf(*(36..41).toArray(), *(100..104).toArray())
    val FEMALE_LEGS = arrayOf(*(70..73).toArray(), *(76..78).toArray(), *(135..140).toArray())

    fun getLegs(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_LEGS
            Gender.FEMALE -> FEMALE_LEGS
        }
    }

    val MALE_FEETS = arrayOf(42,43)
    val FEMALE_FEETS = arrayOf(79, 80)

    fun getFeets(gender: Gender): Array<Int> {
        return when(gender) {
            Gender.MALE -> MALE_FEETS
            Gender.FEMALE -> FEMALE_FEETS
        }
    }
}