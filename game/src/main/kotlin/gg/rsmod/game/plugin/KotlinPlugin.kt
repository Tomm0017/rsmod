package gg.rsmod.game.plugin

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.game.model.container.key.ContainerKey
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
 * Represents a KotlinScript plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(
        displayName = "Kotlin Plugin",
        fileExtension = "kts",
        compilationConfiguration = KotlinPluginConfiguration::class
)
abstract class KotlinPlugin(private val r: PluginRepository, val world: World) {

    /**
     * Set the [gg.rsmod.game.model.region.ChunkCoords] with [chunk] as its
     * [gg.rsmod.game.model.region.ChunkCoords.hashCode], as a multi-combat area.
     */
    fun set_multi_combat_chunk(chunk: Int) {
        r.multiCombatChunks.add(chunk)
    }

    /**
     * Set the 8x8 [gg.rsmod.game.model.region.ChunkCoords]s that belong to [region]
     * as multi-combat areas.
     */
    fun set_multi_combat_region(region: Int) {
        r.multiCombatRegions.add(region)
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc].
     */
    fun set_combat_def(npc: Int, def: NpcCombatDef) {
        check(!r.npcCombatDefs.containsKey(npc)) { "Npc combat definition has been previously set: $npc" }
        r.npcCombatDefs[npc] = def
    }

    /**
     * Set the [NpcCombatDef] for npcs with [Npc.id] of [npc] and [others].
     */
    fun set_combat_def(npc: Int, vararg others: Int, def: NpcCombatDef) {
        set_combat_def(npc, def)
        others.forEach { other -> set_combat_def(other, def) }
    }

    /**
     * Create a [Shop] in our world.
     */
    fun create_shop(name: String, currency: ShopCurrency, stockType: StockType = StockType.NORMAL,
                    stockSize: Int = Shop.DEFAULT_STOCK_SIZE, purchasePolicy: PurchasePolicy = PurchasePolicy.BUY_TRADEABLES,
                    init: Shop.() -> Unit) {
        val shop = Shop(name, stockType, purchasePolicy, currency, arrayOfNulls(stockSize))
        r.shops[name] = shop
        init(shop)
    }

    /**
     * Create a [ContainerKey] to register to the [World] for serialization
     * later on.
     */
    fun register_container_key(key: ContainerKey) {
        r.containerKeys.add(key)
    }

    /**
     * Spawn an [Npc] on the given coordinates.
     */
    fun spawn_npc(npc: Int, x: Int, z: Int, height: Int = 0, walkRadius: Int = 0, direction: Direction = Direction.SOUTH) {
        val n = Npc(npc, Tile(x, z, height), world)
        n.respawns = true
        n.walkRadius = walkRadius
        n.lastFacingDirection = direction
        r.npcSpawns.add(n)
    }

    /**
     * Spawn a [DynamicObject] on the given coordinates.
     */
    fun spawn_obj(obj: Int, x: Int, z: Int, height: Int = 0, type: Int = 10, rot: Int = 0) {
        val o = DynamicObject(obj, type, rot, Tile(x, z, height))
        r.objSpawns.add(o)
    }

