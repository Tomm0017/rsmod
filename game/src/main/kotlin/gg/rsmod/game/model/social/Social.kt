package gg.rsmod.game.model.social

import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.entity.Player

import io.github.oshai.kotlinlogging.KotlinLogging
import java.nio.file.Files
import java.nio.file.Paths

class Social {

    private val friends = mutableListOf<String>()
    private val ignores = mutableListOf<String>()

    /**
     * @TODO
     * Add support for old display name current isn't
     * Need to create actl tutorial island for it -> So that player starts without a name then he can choose w.e he wants
     * And it does exist: displayName shit actl we can make it into String Array
     * [0] = Current one, And move [0] => [1] When user changes name, Just need verification on how many users does Gagex store.
     */
    fun pushFriends(player: Player) {
        if (friends.isEmpty()) {
            friends.add("TEMP_FIX_DONT_REMOVE")
        }
        val world = player.world

        if (friends.isEmpty()) {
            player.write(FriendListLoadedMessage())
        } else {
            friends.forEach {
                val user = world.getPlayerForName(it)
                if (user != null && !user.social.ignores.contains(player.username))
                    player.write(UpdateFriendListMessage(0, user.username, "", 304, 0, 0))
                else
                /**
                 * @TODO
                 * When implementing name change -> save old user name for [previousUsername]
                 * Actually forgot if it was a list of usernames or only latest
                 */
                player.write(UpdateFriendListMessage(0, it, "", 0, 0, 0))
            }
        }
    }

    /**
     * TODO Add support for old display name if current isn't previous/original one
     */
    fun pushIgnores(player: Player) {
        ignores.forEach {
            player.write(UpdateIgnoreListMessage(0, it, ""))
        }
    }

    fun addFriend(player: Player, name: String) {
        if (friends.contains(name))
            return
        val path = Paths.get("../data/saves/")
        val save = path.resolve(name)
        if (!Files.exists(save)) {
            player.writeMessage("Unable to add player; user with this username doesn't exist.")
            return
        }
        friends.add(name)
        pushFriends(player)
        updateStatus(player)
    }

    fun addIgnore(player: Player, name: String) {
        if (ignores.contains(name))
            return
        val path = Paths.get("../data/saves/")
        val save = path.resolve(name)
        if (!Files.exists(save)) {
            player.writeMessage("Unable to ignore player; user with this username doesn't exist.")
            return
        }
        ignores.add(name)
        pushIgnores(player)
        updateStatus(player)
    }

    fun deleteIgnore(player: Player, name: String) {
        ignores.remove(name)
        pushIgnores(player)
        updateStatus(player)
    }

    fun deleteFriend(player: Player, name: String) {
        friends.remove(name)
        pushFriends(player)
        updateStatus(player)
    }

    //TODO Add support for having private off/friends/etc...
    fun updateStatus(player: Player) {
        player.world.players.forEach {
            if (it == player)
                return@forEach
            if (it.social.ignores.contains(player.username))
                return@forEach
            if (it.social.friends.contains(player.username)) {
                it.social.pushFriends(it)
            }
        }
    }

    fun sendPrivateMessage(player: Player, target: Player, length: Int, message: ByteArray) {
        val decompressed = ByteArray(230)
        val huffman = player.world.huffman
        huffman.decompress(message, decompressed, length)
        val unpacked = String(decompressed, 0, length)

        logger.info { "${player.username} is attempting to message: ${target.username} with message: $unpacked" }

        target.write(MessagePrivateReceiverMessage(player.username, 255, 0, player.privilege.icon, "Testing"))
        player.write(MessagePrivateReceiverMessage(target.username, 255, -1, 0, "Testing"))

    }
    companion object {
        private val logger = KotlinLogging.logger{}
    }
}