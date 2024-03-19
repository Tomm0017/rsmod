package gg.rsmod.game.fs

import gg.rsmod.game.fs.def.EnumDef
import gg.rsmod.game.model.World
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

class EnumDefinitions(id : Int, world : World) {

    private var values = Int2ObjectOpenHashMap<Any>()

    private var enumId = id

    private val world = world

    fun get() : EnumDefinitions? {
        val struct = world.definitions.getNullable(EnumDef::class.java, enumId)

        if (struct != null) {
            values = struct.values
        }

        return this
    }

    fun getId() : Int {
        return enumId
    }

    fun getValues() : Int2ObjectOpenHashMap<Any> {
        return values
    }

    fun getValueAsString(param: Int) : String? {
        return values.getOrDefault(param, null) as String?
    }

    fun getValueAsInt(param : Int) : Int? {
        return values.getOrDefault(param, null) as Int?
    }

    fun getValueAsBoolean(param: Int) : Boolean {
        if(values.getOrDefault(param, "no") == "yes") {
            return true
        }

        return false
    }
}