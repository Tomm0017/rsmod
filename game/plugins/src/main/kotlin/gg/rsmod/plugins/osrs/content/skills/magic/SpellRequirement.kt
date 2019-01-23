package gg.rsmod.plugins.osrs.content.skills.magic

import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SpellRequirement(val parent: Int, val child: Int, val spellId: Int, val name: String,
                            val lvl: Int, val items: List<Item>, val combat: Boolean)