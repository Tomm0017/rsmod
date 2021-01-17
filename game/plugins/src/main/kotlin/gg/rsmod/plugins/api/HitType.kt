package gg.rsmod.plugins.api

import gg.rsmod.plugins.api.HitType.Companion.SplatColors.*

/**
 * @author bmyte
 * added support for tinted hitmarks
 *
 * ordinals suffixed with `_W` are reserved for player distinct operations in world not involving
 * the player themselves (i.e. hits generated/taken by entities other than the player) and corresponds
 * to the non suffixed ordinal; which is reserved for the player distinct operations in world by the player.
 *   Note| while the non-player relevant operations are observed to be a single increment offset from
 *   the base [HitType], this might not always be and as such a separate [HitType] ordinal is reserved
 *
 * implementation sees that the base [HitType] is used for registering hits
 * but that the appropriate suffixed ordinal [id] is sent during updates
 * depending on player relevance -> the client handles tinted hitmark displaying
 * based on [TINTED_HITMARK_VARBIT] current state
 */
enum class HitType(val id: Int) {
    POISON(id = GREEN.color), // NA to others, ONLY player themselves
    DISEASE(id = DEFORM_YELLOW.color), // NA to others, ONLY player themselves
    VENOM(id = TEAL.color), // NA to others, ONLY player themselves
    NPC_HEAL(id = MAGENTA.color), // generally used for NPCs -> NO player distinction
    BLOCK(id = 12), // blue -> blue
    BLOCK_W(id = 13), // blue -> dark blue
    HIT(id = 16), // red -> red
    HIT_W(id = 17), // red -> dark red
    CYAN(id = 18), // damage dealt to Verzik Vitur's and The Nightmare's shield by player
    CYAN_W(id = 19), // damage dealt to Verzik Vitur's and The Nightmare's shield by others
    ORANGE(id = 20), // Zalcano armor damage by player
    ORANGE_W(id = 21), // Zalcano armor damage by others
    YELLOW(id = 22), // charge "damage" dealt to nightmare totems by player
    YELLOW_W(id = 23), // charge "damage" dealt to nightmare totems by others
    GREY(id = 24), // parasite "healing" nightmare totems for player
    GREY_W(id = 25); // parasite "healing" nightmare totems for others

    companion object {
        val TINTED_HITMARK_VARBIT = 10236

        /**
         * these [SplatColors] can technically be used directly
         * to display the desired colored hitmark
         */
        enum class SplatColors(val color: Int){
            GREEN(2),
            DARK_YELLOW(3),
            DEFORM_YELLOW(4),
            TEAL(5), // venom
            MAGENTA(6), // NPC healing (primarily reserved for bosses)
            BLUE(26),
            DARK_BLUE(27),
            RED(28),
            DARK_RED(29),
            YELLOW(30),
            UGLY_YELLOW(31),
            GREY(32),
            DARK_GREY(33),
            CYAN(34),
            DARK_CYAN(35),
            ORANGE(36),
            DARK_ORANGE(37);
        }

        val values = values()

        fun get(id: Int): HitType? {
            values.forEach {
                if(it.id == id) return it
            }
            return null
        }
    }
}