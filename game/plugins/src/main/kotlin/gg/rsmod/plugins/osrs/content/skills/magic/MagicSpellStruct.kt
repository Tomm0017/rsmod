package gg.rsmod.plugins.osrs.content.skills.magic

import gg.rsmod.game.model.item.Item

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class MagicSpellStruct(val parent: Int, val child: Int, val name: String, val lvl: Int,
                            val autoCastId: Int, val items: List<Item>)