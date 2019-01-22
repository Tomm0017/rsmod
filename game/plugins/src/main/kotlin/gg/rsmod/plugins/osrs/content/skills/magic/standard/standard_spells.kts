import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.osrs.api.helper.getInteractingNpc
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.osrs.content.skills.magic.Magic
import gg.rsmod.plugins.osrs.content.skills.magic.MagicSpellStruct

world.definitions.get(EnumDef::class.java, Magic.STANDARD_SPELL_DATA_ENUM).let { enum ->
    val dataItems = enum.values.values.map { it as Int }
    for (dataItem in dataItems) {
        val def = world.definitions.get(ItemDef::class.java, dataItem)
        val data = def.data

        val name = data[Magic.SPELL_NAME_KEY] as String
        val lvl = data[Magic.SPELL_LVL_REQ_KEY] as Int
        val interfaceHash = data[Magic.SPELL_INTERFACE_HASH_KEY] as Int
        val combat = data[Magic.SPELL_TYPE_KEY] as Int == 0
        val spellId = data[Magic.SPELL_ID_KEY] as Int
        val parent = interfaceHash shr 16
        val child = interfaceHash and 0xFFFF
        val runes = arrayListOf<Item>()

        if (data.containsKey(Magic.SPELL_RUNE1_ID_KEY)) {
            runes.add(Item(data[Magic.SPELL_RUNE1_ID_KEY] as Int, data[Magic.SPELL_RUNE1_AMT_KEY] as Int))
        }

        if (data.containsKey(Magic.SPELL_RUNE2_ID_KEY)) {
            runes.add(Item(data[Magic.SPELL_RUNE2_ID_KEY] as Int, data[Magic.SPELL_RUNE2_AMT_KEY] as Int))
        }

        if (data.containsKey(Magic.SPELL_RUNE3_ID_KEY)) {
            runes.add(Item(data[Magic.SPELL_RUNE3_ID_KEY] as Int, data[Magic.SPELL_RUNE3_AMT_KEY] as Int))
        }

        if (combat) {
            val spell = CombatSpell.values().firstOrNull { it.id == spellId }
            if (spell != null) {
                Magic.combatStructs[spell] = MagicSpellStruct(parent, child, name, lvl, spell.autoCastId, runes)
            }
        }
    }
}

Magic.combatStructs.forEach { combatStruct ->
    val struct = combatStruct.value
    r.bindSpellOnNpc(struct.parent, struct.child) {
        val player = it.player()
        val npc = it.getInteractingNpc()
        if (Magic.canCast(player, struct.lvl, struct.items)) {
            player.attr[Combat.CASTING_SPELL] = Magic.combatStructs.entries.first { it.value == struct }.key
            player.attack(npc)
        }
    }
}