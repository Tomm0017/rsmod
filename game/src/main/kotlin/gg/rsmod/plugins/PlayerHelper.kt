package gg.rsmod.plugins

import com.google.common.primitives.Ints
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.SkillSet
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.osrs.*
import gg.rsmod.plugins.osrs.model.BonusSlot
import gg.rsmod.plugins.osrs.model.Equipment
import gg.rsmod.plugins.osrs.model.Skills

/**
 * A decoupled file that holds extensions and helper functions, related to players,
 * that can be used throughout plugins.
 *
 * @author Tom <rspsmods@gmail.com>
 */

fun Player.message(message: String, type: OSRSMessageType = OSRSMessageType.GAME) {
    write(SendChatboxTextMessage(type = type.id, message = message, username = null))
}

fun Player.filterableMessage(message: String) {
    write(SendChatboxTextMessage(type = OSRSMessageType.FILTERED.id, message = message, username = null))
}

fun Player.invokeScript(id: Int, vararg args: Any) {
    write(InvokeScriptMessage(id, *args))
}

fun Player.openTab(tab: GameframeTab) {
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

fun Player.openInterface(interfaceId: Int, pane: InterfacePane) {
    val child = getChildId(pane, interfaces.displayMode)
    val parent = getDisplayInterfaceId(interfaces.displayMode)
    openInterface(parent, child, interfaceId, if (pane.clickThrough) 1 else 0, mainInterface = pane == InterfacePane.MAIN_SCREEN)
}

fun Player.openInterface(pane: InterfacePane) {
    val child = getChildId(pane, interfaces.displayMode)
    val parent = getDisplayInterfaceId(interfaces.displayMode)
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

fun Player.sendDisplayInterface(displayMode: DisplayMode) {
    interfaces.setVisible(getDisplayInterfaceId(interfaces.displayMode), false)
    interfaces.displayMode = displayMode

    val interfaceId = getDisplayInterfaceId(displayMode)
    interfaces.setVisible(interfaceId, true)
    write(SetDisplayInterfaceMessage(interfaceId))
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

fun Player.hasEquipped(slot: Equipment, item: Int): Boolean = equipment.hasAt(slot.id, item)

fun Player.getBonus(slot: BonusSlot): Int = equipmentBonuses[slot.id]

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
        setInterfaceText(593, 2, "Combat Lvl: ${getSkills().combatLevel}")
        return true
    }

    return false
}

fun Player.isPrivilegeEligible(to: String): Boolean = world.privileges.isEligible(privilege, to)