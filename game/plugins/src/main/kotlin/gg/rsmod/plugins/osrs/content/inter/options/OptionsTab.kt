package gg.rsmod.plugins.osrs.content.inter.options

import gg.rsmod.game.model.BitStorage

/**
 * @author Tom <rspsmods@gmail.com>
 */
object OptionsTab {

    //6516= friends list to ignore list

    val GAME_NOTIFICATIONS = BitStorage(persistenceKey = "game_notifications")

    const val INTERFACE_ID = 261

    const val ACCEPT_AID_VARP = 427
    const val SCREEN_BRIGHTNESS_VARP = 166
    const val DISABLE_MOUSEWHEEL_ZOOM_VARBIT = 6357

    const val MUSIC_VOLUME_VARP = 168
    const val SFX_VOLUME_VARP = 169
    const val ASX_VOLUME_VARP = 872

    const val CHAT_EFFECTS_VARP = 171
    const val SPLIT_PRIVATE_VARP = 287
    const val HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT = 4089
    const val PROFANITY_VARP = 1074
    const val IDLE_NOTIFICATION_VARBIT = 1627

    const val MOUSE_BUTTONS_VARP = 170
    const val MOUSE_CAMERA_VARBIT = 4134
    const val PET_OPTIONS_VARBIT = 5599
    const val SHIFT_CLICK_DROP_VARBIT = 5542

    const val PLAYER_ATTACK_PRIORITY_VARP = 1107
    const val NPC_ATTACK_PRIORITY_VARP = 1306
}