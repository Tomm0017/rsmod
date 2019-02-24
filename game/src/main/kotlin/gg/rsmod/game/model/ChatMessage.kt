package gg.rsmod.game.model

/**
 * Represents a public chat message for players.
 *
 * @param text
 * The text said in the chat message.
 *
 * @param icon
 * The rank icon for the message.
 *
 * @param type
 * The [ChatType] being used.
 *
 * @param effect
 * The [ChatEffect] being used.
 *
 * @param color
 * The [ChatColor] being used.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class ChatMessage(val text: String, val icon: Int, val type: ChatType, val effect: ChatEffect, val color: ChatColor) {

    enum class ChatType(val id: Int) {
        NONE(0),
        AUTOCHAT(1),
        CLANCHAT(2);

        companion object {
            val values = enumValues<ChatType>()
        }
    }

    enum class ChatEffect(val id: Int) {
        NONE(0),
        WAVE(1),
        WAVE2(2),
        SHAKE(3),
        SCROLL(4),
        SLIDE(5);

        companion object {
            val values = enumValues<ChatEffect>()
        }
    }

    enum class ChatColor(val id: Int) {
        NONE(0),
        RED(1),
        GREEN(2),
        CYAN(3),
        PURPLE(4),
        WHITE(5),
        FLASH1(6),
        FLASH2(7),
        FLASH3(8),
        GLOW1(9),
        GLOW2(10),
        GLOW3(11);

        companion object {
            val values = enumValues<ChatColor>()
        }
    }
}