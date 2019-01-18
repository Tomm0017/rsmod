package gg.rsmod.plugins.osrs.api.helper

import com.google.common.primitives.Ints
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.BitStorage
import gg.rsmod.game.model.SkillSet
import gg.rsmod.game.model.StorageBits
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.service.game.WeaponConfigService
import gg.rsmod.plugins.osrs.api.*
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayer
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayers
import gg.rsmod.plugins.osrs.service.item.ItemValueService
import gg.rsmod.util.BitManipulation

/**
 * A decoupled file that holds extensions and helper functions, related to players,
 * that can be used throughout plugins.
 *
 * @author Tom <rspsmods@gmail.com>
 */

fun Player.message(message: String, type: ChatMessageType = ChatMessageType.GAME) {
    write(SendChatboxTextMessage(type = type.id, message = message, username = null))
}

fun Player.filterableMessage(message: String) {
    write(SendChatboxTextMessage(type = ChatMessageType.FILTERED.id, message = message, username = null))
}

fun Player.invokeScript(id: Int, vararg args: Any) {
    write(InvokeScriptMessage(id, *args))
}

fun Player.focusTab(tab: GameframeTab) {
    invokeScript(915, tab.id)
}

fun Player.setMainInterfaceBackground(color: Int, transparency: Int) {
    invokeScript(2524, color, transparency)
}

fun Player.setInterfaceText(parent: Int, child: Int, text: String) {
    write(SetInterfaceTextMessage(parent, child, text))
}

fun Player.setInterfaceSetting(parent: Int, child: Int, from: Int, to: Int, setting: Int) {
    write(SetInterfaceSettingMessage(hash = ((parent shl 16) or child), fromChild = from, toChild = to, setting = setting))
}

fun Player.setInterfaceSetting(parent: Int, child: Int, range: IntRange, setting: Int) {
    write(SetInterfaceSettingMessage(hash = ((parent shl 16) or child), fromChild = range.start, toChild = range.endInclusive, setting = setting))
}

fun Player.setInterfaceHidden(parent: Int, child: Int, hidden: Boolean) {
    write(SetInterfaceHiddenMessage(hash = ((parent shl 16) or child), hidden = hidden))
}

fun Player.setInterfaceItem(parent: Int, child: Int, item: Int, amountOrZoom: Int) {
    write(SetInterfaceItemMessage(hash = ((parent shl 16) or child), item = item, amount = amountOrZoom))
}

fun Player.setInterfaceNpc(parent: Int, child: Int, npc: Int) {
    write(SetInterfaceNpcMessage(hash = ((parent shl 16) or child), npc = npc))
}

fun Player.setInterfaceAnim(parent: Int, child: Int, anim: Int) {
    write(SetInterfaceAnimationMessage(hash = ((parent shl 16) or child), anim = anim))
}

/**
 * Use this method to open an interface id on top of an [InterfacePane]. This
 * method should always be preferred over
 *
 * ```
 * openInterface(parent: Int, child: Int, interfaceId: Int, type: Int, mainInterface: Boolean)
 * ```
 *
 * as it holds logic that must be handled for certain [InterfacePane]s.
 */
fun Player.openInterface(interfaceId: Int, pane: InterfacePane, fullscreen: Boolean = false) {
    val displayMode = if (!fullscreen || pane.fullscreenChildId == -1) interfaces.displayMode else DisplayMode.FULLSCREEN
    val child = getChildId(pane, displayMode)
    val parent = getDisplayInterfaceId(displayMode)
    if (displayMode == DisplayMode.FULLSCREEN) {
        sendDisplayInterface(displayMode)
    }
    openInterface(parent, child, interfaceId, if (pane.clickThrough) 1 else 0, mainInterface = pane == InterfacePane.MAIN_SCREEN)
}

/**
 * Use this method to "re-open" an [InterfacePane]. This method should always
 * be preferred over
 *
 * ```
 * openInterface(parent: Int, child: Int, interfaceId: Int, type: Int, mainInterface: Boolean)
 * ````
 *
 * as it holds logic that must be handled for certain [InterfacePane]s.
 */
fun Player.openInterface(pane: InterfacePane, fullscreen: Boolean = false) {
    val displayMode = if (!fullscreen || pane.fullscreenChildId == -1) interfaces.displayMode else DisplayMode.FULLSCREEN
    val child = getChildId(pane, displayMode)
    val parent = getDisplayInterfaceId(displayMode)
    if (displayMode == DisplayMode.FULLSCREEN) {
        sendDisplayInterface(displayMode)
    }
    openInterface(parent, child, pane.interfaceId, if (pane.clickThrough) 1 else 0, mainInterface = pane == InterfacePane.MAIN_SCREEN)
}

