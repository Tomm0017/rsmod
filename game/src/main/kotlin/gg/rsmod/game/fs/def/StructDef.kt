package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class StructDef(override val id: Int) : Definition(id) {

    val params = Int2ObjectOpenHashMap<Any>()

    override fun decode(buf: ByteBuf, opcode: Int) {
        if (opcode == 249) {
            params.putAll(readParams(buf))
        }
    }


}