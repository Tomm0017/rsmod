package gg.rsmod.plugins.content.inter.emotes

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.messageBox
import gg.rsmod.plugins.api.ext.setVarbit

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EmotesTab {

    const val COMPONENT_ID = 216

    const val GOBLIN_EMOTES_VARBIT = 532
    const val GLASS_BOX_EMOTE_VARBIT = 1368
    const val CLIMB_ROPE_EMOTE_VARBIT = 1369
    const val LEAN_EMOTE_VARBIT = 1370
    const val GLASS_WALL_EMOTE_VARBIT = 1367
    const val IDEA_EMOTE_VARBIT = 2311
    const val STAMP_EMOTE_VARBIT = 2312
    const val FLAP_EMOTE_VARBIT = 2309
    const val SLAP_HEAD_EMOTE_VARBIT = 2310
    const val ZOMBIE_WALK_EMOTE_VARBIT = 1921
    const val ZOMBIE_DANCE_EMOTE_VARBIT = 1920
    const val SCARED_EMOTE_VARBIT = 1371
    const val RABBIT_HOP_EMOTE_VARBIT = 2055
    const val EXERCISE_EMOTES = 4732
    const val ZOMBIE_HAND_EMOTE_VARBIT = 1000
    const val HYPERMOBILE_DRINKER_EMOTE_VARBIT = 4802
    const val SKILLCAPE_EMOTE_VARBIT = 4797
    const val AIR_GUITAR_EMOTE_VARBIT = 4673
    const val URI_TRANSFORM_EMOTE_VARBIT = 5104
    const val SMOOTH_DANCE_EMOTE_VARBIT = 5597
    const val CRAZY_DANCE_EMOTE_VARBIT = 5598
    const val PREMIER_SHIELD_EMOTE_VARBIT = 6041

    fun unlockAll(p: Player) {
        p.setVarbit(GOBLIN_EMOTES_VARBIT, 8)
        p.setVarbit(GLASS_BOX_EMOTE_VARBIT, 1)
        p.setVarbit(CLIMB_ROPE_EMOTE_VARBIT, 1)
        p.setVarbit(LEAN_EMOTE_VARBIT, 1)
        p.setVarbit(GLASS_WALL_EMOTE_VARBIT, 1)
        p.setVarbit(IDEA_EMOTE_VARBIT, 1)
        p.setVarbit(STAMP_EMOTE_VARBIT, 1)
        p.setVarbit(FLAP_EMOTE_VARBIT, 1)
        p.setVarbit(ZOMBIE_WALK_EMOTE_VARBIT, 1)
        p.setVarbit(ZOMBIE_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(SCARED_EMOTE_VARBIT, 1)
        p.setVarbit(EXERCISE_EMOTES, 1)
        p.setVarbit(ZOMBIE_HAND_EMOTE_VARBIT, 1)
        p.setVarbit(HYPERMOBILE_DRINKER_EMOTE_VARBIT, 1)
        p.setVarbit(AIR_GUITAR_EMOTE_VARBIT, 1)
        p.setVarbit(SMOOTH_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(CRAZY_DANCE_EMOTE_VARBIT, 1)
        p.setVarbit(PREMIER_SHIELD_EMOTE_VARBIT, 1)

    }

    fun performEmote(p: Player, emote: Emote) {
        if (emote.varbit != -1 && p.getVarbit(emote.varbit) != emote.requiredVarbitValue) {
            val description = emote.unlockDescription ?: "You have not unlocked this emote yet."
            p.queue { messageBox(description) }
            return
        }

        if (emote.anim != -1) {
            p.animate(emote.anim)
        }

        if (emote.gfx != -1) {
            p.graphic(emote.gfx)
        }
    }
}