package gg.rsmod.plugins.osrs.content.mechanics.spells

import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.helper.getSpellbook
import gg.rsmod.plugins.osrs.api.helper.getVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpellRequirements {

    const val INF_RUNES_VARP = 375

    private const val SPELL_SPELLBOOK_KEY = 336
    private const val SPELL_RUNE1_ID_KEY = 365
    private const val SPELL_RUNE1_AMT_KEY = 366
    private const val SPELL_RUNE2_ID_KEY = 367
    private const val SPELL_RUNE2_AMT_KEY = 368
    private const val SPELL_RUNE3_ID_KEY = 369
    private const val SPELL_RUNE3_AMT_KEY = 370
    private const val SPELL_COMPONENT_HASH_KEY = 596
    private const val SPELL_ID_KEY = 599
    private const val SPELL_NAME_KEY = 601
    private const val SPELL_DESC_KEY = 602
    private const val SPELL_LVL_REQ_KEY = 604
    private const val SPELL_TYPE_KEY = 605

    val requirements = hashMapOf<Int, SpellRequirement>()

    fun getRequirements(spellId: Int): SpellRequirement? = requirements[spellId]

    fun canCast(p: Player, lvl: Int, items: List<Item>, requiredBook: Int): Boolean {
        if (requiredBook != -1 && p.getSpellbook().id != requiredBook) {
            p.message("You can't cast this spell.")
            return false
        }
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

    fun loadSpellRequirements(world: World, enumId: Int) {
        val enum = world.definitions.get(EnumDef::class.java, enumId)
        val dataItems = enum.values.values.map { it as Int }

        for (dataItem in dataItems) {
            val def = world.definitions.get(ItemDef::class.java, dataItem)
            val data = def.data

            val spellbook = data[SPELL_SPELLBOOK_KEY] as Int
            val name = data[SPELL_NAME_KEY] as String
            val lvl = data[SPELL_LVL_REQ_KEY] as Int
            val componentHash = data[SPELL_COMPONENT_HASH_KEY] as Int
            val spellId = data[SPELL_ID_KEY] as Int
            val combat = (data[SPELL_TYPE_KEY] as Int) == 0 && spellbook in 0..1 // Only standard and ancient spellbooks have combat spells

            val parent = componentHash shr 16
            val child = componentHash and 0xFFFF
            val runes = arrayListOf<Item>()

            if (data.containsKey(SPELL_RUNE1_ID_KEY)) {
                runes.add(Item(data[SPELL_RUNE1_ID_KEY] as Int, data[SPELL_RUNE1_AMT_KEY] as Int))
            }
            if (data.containsKey(SPELL_RUNE2_ID_KEY)) {
                runes.add(Item(data[SPELL_RUNE2_ID_KEY] as Int, data[SPELL_RUNE2_AMT_KEY] as Int))
            }
            if (data.containsKey(SPELL_RUNE3_ID_KEY)) {
                runes.add(Item(data[SPELL_RUNE3_ID_KEY] as Int, data[SPELL_RUNE3_AMT_KEY] as Int))
            }

            val spell = SpellRequirement(parent, child, spellbook, spellId, name, lvl, runes, combat)
            requirements[spellId] = spell
        }
    }
}