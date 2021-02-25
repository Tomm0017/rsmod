package gg.rsmod.plugins.content.areas.motherlode

import gg.rsmod.game.model.MovementQueue.StepType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeKey
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.TimeConstants
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.getVarbit
import gg.rsmod.plugins.api.ext.incrementVarbit
import gg.rsmod.plugins.api.ext.message
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

data class Floater(val npc: Npc, val amount: Int)

object WaterCircuit {
    val SACK_COUNT_VARBIT = 5558

    val PENDING_ORES_ATTR: AttributeKey<ArrayList<Int>> = AttributeKey("pending-ores")

    var WHEELS_RUNNING = 0

    var FLOATERS = arrayListOf<Floater>()

    val END_TILE = Tile(3748, 5660)

    val water_spots_1wheel = arrayOf( DynamicObject(2018, 10, 2, Tile(3744, 5672)),
            DynamicObject(2018, 10, 3, Tile(3748, 5671)),
            DynamicObject(2018, 10, 0, Tile(3748, 5660)),
            DynamicObject(2018, 10, 0, Tile(3744, 5660)))

    fun run_water_wheel(world: World, strut: GameObject) {
        val still_wheel = world.getObject(Tile(strut.tile.x+1, strut.tile.z-1), 10)!!
        world.queue {
            val fixed_strut = DynamicObject(strut, Objs.STRUT)
            val running_wheel = DynamicObject(still_wheel, Objs.WATER_WHEEL_26671)
            val water_flow = DynamicObject(2016, 10, 1, Tile(running_wheel.tile.x, running_wheel.tile.z+3))
            world.remove(strut)
            world.spawn(fixed_strut)
            world.remove(still_wheel)
            world.spawn(running_wheel)
            world.spawn(water_flow)
            WHEELS_RUNNING++
            refreshFlow(world)
            val stops = Random.nextInt(80, 200)
            if(world.devContext.debugObjects){
                world.players.forEach { p ->
                    p.message("wheel stops in ${TimeConstants.cyclesContext(stops)}")
                }
            }
            wait(stops)
            world.remove(fixed_strut)
            world.spawn(DynamicObject(fixed_strut, Objs.BROKEN_STRUT))
            world.remove(running_wheel)
            world.spawn(DynamicObject(running_wheel, Objs.WATER_WHEEL_26672))
            world.remove(water_flow)
            WHEELS_RUNNING--
            refreshFlow(world)
        }
    }

    fun refreshFlow(world: World) {
        when(WHEELS_RUNNING){
            0 -> {
                stop()
                water_spots_1wheel.forEach { waterspot ->
                    world.remove(waterspot)
                }
            }
            1 -> {
                start()
                water_spots_1wheel.forEach { waterspot ->
                    world.spawn(waterspot)
                }
            }
        }
    }

    fun queueFloatPath(floater: Floater) {
        val npc = floater.npc
        val player = npc.owner!!
        val world = npc.world
        val diff = npc.tile.z - END_TILE.z
        val path = ArrayDeque<Tile>()
        for(i in 1..diff)
            path.add(Tile(END_TILE.x, npc.tile.z-i))
        npc.walkPath(path, StepType.NORMAL, false)
        world.players.forEach { p ->
            p.message("floating commenced!")
        }
        npc.queue {
            while(npc.tile != END_TILE)
                wait(2)
            fillSack(player, world, npc, floater.amount)
        }
    }

    fun float(world: World, player: Player, npc: Npc, amount: Int) {
        world.spawn(npc)
        npc.owner = player
        val floater = Floater(npc, amount)
        FLOATERS.add(floater)
        queueFloatPath(floater)
    }

    fun fillSack(player: Player, world: World, npc: Npc, amount: Int) {
        val currentAmount = player.getVarbit(SACK_COUNT_VARBIT)
        if(currentAmount+amount > 81){
            player.message("the sack couldn't fit them all")
            val space = 81 - currentAmount
            player.incrementVarbit(SACK_COUNT_VARBIT, space)
            player.queue {
                while(player.getVarbit(SACK_COUNT_VARBIT) == 81 || WHEELS_RUNNING == 0)
                    wait(4)
                fillSack(player, world, npc, amount-space)
            }
        } else{
            player.incrementVarbit(SACK_COUNT_VARBIT, amount)
            world.remove(npc)
        }
    }

    fun start() {
        FLOATERS.forEach { floater ->
            queueFloatPath(floater)
        }
    }

    fun stop() {
        FLOATERS.forEach { floater ->
            floater.npc.interruptQueues()
        }
    }
}