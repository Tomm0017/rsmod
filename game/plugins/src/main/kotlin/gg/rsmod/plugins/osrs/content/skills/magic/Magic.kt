package gg.rsmod.plugins.osrs.content.skills.magic

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.helper.getVarp
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Magic {

    const val STANDARD_SPELL_DATA_ENUM = 1982
    const val INF_RUNES_VARP = 375

    const val SPELL_RUNE1_ID_KEY = 365
    const val SPELL_RUNE1_AMT_KEY = 366
    const val SPELL_RUNE2_ID_KEY = 367
    const val SPELL_RUNE2_AMT_KEY = 368
    const val SPELL_RUNE3_ID_KEY = 369
    const val SPELL_RUNE3_AMT_KEY = 370
    const val SPELL_INTERFACE_HASH_KEY = 596
    const val SPELL_ID_KEY = 599
    const val SPELL_NAME_KEY = 601
    const val SPELL_DESC_KEY = 602
    const val SPELL_LVL_REQ_KEY = 604
    const val SPELL_TYPE_KEY = 605

    val combatStructs = hashMapOf<CombatSpell, MagicSpellStruct>()

    // TODO: check if on same spellbook
    fun canCast(p: Player, lvl: Int, items: List<Item>): Boolean {
        if (p.getSkills().getMaxLevel(Skills.MAGIC) < lvl) {
            p.message("Your Magic level is not high enough for this spell.")
            return false
        }
        if ((p.getVarp(INF_RUNES_VARP) shr 1) != 1) {
            for (item in items) {
                if (p.inventory.getItemCount(item.id) < item.amount && p.equipment.getItemCount(item.id) < item.amount) {
                    p.message("You do not have enough ${item.getDef(p.world.definitions).name}s to cast this spell.")
                    return false
                }
            }
        }
        return true
    }

    fun removeRunes(p: Player, items: List<Item>) {
        if ((p.getVarp(INF_RUNES_VARP) shr 1) != 1) {
            // TODO: don't remove non-rune items
            for (item in items) {
                p.inventory.remove(item)
            }
        }
    }
}