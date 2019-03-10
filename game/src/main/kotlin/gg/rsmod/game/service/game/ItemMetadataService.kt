package gg.rsmod.game.service.game

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemMetadataService : Service() {

    companion object {
        private val logger = KotlinLogging.logger {  }
    }

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        val path = Paths.get(serviceProperties.getOrDefault("path", "./data/cfg/items.yml"))
        if (!Files.exists(path)) {
            throw FileNotFoundException("Path does not exist. $path")
        }

        Files.newBufferedReader(path).use { reader ->
            load(world.definitions, reader)
        }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load(definitions: DefinitionSet, reader: BufferedReader) {
        val mapper = ObjectMapper(YAMLFactory())
        val items = mapper.readValue(reader, Array<Metadata>::class.java).map { it.id to it }.toMap()

        val itemCount = definitions.getCount(ItemDef::class.java)

        for (i in 0 until itemCount) {
            val def = definitions.get(ItemDef::class.java, i)
            val data = if (items.containsKey(def.id)) (items[def.id])!! else continue
            def.name = data.name
            def.examine = data.examine
            def.tradeable = data.tradeable
            def.weight = data.weight

            if (data.equipment != null) {
                val equipment = data.equipment
                val slots = if (equipment.equipSlot != null) getEquipmentSlots(equipment.equipSlot) else null

                def.attackSpeed = equipment.attackSpeed
                def.weaponType = equipment.weaponType
                def.renderAnimations = equipment.renderAnimations
                if (slots != null) {
                    def.equipSlot = slots.slot
                    def.equipType = slots.secondary
                }
                if (equipment.skillReqs != null) {
                    def.skillReqs = Byte2ByteOpenHashMap()
                    equipment.skillReqs.filter { it.skill != null }.forEach { req ->
                        def.skillReqs[getSkillId(req.skill!!)] = req.level!!.toByte()
                    }
                }

                def.bonuses = intArrayOf(
                        equipment.attackStab, equipment.attackSlash, equipment.attackCrush, equipment.attackMagic, equipment.attackRanged,
                        equipment.defenceStab, equipment.defenceSlash, equipment.defenceCrush, equipment.defenceMagic, equipment.defenceRanged,
                        equipment.meleeStrength, equipment.rangedStrength, equipment.magicDamage, equipment.prayer)
            }
        }

        logger.info { "Loaded ${items.size} item metadata set${if (items.size != 1) "s" else ""}." }
    }

    private fun getSkillId(name: String): Byte = when (name) {
        // Need to get a better
        "attack" -> 0
        "defence" -> 1
        "strength" -> 2
        "hitpoints" -> 3
        "range", "ranged" -> 4
        "prayer" -> 5
        "magic" -> 6
        "cooking" -> 7
        "woodcutting" -> 8
        "fletching" -> 9
        "fishing" -> 10
        "firemaking" -> 11
        "crafting" -> 12
        "smithing" -> 13
        "mining" -> 14
        "herblore" -> 15
        "agility" -> 16
        "thieving", "theiving" -> 17
        "slayer" -> 18
        "farming" -> 19
        "runecrafting" -> 20
        "hunter" -> 21
        "construction", "contruction" -> 22
        "combat" -> 3
        else -> throw IllegalArgumentException("Illegal skill name: $name")
    }

    private fun getEquipmentSlots(slot: String): EquipmentSlots {
        val equipSlot: Int
        var equipType = -1
        when (slot) {
            "head" -> {
                equipSlot = 0
                equipType = 8
            }
            "cape" -> {
                equipSlot = 1
            }
            "neck" -> {
                equipSlot = 2
            }
            "2h" -> {
                equipSlot = 3
                equipType = 5
            }
            "weapon" -> {
                equipSlot = 3
            }
            "body" -> {
                equipSlot = 4
                equipType = 6
            }
            "shield" -> {
                equipSlot = 5
            }
            "legs" -> {
                equipSlot = 7
            }
            "hands" -> {
                equipSlot = 9
            }
            "feet" -> {
                equipSlot = 10
            }
            "ring" -> {
                equipSlot = 12
            }
            "ammo" -> {
                equipSlot = 13
            }
            else -> throw IllegalArgumentException("Illegal equipment slot: $slot")
        }
        return EquipmentSlots(equipSlot, equipType)
    }

    private data class EquipmentSlots(val slot: Int, val secondary: Int)

    private data class Metadata(val id: Int = -1,
                                val name: String = "",
                                val examine: String? = null,
                                val tradeable: Boolean = false,
                                val weight: Double = 0.0,
                                val equipment: Equipment? = null)

    private data class Equipment(@JsonProperty("equip_slot") val equipSlot: String? = null,
                                 @JsonProperty("weapon_type") val weaponType: Int = -1,
                                 @JsonProperty("attack_speed") val attackSpeed: Int = -1,
                                 @JsonProperty("attack_stab") val attackStab: Int = 0,
                                 @JsonProperty("attack_slash") val attackSlash: Int = 0,
                                 @JsonProperty("attack_crush") val attackCrush: Int = 0,
                                 @JsonProperty("attack_magic") val attackMagic: Int = 0,
                                 @JsonProperty("attack_ranged") val attackRanged: Int = 0,
                                 @JsonProperty("defence_stab") val defenceStab: Int = 0,
                                 @JsonProperty("defence_slash") val defenceSlash: Int = 0,
                                 @JsonProperty("defence_crush") val defenceCrush: Int = 0,
                                 @JsonProperty("defence_magic") val defenceMagic: Int = 0,
                                 @JsonProperty("defence_ranged") val defenceRanged: Int = 0,
                                 @JsonProperty("melee_strength") val meleeStrength: Int = 0,
                                 @JsonProperty("ranged_strength") val rangedStrength: Int = 0,
                                 @JsonProperty("magic_damage") val magicDamage: Int = 0,
                                 @JsonProperty("prayer") val prayer: Int = 0,
                                 @JsonProperty("render_animations") val renderAnimations: IntArray? = null,
                                 @JsonProperty("skill_reqs") val skillReqs: Array<SkillRequirement>? = null)

    private data class SkillRequirement(@JsonProperty("skill") val skill: String?,
                                        @JsonProperty("level") val level: Int?)
}