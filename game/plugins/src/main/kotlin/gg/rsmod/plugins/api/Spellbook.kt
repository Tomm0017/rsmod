package gg.rsmod.plugins.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class Spellbook(val id: Int) {
    STANDARD(id = 0),
    ANCIENT(id = 1),
    LUNAR(id = 2),
    ARCEUUS(id = 3);

    companion object {
        val values = enumValues<Spellbook>()
    }
}