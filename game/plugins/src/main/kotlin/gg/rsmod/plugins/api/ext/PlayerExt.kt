package gg.rsmod.plugins.api.ext

import com.google.common.primitives.Ints
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR
import gg.rsmod.game.model.attr.PROTECT_ITEM_ATTR
import gg.rsmod.game.model.bits.BitStorage
import gg.rsmod.game.model.bits.StorageBits
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.timer.SKULL_ICON_DURATION_TIMER
import gg.rsmod.game.sync.block.UpdateBlockType
import gg.rsmod.plugins.api.*
import gg.rsmod.plugins.service.marketvalue.ItemMarketValueService
import gg.rsmod.util.BitManipulation

/**
 * The interface key used by inventory overlays
 */
const val INVENTORY_INTERFACE_KEY = 93

/**
 * The id of the script used to initialise the interface overlay options. The 'big' variant of this script
 * is used as it supports up to eight options rather than five.
 *
 * https://github.com/RuneStar/cs2-scripts/blob/master/scripts/[clientscript,interface_inv_init_big].cs2
 */
const val INTERFACE_INV_INIT_BIG = 150

fun Player.openShop(shop: String) {
    val s = world.getShop(shop)
    if (s != null) {
        attr[CURRENT_SHOP_ATTR] = s
        shopDirty = true

        openInterface(interfaceId = 300, dest = InterfaceDestination.MAIN_SCREEN)
        openInterface(interfaceId = 301, dest = InterfaceDestination.INVENTORY)
        setInterfaceEvents(interfaceId = 300, component = 16, range = 0..s.items.size, setting = 1086)
        setInterfaceEvents(interfaceId = 301, component = 0, range = 0 until inventory.capacity, setting = 1086)
        runClientScript(1074, 13, s.name)
    } else {
        World.logger.warn { "Player \"$username\" is unable to open shop \"$shop\" as it does not exist." }
    }
}

fun Player.message(message: String, type: ChatMessageType = ChatMessageType.CONSOLE) {
    write(MessageGameMessage(type = type.id, message = message, username = null))
}

fun Player.filterableMessage(message: String) {
    write(MessageGameMessage(type = ChatMessageType.SPAM.id, message = message, username = null))
}

fun Player.runClientScript(id: Int, vararg args: Any) {
    write(RunClientScriptMessage(id, *args))
}

fun Player.focusTab(tab: GameframeTab) {
    runClientScript(915, tab.id)
}

fun Player.setInterfaceUnderlay(color: Int, transparency: Int) {
    runClientScript(2524, color, transparency)
}

fun Player.setInterfaceEvents(interfaceId: Int, component: Int, from: Int, to: Int, setting: Int) {
    write(IfSetEventsMessage(hash = ((interfaceId shl 16) or component), fromChild = from, toChild = to, setting = setting))
}

fun Player.setInterfaceEvents(interfaceId: Int, component: Int, range: IntRange, setting: Int) {
    write(IfSetEventsMessage(hash = ((interfaceId shl 16) or component), fromChild = range.start, toChild = range.endInclusive, setting = setting))
}

fun Player.setComponentText(interfaceId: Int, component: Int, text: String) {
    write(IfSetTextMessage(interfaceId, component, text))
}

fun Player.setComponentHidden(interfaceId: Int, component: Int, hidden: Boolean) {
    write(IfSetHideMessage(hash = ((interfaceId shl 16) or component), hidden = hidden))
}

fun Player.setComponentItem(interfaceId: Int, component: Int, item: Int, amountOrZoom: Int) {
    write(IfSetObjectMessage(hash = ((interfaceId shl 16) or component), item = item, amount = amountOrZoom))
}

fun Player.setComponentNpcHead(interfaceId: Int, component: Int, npc: Int) {
    write(IfSetNpcHeadMessage(hash = ((interfaceId shl 16) or component), npc = npc))
}

fun Player.setComponentPlayerHead(interfaceId: Int, component: Int) {
    write(IfSetPlayerHeadMessage(hash = ((interfaceId shl 16) or component)))
}

