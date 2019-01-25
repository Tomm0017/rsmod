package gg.rsmod.game.plugin

import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.World
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(displayName = "Kotlin Plugin", fileExtension = "kts")
abstract class KotlinPlugin(private val r: PluginRepository, val world: World) {

    fun onObjectOption(obj: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ObjectDef::class.java, obj)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for object $obj [valid_options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot, plugin)
    }

    fun onNpcOption(npc: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(NpcDef::class.java, npc)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $npc [valid_options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot, plugin)
    }

    fun onDisplayModeChange(plugin: Function1<Plugin, Unit>) = r.bindDisplayModeChange(plugin)

    fun onLogin(plugin: Function1<Plugin, Unit>) = r.bindLogin(plugin)

    fun onCombat(plugin: Function1<Plugin, Unit>) = r.bindCombat(plugin)

    fun onNpcCombat(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindNpcCombat(npc, plugin)

    fun onSpellOnNpc(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) = r.bindSpellOnNpc(parent, child, plugin)

    fun onInterfaceClose(interfaceId: Int, plugin: Function1<Plugin, Unit>) = r.bindInterfaceClose(interfaceId, plugin)

    fun onButton(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) = r.bindButton(parent, child, plugin)

    fun onTimer(key: TimerKey, plugin: Function1<Plugin, Unit>) = r.bindTimer(key, plugin)

    fun onAnyNpcSpawn(plugin: Function1<Plugin, Unit>) = r.bindGlobalNpcSpawn(plugin)

    fun onNpcSpawn(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindNpcSpawn(npc, plugin)

    fun onCommand(command: String, powerRequired: String? = null, plugin: Function1<Plugin, Unit>) = r.bindCommand(command, powerRequired, plugin)

    fun onEquipSlot(equipSlot: Int, plugin: Function1<Plugin, Unit>) = r.bindEquipSlot(equipSlot, plugin)

    fun canEquipItem(item: Int, plugin: Function1<Plugin, Boolean>) = r.bindEquipItemRequirement(item, plugin)

    fun onItemEquip(item: Int, plugin: Function1<Plugin, Unit>) = r.bindEquipItem(item, plugin)

    fun onItemUnequip(item: Int, plugin: Function1<Plugin, Unit>) = r.bindUnequipItem(item, plugin)

    fun onRegionEnter(regionId: Int, plugin: Function1<Plugin, Unit>) = r.bindRegionEnter(regionId, plugin)

    fun onRegionExit(regionId: Int, plugin: Function1<Plugin, Unit>) = r.bindRegionExit(regionId, plugin)

    fun onChunkEnter(chunkHash: Int, plugin: Function1<Plugin, Unit>) = r.bindChunkEnter(chunkHash, plugin)

    fun onChunkExit(chunkHash: Int, plugin: Function1<Plugin, Unit>) = r.bindChunkExit(chunkHash, plugin)

    fun onItemOption(item: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindItem(item, opt, plugin)

    fun onObjectOption(obj: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindObject(obj, opt, plugin)

    fun onNpcOption(npc: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindNpc(npc, opt, plugin)

    fun setCustomObjectPath(obj: Int, plugin: Function1<Plugin, Unit>) = r.bindCustomObjectPath(obj, plugin)

    fun setCustomNpcPath(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindCustomNpcPath(npc, plugin)
}