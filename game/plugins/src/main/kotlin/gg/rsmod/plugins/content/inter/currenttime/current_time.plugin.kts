package gg.rsmod.plugins.content.inter.currenttime

import gg.rsmod.game.model.interf.DisplayMode
import java.util.Calendar

DisplayMode.values.forEach { mode ->
    val child = when (mode) {
        DisplayMode.RESIZABLE_NORMAL -> 35
        DisplayMode.RESIZABLE_LIST -> 35
        DisplayMode.FIXED -> 31
        else -> return@forEach
    }
    on_button(interfaceId = getDisplayComponentId(mode), component = child) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        player.setVarbit(id = 8354, value = ((hour * 60) + minute))
    }
}