fun Player.setComponentAnim(interfaceId: Int, component: Int, anim: Int) {
    write(IfSetAnimMessage(hash = ((interfaceId shl 16) or component), anim = anim))
}

/**
 * Use this method to open an interface id on top of an [InterfaceDestination]. This
 * method should always be preferred over
 *
 * ```
 * openInterface(parent: Int, child: Int, component: Int, type: Int, isMainComponent: Boolean)
 * ```
 *
 * as it holds logic that must be handled for certain [InterfaceDestination]s.
 */
fun Player.openInterface(interfaceId: Int, dest: InterfaceDestination, fullscreen: Boolean = false) {
    val displayMode = if (!fullscreen || dest.fullscreenChildId == -1) interfaces.displayMode else DisplayMode.FULLSCREEN
    val child = getChildId(dest, displayMode)
    val parent = getDisplayComponentId(displayMode)
    if (displayMode == DisplayMode.FULLSCREEN) {
        openOverlayInterface(displayMode)
    }
    openInterface(parent, child, interfaceId, if (dest.clickThrough) 1 else 0, isModal = dest == InterfaceDestination.MAIN_SCREEN)
}

/**
 * Use this method to "re-open" an [InterfaceDestination]. This method should always
 * be preferred over
 *
 * ```
 * openInterface(parent: Int, child: Int, interfaceId: Int, type: Int, mainInterface: Boolean)
 * ````
 *
 * as it holds logic that must be handled for certain [InterfaceDestination]s.
 */
fun Player.openInterface(dest: InterfaceDestination, autoClose: Boolean = false) {
    val displayMode = if (!autoClose || dest.fullscreenChildId == -1) interfaces.displayMode else DisplayMode.FULLSCREEN
    val child = getChildId(dest, displayMode)
    val parent = getDisplayComponentId(displayMode)
    if (displayMode == DisplayMode.FULLSCREEN) {
        openOverlayInterface(displayMode)
    }
    openInterface(parent, child, dest.interfaceId, if (dest.clickThrough) 1 else 0, isModal = dest == InterfaceDestination.MAIN_SCREEN)
}

fun Player.openInterface(parent: Int, child: Int, interfaceId: Int, type: Int = 0, isModal: Boolean = false) {
    if (isModal) {
        interfaces.openModal(parent, child, interfaceId)
    } else {
        interfaces.open(parent, child, interfaceId)
    }
    write(IfOpenSubMessage(parent, child, interfaceId, type))
}

fun Player.closeInterface(interfaceId: Int) {
    if (interfaceId == interfaces.getModal()) {
        interfaces.setModal(-1)
    }
    val hash = interfaces.close(interfaceId)
    if (hash != -1) {
        write(IfCloseSubMessage(hash))
    }
}

fun Player.closeInterface(dest: InterfaceDestination) {
    val displayMode = interfaces.displayMode
    val child = getChildId(dest, displayMode)
    val parent = getDisplayComponentId(displayMode)
    val hash = interfaces.close(parent, child)
    if (hash != -1) {
        write(IfCloseSubMessage((parent shl 16) or child))
    }
}

fun Player.closeComponent(parent: Int, child: Int) {
    interfaces.close(parent, child)
    write(IfCloseSubMessage((parent shl 16) or child))
}

fun Player.closeInputDialog() {
    write(TriggerOnDialogAbortMessage())
}

fun Player.getInterfaceAt(dest: InterfaceDestination): Int {
    val displayMode = interfaces.displayMode
    val child = getChildId(dest, displayMode)
    val parent = getDisplayComponentId(displayMode)
    return interfaces.getInterfaceAt(parent, child)
}

fun Player.isInterfaceVisible(interfaceId: Int): Boolean = interfaces.isVisible(interfaceId)

