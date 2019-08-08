package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.plugins.api.cfg.Items

enum class Bone(val xp: Double, val item: Int) {
    REGULAR_BONES( 4.5, Items.BONES),
    BURNT_BONES( 4.5, Items.BURNT_BONES),
    BAT_BONES( 4.5, Items.BAT_BONES),
    WOLF_BONES( 4.5, Items.WOLF_BONES),
    BIG_BONES( 15.0, Items.BIG_BONES),
    LONG_BONE( 15.0, Items.LONG_BONE),
    CURVED_BONE( 15.0, Items.CURVED_BONE),
    JOGRE_BONES( 15.0, Items.JOGRE_BONES),
    BABYDRAGON_BONES( 30.0, Items.BABYDRAGON_BONES),
    DRAGON_BONES( 72.0, Items.DRAGON_BONES),
    ZOGRE_BONES( 22.5, Items.ZOGRE_BONES),
    OURG_BONES( 140.0, Items.OURG_BONES),
    WYVERN_BONES( 72.0, Items.WYVERN_BONES),
    DAGANNOTH_BONES( 125.0, Items.DAGANNOTH_BONES),
    LAVA_DRAGON_BONES( 85.0, Items.LAVA_DRAGON_BONES);

    companion object {
        val values = enumValues<Bone>()
    }
}