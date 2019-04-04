package gg.rsmod.plugins.content.cmd

import gg.rsmod.game.model.bits.INFINITE_VARS_STORAGE
import gg.rsmod.game.model.bits.InfiniteVarsType
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.combat.CombatConfigs
import gg.rsmod.plugins.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.formula.RangedCombatFormula
import gg.rsmod.plugins.content.combat.getCombatTarget
import gg.rsmod.plugins.content.inter.bank.openBank
import gg.rsmod.plugins.content.magic.MagicSpells
import java.text.DecimalFormat

on_command("reboot") {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::reboot 500</col>") { values ->
        val cycles = values[0].toInt()
        world.rebootTimer = cycles
        world.sendRebootTimer()
    }
}

on_command("max") {
    val target = player.getCombatTarget() ?: player
    CombatClass.values.forEach { combatClass ->
        val max: Int
        val accuracy: Double
        when (combatClass) {
            CombatClass.MAGIC -> {
                accuracy = MagicCombatFormula.getAccuracy(player, target)
                max = MagicCombatFormula.getMaxHit(player, target)
            }
            CombatClass.RANGED -> {
                accuracy = RangedCombatFormula.getAccuracy(player, target)
                max = RangedCombatFormula.getMaxHit(player, target)
            }
            CombatClass.MELEE -> {
                accuracy = MeleeCombatFormula.getAccuracy(player, target)
                max = MeleeCombatFormula.getMaxHit(player, target)
            }
            else -> throw IllegalStateException("Unhandled combat class: ${CombatConfigs.getCombatClass(player)} for $player")
        }
        val name = combatClass.name.toLowerCase().capitalize()
        val message = """<col=178000>$name:</col>   Max hit=<col=801700>$max</col>   Accuracy=<col=801700>${(accuracy * 100).toInt()}%</col> [${String.format("%.6f", accuracy)}]"""
        player.message(message)
    }
}

on_command("empty") {
    player.inventory.removeAll()
}

on_command("home", Privilege.ADMIN_POWER) {
    val home = world.gameContext.home
    player.moveTo(home)
}

on_command("obank", Privilege.ADMIN_POWER) {
    player.openBank()
}

on_command("mypos", Privilege.ADMIN_POWER) {
    player.message(player.tile.toString() + ", region=${player.tile.regionId}")
}

on_command("tele", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::tele 3200 3200</col>") { values ->
        val x = values[0].toInt()
        val z = values[1].toInt()
        val height = if (values.size > 2) values[2].toInt() else 0
        player.moveTo(x, z, height)
    }
}

on_command("anim", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::anim 1</col>") { values ->
        val id = values[0].toInt()
        player.animate(id)
        player.message("Animate: $id")
    }
}

on_command("gfx", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::gfx 1</col>") { values ->
        val id = values[0].toInt()
        val height = if (values.size >= 2) values[1].toInt() else 100
        player.graphic(id, height)
        player.message("Graphic: $id")
    }
}

on_command("sound", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::sound 1</col>") { values ->
        val id = values[0].toInt()
        player.playSound(id)
        player.message("Sound: $id")
    }
}

on_command("infrun", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)
    player.message("Infinite run: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infpray", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)
    player.message("Infinite prayer: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.PRAY)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infhp", Privilege.ADMIN_POWER) {
    player.toggleStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)
    player.message("Infinite hp: ${if (!player.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.HP)) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("infrunes", Privilege.ADMIN_POWER) {
    player.toggleVarbit(MagicSpells.INF_RUNES_VARBIT)
    player.message("Infinite runes: ${if (player.getVarbit(MagicSpells.INF_RUNES_VARBIT) != 1) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

on_command("invisible", Privilege.ADMIN_POWER) {
    player.invisible = !player.invisible
    player.message("Invisible: ${if (!player.invisible) "<col=801700>false</col>" else "<col=178000>true</col>"}")
}

on_command("npc", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::npc 1</col>") { values ->
        val id = values[0].toInt()
        val npc = Npc(id, player.tile, world)
        world.spawn(npc)
    }
}

on_command("obj", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
        val id = values[0].toInt()
        val type = if (values.size > 1) values[1].toInt() else 10
        val rot = if (values.size > 2) values[2].toInt() else 0
        val obj = DynamicObject(id, type, rot, player.tile)
        world.spawn(obj)
    }
}

