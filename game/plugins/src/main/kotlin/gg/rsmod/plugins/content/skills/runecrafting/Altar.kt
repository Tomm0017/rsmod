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
 */
enum class Altar(val ruins: IntArray? = null, val altar: Int, val exitPortal: Int? = null, val talisman: Int? = null, val tiara: Int? = null, val varbit: Int = 0, val rune: Rune, val entrance: Tile? = null, val exit: Tile? = null, val option: String = "craft-rune") {

    AIR(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS, Objs.MYSTERIOUS_RUINS_29090),             altar = Objs.ALTAR_34760, exitPortal = Objs.PORTAL_34748, talisman = Items.AIR_TALISMAN,    tiara = Items.AIR_TIARA,    varbit = 607, rune = Rune.AIR,      entrance = Tile(2841, 4830), exit = Tile(2983, 3288)),
    MIND(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_29094, Objs.MYSTERIOUS_RUINS_29095),      altar = Objs.ALTAR_34761, exitPortal = Objs.PORTAL_34749, talisman = Items.MIND_TALISMAN,   tiara = Items.MIND_TIARA,   varbit = 608, rune = Rune.MIND,     entrance = Tile(2793, 4829), exit = Tile(2980, 3511)),
    WATER(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_29096, Objs.MYSTERIOUS_RUINS_29097),     altar = Objs.ALTAR_34762, exitPortal = Objs.PORTAL_34750, talisman = Items.WATER_TALISMAN,  tiara = Items.WATER_TIARA,  varbit = 609, rune = Rune.WATER,    entrance = Tile(2725, 4832), exit = Tile(3182, 3162)),
    EARTH(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_29098, Objs.MYSTERIOUS_RUINS_29099),     altar = Objs.ALTAR_34763, exitPortal = Objs.PORTAL_34751, talisman = Items.EARTH_TALISMAN,  tiara = Items.EARTH_TIARA,  varbit = 610, rune = Rune.EARTH,    entrance = Tile(2657, 4830), exit = Tile(3302, 3477)),
    FIRE(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_30371, Objs.MYSTERIOUS_RUINS_30372),      altar = Objs.ALTAR_34764, exitPortal = Objs.PORTAL_34752, talisman = Items.FIRE_TALISMAN,   tiara = Items.FIRE_TIARA,   varbit = 611, rune = Rune.FIRE,     entrance = Tile(2576, 4848), exit = Tile(3310, 3252)),
    BODY(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_30373, Objs.MYSTERIOUS_RUINS_31584),      altar = Objs.ALTAR_34765, exitPortal = Objs.PORTAL_34753, talisman = Items.BODY_TALISMAN,   tiara = Items.BODY_TIARA,   varbit = 612, rune = Rune.BODY,     entrance = Tile(2519, 4847), exit = Tile(3050, 3442)),
    COSMIC(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_31607, Objs.MYSTERIOUS_RUINS_31725),    altar = Objs.ALTAR_34766, exitPortal = Objs.PORTAL_34754, talisman = Items.COSMIC_TALISMAN, tiara = Items.COSMIC_TIARA, varbit = 613, rune = Rune.COSMIC,   entrance = Tile(2142, 4813), exit = Tile(2405, 4381)),
    CHAOS(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_34742, Objs.MYSTERIOUS_RUINS_34743),     altar = Objs.ALTAR_34769, exitPortal = Objs.PORTAL_34757, talisman = Items.CHAOS_TALISMAN,  tiara = Items.CHAOS_TIARA,  varbit = 616, rune = Rune.CHAOS,    entrance = Tile(2280, 4837), exit = Tile(3060, 3585)),
    ASTRAL(                                                                                 altar = Objs.ALTAR_34771,                                                                                                             rune = Rune.ASTRAL),
    NATURE(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_32491, Objs.MYSTERIOUS_RUINS_32492),    altar = Objs.ALTAR_34768, exitPortal = Objs.PORTAL_34756, talisman = Items.NATURE_TALISMAN, tiara = Items.NATURE_TIARA, varbit = 615, rune = Rune.NATURE,   entrance = Tile(2400, 4835), exit = Tile(2865, 3022)),
    LAW(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_32489, Objs.MYSTERIOUS_RUINS_32490),       altar = Objs.ALTAR_34767, exitPortal = Objs.PORTAL_34755, talisman = Items.LAW_TALISMAN,    tiara = Items.LAW_TIARA,    varbit = 614, rune = Rune.LAW,      entrance = Tile(2464, 4819), exit = Tile(2858, 3378)),
    DEATH(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_34744, Objs.MYSTERIOUS_RUINS_34745),     altar = Objs.ALTAR_34770, exitPortal = Objs.PORTAL_34758, talisman = Items.DEATH_TALISMAN,  tiara = Items.DEATH_TIARA,  varbit = 617, rune = Rune.DEATH,    entrance = Tile(2208, 4830), exit = Tile(1863, 4639)),
    BLOOD(                                                                                  altar = Objs.BLOOD_ALTAR,                                                                                                             rune = Rune.BLOOD,                                                                        option = "bind"),
    SOUL(                                                                                   altar = Objs.SOUL_ALTAR,                                                                                                              rune = Rune.SOUL,                                                                         option = "bind"),
    WRATH(ruins = intArrayOf(Objs.MYSTERIOUS_RUINS_34746, Objs.MYSTERIOUS_RUINS_34747),     altar = Objs.ALTAR_34772, exitPortal = Objs.PORTAL_34759, talisman = Items.WRATH_TALISMAN,  tiara = Items.WRATH_TIARA,  varbit = 6220,rune = Rune.WRATH,    entrance = Tile(2335, 4826), exit = Tile(2447, 2822));

    companion object {
        val values = enumValues<Altar>()
    }
}