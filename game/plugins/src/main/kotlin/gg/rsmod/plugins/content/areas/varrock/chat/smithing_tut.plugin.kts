package gg.rsmod.plugins.content.areas.varrock.chat

spawn_npc(npc = Npcs.MASTER_SMITHING_TUTOR, x = 3188, z = 3423, walkRadius = 1, direction = Direction.NORTH)

on_npc_option(npc = Npcs.MASTER_SMITHING_TUTOR, option = "talk-to") {
    player.queue { dialog(this) }
}

suspend fun dialog(it: QueueTask) {
    it.chatPlayer("Any advice for an advance smith?", animation = 567)
    it.chatNpc("As you get better you'll find you will be able to smith Mithril and eventually Adamantite and even Runite. This can be very lucrative but very expensive on the coal front. It may be worth you stockpiling coal for a", animation = 588)
    it.chatNpc("while before attempting these difficult metals or even sticking to good old reliable iron by the bucket load.", animation = 588)
}