fun Player.toggleDisplayInterface(newMode: DisplayMode) {
    if (interfaces.displayMode != newMode) {
        val oldMode = interfaces.displayMode
        interfaces.displayMode = newMode

        openOverlayInterface(newMode)

        InterfaceDestination.values.filter { it.isSwitchable() }.forEach { pane ->
            val fromParent = getDisplayComponentId(oldMode)
            val fromChild = getChildId(pane, oldMode)
            val toParent = getDisplayComponentId(newMode)
            val toChild = getChildId(pane, newMode)

            /*
             * Remove the interfaces from the old display mode's chilren and add
             * them to the new display mode's children.
             */
            if (interfaces.isOccupied(parent = fromParent, child = fromChild)) {
                val oldComponent = interfaces.close(parent = fromParent, child = fromChild)
                if (oldComponent != -1) {
                    if (pane != InterfaceDestination.MAIN_SCREEN) {
                        interfaces.open(parent = toParent, child = toChild, interfaceId = oldComponent)
                    } else {
                        interfaces.openModal(parent = toParent, child = toChild, interfaceId = oldComponent)
                    }
                }
            }

            write(IfMoveSubMessage(from = (fromParent shl 16) or fromChild, to = (toParent shl 16) or toChild))
        }

        if (newMode.isResizable()) {
            setInterfaceUnderlay(color = -1, transparency = -1)
        }
        if (oldMode.isResizable()) {
            openInterface(parent = getDisplayComponentId(newMode), child = getChildId(InterfaceDestination.MAIN_SCREEN, newMode), interfaceId = 60, type = 0)
        }
    }
}

fun Player.openOverlayInterface(displayMode: DisplayMode) {
    if (displayMode != interfaces.displayMode) {
        interfaces.setVisible(parent = getDisplayComponentId(interfaces.displayMode), child = getChildId(InterfaceDestination.MAIN_SCREEN, interfaces.displayMode), visible = false)
    }
    val component = getDisplayComponentId(displayMode)
    interfaces.setVisible(parent = getDisplayComponentId(displayMode), child = 0, visible = true)
    write(IfOpenTopMessage(component))
}

fun Player.sendItemContainer(key: Int, container: ItemContainer) {
    write(UpdateInvFullMessage(containerKey = key, items = container.rawItems))
}

/**
 * Sends a container type referred to as 'invother' in CS2, which is used for displaying a second container with
 * the same container key. An example of this is the trade accept screen, where the list of items being traded is stored
 * in container 90 for both the player's container, and the partner's container. A container becomes 'invother' when it's
 * component hash is less than -70000, which internally translates the container key to (key + 32768). We can achieve this by either
 * sending a component hash of less than -70000, or by setting the key ourselves. I feel like the latter makes more sense.
 *
 * Special thanks to Polar for explaining this concept to me.
 *
 * https://github.com/RuneStar/cs2-scripts/blob/a144f1dceb84c3efa2f9e90648419a11ee48e7a2/scripts/script768.cs2
 */
fun Player.sendItemContainerOther(key: Int, container: ItemContainer) {
    write(UpdateInvFullMessage(containerKey = key + 32768, items = container.rawItems))
}
fun Player.sendItemContainer(parent: Int, child: Int, container: ItemContainer) {
    write(UpdateInvFullMessage(parent = parent, child = child, items = container.rawItems))
}

fun Player.sendItemContainer(parent: Int, child: Int, key: Int, container: ItemContainer) {
    write(UpdateInvFullMessage(parent = parent, child = child, containerKey = key, items = container.rawItems))
}

fun Player.updateItemContainer(key: Int, container: ItemContainer) {
    // TODO: UpdateInvPartialMessage
    write(UpdateInvFullMessage(containerKey = key, items = container.rawItems))
}

fun Player.sendRunEnergy(energy: Int) {
    write(UpdateRunEnergyMessage(energy))
}

fun Player.playSound(id: Int, volume: Int = 1, delay: Int = 0) {
    write(SynthSoundMessage(sound = id, volume = volume, delay = delay))
}

fun Player.playSong(id: Int) {
    write(MidiSongMessage(id))
}

fun Player.getVarp(id: Int): Int = varps.getState(id)

fun Player.setVarp(id: Int, value: Int) {
    varps.setState(id, value)
}

fun Player.toggleVarp(id: Int) {
    varps.setState(id, varps.getState(id) xor 1)
}

fun Player.syncVarp(id: Int) {
    setVarp(id, getVarp(id))
}

