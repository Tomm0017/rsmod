package gg.rsmod.game.service.game

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import gg.rsmod.game.Server
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import mu.KLogging
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemMetadataService : Service {

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

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    private fun load(definitions: DefinitionSet, reader: BufferedReader) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.propertyNamingStrategy = PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES

        val items = mapper.readValue(reader, Array<Metadata>::class.java).map { it.id to it }.toMap()

        val itemCount = definitions.getCount(ItemDef::class.java)

        for (i in 0 until itemCount) {
            val def = definitions.get(ItemDef::class.java, i)
            val data = items[def.id] ?: continue
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
                    val reqs = Byte2ByteOpenHashMap()
                    equipment.skillReqs.filter { it.skill != null }.forEach { req ->
                        reqs[getSkillId(req.skill!!)] = req.level!!.toByte()
                    }
                    def.skillReqs = reqs
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
        // Need to get a better dump db. As we can see, this one has some
        // inconsistency for some reason.
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
            "hat" -> {
                equipSlot = 0
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

    private data class Metadata(val id: Int = -1,
                                val name: String = "",
                                val examine: String? = null,
                                val tradeable: Boolean = false,
                                val weight: Double = 0.0,
                                val equipment: Equipment? = null)

    private data class EquipmentSlots(val slot: Int, val secondary: Int)

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
                                 @JsonProperty("skill_reqs") val skillReqs: Array<SkillRequirement>? = null) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Equipment

            if (equipSlot != other.equipSlot) return false
            if (weaponType != other.weaponType) return false
            if (attackSpeed != other.attackSpeed) return false
            if (attackStab != other.attackStab) return false
            if (attackSlash != other.attackSlash) return false
            if (attackCrush != other.attackCrush) return false
            if (attackMagic != other.attackMagic) return false
            if (attackRanged != other.attackRanged) return false
            if (defenceStab != other.defenceStab) return false
            if (defenceSlash != other.defenceSlash) return false
            if (defenceCrush != other.defenceCrush) return false
            if (defenceMagic != other.defenceMagic) return false
            if (defenceRanged != other.defenceRanged) return false
            if (meleeStrength != other.meleeStrength) return false
            if (rangedStrength != other.rangedStrength) return false
            if (magicDamage != other.magicDamage) return false
            if (prayer != other.prayer) return false
            if (renderAnimations != null) {
                if (other.renderAnimations == null) return false
                if (!renderAnimations.contentEquals(other.renderAnimations)) return false
            } else if (other.renderAnimations != null) return false
            if (skillReqs != null) {
                if (other.skillReqs == null) return false
                if (!skillReqs.contentEquals(other.skillReqs)) return false
            } else if (other.skillReqs != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = equipSlot?.hashCode() ?: 0
            result = 31 * result + weaponType
            result = 31 * result + attackSpeed
            result = 31 * result + attackStab
            result = 31 * result + attackSlash
            result = 31 * result + attackCrush
            result = 31 * result + attackMagic
            result = 31 * result + attackRanged
            result = 31 * result + defenceStab
            result = 31 * result + defenceSlash
            result = 31 * result + defenceCrush
            result = 31 * result + defenceMagic
            result = 31 * result + defenceRanged
            result = 31 * result + meleeStrength
            result = 31 * result + rangedStrength
            result = 31 * result + magicDamage
            result = 31 * result + prayer
            result = 31 * result + (renderAnimations?.contentHashCode() ?: 0)
            result = 31 * result + (skillReqs?.contentHashCode() ?: 0)
            return result
        }

    }
    private data class SkillRequirement(@JsonProperty("skill") val skill: String?,
                                        @JsonProperty("level") val level: Int?)

    companion object: KLogging()
}
