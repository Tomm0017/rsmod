package gg.rsmod.plugins.content.inter.currenttime

import java.util.*

val time = TimerKey()

on_login {
    setClanTime(player = player)
}

on_timer(time) {
    setClanTime(player = player)
}

fun setClanTime(player : Player) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val second = calendar.get(Calendar.SECOND)
    player.timers[time] = 60 - second
    player.setVarbit(id = 8354, value = ((hour * 60) + minute))
}