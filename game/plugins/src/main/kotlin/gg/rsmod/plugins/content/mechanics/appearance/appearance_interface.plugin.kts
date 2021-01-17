package gg.rsmod.plugins.content.mechanics.appearance

import gg.rsmod.game.model.appearance.*
import gg.rsmod.game.model.attr.APPEARANCE_SET_ATTR
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.plugins.content.mechanics.appearance.Appearance_interface_plugin.AppearanceOps.Companion.getOp
import gg.rsmod.plugins.content.mechanics.appearance.Appearance_interface_plugin.AppearanceOps.Companion.isColourOp
import gg.rsmod.plugins.content.mechanics.appearance.Appearance_interface_plugin.AppearanceOps.Companion.isLookOp
import java.lang.IllegalArgumentException

val APPEARANCE_INTERFACE_ID = 679

/**
 * Change [Gender] to [Gender.MALE]
 */
on_button(APPEARANCE_INTERFACE_ID, 65) {
    player.setVarbit(11697, 0)
    player.appearance = Appearance.DEFAULT_MALE
    player.addBlock(UpdateBlockType.APPEARANCE)
}

/**
 * Change [Gender] to [Gender.FEMALE]
 */
on_button(APPEARANCE_INTERFACE_ID, 66) {
    player.setVarbit(11697, 1)
    player.appearance = Appearance.DEFAULT_FEMALE
    player.addBlock(UpdateBlockType.APPEARANCE)
}

/**
 * Confirms [Player] [Appearance] selection and closes interface
 */
on_button(APPEARANCE_INTERFACE_ID, 68) {
    player.attr[APPEARANCE_SET_ATTR] = true
    player.unlock()
    player.closeInterface(APPEARANCE_INTERFACE_ID)
}

enum class AppearanceOps(val component: Int) {
    HEAD(10),
    JAW(14),
    TORSO(18),
    ARMS(22),
    HANDS(26),
    LEGS(30),
    FEET(34),
    HAIR_COLOUR(41),
    TORSO_COLOUR(45),
    LEGS_COLOUR(49),
    FEET_COLOUR(53),
    SKIN_COLOUR(57),
    NONE(0);

    companion object {
        fun isValidOp(option: Int): Boolean = (option >= 10 || option <= 60)

        fun isLookOp(option: Int): Boolean = (option in 10..37)

        fun isColourOp(option: Int): Boolean = (option in 41..60)

        fun getOp(option: Int): AppearanceOps {
            return when {
                !isValidOp(option) -> NONE
                isLookOp(option) -> getLookOp(option)
                isColourOp(option) -> getColourOp(option)
                else -> NONE
            }
        }

        private fun getLookOp(option: Int): AppearanceOps {
            return when((option-10)/4) {
                0 -> HEAD
                1 -> JAW
                2 -> TORSO
                3 -> ARMS
                4 -> HANDS
                5 -> LEGS
                6 -> FEET
                else -> NONE
            }
        }

        private fun getColourOp(option: Int): AppearanceOps {
            return when((option-41)/4) {
                0 -> HAIR_COLOUR
                1 -> TORSO_COLOUR
                2 -> LEGS_COLOUR
                3 -> FEET_COLOUR
                4 -> SKIN_COLOUR
                else -> return NONE
            }
        }
    }
}

AppearanceOps.values().filterNot { it == AppearanceOps.NONE }.forEach { op ->

    // decrement option
    on_button(APPEARANCE_INTERFACE_ID, op.component+2) {
        val opt = op.component
        when {
            isLookOp(opt) -> {
                val pos = (opt-10)/4
                val current = when(player.appearance.gender){
                    Gender.MALE -> player.appearance.looks[pos]
                    Gender.FEMALE -> {
                        if (pos == 0) player.appearance.looks[pos]
                        else if (pos == 1) throw IllegalArgumentException("female jaw option illegally attempted change")
                        else player.appearance.looks[pos-1]
                    }
                }

                val looks = getLooks(opt, player.appearance.gender)
                val previous = if(current-1 < 0) looks.size-1 else current-1
                when(player.appearance.gender){
                    Gender.MALE -> player.appearance.looks[pos] = previous
                    Gender.FEMALE -> {
                        if(pos == 0) player.appearance.looks[pos] = previous
                        else if (pos == 1) throw IllegalArgumentException("female jaw option illegally attempted change")
                        else player.appearance.looks[pos-1] = previous
                    }
                }
                player.addBlock(UpdateBlockType.APPEARANCE)
            }
            isColourOp(opt) -> {
                val pos = (opt-41)/4
                val current = player.appearance.colors[pos]
                val colors = getColours(opt)
                val previous = if(current-1 < 0) colors.size-1 else current-1
                player.appearance.colors[pos] = previous
                player.addBlock(UpdateBlockType.APPEARANCE)
            }
        }
    }

    // increment option
    on_button(APPEARANCE_INTERFACE_ID, op.component+3) {
        val opt = op.component
        when {
            isLookOp(opt) -> {
                val pos = (opt-10)/4
                val current = when(player.appearance.gender){
                    Gender.MALE -> player.appearance.looks[pos]
                    Gender.FEMALE -> {
                        if (pos == 0) player.appearance.looks[pos]
                        else if (pos == 1) throw IllegalArgumentException("female jaw option illegally attempted change")
                        else player.appearance.looks[pos-1]
                    }
                }

                val looks = getLooks(opt, player.appearance.gender)
                val next = if(current+1 > looks.size) 0 else current+1
                when(player.appearance.gender){
                    Gender.MALE -> player.appearance.looks[pos] = next
                    Gender.FEMALE -> {
                        if (pos == 0) player.appearance.looks[pos] = next
                        else if (pos == 1) throw IllegalArgumentException("female jaw option illegally attempted change")
                        else player.appearance.looks[pos-1] = next
                    }
                }
                player.addBlock(UpdateBlockType.APPEARANCE)
            }
            isColourOp(opt) -> {
                val pos = (opt-41)/4
                val current = player.appearance.colors[pos]
                val colors = getColours(opt)
                val next = if(current+1 > colors.size) 0 else current+1
                player.appearance.colors[pos] = next
                player.addBlock(UpdateBlockType.APPEARANCE)
            }
        }
    }
}

fun getLooks(option: Int, gender: Gender): Array<Int> {
    when(gender){
        Gender.MALE ->  {
            return when((option-10)/4) {
                0 -> Looks.getHeads(gender)
                1 -> Looks.getJaws(gender)
                2 -> Looks.getTorsos(gender)
                3 -> Looks.getArms(gender)
                4 -> Looks.getHands(gender)
                5 -> Looks.getLegs(gender)
                6 -> Looks.getFeets(gender)
                else -> return arrayOf(-1)
            }
        }
        Gender.FEMALE -> {
            return when((option-10)/4) {
                0 -> Looks.getHeads(gender)
                2 -> Looks.getTorsos(gender)
                3 -> Looks.getArms(gender)
                4 -> Looks.getHands(gender)
                5 -> Looks.getLegs(gender)
                6 -> Looks.getFeets(gender)
                else -> return arrayOf(-1)
            }
        }
    }
}

fun getColours(option: Int): Array<Int> {
    return when((option-41)/4) {
        0 -> Colours.HAIR_COLOURS
        1 -> Colours.TORSO_COLOURS
        2 -> Colours.LEG_COLOURS
        3 -> Colours.FEET_COLOURS
        4 -> Colours.SKIN_COLOURS
        else -> return arrayOf(-1)
    }
}