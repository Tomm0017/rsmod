package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.model.COMMAND_ARGS_ATTR
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.*
import gg.rsmod.plugins.osrs.InterfacePane
import gg.rsmod.plugins.osrs.model.Skills
import gg.rsmod.util.Misc
import java.text.DecimalFormat

/**
 * @author Tom <rspsmods@gmail.com>
 */
object Commands {

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindCommand("empty") {
            it.player().inventory.removeAll()
        }

        r.bindCommand("tele", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::tele 3200 3200</col>") { values ->
                val x = values[0].toInt()
                val z = values[1].toInt()
                val height = if (values.size > 2) values[2].toInt() else 0
                p.teleport(x, z, height)
            }
        }

        r.bindCommand("npc", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
                val id = values[0].toInt()
                val npc = Npc(id, p.tile, p.world)
                p.world.spawn(npc)
            }
        }

        r.bindCommand("obj", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
                val id = values[0].toInt()
                val obj = DynamicObject(id, 10, 0, p.tile)
                p.world.spawn(obj)
            }
        }

        r.bindCommand("removeobj", Privilege.ADMIN_POWER) {
            val p = it.player()

            val chunk = p.world.chunks.getForTile(p.tile)
            val obj = chunk.getEntities<GameObject>(p.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
            if (obj != null) {
                p.world.remove(obj)
            } else {
                p.message("No object found in tile.")
            }
        }

        r.bindCommand("master", Privilege.ADMIN_POWER) {
            val p = it.player()

            for (i in 0 until p.getSkills().maxSkills) {
                p.getSkills().setBaseLevel(i, 99)
            }
            p.calculateAndSetCombatLevel()
        }

        r.bindCommand("reset", Privilege.ADMIN_POWER) {
            val p = it.player()

            for (i in 0 until p.getSkills().maxSkills) {
                p.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
            }
            p.calculateAndSetCombatLevel()
        }

        r.bindCommand("setlvl", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::setlvl 0 99</col> or <col=801700>::setlvl attack 99</col>") { values ->
                var skill: Int
                try {
                    skill = values[0].toInt()
                } catch (e: NumberFormatException) {
                    var name = values[0].toLowerCase()
                    when (name) {
                        "con" -> name = "construction"
                        "hp" -> name = "hitpoints"
                        "craft" -> name = "crafting"
                        "hunt" -> name = "hunter"
                        "slay" -> name = "slayer"
                        "pray" -> name = "prayer"
                        "mage" -> name = "magic"
                        "fish" -> name = "fishing"
                        "herb" -> name = "herblore"
                        "rc" -> name = "runecrafting"
                        "fm" -> name = "firemaking"
                    }
                    skill = Skills.getSkillForName(p.world, p.getSkills().maxSkills, name)
                }
                if (skill != -1) {
                    val level = values[1].toInt()
                    p.getSkills().setBaseLevel(skill, level)
                } else {
                    p.message("Could not find skill with identifier: ${values[0]}")
                }
            }
        }

        r.bindCommand("obank", Privilege.ADMIN_POWER) {
            val p = it.player()

            it.suspendable {
                p.openInterface(12, InterfacePane.MAIN_SCREEN)
                p.openInterface(15, InterfacePane.TAB_AREA)

                p.setInterfaceSetting(parent = 15, child = 3, range = 0..27, setting = 1181438)
                p.setInterfaceSetting(parent = 15, child = 10, range = 0..27, setting = 1054)

                it.interruptAction = { p.closeInterface(15) }
                it.waitInterfaceClose(12)
                it.interruptAction?.invoke(it)
            }
        }

        r.bindCommand("item", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>") { values ->
                val item = values[0].toInt()
                val amount = if (values.size > 1) Misc.parseAmount(values[1]) else 1
                if (item < p.world.definitions.getCount(ItemDef::class.java)) {
                    val def = p.world.definitions[ItemDef::class.java][Item(item).toUnnote(p.world.definitions).id]
                    val result = p.inventory.add(id = item, amount = amount, assureFullInsertion = false)
                    p.message("You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).")
                } else {
                    p.message("Item $item does not exist in cache.")
                }
            }
        }

        r.bindCommand("food", Privilege.ADMIN_POWER) {
            val p = it.player()

            p.inventory.add(id = 391, amount = p.inventory.getFreeSlotCount())
        }

        r.bindCommand("varp", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::varp 173 1</col>") { values ->
                val varp = values[0].toInt()
                val state = values[1].toInt()
                val oldState = p.getVarp(varp)
                p.setVarp(varp, state)
                p.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>$state</col>")
            }
        }

        r.bindCommand("varbit", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::varbit 5451 1</col>") { values ->
                val varbit = values[0].toInt()
                val state = values[1].toInt()
                val oldState = p.getVarbit(varbit)
                p.setVarbit(varbit, state)
                p.message("Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>$state</col>")
            }
        }

        r.bindCommand("varbitsof", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::varbitsof 83</col>") { values ->
                val varp = values[0].toInt()
                val varbits = arrayListOf<VarbitDef>()
                val totalVarbits = p.world.definitions.getCount(VarbitDef::class.java)
                for (i in 0 until totalVarbits) {
                    val varbit = p.world.definitions.getNullable(VarbitDef::class.java, i)
                    if (varbit?.varp == varp) {
                        varbits.add(varbit)
                    }
                }
                p.message("Varbits of varp <col=801700>$varp</col>:")
                varbits.forEach { varbit ->
                    p.message("  ${varbit.id} [bits ${varbit.startBit}-${varbit.endBit}]")
                }
            }
        }

        r.bindCommand("interface", Privilege.ADMIN_POWER) {
            val p = it.player()

            val args = p.attr[COMMAND_ARGS_ATTR]
            tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
                val interfaceId = values[0].toInt()
                p.openInterface(interfaceId, InterfacePane.MAIN_SCREEN)
                p.message("Opening interface <col=801700>$interfaceId</col>")
            }
        }
    }

    private fun tryWithUsage(player: Player, args: Array<String>, failMessage: String, tryUnit: Function1<Array<String>, Unit>) {
        try {
            tryUnit.invoke(args)
        } catch (e: Exception) {
            player.message(failMessage)
            e.printStackTrace()
        }
    }
}