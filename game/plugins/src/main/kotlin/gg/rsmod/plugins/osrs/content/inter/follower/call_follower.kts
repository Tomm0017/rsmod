package gg.rsmod.plugins.osrs.content.inter.follower

import gg.rsmod.plugins.osrs.api.ext.player

onButton(parent = 387, child = 23) {
    it.player().message("You do not have a follower.")
}