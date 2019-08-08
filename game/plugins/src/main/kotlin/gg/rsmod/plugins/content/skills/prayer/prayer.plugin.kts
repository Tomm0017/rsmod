package gg.rsmod.plugins.content.skills.prayer

val bones = Bone.values

private val altars = setOf(
    Objs.ALTAR_14860,
    Objs.ALTAR_409,
    Objs.ALTAR_2640,
    Objs.CHAOS_ALTAR_411
)

bones.forEach { bone ->
    on_item_option(item = bone.item, option = "bury") {
        player.queue {
            BoneBurying.bury(this, bone)
        }
    }
}

altars.forEach { altar ->
    bones.forEach { bone ->
        on_item_on_obj(obj = altar, item = bone.item) {
            player.queue {
                var obj = player.getInteractingGameObj()
                BoneBurying.bonesOnAltar(this, obj, bone)
            }
        }
        on_obj_option(obj = altar, option = "pray-at") {
            player.queue {
                player.lock()
                player.animate(645)
                wait(5)
                player.getSkills().restore(Skills.PRAYER)
                player.filterableMessage("Your Prayer Points Are Restored")
                player.unlock()
            }
        }
    }
}