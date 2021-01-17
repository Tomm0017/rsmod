package gg.rsmod.plugins.content.skills.thieving.objs

Stall.values().forEach{ stall ->
    on_obj_option(stall.stallID, "Steal-from") {
        stall.steal(player)
    }
}