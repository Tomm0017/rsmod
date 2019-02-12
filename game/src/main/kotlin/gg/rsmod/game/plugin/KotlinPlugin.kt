package gg.rsmod.game.plugin

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(displayName = "Kotlin Plugin", fileExtension = "kts")
abstract class KotlinPlugin(private val r: PluginRepository, val world: World) {

    private val npcSpawns = arrayListOf<Npc>()

    private val objSpawns = arrayListOf<DynamicObject>()

    private val itemSpawns = arrayListOf<GroundItem>()

    internal fun handleSpawns() {
        npcSpawns.forEach { npc -> world.spawn(npc) }
        npcSpawns.clear()

        objSpawns.forEach { obj -> world.spawn(obj) }
        objSpawns.clear()

        itemSpawns.forEach { item -> world.spawn(item) }
        itemSpawns.clear()
    }

    fun spawnNpc(npc: Int, x: Int, z: Int, height: Int = 0, walkRadius: Int = 0, direction: Direction = Direction.SOUTH) {
        val n = Npc(npc, Tile(x, z, height), world)
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        npcSpawns.add(n)
    }

    fun spawnObj(obj: Int, x: Int, z: Int, height: Int = 0, type: Int = 10, rot: Int = 0) {
        val o = DynamicObject(obj, type, rot, Tile(x, z, height))
        objSpawns.add(o)
    }

    fun spawnItem(item: Int, amount: Int, x: Int, z: Int, height: Int = 0, respawnCycles: Int = 50) {
        val ground = GroundItem(item, amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        itemSpawns.add(ground)
    }

    fun onItemOption(item: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ItemDef::class.java, item)
        val slot = def.inventoryMenu.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $item [options=${def.inventoryMenu.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(item, slot, plugin)
    }

    fun onObjectOption(obj: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ObjectDef::class.java, obj)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for object $obj [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot, plugin)
    }

    fun onNpcOption(npc: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(NpcDef::class.java, npc)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $npc [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot, plugin)
    }

    fun onWorldInit(plugin: (Plugin) -> Unit) = r.bindWorldInit(plugin)

    fun onDisplayModeChange(plugin: Function1<Plugin, Unit>) = r.bindDisplayModeChange(plugin)

    fun onLogin(plugin: Function1<Plugin, Unit>) = r.bindLogin(plugin)

    fun onLogout(plugin: Function1<Plugin, Unit>) = r.bindLogout(plugin)

    fun onCombat(plugin: Function1<Plugin, Unit>) = r.bindCombat(plugin)

    fun onNpcCombat(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindNpcCombat(npc, plugin)

    fun onSpellOnNpc(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) = r.bindSpellOnNpc(parent, child, plugin)

    fun onComponentClose(component: Int, plugin: Function1<Plugin, Unit>) = r.bindComponentClose(component, plugin)

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