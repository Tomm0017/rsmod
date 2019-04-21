package gg.rsmod.game.sync.block

import gg.rsmod.game.message.MessageValue
import gg.rsmod.net.packet.DataOrder
import gg.rsmod.net.packet.DataSignature
import gg.rsmod.net.packet.DataTransformation
import gg.rsmod.net.packet.DataType
import gg.rsmod.util.ServerProperties
import java.util.ArrayList
import java.util.EnumMap
import java.util.LinkedHashMap

/**
 * A set of [UpdateBlockType] information that is required to encode its respective
 * block data.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class UpdateBlockSet {

    /**
     * The updating packet opcode.
     */
    var updateOpcode = -1

    /**
     * A secondary packet opcode used in 'large scene' synchronization.
     */
    var largeSceneUpdateOpcode = -1

    /**
     * When [gg.rsmod.game.sync.block.UpdateBlockBuffer.blockValue] exceeds the
     * value 0xFF, we have to write it as a short. The client uses a certain bit
     * to identify when this is the case.
     */
    var updateBlockExcessMask = -1

    /**
     * The order in which [UpdateBlockType]s must be handled in the synchronization
     * process.
     */
    val updateBlockOrder = mutableListOf<UpdateBlockType>()

    val updateBlocks = EnumMap<UpdateBlockType, UpdateBlockStructure>(UpdateBlockType::class.java)

    fun load(properties: ServerProperties) {
        check(this.updateOpcode == -1)
        check(this.updateBlockExcessMask == -1)
        check(this.updateBlockOrder.isEmpty())
        check(this.updateBlocks.isEmpty())

        val updateOpcode = properties.get<Int>("updating-opcode")!!
        val largeSceneUpdateOpcode = properties.getOrDefault("large-updating-opcode", -1)
        val excessMask = if (properties.has("excess-mask")) Integer.decode(properties.get<String>("excess-mask")) else -1
        this.updateOpcode = updateOpcode
        this.updateBlockExcessMask = excessMask
        this.largeSceneUpdateOpcode = largeSceneUpdateOpcode

        val orders = properties.getOrDefault("order", ArrayList<Any>())
        orders.forEach { order ->
            val blockType = UpdateBlockType.valueOf((order as String).toUpperCase())
            this.updateBlockOrder.add(blockType)
        }

        val blocks = properties.getOrDefault("blocks", ArrayList<Any>())
        blocks.forEach { packet ->
            val values = packet as LinkedHashMap<*, *>
            val blockType = (values["block"] as String).toUpperCase()
            val bit = if (values.containsKey("bit")) Integer.decode(values["bit"] as String) else -1
            val structureValues = mutableListOf<MessageValue>()

            if (values.containsKey("structure")) {
                val structures = values["structure"] as ArrayList<*>
                structures.forEach { structure ->
                    val map = structure as LinkedHashMap<*, *>
                    val name = map["name"] as String
                    val order = if (map.containsKey("order")) DataOrder.valueOf(map["order"] as String) else DataOrder.BIG
                    val transform = if (map.containsKey("trans")) DataTransformation.valueOf(map["trans"] as String) else DataTransformation.NONE
                    val type = DataType.valueOf(map["type"] as String)
                    val signature = if (map.containsKey("sign")) DataSignature.valueOf((map["sign"] as String).toUpperCase()) else DataSignature.SIGNED
                    structureValues.add(MessageValue(id = name, order = order, transformation = transform, type = type, signature = signature))
                }
            }

            val block = UpdateBlockType.valueOf(blockType)
            val structure = UpdateBlockStructure(bit = bit, values = structureValues)
            this.updateBlocks[block] = structure
        }
    }
}