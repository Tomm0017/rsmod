package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class EquipmentType(val id: Int) {
    HEAD(id = 0),
    CAPE(id = 1),
    AMULET(id = 2),
    WEAPON(id = 3),
    CHEST(id = 4),
    SHIELD(id = 5),
    //ARMS(id = 6),
    LEGS(id = 7),
    //HAIR(id = 8),
    GLOVES(id = 9),
    BOOTS(id = 10),
    //BEARD(id = 11),
    RING(id = 12),
    AMMO(id = 13);

    companion object {
        val values = enumValues<EquipmentType>()
    }
}