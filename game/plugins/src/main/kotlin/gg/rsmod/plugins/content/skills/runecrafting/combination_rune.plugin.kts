package gg.rsmod.plugins.content.skills.runecrafting

CombinationRune.values.forEach { combo ->

    val altar = combo.altar

    on_item_on_obj(obj = altar.altar, item = combo.talisman) {
        player.queue { RunecraftAction.craftCombination(this, combo) }
    }

    on_item_on_obj(obj = altar.altar, item = combo.rune) {
        player.queue { RunecraftAction.craftCombination(this, combo) }
    }
}