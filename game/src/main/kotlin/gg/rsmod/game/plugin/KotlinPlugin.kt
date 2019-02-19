package gg.rsmod.game.plugin

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.shop.PurchasePolicy
import gg.rsmod.game.model.shop.Shop
import gg.rsmod.game.model.shop.ShopCurrency
import gg.rsmod.game.model.shop.StockType
import gg.rsmod.game.model.timer.TimerKey
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(displayName = "Kotlin Plugin", fileExtension = "kts")
abstract class KotlinPlugin(private val r: PluginRepository, val world: World) {

    fun set_multi_combat_chunk(chunk: Int) {
        r.multiCombatChunks.add(chunk)
    }

    fun set_multi_combat_region(region: Int) {
        r.multiCombatRegions.add(region)
    }

    fun set_combat_def(npc: Int, def: NpcCombatDef) {
        check(!r.npcCombatDefs.containsKey(npc)) { "Npc combat definition has been previously set: $npc" }
        r.npcCombatDefs[npc] = def
    }

    fun set_combat_def(npc: Int, vararg others: Int, def: NpcCombatDef) {
        set_combat_def(npc, def)
        others.forEach { other -> set_combat_def(other, def) }
    }

    fun create_shop(name: String, currency: ShopCurrency, stockType: StockType = StockType.GLOBAL,
                    stockSize: Int = Shop.DEFAULT_STOCK_SIZE, purchasePolicy: PurchasePolicy = PurchasePolicy.BUY_TRADEABLES,
                    init: Shop.() -> Unit) {
        val shop = Shop(name, stockType, purchasePolicy, currency, arrayOfNulls(stockSize))
        r.shops[name] = shop
        init(shop)
    }

    fun spawn_npc(npc: Int, x: Int, z: Int, height: Int = 0, walkRadius: Int = 0, direction: Direction = Direction.SOUTH) {
        val n = Npc(npc, Tile(x, z, height), world)
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        r.npcSpawns.add(n)
    }

    fun spawn_obj(obj: Int, x: Int, z: Int, height: Int = 0, type: Int = 10, rot: Int = 0) {
        val o = DynamicObject(obj, type, rot, Tile(x, z, height))
        r.objSpawns.add(o)
    }

    fun spawn_item(item: Int, amount: Int, x: Int, z: Int, height: Int = 0, respawnCycles: Int = 50) {
        val ground = GroundItem(item, amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    fun on_item_option(item: Int, option: String, plugin: (Plugin) -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ItemDef::class.java, item)
        val slot = def.inventoryMenu.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $item [options=${def.inventoryMenu.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindItem(item, slot + 1, plugin)
    }

    fun on_obj_option(obj: Int, option: String, lineOfSightDistance: Int = -1, plugin: (Plugin) -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ObjectDef::class.java, obj)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for object $obj [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot + 1, lineOfSightDistance, plugin)
    }

    fun on_npc_option(npc: Int, option: String, lineOfSightDistance: Int = -1, plugin: (Plugin) -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(NpcDef::class.java, npc)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $npc [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot + 1, lineOfSightDistance, plugin)
    }

    fun set_window_status_logic(plugin: (Plugin) -> Unit) = r.bindWindowStatus(plugin)

    fun set_modal_close_logic(plugin: (Plugin) -> Unit) = r.bindModalClose(plugin)

    fun set_combat_logic(plugin: (Plugin) -> Unit) = r.bindCombat(plugin)

    fun on_world_init(plugin: (Plugin) -> Unit) = r.bindWorldInit(plugin)

    fun on_login(plugin: (Plugin) -> Unit) = r.bindLogin(plugin)

    fun on_logout(plugin: (Plugin) -> Unit) = r.bindLogout(plugin)

    fun on_npc_combat(npc: Int, vararg others: Int, plugin: (Plugin) -> Unit) {
        r.bindNpcCombat(npc, plugin)
        others.forEach { other -> r.bindNpcCombat(other, plugin) }
    }

    fun on_spell_on_npc(parent: Int, child: Int, plugin: (Plugin) -> Unit) = r.bindSpellOnNpc(parent, child, plugin)

    fun on_interface_open(interfaceId: Int, logic: (Plugin) -> Unit) = r.bindInterfaceOpen(interfaceId, logic)

    fun on_interface_close(interfaceId: Int, plugin: (Plugin) -> Unit) = r.bindInterfaceClose(interfaceId, plugin)

    fun on_button(interfaceId: Int, component: Int, plugin: (Plugin) -> Unit) = r.bindButton(interfaceId, component, plugin)

    fun on_timer(key: TimerKey, plugin: (Plugin) -> Unit) = r.bindTimer(key, plugin)

    fun on_global_npc_spawn(plugin: (Plugin) -> Unit) = r.bindGlobalNpcSpawn(plugin)

    fun on_npc_spawn(npc: Int, plugin: (Plugin) -> Unit) = r.bindNpcSpawn(npc, plugin)

    fun on_command(command: String, powerRequired: String? = null, plugin: (Plugin) -> Unit) = r.bindCommand(command, powerRequired, plugin)

    fun on_equip_to_slot(equipSlot: Int, plugin: (Plugin) -> Unit) = r.bindEquipSlot(equipSlot, plugin)

    fun can_equip_item(item: Int, plugin: Function1<Plugin, Boolean>) = r.bindEquipItemRequirement(item, plugin)

    fun on_item_equip(item: Int, plugin: (Plugin) -> Unit) = r.bindEquipItem(item, plugin)

    fun on_item_unequip(item: Int, plugin: (Plugin) -> Unit) = r.bindUnequipItem(item, plugin)

    fun on_enter_region(regionId: Int, plugin: (Plugin) -> Unit) = r.bindRegionEnter(regionId, plugin)

    fun on_exit_region(regionId: Int, plugin: (Plugin) -> Unit) = r.bindRegionExit(regionId, plugin)

    fun on_enter_chunk(chunkHash: Int, plugin: (Plugin) -> Unit) = r.bindChunkEnter(chunkHash, plugin)

    fun on_exit_chunk(chunkHash: Int, plugin: (Plugin) -> Unit) = r.bindChunkExit(chunkHash, plugin)

    fun on_item_option(item: Int, opt: Int, plugin: (Plugin) -> Unit) = r.bindItem(item, opt, plugin)

    fun on_obj_option(obj: Int, option: Int, lineOfSightDistance: Int = -1, plugin: (Plugin) -> Unit) = r.bindObject(obj, option, lineOfSightDistance, plugin)

    fun on_npc_option(npc: Int, opt: Int, lineOfSightDistance: Int = -1, plugin: (Plugin) -> Unit) = r.bindNpc(npc, opt, lineOfSightDistance, plugin)
}