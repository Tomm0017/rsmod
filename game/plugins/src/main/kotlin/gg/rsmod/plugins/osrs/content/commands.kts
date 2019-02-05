
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.osrs.api.ComponentPane
import gg.rsmod.plugins.osrs.api.Skills
import gg.rsmod.plugins.osrs.api.cfg.Items
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.bank.Bank
import gg.rsmod.plugins.osrs.content.mechanics.prayer.Prayers
import gg.rsmod.plugins.osrs.content.mechanics.spells.SpellRequirements
import gg.rsmod.util.Misc
import java.text.DecimalFormat

onCommand("empty") {
    it.player().inventory.removeAll()
}

onCommand("obank", Privilege.ADMIN_POWER) {
    Bank.open(it.player())
}

onCommand("mypos", Privilege.ADMIN_POWER) {
    val p = it.player()
    p.message(p.tile.toString())
}

onCommand("tele", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::tele 3200 3200</col>") { values ->
        val x = values[0].toInt()
        val z = values[1].toInt()
        val height = if (values.size > 2) values[2].toInt() else 0
        p.teleport(x, z, height)
    }
}

onCommand("infpray", Privilege.ADMIN_POWER) {
    val p = it.player()
    p.toggleVarbit(Prayers.INF_PRAY_VARBIT)
    p.message("Infinite prayer: ${if (p.getVarbit(Prayers.INF_PRAY_VARBIT) == 0) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

onCommand("elerunes", Privilege.ADMIN_POWER) {
    val p = it.player()
    p.toggleVarbit(SpellRequirements.INF_ELEMENTAL_RUNES_VARBIT)
    p.message("Infinite elemental runes: ${if (p.getVarbit(SpellRequirements.INF_ELEMENTAL_RUNES_VARBIT) != 1) "<col=801700>disabled</col>" else "<col=178000>enabled</col>"}")
}

onCommand("npc", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::npc 1</col>") { values ->
        val id = values[0].toInt()
        val npc = Npc(id, p.tile, p.world)
        p.world.spawn(npc)
    }
}

onCommand("obj", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::obj 1</col>") { values ->
        val id = values[0].toInt()
        val type = if (values.size > 1) values[1].toInt() else 10
        val obj = DynamicObject(id, type, 0, p.tile)
        p.world.spawn(obj)
    }
}

onCommand("removeobj", Privilege.ADMIN_POWER) {
    val p = it.player()

    val chunk = p.world.chunks.getForTile(p.tile)
    val obj = chunk.getEntities<GameObject>(p.tile, EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT).firstOrNull()
    if (obj != null) {
        p.world.remove(obj)
    } else {
        p.message("No object found in tile.")
    }
}

onCommand("master", Privilege.ADMIN_POWER) {
    val p = it.player()

    for (i in 0 until p.getSkills().maxSkills) {
        p.getSkills().setBaseLevel(i, 99)
    }
    p.calculateAndSetCombatLevel()
}

onCommand("reset", Privilege.ADMIN_POWER) {
    val p = it.player()

    for (i in 0 until p.getSkills().maxSkills) {
        p.getSkills().setBaseLevel(i, if (i == Skills.HITPOINTS) 10 else 1)
    }
    p.calculateAndSetCombatLevel()
}

onCommand("setlvl", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
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

onCommand("item", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::item 4151 1</col> or <col=801700>::item 4151</col>") { values ->
        val item = values[0].toInt()
        val amount = if (values.size > 1) Math.min(Int.MAX_VALUE.toLong(), Misc.parseAmount(values[1])).toInt() else 1
        if (item < p.world.definitions.getCount(ItemDef::class.java)) {
            val def = p.world.definitions[ItemDef::class.java][Item(item).toUnnoted(p.world.definitions).id]
            val result = p.inventory.add(id = item, amount = amount, assureFullInsertion = false)
            p.message("You have spawned <col=801700>${DecimalFormat().format(result.completed)} x ${def.name}</col></col> ($item).")
        } else {
            p.message("Item $item does not exist in cache.")
        }
    }
}

onCommand("food", Privilege.ADMIN_POWER) {
    val p = it.player()

    p.inventory.add(id = Items.MANTA_RAY, amount = p.inventory.getFreeSlotCount())
}

onCommand("varp", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::varp 173 1</col>") { values ->
        val varp = values[0].toInt()
        val state = values[1].toInt()
        val oldState = p.getVarp(varp)
        p.setVarp(varp, state)
        p.message("Set varp (<col=801700>$varp</col>) from <col=801700>$oldState</col> to <col=801700>${p.getVarp(varp)}</col>")
    }
}

onCommand("varbit", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::varbit 5451 1</col>") { values ->
        val varbit = values[0].toInt()
        val state = values[1].toInt()
        val oldState = p.getVarbit(varbit)
        p.setVarbit(varbit, state)
        p.message("Set varbit (<col=801700>$varbit</col>) from <col=801700>$oldState</col> to <col=801700>${p.getVarbit(varbit)}</col>")
    }
}

onCommand("getvarbits", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::getvarbits 83</col>") { values ->
        val varp = values[0].toInt()
        val varbits = arrayListOf<VarbitDef>()
        val totalVarbits = p.world.definitions.getCount(VarbitDef::class.java)
        for (i in 0 until totalVarbits) {
            val varbit = p.world.definitions.getNullable(VarbitDef::class.java, i)
            if (varbit?.varp == varp) {
                varbits.add(varbit)
            }
        }
        p.message("Varbits for varp <col=801700>$varp</col>:")
        varbits.forEach { varbit ->
            p.message("  ${varbit.id} [bits ${varbit.startBit}-${varbit.endBit}] [current ${p.getVarbit(varbit.id)}]")
        }
    }
}

onCommand("interface", Privilege.ADMIN_POWER) {
    val p = it.player()

    val args = it.getCommandArgs()
    tryWithUsage(p, args, "Invalid format! Example of proper command <col=801700>::interface 214</col>") { values ->
        val component = values[0].toInt()
        p.openComponent(component, ComponentPane.MAIN_SCREEN)
        p.message("Opening interface <col=801700>$component</col>")
    }
}

onCommand("dialogs", Privilege.ADMIN_POWER) {
    it.suspendable {
        it.options("test")
        it.inputInteger("test")
        it.messageDialog("test")
        it.npcDialog("test", 3080)
        it.itemDialog("test", 4151)
        it.doubleItemDialog("test", 4151, 11802)
    }
}

onCommand("clip", Privilege.ADMIN_POWER) {
    val player = it.player()
    val chunk = player.world.chunks.getForTile(player.tile)
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