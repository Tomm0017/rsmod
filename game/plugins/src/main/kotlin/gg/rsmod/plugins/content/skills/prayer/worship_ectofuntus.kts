package gg.rsmod.plugins.content.skills.prayer

/**
 * @author Misterbaho <MisterBaho#6447>
 */

on_obj_option(obj = Objs.ECTOFUNTUS, option = "worship") {
    player.queue {
        Prayer.worship(this)
    }
}