fun Player.getVarbit(id: Int): Int {
    val def = world.definitions.get(VarbitDef::class.java, id)
    return varps.getBit(def.varp, def.startBit, def.endBit)
}

fun Player.setVarbit(id: Int, value: Int) {
    val def = world.definitions.get(VarbitDef::class.java, id)
    varps.setBit(def.varp, def.startBit, def.endBit, value)
}

/**
 * Write a varbit message to the player's client without actually modifying
 * its varp value in [Player.varps].
 */
fun Player.sendTempVarbit(id: Int, value: Int) {
    val def = world.definitions.get(VarbitDef::class.java, id)
    val state = BitManipulation.setBit(varps.getState(def.varp), def.startBit, def.endBit, value)
    val message = if (state in -Byte.MAX_VALUE..Byte.MAX_VALUE) VarpSmallMessage(def.varp, state) else VarpLargeMessage(def.varp, state)
    write(message)
}

fun Player.toggleVarbit(id: Int) {
    val def = world.definitions.get(VarbitDef::class.java, id)
    varps.setBit(def.varp, def.startBit, def.endBit, getVarbit(id) xor 1)
}

fun Player.setMapFlag(x: Int, z: Int) {
    write(SetMapFlagMessage(x, z))
}

fun Player.clearMapFlag() {
    setMapFlag(255, 255)
}

