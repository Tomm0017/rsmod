package gg.rsmod.plugins.content.skills.runecrafting

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the various Runecrafting tiaras
 *
 * @param id    The tiara id
 * @param altar The altar this tiara can be crafted at
 * @param xp    The experience granted for crafting a tiara
 */
enum class Tiara(val id: Int, val altar: Altar, val xp: Double) {

    AIR(Items.AIR_TIARA, Altar.AIR, 25.0),
    MIND(Items.MIND_TIARA, Altar.MIND, 27.5),
    WATER(Items.WATER_TIARA, Altar.WATER, 30.0),
    EARTH(Items.EARTH_TIARA, Altar.EARTH, 32.5),
    FIRE(Items.FIRE_TIARA, Altar.FIRE, 35.0),
    BODY(Items.BODY_TIARA, Altar.BODY, 37.5),
    COSMIC(Items.COSMIC_TIARA, Altar.COSMIC, 40.0),
    CHAOS(Items.CHAOS_TIARA, Altar.CHAOS, 42.5),
    NATURE(Items.NATURE_TIARA, Altar.NATURE, 45.0),
    LAW(Items.LAW_TIARA, Altar.LAW, 47.5),
    DEATH(Items.DEATH_TIARA, Altar.DEATH, 50.0),
    WRATH(Items.WRATH_TIARA, Altar.WRATH, 52.5);

    companion object {
        val values = enumValues<Tiara>()
    }
}