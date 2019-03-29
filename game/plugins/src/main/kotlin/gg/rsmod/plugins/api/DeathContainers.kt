package gg.rsmod.plugins.api

import gg.rsmod.game.model.container.ItemContainer

/**
 * Represents [ItemContainer]s for items that should be kept on death and items
 * that will be lost on death.
 *
 * @see gg.rsmod.plugins.api.ext.calculateDeathContainers
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class DeathContainers(val kept: ItemContainer, val lost: ItemContainer)