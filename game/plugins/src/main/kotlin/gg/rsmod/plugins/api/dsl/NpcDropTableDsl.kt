package gg.rsmod.plugins.api.dsl

import gg.rsmod.game.plugin.KotlinPlugin
import gg.rsmod.plugins.api.NpcDropTableBuilder

fun KotlinPlugin.set_drop_table(npc: Int, init: NpcDropTableDsl.Builder.() -> Unit) {
    val builder = NpcDropTableDsl.Builder()
    init(builder)

    set_drop_table(npc, builder.build())
}

object NpcDropTableDsl {
    @DslMarker
    annotation class NpcDropTableDslMarker

    @NpcDropTableDslMarker
    class Builder {
        private val dropTableBuilder = NpcDropTableBuilder()

        fun build() = dropTableBuilder.build()

        fun droptable(init: DropTableBuilder.() -> Unit) {
            val builder = DropTableBuilder()
            init(builder)

            dropTableBuilder.setJson(builder.dropTableJSON)
        }
    }

    @NpcDropTableDslMarker
    class DropTableBuilder {
        var dropTableJSON = ""
    }
}