fun Player.openInterface(parent: Int, child: Int, interfaceId: Int, type: Int = 0, mainInterface: Boolean = false) {
    if (mainInterface) {
        interfaces.openMain(parent, child, interfaceId)
    } else {
        interfaces.open(parent, child, interfaceId)
    }
    write(OpenInterfaceMessage(parent, child, interfaceId, type))
}

fun Player.closeInterface(interfaceId: Int) {
    val hash = interfaces.close(interfaceId)
    if (hash != -1) {
        write(CloseInterfaceMessage(hash))
    }
}

fun Player.closeInterface(parent: Int, child: Int) {
    interfaces.close(parent, child)
    write(CloseInterfaceMessage((parent shl 16) or child))
}

fun Player.isInterfaceVisible(interfaceId: Int): Boolean = interfaces.isVisible(interfaceId)

fun Player.toggleDisplayInterface(newMode: DisplayMode) {
    if (interfaces.displayMode != newMode) {
        val oldMode = interfaces.displayMode
        interfaces.displayMode = newMode

        sendDisplayInterface(newMode)

        InterfacePane.values().filter { it.isSwitchable() }.forEach { pane ->
            val fromParent = getDisplayInterfaceId(oldMode)
            val fromChild = getChildId(pane, oldMode)
            val toParent = getDisplayInterfaceId(newMode)
            val toChild = getChildId(pane, newMode)

            /**
             * Remove the interfaces from the old display mode's chilren and add
             * them to the new display mode's children.
             */
            if (interfaces.isOccupied(parent = fromParent, child = fromChild)) {
                val oldInterface = interfaces.close(parent = fromParent, child = fromChild)
                if (oldInterface != -1) {
                    if (pane != InterfacePane.MAIN_SCREEN) {
                        interfaces.open(parent = toParent, child = toChild, interfaceId = oldInterface)
                    } else {
                        interfaces.openMain(parent = toParent, child = toChild, interfaceId = oldInterface)
                    }
                }
            }

            write(InterfaceSwitchMessage(from = (fromParent shl 16) or fromChild, to = (toParent shl 16) or toChild))
        }

        if (newMode.isResizable()) {
            setMainInterfaceBackground(color = -1, transparency = -1)
        }
        if (oldMode.isResizable()) {
            openInterface(parent = getDisplayInterfaceId(newMode), child = getChildId(InterfacePane.MAIN_SCREEN, newMode), interfaceId = 60, type = 0)
        }
    }
}

fun Player.sendDisplayInterface(displayMode: DisplayMode) {
    if (displayMode != interfaces.displayMode) {
        interfaces.setVisible(parent = getDisplayInterfaceId(interfaces.displayMode), child = getChildId(InterfacePane.MAIN_SCREEN, interfaces.displayMode), visible = false)
    }
    val interfaceId = getDisplayInterfaceId(displayMode)
    interfaces.setVisible(parent = getDisplayInterfaceId(displayMode), child = 0, visible = true)
    write(SetDisplayInterfaceMessage(interfaceId))
}

fun Player.sendContainer(key: Int, container: ItemContainer) {
    write(SetItemContainerMessage(containerKey = key, items = container.getBackingArray()))
}

fun Player.sendContainer(parent: Int, child: Int, container: ItemContainer) {
    write(SetItemContainerMessage(parent = parent, child = child, items = container.getBackingArray()))
}

fun Player.sendRunEnergy() {
    write(SetRunEnergyMessage(runEnergy.toInt()))
}

fun Player.playSound(id: Int, volume: Int = 1, delay: Int = 0) {
    write(PlaySoundMessage(sound = id, volume = volume, delay = delay))
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
    val def = world.definitions[VarbitDef::class.java][id]
    return varps.getBit(def.varp, def.startBit, def.endBit)
}

fun Player.setVarbit(id: Int, value: Int) {
    val def = world.definitions[VarbitDef::class.java][id]
    varps.setBit(def.varp, def.startBit, def.endBit, value)
}

fun Player.toggleVarbit(id: Int) {
    val def = world.definitions[VarbitDef::class.java][id]
    varps.setBit(def.varp, def.startBit, def.endBit, getVarbit(id) xor 1)
}

