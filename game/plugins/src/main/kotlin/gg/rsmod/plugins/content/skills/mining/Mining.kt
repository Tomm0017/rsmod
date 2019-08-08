package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.GroundItem
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*
import kotlin.random.Random

/**
 * @author Anthony Loukinas <anthony.loukinas@gmail.com>
 */
object Mining {
    data class Ore(val type: OreType, val obj: Int, val emptyOreId: Int)

    suspend fun prospectOre(it: QueueTask, ore: OreType) {
        val p = it.player

        // TODO: Find prospecting ore sound
        p.playSound(3598) // need to find the real prospecting sound.. this one is placeholder
        p.filterableMessage("You examine the rock for ores...")
        it.wait(4)
        p.filterableMessage("This rock contains ${ore.name.toLowerCase()}")
    }

    suspend fun mineOre(it: QueueTask, obj: GameObject, ore: OreType, emptyOreId: Int) {
        val p = it.player
        var loops: Int = 0
        val maxLoops: Int = 0 // will be used

        if(!canMine(p, obj, ore)) {
            return
        }

        var oreName = p.world.definitions.get(ItemDef::class.java, ore.ore).name
        val equipmentPickaxe = PickaxeType.values.firstOrNull {
            p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.equipment.contains(it.item))
        }
        val inventoryPickaxe = PickaxeType.values.lastOrNull {
            p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.inventory.contains(it.item))
        }
        val pickaxe = equipmentPickaxe ?: inventoryPickaxe!!

        p.filterableMessage("You swing your pickaxe at the rock.")
        while (true) {

            // Increment loop by one, for future timings
            loops++

            // TODO: Need to implement a max loops, sometimes at low levels it can take 10+ loops
            it.wait(2)
            p.animate(pickaxe.animation)

            if(!canMine(p, obj, ore)) {
                p.animate(-1)
                break
            }

            it.wait(3)

            val level = p.getSkills().getCurrentLevel(Skills.MINING)
            // TODO: Implement pickaxe type interpolation
            val mineChance = level.interpolate(minChance = 60, maxChance = 190, minLvl = 1, maxLvl = 99, cap = 255)

            if(mineChance) {

                when(ore) {
                    OreType.RUNE_ESSENCE -> {
                        val essenceType = evalEssenceType(level)
                        oreName = p.world.definitions.get(ItemDef::class.java, essenceType.ore).name
                        p.filterableMessage("You manage to mine some $oreName.")
                        p.playSound(3600) // may need to update this
                        p.inventory.add(essenceType.ore)
                        p.addXp(Skills.MINING, essenceType.xp)
                    }
                    OreType.SANDSTONE1 -> {
                        val sandstoneWeight = evalSandstoneWeight()
                        oreName = p.world.definitions.get(ItemDef::class.java, sandstoneWeight.ore).name
                        p.filterableMessage("You manage to mine some $oreName.")
                        p.playSound(3600) // may need to update this
                        p.inventory.add(sandstoneWeight.ore)
                        p.addXp(Skills.MINING, sandstoneWeight.xp)
                    }
                    OreType.GRANITE1 -> {
                        val graniteWeight = evalGraniteWeight()
                        oreName = p.world.definitions.get(ItemDef::class.java, graniteWeight.ore).name
                        p.filterableMessage("You manage to mine some $oreName.")
                        p.playSound(3600) // may need to update this
                        p.inventory.add(graniteWeight.ore)
                        p.addXp(Skills.MINING, graniteWeight.xp)
                    }
                    OreType.GEMSTONE1 -> {
                        val gemstoneWeight = evalGemWeight()
                        oreName = p.world.definitions.get(ItemDef::class.java, gemstoneWeight.ore).name
                        p.filterableMessage("You manage to mine some $oreName.")
                        p.playSound(3600) // may need to update this
                        p.inventory.add(gemstoneWeight.ore)
                        p.addXp(Skills.MINING, gemstoneWeight.xp)
                    }
                    else -> {
                        p.filterableMessage("You manage to mine some $oreName.")
                        p.playSound(3600) // may need to update this
                        p.inventory.add(ore.ore)
                        p.addXp(Skills.MINING, ore.xp)
                    }
                }

                p.animate(-1) // reset animation
                val world = p.world

                if(emptyOreId != 0) {
                    world.queue {
                        val emptyOreVein = DynamicObject(obj, emptyOreId)
                        world.remove(obj)
                        world.spawn(emptyOreVein)
                        wait(ore.respawnTime.random())
                        world.remove(emptyOreVein)
                        world.spawn(DynamicObject(obj))
                    }
                }
                randomGemEvent(p)
                break
            }
        }
    }

    private fun randomGemEvent(p: Player) {
        // TODO: Add a gemDropRate val on OreType
//        val level = p.getSkills().getCurrentLevel(Skills.MINING)
        val listOfGlorys = arrayOf(
                Items.AMULET_OF_GLORY1,
                Items.AMULET_OF_GLORY2,
                Items.AMULET_OF_GLORY3,
                Items.AMULET_OF_GLORY4,
                Items.AMULET_OF_GLORY5,
                Items.AMULET_OF_GLORY6,
                Items.AMULET_OF_GLORY_T1,
                Items.AMULET_OF_GLORY_T2,
                Items.AMULET_OF_GLORY_T3,
                Items.AMULET_OF_GLORY_T4,
                Items.AMULET_OF_GLORY_T5,
                Items.AMULET_OF_GLORY_T6
        )
        val listOfGems = arrayOf(
                Items.UNCUT_SAPPHIRE,
                Items.UNCUT_EMERALD,
                Items.UNCUT_RUBY,
                Items.UNCUT_DIAMOND
        )

        var isWearingGlory = false
        listOfGlorys.forEach { glory ->
            if(p.equipment.contains(glory)) {
                isWearingGlory = true
            }
        }

        val isDropped: Int
        when(isWearingGlory) {
            true -> isDropped = Random.nextInt(1, 86) // 1/86
            else -> isDropped = Random.nextInt(1, 256) // 1/256
        }
        when(isDropped) {
            1 -> {
                if(p.inventory.isFull){
                    p.filterableMessage("You discovered a gem while mining, but your inventory was full.")
                    p.world.spawn(GroundItem(listOfGems.random(), 1, p.tile, p))
                } else {
                    p.filterableMessage("You discovered a gem while mining, it has been placed in your inventory.")
                    p.inventory.add(listOfGems.random())
                }
            }
        }
    }

    private fun evalEssenceType(level: Int): OreType {
        return when {
            level >= 30 -> OreType.PURE_ESSENCE
            else -> OreType.RUNE_ESSENCE
        }
    }

    private fun evalSandstoneWeight(): OreType {
        val listOfOres = arrayOf(OreType.SANDSTONE1, OreType.SANDSTONE2, OreType.SANDSTONE3, OreType.SANDSTONE4)
        return listOfOres.random()
    }

    private fun evalGraniteWeight(): OreType {
        val listOfOres = arrayOf(OreType.GRANITE1, OreType.GRANITE2, OreType.GRANITE3)
        return listOfOres.random()
    }

    private fun evalGemWeight(): OreType {
        val listOfOres = arrayOf(OreType.GEMSTONE1, OreType.GEMSTONE2, OreType.GEMSTONE3, OreType.GEMSTONE4, OreType.GEMSTONE5, OreType.GEMSTONE6, OreType.GEMSTONE7)
        return listOfOres.random()
    }

    private fun canMine(p: Player, obj: GameObject, ore: OreType): Boolean {
        val oreName = p.world.definitions.get(ItemDef::class.java, ore.ore).name

        if(!p.world.isSpawned(obj)) {
            return false
        }

        val pickaxe = PickaxeType.values.lastOrNull { p.getSkills().getMaxLevel(Skills.MINING) >= it.level && (p.equipment.contains(it.item) || p.inventory.contains(it.item)) }
        if(pickaxe == null){
            p.queue {messageBox("You need a pickaxe to mine this rock. You do not have a pickaxe<br>which you have the Mining level to use.")}
            return false
        }

        if(p.getSkills().getMaxLevel(Skills.MINING) < ore.level) {
            p.message("You need a Mining level of ${ore.level} to mine this ore.")
            return false
        }

        if(p.inventory.isFull) {
            p.message("Your inventory is too full to hold any more $oreName.")
            return false
        }

        return true
    }
}