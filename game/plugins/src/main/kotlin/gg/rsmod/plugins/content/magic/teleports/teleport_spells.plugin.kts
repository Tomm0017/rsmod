package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.plugins.content.magic.*
import gg.rsmod.plugins.content.magic.MagicSpells.on_magic_spell_button

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 10
private val SOUNDAREA_VOLUME = 1

fun Player.teleport(spell: TeleportSpell, data: SpellMetadata) = teleport(spell.type, spell.endArea.randomTile, spell.xp, data)

fun Player.teleport(type: TeleportType, endTile: Tile, xp: Double, data: SpellMetadata) {
    if (!MagicSpells.canCast(this, data.lvl, data.items, data.spellbook)) {
        return
    }

    if (canTeleport(type)) {
        MagicSpells.removeRunes(this, data.items)
        teleport(endTile, type)
        addXp(Skills.MAGIC, xp)
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
    }
}