on_command("removeobj", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val obj = chunk.getEntities<GameObject>(player.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        world.remove(obj)
    } else {
        player.message("No object found in tile.")
    }
}

on_command("master", Privilege.ADMIN_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, 99)
    }
    player.calculateAndSetCombatLevel()
}

on_command("reset", Privilege.ADMIN_POWER) {
    for (i in 0 until player.getSkills().maxSkills) {
        player.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
    }
    player.calculateAndSetCombatLevel()
}

on_command("setlvl", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::setlvl 0 99</col> or <col=801700>::setlvl attack 99</col>") { values ->
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
            skill = Skills.getSkillForName(world, player.getSkills().maxSkills, name)
        }
        if (skill != -1) {
            val level = values[1].toInt()
            player.getSkills().setBaseLevel(skill, level)
        } else {
            player.message("Could not find skill with identifier: ${values[0]}")
        }
    }
}

on_command("item", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>") { values ->
        val item = values[0].toInt()
        val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), values[1].parseAmount()).toInt() else 1
        if (item < world.definitions.getCount(ItemDef::class.java)) {
            val def = world.definitions.get(ItemDef::class.java, Item(item).toUnnoted(world.definitions).id)
            val result = player.inventory.add(item = item, amount = amount, assureFullInsertion = false)
            player.message("You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).")
        } else {
            player.message("Item $item does not exist in cache.")
        }
    }
}

on_command("food", Privilege.ADMIN_POWER) {
    player.inventory.add(item = Items.MANTA_RAY, amount = player.inventory.freeSlotCount)
}

on_command("varp", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varp 173 1</col>") { values ->
        val varp = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarp(varp)
        player.setVarp(varp, state)
        player.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarp(varp)}</col>")
    }
}

on_command("varbit", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::varbit 5451 1</col>") { values ->
        val varbit = values[0].toInt()
        val state = values[1].toInt()
        val oldState = player.getVarbit(varbit)
        player.setVarbit(varbit, state)
        player.message("Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>${player.getVarbit(varbit)}</col>")
    }
}

on_command("getvarbit", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbit 5451</col>") { values ->
        val varbit = values[0].toInt()
        val state = player.getVarbit(varbit)
        player.message("Get varbit (<col=801700>$varbit</col>): <col=801700>$state</col>")
    }
}

on_command("getvarbits", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::getvarbits 83</col>") { values ->
        val varp = values[0].toInt()
        val varbits = arrayListOf<VarbitDef>()
        val totalVarbits = world.definitions.getCount(VarbitDef::class.java)
        for (i in 0 until totalVarbits) {
            val varbit = world.definitions.getNullable(VarbitDef::class.java, i)
            if (varbit?.varp == varp) {
                varbits.add(varbit)
            }
        }
        player.message("Varbits for varp <col=801700>$varp</col>:")
        varbits.forEach { varbit ->
            player.message("  ${varbit.id} [bits ${varbit.startBit}-${varbit.endBit}] [current ${player.getVarbit(varbit.id)}]")
        }
    }
}

on_command("interface", Privilege.ADMIN_POWER) {
    val args = player.getCommandArgs()
    tryWithUsage(player, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
        val component = values[0].toInt()
        player.openInterface(component, InterfaceDestination.MAIN_SCREEN)
        player.message("Opening interface <col=801700>$component</col>")
    }
}

on_command("clip", Privilege.ADMIN_POWER) {
    val chunk = world.chunks.getOrCreate(player.tile)
    val matrix = chunk.getMatrix(player.tile.height)
    val lx = player.tile.x % 8
    val lz = player.tile.z % 8
    player.message("Tile flags: ${chunk.getMatrix(player.tile.height).get(lx, lz)}")
    Direction.RS_ORDER.forEach { dir ->
        val walkBlocked = matrix.isBlocked(lx, lz, dir, projectile = false)
        val projectileBlocked = matrix.isBlocked(lx, lz, dir, projectile = true)
        val walkable = if (walkBlocked) "<col=801700>blocked</col>" else "<col=178000>walkable</col>"
        val projectile = if (projectileBlocked) "<col=801700>projectiles blocked" else "<col=178000>projectiles allowed"
        player.message("$dir: $walkable - $projectile")
    }
}

fun tryWithUsage(player: Player, args: Array<String>, failMessage: String, tryUnit: Function1<Array<String>, Unit>) {
    try {
        tryUnit.invoke(args)
    } catch (e: Exception) {
        player.message(failMessage)
        e.printStackTrace()
    }
}