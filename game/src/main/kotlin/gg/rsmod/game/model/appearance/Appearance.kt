package gg.rsmod.game.model.appearance

import gg.rsmod.game.model.appearance.Looks.getArms
import gg.rsmod.game.model.appearance.Looks.getFeets
import gg.rsmod.game.model.appearance.Looks.getHands
import gg.rsmod.game.model.appearance.Looks.getHeads
import gg.rsmod.game.model.appearance.Looks.getJaws
import gg.rsmod.game.model.appearance.Looks.getLegs
import gg.rsmod.game.model.appearance.Looks.getTorsos

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Appearance(val looks: IntArray, val colors: IntArray, var gender: Gender) {

    /**
     * @param option - the specified look to select from the [Appearance]'s [looks]
     *      with valid options explicitly as follows:
     *      0 -> HEAD
     *      1 -> JAW
     *      2 -> TORSO
     *      3 -> ARMS
     *      4 -> HANDS
     *      5 -> LEGS
     *      6 -> FEET
     * Note| the JAW option is currently not provided for [Gender.FEMALE]
     *
     * @returns the appropriate look model value for current appearance
     *      based on the supplies option
     */
    fun getLook(option: Int): Int {
        return when(gender) {
            Gender.MALE -> {
                when(option) {
                    0 -> getHeads(gender)[looks[0]]
                    1 -> getJaws(gender)[looks[1]]
                    2 -> getTorsos(gender)[looks[2]]
                    3 -> getArms(gender)[looks[3]]
                    4 -> getHands(gender)[looks[4]]
                    5 -> getLegs(gender)[looks[5]]
                    6 -> getFeets(gender)[looks[6]]
                    else -> -1
                }
            }
            Gender.FEMALE -> {
                when(option) {
                    0 -> getHeads(gender)[looks[0]]
                    2 -> getTorsos(gender)[looks[1]]
                    3 -> getArms(gender)[looks[2]]
                    4 -> getHands(gender)[looks[3]]
                    5 -> getLegs(gender)[looks[4]]
                    6 -> getFeets(gender)[looks[5]]
                    else -> -1
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Appearance

        if (!looks.contentEquals(other.looks)) return false
        if (!colors.contentEquals(other.colors)) return false
        if (gender != other.gender) return false

        return true
    }

    override fun hashCode(): Int {
        var result = looks.contentHashCode()
        result = 31 * result + colors.contentHashCode()
        result = 31 * result + gender.hashCode()
        return result
    }

    companion object {
        private val DEFAULT_COLORS = intArrayOf(0, 27, 9, 0, 0)

        private val DEFAULT_MALE_LOOKS = intArrayOf(15, 9, 3, 8, 0, 3, 1) // 133, 113, 21, 86, 33, 39, 43
        val DEFAULT_MALE = Appearance(DEFAULT_MALE_LOOKS, DEFAULT_COLORS, Gender.MALE)

        private val DEFAULT_FEMALE_LOOKS = intArrayOf(0, 0, 0, 0, 0, 0) // 45, 56, 61, 67, 70, 79
        val DEFAULT_FEMALE = Appearance(DEFAULT_FEMALE_LOOKS, DEFAULT_COLORS, Gender.FEMALE)

    }
}