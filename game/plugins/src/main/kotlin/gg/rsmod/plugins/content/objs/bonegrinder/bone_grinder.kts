package gg.rsmod.plugins.content.objs.bonegrinder

import gg.rsmod.plugins.content.skills.prayer.Bones

/**
 * @author Misterbaho <MisterBaho#6447>
 */

Bones.values.forEach { bones ->
    on_item_on_obj(item = bones.item, obj = Objs.LOADER) {
        player.queue {
            BoneGrinder.load(this, bones)
        }
    }
}

on_obj_option(obj = Objs.BONE_GRINDER_16655, option = "status") {
    player.queue {
        BoneGrinder.status(this)
    }
}

on_obj_option(obj = Objs.BONE_GRINDER_16655, option = "wind") {
    player.queue {
        BoneGrinder.crush(this)
    }
}

on_obj_option(obj = Objs.BIN, option = "empty") {
    player.queue {
        BoneGrinder.emptyBin(this)
    }
}