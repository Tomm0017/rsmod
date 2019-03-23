package gg.rsmod.plugins.content.items.essencepouch

/**
 * @author Triston Plummer ("Dread')
 *
 * @param id        The essence pouch item id
 * @param levelReq  The Ruencrafting level required to use the pouch
 * @param capacity  The maximum capacity of theessence pouch
 */
data class EssencePouch(val id: Int, val levelReq: Int, val capacity: Int)