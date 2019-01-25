import gg.rsmod.plugins.osrs.api.helper.player

onButton(parent = 387, child = 23) {
    it.player().message("You do not have a follower.")
}