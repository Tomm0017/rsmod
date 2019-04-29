package gg.rsmod.plugins.content.inter.options

on_button(interfaceId = OptionsTab.INTERFACE_ID, component = 71) {
    player.queue {
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
            // we can just slice up notifications and handle it from there once we
            // need to.
            val page1 = notifications.map { n ->
                var text = n.second
                if (n.first != null) {
                    if (n.third) {
                        text += ": ${if (player.hasStorageBit(OptionsTab.GAME_NOTIFICATIONS, n.first!!)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}"
                    } else {
                        text += ": ${if (player.hasStorageBit(OptionsTab.GAME_NOTIFICATIONS, n.first!!)) "<col=178000>enabled</col>" else "<col=801700>disabled</col>"}"
                    }
                }
                text
            }

            val option = options(options = *page1.toTypedArray(), title = "Select an option to toggle")
            if (option >= 1 && option <= notifications.size) {
                val notification = notifications[option - 1]
                if (notification.first != null) {
                    player.toggleStorageBit(OptionsTab.GAME_NOTIFICATIONS, notification.first!!)
                } else {
                    break
                }
            }
        }
    }
}