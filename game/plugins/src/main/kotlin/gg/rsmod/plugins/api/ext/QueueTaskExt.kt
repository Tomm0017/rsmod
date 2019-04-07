package gg.rsmod.plugins.api.ext

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.message.impl.ResumePauseButtonMessage
import gg.rsmod.game.model.Appearance
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.Skills

/**
 * The child id of the chat box in the gameframe interface. This can change
 * with revision.
 */
const val CHATBOX_CHILD = 561

/**
 * The id for the appearance interface.
 */
const val APPEARANCE_INTERFACE_ID = 269

/**
 * The default action that will occur when interrupting or finishing a dialog.
 */
private val closeDialog: QueueTask.() -> Unit = {
    player.closeComponent(parent = 162, child = CHATBOX_CHILD)
}

/**
 * Invoked when input dialog queues are interrupted.
 */
private val closeInput: QueueTask.() -> Unit = {
    player.closeInputDialog()
}

/**
 * Invoked when the appearance input is interrupted.
 */
private val closeAppearance: QueueTask.() -> Unit = {
    player.closeInterface(APPEARANCE_INTERFACE_ID)
}

/**
 * Gets the [QueueTask.ctx] as a [Pawn].
 *
 * If [QueueTask.ctx] is not a [Pawn], a cast exception will be thrown.
 */
inline val QueueTask.pawn: Pawn get() = ctx as Pawn

/**
 * Gets the [QueueTask.ctx] as a [Player].
 *
 * If [QueueTask.ctx] is not a [Pawn], a cast exception will be thrown.
 */
inline val QueueTask.player: Player get() = ctx as Player

/**
 * Gets the [QueueTask.ctx] as an [Npc].
 *
 * If [QueueTask.ctx] is not a [Pawn], a cast exception will be thrown.
 */
inline val QueueTask.npc: Npc get() = ctx as Npc

/**
 * Prompts the player with options.
 *
 * @return
 * The id of the option chosen. The id can range from [1] inclusive to [options.size] inclusive.
 */
suspend fun QueueTask.options(vararg options: String, title: String = "Select an Option"): Int {
    player.sendTempVarbit(5983, 1)
    player.runClientScript(2379)
    player.setInterfaceEvents(interfaceId = 219, component = 1, from = 1, to = options.size, setting = 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 219)
    player.runClientScript(58, title, options.joinToString("|"))

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)

    return (requestReturnValue as? ResumePauseButtonMessage)?.slot ?: -1
}

/**
 * Prompts the player with an input dialog where they can only enter an integer.
 *
 * @return
 * The integer input.
 */
suspend fun QueueTask.inputInt(description: String = "Enter amount"): Int {
    player.runClientScript(108, description)

    terminateAction = closeInput
    waitReturnValue()
    terminateAction!!(this)

    return requestReturnValue as? Int ?: -1
}

/**
 * Prompts the player with a chatbox interface that allows them to search
 * for an item.
 *
 * @return
 * The selected item's id.
 */
suspend fun QueueTask.searchItemInput(message: String): Int {
    player.runClientScript(750, message, 1, -1)

    terminateAction = closeInput
    waitReturnValue()

    return requestReturnValue as? Int ?: -1
}

/**
 * Sends a normal message dialog.
 *
 * @message
 * The message to render on the dialog box.
 *
 * @lineSpacing
 * The spacing, in pixels, in between each line that will be rendered on the
 * dialog box.
 */
