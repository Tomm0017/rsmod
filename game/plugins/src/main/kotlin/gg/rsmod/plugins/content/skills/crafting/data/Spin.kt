package gg.rsmod.plugins.content.skills.crafting.data

import gg.rsmod.plugins.api.cfg.Items

/**
 * @author Triston Plummer ("Dread')
 * @editor Pitch Blac23
 */
enum class Spin(val id: Int,val prefix: String, val unSpun: Int, val level: Int, val craftXp: Double = 0.0, val animation: Int) {
    BALL_OF_WOOL(id = Items.BALL_OF_WOOL, prefix = "ball of wool", unSpun = Items.WOOL, level = 1, craftXp = 2.5, animation = 894),
    GOLDEN_WOOL(id = Items.GOLDEN_FLEECE, prefix = "golden wool", unSpun = Items.GOLDEN_WOOL, level = 40, craftXp = 2.5, animation = 894),
    FLAX(id = Items.BOW_STRING, prefix = "flax", unSpun = Items.FLAX, level = 10, craftXp = 15.0, animation = 894),
    SINEW(id = Items.CROSSBOW_STRING, prefix = "sinew", unSpun = Items.SINEW, level = 10, craftXp = 15.0, animation = 894),
    MAGIC_ROOTS(id = Items.MAGIC_STRING, prefix = "magic roots", unSpun = Items.MAGIC_ROOTS, level = 19, craftXp = 30.0, animation = 894),
    YAK_HAIR(id = Items.ROPE, prefix = "yak hair", unSpun = Items.HAIR, level = 30, craftXp = 25.0, animation = 894);

    companion object {
        /**
         * The cached array of enum definitions
         */
        val values = enumValues<Spin>()
    }
}