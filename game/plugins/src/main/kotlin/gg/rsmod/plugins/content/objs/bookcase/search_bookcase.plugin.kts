package gg.rsmod.plugins.content.objs.bookcase

private val BOOKCASES = setOf(Objs.BOOKCASE_380, Objs.BOOKCASE_381)

BOOKCASES.forEach { case ->
    on_obj_option(obj = case, option = "search") {
        player.queue {
            search(this, player)
        }
    }
}

suspend fun search(it: QueueTask, p: Player) {
    p.lock()
    p.message("You search the books...")
    it.wait(3)
    p.unlock()
    p.message("You don't find anything that you'd ever want to read.")
}