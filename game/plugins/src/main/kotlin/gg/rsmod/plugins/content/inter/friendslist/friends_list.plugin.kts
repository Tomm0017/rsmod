package gg.rsmod.plugins.content.inter.friendslist

import gg.rsmod.game.message.impl.LoadFriendListMessage

/**
 * on_login send client player's friend list.
 *
 */
on_login { player.write(LoadFriendListMessage(online = 100,username =  player.username, previousUsername = player.username,world = 1)) }//Todo:make player loop through his friends list and send each friend name/previous name and status(online +which world)