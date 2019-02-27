package gg.rsmod.plugins.content.inter.follower

import gg.rsmod.plugins.api.ext.player

on_button(interfaceId = 387, component = 23) {
    player.message("You do not have a follower.")
}