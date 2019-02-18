package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class WeaponType(val id: Int) {
    NONE(id = 0),
    AXE(id = 1),
    HAMMER(id = 2),
    BOW(id = 3),
    CLAWS(id = 4),
    CROSSBOW(id = 5),
    SALAMANDER(id = 6),
    CHINCHOMPA(id = 7),
    LONG_SWORD(id = 9),
    TWO_HANDED(id = 10),
    PICKAXE(id = 11),
    HALBERD(id = 12),
    STAFF(id = 13), // No autocast
    SCYTHE(id = 14),
    SPEAR(id = 15),
    MACE(id = 16),
    DAGGER(id = 17),
    MAGIC_STAFF(id = 18),
    THROWN(id = 19),
    WHIP(id = 20),
    STAFF_HALBERD(id = 21),
    TRIDENT(id = 23),
    BLUDGEON(id = 26),
    BULWARK(id = 27),
}