fun Player.sendVisualVarbit(id: Int, value: Int) {
    val def = world.definitions[VarbitDef::class.java][id]
    val state = BitManipulation.setBit(varps.getState(def.varp), def.startBit, def.endBit, value)
    val message = if (state < Byte.MAX_VALUE) SetSmallVarpMessage(def.varp, state) else SetBigVarpMessage(def.varp, state)
    write(message)
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

fun Player.getWeaponType(): Int = getVarp(843)

fun Player.getAttackStyle(): Int = getVarp(43)

fun Player.hasWeaponType(type: WeaponType, vararg others: WeaponType): Boolean = getWeaponType() == type.id || others.isNotEmpty() && getWeaponType() in others.map { it.id }

fun Player.hasEquipped(slot: EquipmentType, item: Int): Boolean = equipment.hasAt(slot.id, item)

fun Player.getEquipment(slot: EquipmentType): Item? = equipment[slot.id]

fun Player.getBonus(slot: BonusSlot): Int = equipmentBonuses[slot.id]

fun Player.hasPrayerIcon(icon: PrayerIcon): Boolean = prayerIcon == icon.id

fun Player.hasSkullIcon(icon: SkullIcon): Boolean = skullIcon == icon.id

fun Player.isClientResizable(): Boolean = interfaces.displayMode == DisplayMode.RESIZABLE_NORMAL || interfaces.displayMode == DisplayMode.RESIZABLE_LIST

fun Player.sendWorldMapTile() {
    invokeScript(1749, tile.to30BitInteger())
}

fun Player.sendCombatLevelText() {
    setInterfaceText(593, 2, "Combat Lvl: ${getSkills().combatLevel}")
}

fun Player.sendWeaponInterfaceInformation() {
    val weapon = getEquipment(EquipmentType.WEAPON)

    val name: String
    val panel: Int

    if (weapon != null) {
        val definition = world.definitions.get(ItemDef::class.java, weapon.id)
        name = definition.name

        val weaponConfig = world.getService(WeaponConfigService::class.java).orElse(null)
        panel = weaponConfig?.get(weapon.id)?.type ?: 0
    } else {
        name = "Unarmed"
        panel = 0
    }

    setInterfaceText(593, 1, name)
    setVarp(843, panel)
}

fun Player.addXp(skill: Int, xp: Double) {
    val currentXp = getSkills().getCurrentXp(skill)
    if (currentXp >= SkillSet.MAX_XP) {
        return
    }
    val totalXp = Math.min(SkillSet.MAX_XP.toDouble(), (currentXp + xp))
    /**
     * Amount of levels that have increased with the addition of [xp].
     */
    val increment = SkillSet.getLevelForXp(totalXp) - SkillSet.getLevelForXp(currentXp)

    getSkills().setBaseXp(skill, totalXp)

    if (increment > 0) {
        /**
         * Calculate the combat level for the player if they leveled up a combat
         * skill.
         */
        if (Skills.isCombat(skill)) {
            calculateAndSetCombatLevel()
        }
        /**
         * Show the level-up chatbox interface.
         */
        executePlugin {
            it.suspendable { it.levelUpDialog(skill, increment) }
        }
    }
}

fun Player.calculateAndSetCombatLevel(): Boolean {
    val old = getSkills().combatLevel

    val attack = getSkills().getMaxLevel(Skills.ATTACK)
    val defence = getSkills().getMaxLevel(Skills.DEFENCE)
    val strength = getSkills().getMaxLevel(Skills.STRENGTH)
    val hitpoints = getSkills().getMaxLevel(Skills.HITPOINTS)
    val prayer = getSkills().getMaxLevel(Skills.PRAYER)
    val ranged = getSkills().getMaxLevel(Skills.RANGED)
    val magic = getSkills().getMaxLevel(Skills.MAGIC)

    val base = Ints.max(strength + attack, magic * 2, ranged * 2)

    getSkills().combatLevel = ((base * 1.3 + defence + hitpoints + prayer / 2) / 4).toInt()

    val changed = getSkills().combatLevel != old
    if (changed) {
        invokeScript(389, getSkills().combatLevel)
        sendCombatLevelText()
        return true
    }

    return false
}

fun Player.calculateDeathContainers(): DeathContainers {
    var keepAmount = if (hasSkullIcon(SkullIcon.WHITE)) 0 else 3
    if (Prayers.isActive(this, Prayer.PROTECT_ITEM)) {
        keepAmount++
    }

    val keptContainer = ItemContainer(world.definitions, keepAmount, ContainerStackType.NO_STACK)
    val lostContainer = ItemContainer(world.definitions, inventory.capacity + equipment.capacity, ContainerStackType.NORMAL)

    var totalItems = inventory.getBackingArray().filterNotNull() + equipment.getBackingArray().filterNotNull()
    val valueService = world.getService(ItemValueService::class.java).orElse(null)

    if (valueService != null) {
        totalItems = totalItems.sortedByDescending { valueService.get(it.id) }
    } else {
        totalItems = totalItems.sortedByDescending { world.definitions.getNullable(ItemDef::class.java, it.id)?.cost ?: 0 }
    }

    totalItems.forEach { item ->
        if (keepAmount > 0 && !keptContainer.isFull()) {
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

fun Player.isPrivilegeEligible(to: String): Boolean = world.privileges.isEligible(privilege, to)