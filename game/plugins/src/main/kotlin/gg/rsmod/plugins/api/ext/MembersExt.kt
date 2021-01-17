package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.attr.MEMBERS_EXPIRES_ATTR
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.TimeConstants.DAY
import kotlin.math.roundToInt

fun Player.membersDaysLeft(): Int {
    val now = System.currentTimeMillis()
    val expires = attr.getOrDefault(MEMBERS_EXPIRES_ATTR, System.currentTimeMillis().toString()).toLong()
    return ((expires - now) / DAY.toDouble()).roundToInt()
}