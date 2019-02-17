package gg.rsmod.plugins.osrs.content.inter.follower

import gg.rsmod.plugins.osrs.api.ext.player

on_button(interfaceId = 387, component = 23) {
    it.player().message("You do not have a follower.")
}