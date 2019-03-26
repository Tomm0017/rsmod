package gg.rsmod.plugins.content.magic.teleports

import gg.rsmod.plugins.content.magic.*
import gg.rsmod.plugins.content.magic.MagicSpells.on_magic_spell_button

val SOUNDAREA_ID = 200
val SOUNDAREA_RADIUS = 10
val SOUNDAREA_VOLUME = 1

TeleportSpell.values.forEach { teleport ->
    on_magic_spell_button(teleport.spellName) { metadata ->
        player.teleport(teleport, metadata)
    }
}

fun Player.teleport(spell: TeleportSpell, data: SpellMetadata) {
    if (!MagicSpells.canCast(this, data.lvl, data.items, data.spellbook)) {
        return
    }

    val type = spell.type
    val endTile = spell.endArea.randomTile

    if (canTeleport(type)) {
        MagicSpells.removeRunes(this, data.items)
        teleport(endTile, type)
        addXp(Skills.MAGIC, spell.xp)
        world.spawn(AreaSound(tile, SOUNDAREA_ID, SOUNDAREA_RADIUS, SOUNDAREA_VOLUME))
    }
}

enum class TeleportSpell(val spellName: String, val type: TeleportType, val endArea: Area,
                         val xp: Double) {
    /**
     * Standard.
     */
    VARROCK("Varrock Teleport", TeleportType.MODERN, Area(3210, 3423, 3216, 3425), 35.0),
    LUMBRIDGE("Lumbridge Teleport", TeleportType.MODERN, Area(3221, 3218, 3222, 3219), 41.0),
    FALADOR("Falador Teleport", TeleportType.MODERN, Area(2961, 3376, 2969, 3385), 47.0),
    CAMELOT("Camelot Teleport", TeleportType.MODERN, Area(2756, 3476, 2758, 3480), 55.5),
    ARDOUGNE("Ardougne Teleport", TeleportType.MODERN, Area(2659, 3300, 2665, 3310), 61.0),
    WATCHTOWER("Watchtower Teleport", TeleportType.MODERN, Area(2551, 3113, 2553, 3116), 68.0),
    TROLLHEIM("Trollheim Teleport", TeleportType.MODERN, Area(2888, 3675, 2890, 3678), 68.0),
    APE_ATOLL("Ape Atoll Teleport", TeleportType.MODERN, Area(2760, 2781, 2763, 2784), 74.0),
    KOUREND_CASTLE("Kourend Castle Teleport", TeleportType.MODERN, Area(1633, 3665, 1639, 3670), 82.0),

    /**
     * Ancients.
     */
    PADDEWWA("Paddewwa Teleport", TeleportType.ANCIENT, Area(3095, 9880, 3099, 9884), 64.0),
    SENNTISTEN("Senntisten Teleport", TeleportType.ANCIENT, Area(3346, 3343, 3350, 3346), 70.0),
    KHARYRLL("Kharyrll Teleport", TeleportType.ANCIENT, Area(3491, 3476, 3494, 3478), 76.0),
    LASSAR("Lassar Teleport", TeleportType.ANCIENT, Area(3003, 3473, 3008, 3476), 82.0),
    DAREEYAK("Dareeyak Teleport", TeleportType.ANCIENT, Area(2965, 3693, 2969, 3697), 88.0),
    CARRALLANGAR("Carrallangar Teleport", TeleportType.ANCIENT, Area(3146, 3668, 3149, 3671), 94.0),
    ANNAKARL("Annakarl Teleport", TeleportType.ANCIENT, Area(3293, 3885, 3297, 3888), 100.0),
    GHORROCK("Ghorrock Teleport", TeleportType.ANCIENT, Area(2966, 3872, 2972, 3878), 106.0),

    /**
     * Lunars.
     */
    MOONCLAN("Moonclan Teleport", TeleportType.LUNAR, Area(2096, 3912, 2099, 3915), 66.0),
    OURANIA("Ourania Teleport", TeleportType.LUNAR, Area(2454, 3232, 2455, 3233), 69.0),
    WATERBIRTH("Waterbirth Teleport", TeleportType.LUNAR, Area(2545, 3756, 2548, 3759), 71.0),
    BARBARIAN("Barbarian Teleport", TeleportType.LUNAR, Area(2547, 3566, 2549, 3571), 76.0),
    KHAZARD("Khazard Teleport", TeleportType.LUNAR, Area(2652, 3156, 2660, 3159), 80.0),
    CATHERBY("Catherby Teleport", TeleportType.LUNAR, Area(2802, 3432, 2806, 3435), 92.0),
    ICE_PLATEAU("Ice Plateau Teleport", TeleportType.LUNAR, Area(2979, 3940, 2984, 3946), 96.0)
    ;

    companion object {
        val values = enumValues<TeleportSpell>()
    }
}