    /**
     * Spawn a [GroundItem] on the given coordinates.
     */
    fun spawn_item(item: Int, amount: Int, x: Int, z: Int, height: Int = 0, respawnCycles: Int = GroundItem.DEFAULT_RESPAWN_CYCLES) {
        val ground = GroundItem(item, amount, Tile(x, z, height))
        ground.respawnCycles = respawnCycles
        r.itemSpawns.add(ground)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an inventory
     * [gg.rsmod.game.model.item.Item].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_item_option(item: Int, option: String, logic: (Plugin).() -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ItemDef::class.java, item)
        val slot = def.inventoryMenu.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for item $item [options=${def.inventoryMenu.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindItem(item, slot + 1, logic)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on a
     * [gg.rsmod.game.model.entity.GameObject].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_obj_option(obj: Int, option: String, lineOfSightDistance: Int = -1, logic: (Plugin).() -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ObjectDef::class.java, obj)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for object $obj [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindObject(obj, slot + 1, lineOfSightDistance, logic)
    }

    /**
     * Invoke [logic] when the [option] option is clicked on an [Npc].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_npc_option(npc: Int, option: String, lineOfSightDistance: Int = -1, logic: (Plugin).() -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(NpcDef::class.java, npc)
        val slot = def.options.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for npc $npc [options=${def.options.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindNpc(npc, slot + 1, lineOfSightDistance, logic)
    }

    /**
     * Invoke [logic] when [option] option is clicked on a [GroundItem].
     *
     * This method should be used over the option-int variant whenever possible.
     */
    fun on_ground_item_option(item: Int, option: String, logic: (Plugin).() -> Unit) {
        val opt = option.toLowerCase()
        val def = world.definitions.get(ItemDef::class.java, item)
        val slot = def.groundMenu.filterNotNull().indexOfFirst { it.toLowerCase() == opt }

        check(slot != -1) { "Option \"$option\" not found for ground item $item [options=${def.groundMenu.filterNotNull().filter { it.isNotBlank() }}]" }

        r.bindGroundItem(item, slot + 1, logic)
    }

    /**
     * Set the logic to execute when [gg.rsmod.game.message.impl.WindowStatusMessage]
     * is handled.
     */
    fun set_window_status_logic(logic: (Plugin).() -> Unit) = r.bindWindowStatus(logic)

    /**
     * Set the logic to execute when [gg.rsmod.game.message.impl.CloseModalMessage]
     * is handled.
     */
    fun set_modal_close_logic(logic: (Plugin).() -> Unit) = r.bindModalClose(logic)

    /**
     * Set the logic to execute by default when [gg.rsmod.game.model.entity.Pawn.attack]
     * is handled.
     */
    fun set_combat_logic(logic: (Plugin).() -> Unit) = r.bindCombat(logic)

    /**
     * Set the logic to execute when a player levels a skill.
     */
    fun set_level_up_logic(logic: (Plugin).() -> Unit) = r.bindSkillLevelUp(logic)

    /**
     * Invoke [logic] when [World.postLoad] is handled.
     */
    fun on_world_init(logic: (Plugin).() -> Unit) = r.bindWorldInit(logic)

    /**
     * Invoke [logic] on player log in.
     */
    fun on_login(logic: (Plugin).() -> Unit) = r.bindLogin(logic)

    /**
     * Invoke [logic] on player log out.
     */
    fun on_logout(logic: (Plugin).() -> Unit) = r.bindLogout(logic)

    /**
     * Set the combat logic for [npc] and [others], which will override the [set_combat_logic]
     * logic.
     */
    fun on_npc_combat(npc: Int, vararg others: Int, logic: (Plugin).() -> Unit) {
        r.bindNpcCombat(npc, logic)
        others.forEach { other -> r.bindNpcCombat(other, logic) }
    }

    /**
     * Invoke [logic] when [gg.rsmod.game.message.impl.OpNpcTMessage] is handled.
     */
    fun on_spell_on_npc(parent: Int, child: Int, logic: (Plugin).() -> Unit) = r.bindSpellOnNpc(parent, child, logic)

    /**
     * Invoke [logic] when [gg.rsmod.game.message.impl.IfOpenSubMessage] is handled.
     */
    fun on_interface_open(interfaceId: Int, logic: (Plugin).() -> Unit) = r.bindInterfaceOpen(interfaceId, logic)

    /**
     * Invoke [logic] when [gg.rsmod.game.model.interf.InterfaceSet.closeByHash]
     * is handled.
     */
    fun on_interface_close(interfaceId: Int, logic: (Plugin).() -> Unit) = r.bindInterfaceClose(interfaceId, logic)

    /**
     * Invoke [logic] when [gg.rsmod.game.message.impl.IfButtonMessage] is handled.
     */
    fun on_button(interfaceId: Int, component: Int, logic: (Plugin).() -> Unit) = r.bindButton(interfaceId, component, logic)

    /**
     * Invoke [logic] when [key] reaches a time value of 0.
     */
    fun on_timer(key: TimerKey, logic: (Plugin).() -> Unit) = r.bindTimer(key, logic)

    /**
     * Invoke [logic] when any npc is spawned into the game with [World.spawn].
     */
    fun on_global_npc_spawn(logic: (Plugin).() -> Unit) = r.bindGlobalNpcSpawn(logic)

    /**
     * Invoke [logic] when a ground item is picked up by a [gg.rsmod.game.model.entity.Player].
     */
    fun on_global_item_pickup(logic: Plugin.() -> Unit) = r.bindGlobalGroundItemPickUp(logic)

    /**
     * Invoke [logic] when an npc with [Npc.id] matching [npc] is spawned into
     * the game with [World.spawn].
     */
    fun on_npc_spawn(npc: Int, logic: (Plugin).() -> Unit) = r.bindNpcSpawn(npc, logic)

    /**
     * Invoke [logic] when [gg.rsmod.game.message.impl.ClientCheatMessage] is handled.
     */
    fun on_command(command: String, powerRequired: String? = null, logic: (Plugin).() -> Unit) = r.bindCommand(command, powerRequired, logic)

    /**
     * Invoke [logic] when an item is equipped onto equipment slot [equipSlot].
     */
    fun on_equip_to_slot(equipSlot: Int, logic: (Plugin).() -> Unit) = r.bindEquipSlot(equipSlot, logic)

    /**
     * Return true if [item] can be equipped, false if it can't.
     */
    fun can_equip_item(item: Int, logic: (Plugin).() -> Boolean) = r.bindEquipItemRequirement(item, logic)

    /**
     * Invoke [logic] when [item] is equipped.
     */
    fun on_item_equip(item: Int, logic: (Plugin).() -> Unit) = r.bindEquipItem(item, logic)

    /**
     * Invoke [logic] when [item] is removed from equipment.
     */
    fun on_item_unequip(item: Int, logic: (Plugin).() -> Unit) = r.bindUnequipItem(item, logic)

    /**
     * Invoke [logic] when a player enters a region (8x8 Chunks).
     */
    fun on_enter_region(regionId: Int, logic: (Plugin).() -> Unit) = r.bindRegionEnter(regionId, logic)

    /**
     * Invoke [logic] when a player exits a region (8x8 Chunks).
     */
    fun on_exit_region(regionId: Int, logic: (Plugin).() -> Unit) = r.bindRegionExit(regionId, logic)

    /**
     * Invoke [logic] when a player enters a chunk (8x8 Tiles).
     */
    fun on_enter_chunk(chunkHash: Int, logic: (Plugin).() -> Unit) = r.bindChunkEnter(chunkHash, logic)

    /**
     * Invoke [logic] when a player exits a chunk (8x8 Tiles).
     */
    fun on_exit_chunk(chunkHash: Int, logic: (Plugin).() -> Unit) = r.bindChunkExit(chunkHash, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an inventory item.
     *
     * [on_item_option] method should be used over this method whenever possible.
     */
    fun on_item_option(item: Int, option: Int, logic: (Plugin).() -> Unit) = r.bindItem(item, option, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on a
     * [gg.rsmod.game.model.entity.GameObject].
     *
     * [on_obj_option] method should be used over this method whenever possible.
     */
    fun on_obj_option(obj: Int, option: Int, lineOfSightDistance: Int = -1, logic: (Plugin).() -> Unit) = r.bindObject(obj, option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on an [Npc].
     *
     * [on_npc_option] method should be used over this method whenever possible.
     */
    fun on_npc_option(npc: Int, option: Int, lineOfSightDistance: Int = -1, logic: (Plugin).() -> Unit) = r.bindNpc(npc, option, lineOfSightDistance, logic)

    /**
     * Invoke [logic] when the the option in index [option] is clicked on a [GroundItem].
     *
     * [on_ground_item_option] method should be used over this method whenever possible.
     */
    fun on_ground_item_option(item: Int, option: Int, logic: (Plugin).() -> Unit) = r.bindGroundItem(item, option, logic)

    /**
     * Returns true if the item can be dropped on the floor via the 'drop' menu
     * option - return false otherwise.
     */
    fun can_drop_item(item: Int, plugin: (Plugin).() -> Boolean) = r.bindCanItemDrop(item, plugin)
}