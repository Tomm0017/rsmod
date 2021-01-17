package gg.rsmod.game.model.container.key

import gg.rsmod.game.model.container.ContainerStackType

/**
 * A decoupled file that holds [ContainerKey]s that are pre-defined in our core
 * game-module.
 *
 * @author Tom <rspsmods@gmail.com>
 */

val BOND_POUCH_KEY = ContainerKey("bonds", capacity = 200, stackType = ContainerStackType.NO_STACK)
val INVENTORY_KEY = ContainerKey("inventory", capacity = 28, stackType = ContainerStackType.NORMAL)
val EQUIPMENT_KEY = ContainerKey("equipment", capacity = 14, stackType = ContainerStackType.NORMAL)
val BANK_KEY = ContainerKey("bank", capacity = 800, stackType = ContainerStackType.STACK)