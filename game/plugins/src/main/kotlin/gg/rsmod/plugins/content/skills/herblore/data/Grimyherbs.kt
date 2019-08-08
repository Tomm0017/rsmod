package gg.rsmod.plugins.content.skills.herblore.data

import gg.rsmod.plugins.api.cfg.Items

enum class Grimy(val herb: Int, val grimyherb: Int, val prefix: String, val level: Int, val herbloreXp: Double) {

    GUAM_LEAF(herb = Items.GUAM_LEAF, grimyherb = Items.GRIMY_GUAM_LEAF, prefix = "guam leaf", level = 3, herbloreXp = 2.5),
    MARRENTILL(herb = Items.MARRENTILL, grimyherb = Items.GRIMY_MARRENTILL, prefix = "marrentill", level = 5, herbloreXp = 3.8),
    TARROMIN(herb = Items.TARROMIN, grimyherb = Items.GRIMY_TARROMIN, prefix = "tarromin", level = 11, herbloreXp = 5.0),
    HARRLANDER(herb = Items.HARRALANDER, grimyherb = Items.GRIMY_HARRALANDER, prefix = "harralander", level = 20, herbloreXp = 6.3),
    RANARR_WEED(herb = Items.RANARR_WEED, grimyherb = Items.GRIMY_RANARR_WEED, prefix = "ranarr weed", level = 25, herbloreXp = 7.5),
    TOADFLAX(herb = Items.TOADFLAX, grimyherb = Items.GRIMY_TOADFLAX, prefix = "toadflax", level = 30, herbloreXp = 8.0),
    IRIT_LEAF(herb = Items.IRIT_LEAF, grimyherb = Items.GRIMY_IRIT_LEAF, prefix = "irit leaf", level = 40, herbloreXp = 8.8),
    AVANTOE(herb = Items.AVANTOE, grimyherb = Items.GRIMY_AVANTOE, prefix = "avantoe", level = 48, herbloreXp = 10.0),
    KWUARM(herb = Items.KWUARM, grimyherb = Items.GRIMY_KWUARM, prefix = "kwuarm", level = 54, herbloreXp = 11.3),
    SNAPDRAGON(herb = Items.SNAPDRAGON, grimyherb = Items.GRIMY_SNAPDRAGON, prefix = "snapdragon", level = 59, herbloreXp = 11.8),
    CADANTINE(herb = Items.CADANTINE, grimyherb = Items.GRIMY_CADANTINE, prefix = "cadantine", level = 65, herbloreXp = 12.5),
    LANTADYME(herb = Items.LANTADYME, grimyherb = Items.GRIMY_LANTADYME, prefix = "lantadyme", level = 67, herbloreXp = 13.1),
    DWARF_WEED(herb = Items.DWARF_WEED, grimyherb = Items.GRIMY_DWARF_WEED, prefix = "dwarf weed", level = 70, herbloreXp = 13.8),
    TORSTOL(herb = Items.TORSTOL, grimyherb = Items.GRIMY_TORSTOL, prefix = "torstol", level = 75, herbloreXp = 15.0);

    companion object {
        /**
         * The cached array of enum definitions
         */
        val values = enumValues<Grimy>()
    }
}