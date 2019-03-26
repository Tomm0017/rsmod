package gg.rsmod.plugins.content.skills.runecrafting

CombinationRune.values.forEach { combo ->

    val altar = combo.altar

    item_on_obj(obj = altar.altar, item = combo.talisman) {
        player.queue { RunecraftAction.craftCombination(this, combo) }
    }

    item_on_obj(obj = altar.altar, item = combo.rune) {
        player.queue { RunecraftAction.craftCombination(this, combo) }
    }
}