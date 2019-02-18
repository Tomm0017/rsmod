package gg.rsmod.plugins.content.inter.keybind

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class Hotkey(val id: Int, val child: Int, val varbit: Int, val defaultValue: Int) {
    COMBAT(id = 0, child = 9, varbit = 4675, defaultValue = 5),
    SKILLS(id = 1, child = 16, varbit = 4676, defaultValue = 0),
    QUESTS(id = 2, child = 23, varbit = 4677, defaultValue = 0),
    INVENTORY(id = 3, child = 30, varbit = 4678, defaultValue = 1),
    EQUIPMENT(id = 4, child = 37, varbit = 4679, defaultValue = 2),
    PRAYERS(id = 5, child = 44, varbit = 4680, defaultValue = 3),
    MAGIC(id = 6, child = 51, varbit = 4682, defaultValue = 4),
    SOCIAL(id = 7, child = 58, varbit = 4684, defaultValue = 8),
    ACCOUNT_MANAGEMENT(id = 8, child = 65, varbit = 6517, defaultValue = 9),
    LOG_OUT(id = 9, child = 72, varbit = 4689, defaultValue = 0),
    SETTINGS(id = 10, child = 79, varbit = 4686, defaultValue = 10),
    EMOTES(id = 11, child = 86, varbit = 4687, defaultValue = 11),
    CLAN_CHAT(id = 12, child = 93, varbit = 4683, defaultValue = 7),
    MUSIC(id = 13, child = 100, varbit = 4688, defaultValue = 12);

    companion object {
        val values = enumValues<Hotkey>()
    }
}