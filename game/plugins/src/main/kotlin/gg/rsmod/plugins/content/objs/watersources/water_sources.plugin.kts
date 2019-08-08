package gg.rsmod.plugins.content.objs.watersources

data class Source(val obj: Int, val name: String)
data class FillableItem(val previous: Int, val next: Int, val name: String)

val sources = setOf(
        Source(Objs.SINK_14868, "sink"),
        Source(Objs.FOUNTAIN_879, "fountain")
)

val mappings = setOf(
        FillableItem(Items.BUCKET, Items.BUCKET_OF_WATER, "bucket"),
        FillableItem(Items.BOWL, Items.BOWL_OF_WATER, "bowl"),
        FillableItem(Items.JUG, Items.JUG_OF_WATER, "jug")
)

for (source in sources) {
    for (item in mappings) {
        on_item_on_obj(source.obj, item.previous) {
            player.inventory.remove(item.previous)
            player.inventory.add(item.next)
            player.filterableMessage("You fill the ${item.name} from the ${source.name}.")
        }
    }
}