suspend fun QueueTask.messageBox(message: String, lineSpacing: Int = 31) {
    player.setComponentText(interfaceId = 229, component = 1, text = message)
    player.setComponentText(interfaceId = 229, component = 2, text = "Click here to continue")
    player.setInterfaceEvents(interfaceId = 229, component = 2, range = -1..-1, setting = 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 229)
    player.runClientScript(600, 1, 1, lineSpacing, 15007745)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

/**
 * Send a dialog with an npc's head model.
 *
 * @param message
 * The message to render on the dialog box.
 *
 * @npc
 * The npc id which represents the npc that will be drawn on the dialog box.
 * If set to -1, the npc id will be set to the player's interacting npc. If the
 * player is not interacting with an npc, a [RuntimeException] will be thrown.
 *
 * @animation
 * The animation id of the npc's head model.
 *
 * @title
 * The title of the dialog, if left as null, the npc's name will be used.
 */
suspend fun QueueTask.chatNpc(message: String, npc: Int = -1, animation: Int = 588, title: String? = null) {
    val npcId = if (npc != -1) npc else player.attr[INTERACTING_NPC_ATTR]?.get()?.getTransform(player) ?: throw RuntimeException("Npc id must be manually set as the player is not interacting with an npc.")
    val dialogTitle = title ?: player.world.definitions.get(NpcDef::class.java, npcId).name

    player.setComponentNpcHead(interfaceId = 231, component = 1, npc = npcId)
    player.setComponentText(interfaceId = 231, component = 2, text = dialogTitle)
    player.setComponentText(interfaceId = 231, component = 3, text = "Click here to continue")
    player.setComponentText(interfaceId = 231, component = 4, text = message)
    player.setComponentAnim(interfaceId = 231, component = 1, anim = animation)
    player.setInterfaceEvents(interfaceId = 231, component = 3, from = -1, to = -1, setting = 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 231)
    player.runClientScript(600, 1, 1, 16, 15138820)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

/**
 * Send a dialog with your player's head model.
 *
 * @param message
 * The message to render on the dialog box.
 */
suspend fun QueueTask.chatPlayer(message: String, animation: Int = 588, title: String? = null) {
    val dialogTitle = title ?: player.username

    player.setComponentPlayerHead(interfaceId = 217, component = 1)
    player.setComponentText(interfaceId = 217, component = 2, text = dialogTitle)
    player.setComponentText(interfaceId = 217, component = 3, text = "Click here to continue")
    player.setComponentText(interfaceId = 217, component = 4, text = message)
    player.setComponentAnim(interfaceId = 217, component = 1, anim = animation)
    player.setInterfaceEvents(interfaceId = 217, component = 3, from = -1, to = -1, setting = 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 217)
    player.runClientScript(600, 1, 1, 16, 14221316)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

/**
 * Sends a single item dialog.
 *
 * @param message
 * The message to render on the dialog box.
 *
 * @param item
 * The id of the item to show on the dialog.
 *
 * @param amount
 * The amount of the item to show on the dialog.
 */
suspend fun QueueTask.itemMessageBox(message: String, item: Int, amount: Int = 1) {
    player.setComponentItem(interfaceId = 193, component = 1, item = item, amountOrZoom = amount)
    player.setComponentText(interfaceId = 193, component = 2, text = message)
    player.setComponentText(interfaceId = 193, component = 3, text = "Click here to continue")
    player.setInterfaceEvents(interfaceId = 193, component = 3, range = -1..-1, setting = 1)
    player.setInterfaceEvents(interfaceId = 193, component = 4, range = -1..-1, setting = 0)
    player.setInterfaceEvents(interfaceId = 193, component = 5, range = -1..-1, setting = 0)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 193)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

suspend fun QueueTask.doubleItemMessageBox(message: String, item1: Int, item2: Int, amount1: Int = 1, amount2: Int = 1) {
    player.setComponentItem(interfaceId = 11, component = 1, item = item1, amountOrZoom = amount1)
    player.setComponentText(interfaceId = 11, component = 2, text = message)
    player.setComponentItem(interfaceId = 11, component = 3, item = item2, amountOrZoom = amount2)
    player.setComponentText(interfaceId = 11, component = 4, text = "Click here to continue")
    player.setInterfaceEvents(interfaceId = 11, component = 4, range = -1..-1, setting = 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 11)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

suspend fun QueueTask.destroyItem(title: String = "Are you sure you want to destroy this item?", note: String, item: Int, amount: Int): Boolean {
    player.sendTempVarbit(5983, 0)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 584)
    player.runClientScript(814, item, amount, 0, title, note)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)

    val msg = requestReturnValue as? ResumePauseButtonMessage
    return msg?.slot == 1
}

suspend fun QueueTask.selectAppearance(): Appearance? {
    player.openInterface(APPEARANCE_INTERFACE_ID, InterfaceDestination.MAIN_SCREEN)

    terminateAction = closeAppearance
    waitReturnValue()
    terminateAction?.invoke(this)

    return requestReturnValue as? Appearance
}

suspend fun QueueTask.levelUpMessageBox(skill: Int, levelIncrement: Int) {
    if (skill != Skills.HUNTER) {
        val children = mapOf(
                Skills.AGILITY to 4,
                Skills.ATTACK to 6,
                Skills.CONSTRUCTION to 9,
                Skills.COOKING to 12,
                Skills.CRAFTING to 14,
                Skills.DEFENCE to 17,
                Skills.FARMING to 19,
                Skills.FIREMAKING to 21,
                Skills.FISHING to 23,
                Skills.FLETCHING to 25,
                Skills.HERBLORE to 28,
                Skills.HITPOINTS to 30,
                Skills.MAGIC to 32,
                Skills.MINING to 34,
                Skills.PRAYER to 36,
                Skills.RANGED to 38,
                Skills.RUNECRAFTING to 41,
                Skills.SLAYER to 43,
                Skills.SMITHING to 45,
                Skills.STRENGTH to 47,
                Skills.THIEVING to 49,
                Skills.WOODCUTTING to 51)

        children.forEach { key, value ->
            player.setComponentHidden(interfaceId = 233, component = value, hidden = skill != key)
        }

        val skillName = Skills.getSkillName(player.world, skill)
        val initialChar = Character.toLowerCase(skillName.toCharArray().first())
        val vowel = initialChar == 'a' || initialChar == 'e' || initialChar == 'i' || initialChar == 'o' || initialChar == 'u'
        val levelFormat = if (levelIncrement == 1) (if (vowel) "an" else "a") else "$levelIncrement"

        player.setComponentText(interfaceId = 233, component = 1, text = "<col=000080>Congratulations, you just advanced $levelFormat $skillName ${"level".pluralSuffix(levelIncrement)}.")
        player.setComponentText(interfaceId = 233, component = 2, text = "Your $skillName level is now ${player.getSkills().getMaxLevel(skill)}.")
        player.setComponentText(interfaceId = 233, component = 3, text = "Click here to continue")
        player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 233)
    } else {
        val levelFormat = if (levelIncrement == 1) "a" else "$levelIncrement"

        player.setInterfaceEvents(interfaceId = 132, component = 3, from = -1, to = -1, setting = 1)
        player.setInterfaceEvents(interfaceId = 132, component = 4, from = -1, to = -1, setting = 0)
        player.setInterfaceEvents(interfaceId = 132, component = 5, from = -1, to = -1, setting = 0)

        player.setComponentItem(interfaceId = 193, component = 1, item = 9951, amountOrZoom = 400)

        player.setComponentText(interfaceId = 193, component = 2, text = "<col=000080>Congratulations, you've just advanced $levelFormat Hunter ${"level".pluralSuffix(levelIncrement)}." +
                "<col=000000><br><br>Your Hunter level is now ${player.getSkills().getMaxLevel(skill)}.")
        player.setComponentText(interfaceId = 193, component = 3, text = "Click here to continue")
        player.setComponentText(interfaceId = 193, component = 4, text = "")
        player.setComponentText(interfaceId = 193, component = 5, text = "")

        player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 193)
    }

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)
}

suspend fun QueueTask.produceItemBox(vararg items: Int, title: String = "What would you like to make?", maxItems: Int = player.inventory.capacity, logic: Player.(Int, Int) -> Unit) {
    val defs = player.world.definitions
    val itemDefs = items.map { defs.get(ItemDef::class.java, it) }

    val baseChild = 14
    val itemArray = Array(10) { -1 }
    val nameArray = Array(10) { "|" }

    itemDefs.withIndex().forEach {
        val def = it.value
        itemArray[it.index] = def.id
        nameArray[it.index] = "|${def.name}"
    }

    player.sendTempVarbit(5983, 1)
    player.openInterface(parent = 162, child = CHATBOX_CHILD, interfaceId = 270)
    player.runClientScript(2046, 0, "$title${nameArray.joinToString("")}", maxItems, *itemArray)

    terminateAction = closeDialog
    waitReturnValue()
    terminateAction!!(this)

    val msg = requestReturnValue as? ResumePauseButtonMessage ?: return
    val child = msg.child

    if (child < baseChild || child >= baseChild + items.size)
        return

    val item = items[child - baseChild]
    val qty = msg.slot

    logic(player, item, qty)
}