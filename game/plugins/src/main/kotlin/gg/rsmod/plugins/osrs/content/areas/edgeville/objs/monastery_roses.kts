package gg.rsmod.plugins.osrs.content.areas.edgeville.objs

import gg.rsmod.plugins.osrs.api.ext.player

for (roses in 9260..9262) {
    on_obj_option(obj = roses, option = "take-seed") {
        it.player().message("There doesn't seem to be any seeds on this rosebush.")
    }
}