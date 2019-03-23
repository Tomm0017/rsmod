
package gg.rsmod.plugins.content.skills.thieving

import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.cfg.Npcs
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

/**
 * @ids = an array of NPC ids for them to pickpocket
 * @exp = the amount of experience given per pickpocket
 * @lvl = the level requirement to pickpocket that npc
 * @npcName = the name of the NPC for the chat messages
 * @rewards = a 2d array of rewards
 */
enum class NpcTargetType(val ids: IntArray, val exp: Double, val lvl: Int, val npcName: String, val rewards: Array<Item>, val damage: IntRange) {
    MAN(
            ids = intArrayOf(Npcs.MAN_3078, Npcs.MAN_3079, Npcs.MAN_3080, Npcs.MAN_3081, Npcs.MAN_3082),
            exp = 8.0,
            lvl = 1,
            npcName = "Man",
            rewards = arrayOf(Item(995,3)),
            damage = 1..1
    ),
    VARROCK_GUARD (
            ids = intArrayOf(Npcs.GUARD_3094),
            exp = 46.8,
            lvl = 40,
            npcName = "Guard",
            rewards = arrayOf(Item(995,10), Item(995,25)),
            damage = 2..2
    );
}

/**
 * A method to get a percentage chance to successfully complete an action
 * given the players level and requrired level
 * @pLevel = the player's Level in a skill
 * @reqLevel = the required level to do the action
 */
fun successChance(pLevel: Int, reqLevel: Int): Int{
    var rNum: Double = Random.nextInt(pLevel..200)/2.0

    var chanceOfSuccess: Int = ((pLevel - reqLevel + Random.nextInt(30)) / rNum*100).roundToInt()
    println(chanceOfSuccess)
    return chanceOfSuccess
}