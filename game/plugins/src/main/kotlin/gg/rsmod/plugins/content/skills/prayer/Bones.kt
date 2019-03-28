package gg.rsmod.plugins.content.skills.prayer

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Misterbaho <MisterBaho#6447>
 */

enum class Bones(val item: Int, val xp: Double, val crushedBone: Int = 0) {
    BONES(item = Items.BONES, xp = 4.5, crushedBone = Items.BONEMEAL),
    WOLF_BONES(item = Items.WOLF_BONES, xp = 4.5, crushedBone = Items.BONEMEAL_4256),
    BURNT_BONES(item = Items.BURNT_BONES, xp = 4.5, crushedBone = Items.BONEMEAL_4257),
    MONKEY_BONES(item = Items.MONKEY_BONES, xp = 5.0, crushedBone = Items.BONEMEAL_4258),
    BAT_BONES(item = Items.BAT_BONES, xp = 5.3, crushedBone = Items.BONEMEAL_4259),
    BIG_BONES(item = Items.BIG_BONES, xp = 15.0, crushedBone = Items.BONEMEAL_4260),
    JOGRE_BONES(item = Items.JOGRE_BONES, xp = 15.0, crushedBone = Items.BONEMEAL_4261),
    BURNT_JOGRE_BONES(item = Items.BURNT_JOGRE_BONES, xp = 16.0, crushedBone = Items.BONEMEAL_4262),
    ZOGRE_BONES(item = Items.ZOGRE_BONES, xp = 22.5, crushedBone = Items.BONEMEAL_4263),
    SHAIKAHAN_BONES(item = Items.SHAIKAHAN_BONES, xp = 25.0, crushedBone = Items.BONEMEAL_4264),
    BABYDRAGON_BONES(item = Items.BABYDRAGON_BONES, xp = 30.0, crushedBone = Items.BONEMEAL_4265),
    WYRM_BONES(item = Items.WYRM_BONES, xp = 50.0, crushedBone = Items.BONEMEAL_4267),
    DRAKE_BONES(item = Items.DRAKE_BONES, xp = 80.0, crushedBone = Items.BONEMEAL_4268),
    WYVERN_BONES(item = Items.WYVERN_BONES, xp = 72.0, crushedBone = Items.BONEMEAL_4269),
    DRAGON_BONES(item = Items.DRAGON_BONES, xp = 72.0, crushedBone = Items.BONEMEAL_4270),
    FAYRG_BONES(item = Items.FAYRG_BONES, xp = 84.0, crushedBone = Items.BONEMEAL_4271),
    LAVA_DRAGON_BONES(item = Items.LAVA_DRAGON_BONES, xp = 85.0, crushedBone = Items.BONEMEAL_4852),
    HYDRA_BONES(item = Items.HYDRA_BONES, xp = 110.0, crushedBone = Items.BONEMEAL_4853),
    RAURG_BONES(item = Items.RAURG_BONES, xp = 96.0, crushedBone = Items.BONEMEAL_4854),
    DAGANNOTH_BONES(item = Items.DAGANNOTH_BONES, xp = 125.0, crushedBone = Items.BONEMEAL_4855),
    OURG_BONES(item = Items.OURG_BONES, xp = 140.0, crushedBone = Items.BONEMEAL_5615),
    SUPERIOR_DRAGON_BONES(item = Items.SUPERIOR_DRAGON_BONES, xp = 150.0, crushedBone = Items.BONEMEAL_22116);

    companion object {
        val values = enumValues<Bones>()
    }
}