package gg.rsmod.plugins.content.inter.skillguides

enum class SkillGuide(val child: Int, val bit: Int) {
    ATTACK(child = 1, bit = 1),
    STRENGTH(child = 2, bit = 2),
    DEFENCE(child = 3, bit = 5),
    RANGED(child = 4, bit = 3),
    PRAYER(child = 5, bit = 7),
    MAGIC(child = 6, bit = 4),
    RUNECRAFTING(child = 7, bit = 12),
    CONSTRUCTION(child = 8, bit = 22),
    HITPOINTS(child = 9, bit = 6),
    AGILITY(child = 10, bit = 8),
    HERBLORE(child = 11, bit = 9),
    THIEVING(child = 12, bit = 10),
    CRAFTING(child = 13, bit = 11),
    FLETCHING(child = 14, bit = 19),
    SLAYER(child = 15, bit = 20),
    HUNTER(child = 16, bit = 23),
    MINING(child = 17, bit = 13),
    SMITHING(child = 18, bit = 14),
    FISHING(child = 19, bit = 15),
    COOKING(child = 20, bit = 16),
    FIREMAKING(child = 21, bit = 17),
    WOODCUTTING(child = 22, bit = 18),
    FARMING(child = 23, bit = 21);

    companion object {
        val values = enumValues<SkillGuide>()
    }
}