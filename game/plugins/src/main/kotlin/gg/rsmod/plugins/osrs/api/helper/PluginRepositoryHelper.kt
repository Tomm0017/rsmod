package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.plugin.PluginRepository

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun PluginRepository.bindObjOption(obj: Int, option: String, plugin: Function1<Plugin, Unit>) {
    val lowercase = option.toLowerCase()
    val def = world.definitions.get(ObjectDef::class.java, obj)
    val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == lowercase }

    check(slot != -1) { "Option \"$option\" not found for object $obj [valid_options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

    bindObject(obj, slot, plugin)
}

fun PluginRepository.bindNpcOption(npc: Int, option: String, plugin: Function1<Plugin, Unit>) {
    val lowercase = option.toLowerCase()
    val def = world.definitions.get(NpcDef::class.java, npc)
    val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == lowercase }

    check(slot != -1) { "Option \"$option\" not found for npc $npc [valid_options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

    bindNpc(npc, slot, plugin)
}