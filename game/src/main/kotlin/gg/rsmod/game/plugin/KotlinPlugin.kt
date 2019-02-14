package gg.rsmod.game.plugin

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.service.game.NpcStatsService
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(displayName = "Kotlin Plugin", fileExtension = "kts")
abstract class KotlinPlugin(private val r: PluginRepository, val world: World) {

    private val npcSpawns = ObjectArrayList<Npc>()

    private val objSpawns = ObjectArrayList<DynamicObject>()

    private val itemSpawns = ObjectArrayList<GroundItem>()

    private val npcCombatDefs = Int2ObjectOpenHashMap<NpcCombatDef>()

    internal fun spawnEntities() {
        npcSpawns.forEach { npc -> world.spawn(npc) }
        npcSpawns.clear()

        objSpawns.forEach { obj -> world.spawn(obj) }
        objSpawns.clear()

        itemSpawns.forEach { item -> world.spawn(item) }
        itemSpawns.clear()
    }

    internal fun overrideNpcCombatDefs() {
        if (npcCombatDefs.isEmpty()) {
            return
        }
        world.getService(NpcStatsService::class.java).ifPresent { s ->
            npcCombatDefs.forEach { npc, def -> s.set(npc, def) }
        }
    }

    fun combat_def(npc: Int, def: NpcCombatDef) {
        check(!npcCombatDefs.containsKey(npc)) { "Npc combat definition has been previously set: $npc" }
        npcCombatDefs[npc] = def
    }

    fun combat_def(npc: Int, vararg others: Int, def: NpcCombatDef) {
        combat_def(npc, def)
        others.forEach { other -> combat_def(other, def) }
    }

    fun spawn_npc(npc: Int, x: Int, z: Int, height: Int = 0, walkRadius: Int = 0, direction: Direction = Direction.SOUTH) {
        val n = Npc(npc, Tile(x, z, height), world)
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        npcSpawns.add(n)
    }

    fun spawn_object(obj: Int, x: Int, z: Int, height: Int = 0, type: Int = 10, rot: Int = 0) {
        val o = DynamicObject(obj, type, rot, Tile(x, z, height))
        objSpawns.add(o)
    }

    fun spawn_item(item: Int, amount: Int, x: Int, z: Int, height: Int = 0, respawnCycles: Int = 50) {
        val ground = GroundItem(item, amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        itemSpawns.add(ground)
    }

    fun on_item_option(item: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ItemDef::class.java, item)
        val slot = def.inventoryMenu.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $item [options=${def.inventoryMenu.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(item, slot, plugin)
    }

    fun on_object_option(obj: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ObjectDef::class.java, obj)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for object $obj [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot, plugin)
    }

    fun on_npc_option(npc: Int, option: String, plugin: Function1<Plugin, Unit>) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(NpcDef::class.java, npc)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $npc [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot, plugin)
    }

    fun on_world_init(plugin: (Plugin) -> Unit) = r.bindWorldInit(plugin)

    fun on_display_change(plugin: Function1<Plugin, Unit>) = r.bindDisplayModeChange(plugin)

    fun on_login(plugin: Function1<Plugin, Unit>) = r.bindLogin(plugin)

    fun on_logout(plugin: Function1<Plugin, Unit>) = r.bindLogout(plugin)

    fun on_combat(plugin: Function1<Plugin, Unit>) = r.bindCombat(plugin)

    fun on_npc_combat(npc: Int, vararg others: Int, plugin: Function1<Plugin, Unit>) {
        r.bindNpcCombat(npc, plugin)
        others.forEach { other -> r.bindNpcCombat(other, plugin) }
    }

    fun on_spell_on_npc(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) = r.bindSpellOnNpc(parent, child, plugin)

    fun on_interface_close(interfaceId: Int, plugin: Function1<Plugin, Unit>) = r.bindInterfaceClose(interfaceId, plugin)

    fun on_button(parent: Int, child: Int, plugin: Function1<Plugin, Unit>) = r.bindButton(parent, child, plugin)

    fun on_timer(key: TimerKey, plugin: Function1<Plugin, Unit>) = r.bindTimer(key, plugin)

    fun on_global_npc_spawn(plugin: Function1<Plugin, Unit>) = r.bindGlobalNpcSpawn(plugin)

    fun on_npc_spawn(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindNpcSpawn(npc, plugin)

    fun on_client_cheat(command: String, powerRequired: String? = null, plugin: Function1<Plugin, Unit>) = r.bindCommand(command, powerRequired, plugin)

    fun on_equip_to_slot(equipSlot: Int, plugin: Function1<Plugin, Unit>) = r.bindEquipSlot(equipSlot, plugin)

    fun can_equip_item(item: Int, plugin: Function1<Plugin, Boolean>) = r.bindEquipItemRequirement(item, plugin)

    fun on_item_equip(item: Int, plugin: Function1<Plugin, Unit>) = r.bindEquipItem(item, plugin)

    fun on_item_unequip(item: Int, plugin: Function1<Plugin, Unit>) = r.bindUnequipItem(item, plugin)

    fun on_enter_region(regionId: Int, plugin: Function1<Plugin, Unit>) = r.bindRegionEnter(regionId, plugin)

    fun on_exit_region(regionId: Int, plugin: Function1<Plugin, Unit>) = r.bindRegionExit(regionId, plugin)

    fun on_enter_chunk(chunkHash: Int, plugin: Function1<Plugin, Unit>) = r.bindChunkEnter(chunkHash, plugin)

    fun on_exit_chunk(chunkHash: Int, plugin: Function1<Plugin, Unit>) = r.bindChunkExit(chunkHash, plugin)

    fun on_item_option(item: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindItem(item, opt, plugin)

    fun on_object_option(obj: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindObject(obj, opt, plugin)

    fun on_npc_option(npc: Int, opt: Int, plugin: Function1<Plugin, Unit>) = r.bindNpc(npc, opt, plugin)

    fun set_custom_object_path(obj: Int, plugin: Function1<Plugin, Unit>) = r.bindCustomObjectPath(obj, plugin)

    fun set_custom_npc_path(npc: Int, plugin: Function1<Plugin, Unit>) = r.bindCustomNpcPath(npc, plugin)
}