fun Player.sendOption(option: String, id: Int, leftClick: Boolean = false) {
    check(id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    val index = id - 1
    options[index] = option
    write(SetOpPlayerMessage(option, index, leftClick))
}

/**
 * Checks if the player has an option with the name [option] (case-sensitive).
 */
fun Player.hasOption(option: String, id: Int = -1): Boolean {
    check(id == -1 || id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    return if (id != -1) options.any { it == option } else options[id - 1] == option
}

/**
 * Removes the option with [id] from this player.
 */
fun Player.removeOption(id: Int) {
    check(id in 1..options.size) { "Option id must range from [1-${options.size}]" }
    val index = id - 1
    write(SetOpPlayerMessage("null", index, false))
    options[index] = null
}

fun Player.getStorageBit(storage: BitStorage, bits: StorageBits): Int = storage.get(this, bits)

fun Player.hasStorageBit(storage: BitStorage, bits: StorageBits): Boolean = storage.get(this, bits) != 0

fun Player.setStorageBit(storage: BitStorage, bits: StorageBits, value: Int) {
    storage.set(this, bits, value)
}

fun Player.toggleStorageBit(storage: BitStorage, bits: StorageBits) {
    storage.set(this, bits, storage.get(this, bits) xor 1)
}

fun Player.heal(amount: Int, capValue: Int = 0) {
    getSkills().alterCurrentLevel(skill = Skills.HITPOINTS, value = amount, capValue = capValue)
}

fun Player.hasSpellbook(book: Spellbook): Boolean = getVarbit(4070) == book.id

fun Player.getSpellbook(): Spellbook = Spellbook.values.first { getVarbit(4070) == it.id }

fun Player.getWeaponType(): Int = getVarbit(357)

fun Player.getAttackStyle(): Int = getVarp(43)

fun Player.hasWeaponType(type: WeaponType, vararg others: WeaponType): Boolean = getWeaponType() == type.id || others.isNotEmpty() && getWeaponType() in others.map { it.id }

fun Player.hasEquipped(slot: EquipmentType, vararg items: Int): Boolean {
    check(items.isNotEmpty()) { "Items shouldn't be empty." }
    return items.any { equipment.hasAt(slot.id, it) }
}

fun Player.hasEquipped(items: IntArray) = items.all { equipment.contains(it) }

fun Player.getEquipment(slot: EquipmentType): Item? = equipment[slot.id]

fun Player.setSkullIcon(icon: SkullIcon) {
    skullIcon = icon.id
    addBlock(UpdateBlockType.APPEARANCE)
}

fun Player.skull(icon: SkullIcon, durationCycles: Int) {
    check(icon != SkullIcon.NONE)
    setSkullIcon(icon)
    timers[SKULL_ICON_DURATION_TIMER] = durationCycles
}

fun Player.hasSkullIcon(icon: SkullIcon): Boolean = skullIcon == icon.id

fun Player.isClientResizable(): Boolean = interfaces.displayMode == DisplayMode.RESIZABLE_NORMAL || interfaces.displayMode == DisplayMode.RESIZABLE_LIST

fun Player.inWilderness(): Boolean = getInterfaceAt(InterfaceDestination.PVP_OVERLAY) != -1

fun Player.sendWorldMapTile() {
    runClientScript(1749, tile.as30BitInteger)
}

fun Player.sendCombatLevelText() {
    setComponentText(593, 2, "Combat Lvl: $combatLevel")
}

fun Player.sendWeaponComponentInformation() {
    val weapon = getEquipment(EquipmentType.WEAPON)

    val name: String
    val panel: Int

    if (weapon != null) {
        val definition = world.definitions.get(ItemDef::class.java, weapon.id)
        name = definition.name

        panel = Math.max(0, definition.weaponType)
    } else {
        name = "Unarmed"
        panel = 0
    }

    setComponentText(593, 1, name)
    setVarbit(357, panel)
}

fun Player.calculateAndSetCombatLevel(): Boolean {
    val old = combatLevel

    val attack = getSkills().getMaxLevel(Skills.ATTACK)
    val defence = getSkills().getMaxLevel(Skills.DEFENCE)
    val strength = getSkills().getMaxLevel(Skills.STRENGTH)
    val hitpoints = getSkills().getMaxLevel(Skills.HITPOINTS)
    val prayer = getSkills().getMaxLevel(Skills.PRAYER)
    val ranged = getSkills().getMaxLevel(Skills.RANGED)
    val magic = getSkills().getMaxLevel(Skills.MAGIC)

    val base = Ints.max(strength + attack, magic * 2, ranged * 2)

    combatLevel = ((base * 1.3 + defence + hitpoints + prayer / 2) / 4).toInt()

    val changed = combatLevel != old
    if (changed) {
        runClientScript(389, combatLevel)
        sendCombatLevelText()
        return true
    }

    return false
}

fun Player.calculateDeathContainers(): DeathContainers {
    var keepAmount = if (hasSkullIcon(SkullIcon.WHITE)) 0 else 3
    if (attr[PROTECT_ITEM_ATTR] == true) {
        keepAmount++
    }

    val keptContainer = ItemContainer(world.definitions, keepAmount, ContainerStackType.NO_STACK)
    val lostContainer = ItemContainer(world.definitions, inventory.capacity + equipment.capacity, ContainerStackType.NORMAL)

    var totalItems = inventory.rawItems.filterNotNull() + equipment.rawItems.filterNotNull()
    val valueService = world.getService(ItemMarketValueService::class.java)

    totalItems = if (valueService != null) {
        totalItems.sortedBy { it.id }.sortedWith(compareByDescending { valueService.get(it.id) })
    } else {
        totalItems.sortedBy { it.id }.sortedWith(compareByDescending { world.definitions.get(ItemDef::class.java, it.id).cost })
    }

    totalItems.forEach { item ->
        if (keepAmount > 0 && !keptContainer.isFull) {
            val add = keptContainer.add(item, assureFullInsertion = false)
            keepAmount -= add.completed
            if (add.getLeftOver() > 0) {
                lostContainer.add(item.id, add.getLeftOver())
            }
        } else {
            lostContainer.add(item)
        }
    }

    return DeathContainers(kept = keptContainer, lost = lostContainer)
}

// Note: this does not take ground items, that may belong to the player, into
// account.
fun Player.hasItem(item: Int, amount: Int = 1): ItemContainer? = containers.values.firstOrNull { container -> container.getItemCount(item) >= amount }

fun Player.isPrivilegeEligible(to: String): Boolean = world.privileges.isEligible(privilege, to)

fun Player.getStrengthBonus(): Int = equipmentBonuses[10]

fun Player.getRangedStrengthBonus(): Int = equipmentBonuses[11]

fun Player.getMagicDamageBonus(): Int = equipmentBonuses[12]

fun Player.getPrayerBonus(): Int = equipmentBonuses[13]