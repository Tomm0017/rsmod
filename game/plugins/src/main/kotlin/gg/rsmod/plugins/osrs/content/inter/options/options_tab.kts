
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.options.GameNotificationType
import gg.rsmod.plugins.osrs.content.inter.options.OptionsTab

fun bindSetting(childOffset: Int, plugin: Function1<Plugin, Unit>) {
    r.bindButton(parent = OptionsTab.INTERFACE_ID, child = OptionsTab.CHILD_START + childOffset) {
        plugin.invoke(it)
    }
}

/**
 * Toggle mouse scroll wheel zoom.
 */
r.bindButton(parent = OptionsTab.INTERFACE_ID, child = 5) {
    // TODO(Tom): figure out why this varbit isn't causing the cross to be drawn.
    // It technically works since it won't allow zooming with mouse wheel, but it
    // doesn't visually show on the interface.
    //
    //  NORMAL:  id=1055, state=467456
    //  CROSSED: id=1055, state=537338368
    it.player().toggleVarbit(OptionsTab.DISABLE_MOUSEWHEEL_ZOOM_VARBIT)
}

/**
 * Screen brightness.
 */
for (offset in 0..3) {
    bindSetting(childOffset = offset) {
        it.player().setVarp(OptionsTab.SCREEN_BRIGHTNESS_VARP, offset + 1)
    }
}

/**
 * Music volume.
 */
for (offset in 10..14) {
    bindSetting(childOffset = offset) {
        it.player().setVarp(OptionsTab.SCREEN_BRIGHTNESS_VARP, Math.abs(offset - 14))
    }
}

/**
 * Sound effect volume.
 */
for (offset in 16..20) {
    bindSetting(childOffset = offset) {
        it.player().setVarp(OptionsTab.SCREEN_BRIGHTNESS_VARP, Math.abs(offset - 20))
    }
}
/**
 * Area of sound effect volume.
 */
for (offset in 22..26) {
    bindSetting(childOffset = offset) {
        it.player().setVarp(OptionsTab.SCREEN_BRIGHTNESS_VARP, Math.abs(offset - 26))
    }
}

/**
 * Toggle chat effects.
 */
bindSetting(childOffset = 45) {
    it.player().toggleVarp(OptionsTab.CHAT_EFFECTS_VARP)
}

/**
 * Toggle split private chat.
 */
bindSetting(childOffset = 47) {
    val p = it.player()
    p.toggleVarp(OptionsTab.SPLIT_PRIVATE_VARP)
    p.invokeScript(83)
}

/**
 * Hide private messages when chat hidden.
 */
bindSetting(childOffset = 49) {
    val p = it.player()
    if (!p.isClientResizable() || p.getVarp(OptionsTab.SPLIT_PRIVATE_VARP) == 0) {
        p.message("That option is applicable only in resizable mode with 'Split Private Chat' enabled.")
    } else {
        p.toggleVarbit(OptionsTab.HIDE_PM_WHEN_CHAT_HIDDEN_VARBIT)
    }
}

/**
 * Toggle profanity filter.
 */
bindSetting(childOffset = 51) {
    it.player().toggleVarbit(OptionsTab.PROFANITY_VARP)
}

/**
 * Toggle game notifications.
 */
bindSetting(childOffset = 53) {
    it.suspendable {
        val p = it.player()
        while (true) {

            /**
             * Notification, option text, "flipped" (the notification type is the opposite of what text describes)
             *
             * Flipped would be enabled when you want something to "start off as enabled".
             * For example we want yells to be enabled by default, but don't want the text
             * to be "Disable Yell Broadcasts", so we just flip it instead.
             */
            val notifications = arrayOf(
                    Triple(GameNotificationType.DISABLE_YELL, "Yell broadcasts", true),
                    Triple(null, "Cancel", false)
            )

            // NOTE(Tom): this currently only supports one "page" of options (dialog),
            // we can just splice up notifications and handle it from there once we
            // need to.
            val page1 = notifications.map { n ->
                var text = n.second
                if (n.first != null) {
                    if (n.third) {
                        text += ": ${if (p.hasStorageBit(OptionsTab.GAME_NOTIFICATIONS, n.first!!)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}"
                    } else {
                        text += ": ${if (p.hasStorageBit(OptionsTab.GAME_NOTIFICATIONS, n.first!!)) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}"
                    }
                }
                text
            }

            val option = it.options(options = *page1.toTypedArray(), title = "Select an option to toggle")
            if (option >= 1 && option <= notifications.size) {
                val notification = notifications[option - 1]
                if (notification.first != null) {
                    p.toggleStorageBit(OptionsTab.GAME_NOTIFICATIONS, notification.first!!)
                } else {
                    break
                }
            }
        }
    }
}