package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.plugins.content.magic.*
import gg.rsmod.plugins.content.magic.MagicSpells.on_magic_spell_button

private val SOUNDAREA_ID = 200
private val SOUNDAREA_RADIUS = 10
private val SOUNDAREA_VOLUME = 1

TeleportSpell.values.forEach { teleport ->
    if (teleport.paramItem == null) {
        on_magic_spell_button(teleport.spellName) { metadata ->
            player.teleport(teleport, metadata)
        }
    } else {
        val metadata = MagicSpells.getMetadata(teleport.paramItem)!!
        on_button(metadata.interfaceId, metadata.component) {
            player.teleport(teleport, metadata)
        }
    }
}

on_magic_spell_button("Respawn Teleport") { metadata ->
    player.teleport(TeleportType.ARCEUUS, world.gameContext.home, 27.0, metadata)
}

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