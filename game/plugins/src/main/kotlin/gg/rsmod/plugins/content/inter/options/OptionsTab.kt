package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.bits.BitStorage

/**
 * @author Tom <rspsmods@gmail.com>
 */
object OptionsTab {

    const val OPTIONS_INTERFACE_ID = 116
    const val OPTIONS_TAB_SELECTED_VARBIT = 9683
    const val ALL_SETTINGS_INTERFACE_ID = 134
    const val ALL_SETTINGS_BUTTON_ID = 29

    /**
     * Control Settings Tab
     */
    const val CONTROL_SETTINGS_BUTTON_ID = 65
    const val ACCEPT_AID_BUTTON_ID = 26
    const val ACCEPT_AID_VARBIT = 4180
    const val RUN_MODE_VARP = 173

    const val HOUSE_OPT_BUTTON_ID = 28
    const val HOUSE_OPT_INTERFACE_ID = 370 // close button on 21

    const val BOND_BUTTON_ID = 30
    const val BONDS_INTERFACE_ID = 65

    /**
     * Audio Settings Tab
     */
    const val AUDIO_SETTINGS_BUTTON_ID = 70
    const val MUSIC_UNLOCK_MESSAGE_VARBIT = 10078

    /**
     * Display Settings Tab
     */
    const val DISPLAY_SETTINGS_BUTTON_ID = 71
    const val DISPLAY_MODE_DROPDOWN_ID = 12

    val GAME_NOTIFICATIONS = BitStorage(persistenceKey = "game_notifications")
}