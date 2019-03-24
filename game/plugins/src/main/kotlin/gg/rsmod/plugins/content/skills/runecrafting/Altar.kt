package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.game.model.Tile
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the various Runecrafting altars
 *
 * @param ruins         An array of object ids that should include the Mysterious Ruins and it's transformed counterpart (when wearing the tiara)
 * @param altar         The object of the Runecrafting altar
 * @param exitPortal    The object id of the altar exit portal
 * @param talisman      The talisman item id
 * @param tiara         The tiara item id
 * @param varbit        The varbit used to transform the ruins object when wearing a tiara
 * @param entrance      The tile the player is teleported to upon entering the ruins
 * @param exit          The tile the player is teleported to upon exiting the altar
 *
 * TODO: Gather data for the Wrath altar
 */
enum class Altar(val ruins: IntArray? = null, val altar: Int, val exitPortal: Int? = null, val talisman: Int? = null, val tiara: Int? = null, val varbit: Int = 0, val rune: Rune, val entrance: Tile? = null, val exit: Tile? = null, val option: String = "craft-rune") {

    AIR(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS, Objs.MYSTERIOUS_RUINS_14399),             altar = Objs.ALTAR_14897, exitPortal = Objs.PORTAL_14841, talisman = Items.AIR_TALISMAN,    tiara = Items.AIR_TIARA,    varbit = 607, rune = Rune.AIR,      entrance = Tile(2841, 4830), exit = Tile(2983, 3288)),
    MIND(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14400, Objs.MYSTERIOUS_RUINS_14401),      altar = Objs.ALTAR_14898, exitPortal = Objs.PORTAL_14842, talisman = Items.MIND_TALISMAN,   tiara = Items.MIND_TIARA,   varbit = 608, rune = Rune.MIND,     entrance = Tile(2793, 4829), exit = Tile(2980, 3511)),
    WATER(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14402, Objs.MYSTERIOUS_RUINS_14403),     altar = Objs.ALTAR_14899, exitPortal = Objs.PORTAL_14843, talisman = Items.WATER_TALISMAN,  tiara = Items.WATER_TIARA,  varbit = 609, rune = Rune.WATER,    entrance = Tile(2725, 4832), exit = Tile(3182, 3162)),
    EARTH(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14404, Objs.MYSTERIOUS_RUINS_14405),     altar = Objs.ALTAR_14900, exitPortal = Objs.PORTAL_14844, talisman = Items.EARTH_TALISMAN,  tiara = Items.EARTH_TIARA,  varbit = 610, rune = Rune.EARTH,    entrance = Tile(2657, 4830), exit = Tile(3302, 3477)),
    FIRE(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14406, Objs.MYSTERIOUS_RUINS_14407),      altar = Objs.ALTAR_14901, exitPortal = Objs.PORTAL_14845, talisman = Items.FIRE_TALISMAN,   tiara = Items.FIRE_TIARA,   varbit = 611, rune = Rune.FIRE,     entrance = Tile(2576, 4848), exit = Tile(3310, 3252)),
    BODY(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14408, Objs.MYSTERIOUS_RUINS_14409),      altar = Objs.ALTAR_14902, exitPortal = Objs.PORTAL_14846, talisman = Items.BODY_TALISMAN,   tiara = Items.BODY_TIARA,   varbit = 612, rune = Rune.BODY,     entrance = Tile(2519, 4847), exit = Tile(3050, 3442)),
    COSMIC(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14410, Objs.MYSTERIOUS_RUINS_14411),    altar = Objs.ALTAR_14903, exitPortal = Objs.PORTAL_14847, talisman = Items.COSMIC_TALISMAN, tiara = Items.COSMIC_TIARA, varbit = 613, rune = Rune.COSMIC,   entrance = Tile(2142, 4813), exit = Tile(2405, 4381)),
    CHAOS(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14833, Objs.MYSTERIOUS_RUINS_14834),     altar = Objs.ALTAR_14906, exitPortal = Objs.PORTAL_14893, talisman = Items.CHAOS_TALISMAN,  tiara = Items.CHAOS_TIARA,  varbit = 616, rune = Rune.CHAOS,    entrance = Tile(2275, 4847), exit = Tile(3060, 3585)),
    ASTRAL(                                                                                 altar = Objs.ALTAR_14911,                                                                                                             rune = Rune.ASTRAL),
    NATURE(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14414, Objs.MYSTERIOUS_RUINS_14832),    altar = Objs.ALTAR_14905, exitPortal = Objs.PORTAL_14892, talisman = Items.NATURE_TALISMAN, tiara = Items.NATURE_TIARA, varbit = 615, rune = Rune.NATURE,   entrance = Tile(2400, 4835), exit = Tile(2865, 3022)),
    LAW(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_14412, Objs.MYSTERIOUS_RUINS_14413),       altar = Objs.ALTAR_14904, exitPortal = Objs.PORTAL_14848, talisman = Items.LAW_TALISMAN,    tiara = Items.LAW_TIARA,    varbit = 614, rune = Rune.LAW,      entrance = Tile(2464, 4819), exit = Tile(2858, 3378)),
    // TODO: Death
    BLOOD(                                                                                  altar = Objs.BLOOD_ALTAR,                                                                                                             rune = Rune.BLOOD,                                                                        option = "bind"),
    SOUL(                                                                                   altar = Objs.SOUL_ALTAR,                                                                                                              rune = Rune.SOUL,                                                                         option = "bind");
    // TODO: Wrath

    companion object {
        val values = enumValues<Altar>()
    }
}