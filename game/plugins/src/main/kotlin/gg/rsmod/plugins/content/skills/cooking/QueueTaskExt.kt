package gg.rsmod.plugins.content.skills.cooking

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.message.impl.ResumePauseButtonMessage
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.cooking.data.CookingObj

private val closeCookingDialog: QueueTask.() -> Unit = {
    player.closeComponent(parent = 162, child = 561)
}

suspend fun QueueTask.cookingMessageBox(vararg items: Int, title: String = "What would you like to cook?", maxItems: Int = player.inventory.capacity, obj: CookingObj?, logic: Player.(Int, Int, CookingObj?) -> Unit) {
    val defs = player.world.definitions
    val itemDefs = items.map { defs.get(ItemDef::class.java, it) }

    val itemArray = Array(10) { -1 }
    val nameArray = Array(10) { "|" }

    itemDefs.withIndex().forEach{
        val def = it.value
        itemArray[it.index] = def.id
        nameArray[it.index] = "|${def.name}"
    }

    player.sendTempVarbit(5983, 1)
    player.runClientScript(2379)
    player.openInterface(parent = 162, child = 561, interfaceId = 270)
    player.setInterfaceEvents(interfaceId = 270, component = 14, range = (1..10), setting = 1)
    player.setInterfaceEvents(interfaceId = 270, component = 15, range = (1..10), setting = 1)
    player.runClientScript(2046, 6, "$title${nameArray.joinToString("")}", maxItems, *itemArray, maxItems)

    terminateAction = closeCookingDialog
    waitReturnValue()
    terminateAction!!(this)

    val result = requestReturnValue as? ResumePauseButtonMessage ?: return
    val child = result.component

    if(child < 14 || child >= 14 + items.size) {
        return
    }

    val item = items[child - 14]
    val qty = result.slot

    logic(player, item, qty, obj)
}