package gg.rsmod.game.service.game

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.common.base.Stopwatch
import dev.openrune.cache.CacheManager.item
import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.bytes.Byte2ByteOpenHashMap
import net.runelite.cache.util.Namer
import org.yaml.snakeyaml.LoaderOptions
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ItemMetadataService : Service {
    override fun postLoad(server: Server, world: World) {
    }

    override fun bindNet(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        loadAll(world)
    }

    var ms: Long = 0
    fun loadAll(world: World) {
        val stopwatch = Stopwatch.createStarted().reset().start()
        val loaderOptions = LoaderOptions()

        loaderOptions.codePointLimit = 10 * 1024 * 1024 // 10 MB

        val yamlFactory = YAMLFactory.builder()
            .loaderOptions(loaderOptions)
            .build()
        val mapper = YAMLMapper(yamlFactory)


        val path = Paths.get("../data/cfg/items")

        try {
            Files.newBufferedReader(path.resolve("_Items.yml")).use { reader ->
                val data = mapper.readValue(reader, Array<Metadata>::class.java)
                data.forEach { item ->
                    load(item, world)
                }
            }

            Files.walk(path.resolve("equippable")).parallel().filter { it.toFile().isFile }.forEach {
                val data = mapper.readValue(it.toFile(), Metadata::class.java)
                load(data, world)
            }
        } catch (e: Exception) {
            throw e
        }
        ms = stopwatch.elapsed(TimeUnit.MILLISECONDS)
    }


    /**
     * Continue later. @TODO -> Removal of all default 0's
     */
    private fun newData(item: Metadata) {
        val mapper: ObjectMapper = YAMLMapper()
        if (item.id == 35) {
            if (item.equipment != null) {
                val namer = Namer()
                val name = namer.name(item.name, item.id).lowercase()
                //val file: File = File("./data/cfg/newItems/equipable/", "${item.id}_$name.yml")
                println(item)
            }
        }
    }

    private fun load(item: Metadata, world: World) {
        val def = item(item.id)
        def.name = item.name
        def.examine = item.examine
        def.isTradeable = item.tradeable
        def.weight = item.weight
        if (item.equipment != null) {
            val equipment = item.equipment
            val slots = if (equipment.equipSlot != null) getEquipmentSlots(equipment.equipSlot, def.id) else null

            def.attackSpeed = equipment.attackSpeed

            if (equipment.weaponType == -1 && slots != null) {
                if (slots.slot == 3) {
                    def.weaponType = 17
                }
            } else {
                def.weaponType = equipment.weaponType
            }

            def.renderAnimations = equipment.renderAnimations?.getAsArray()
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
                equipment.attackStab,
                equipment.attackSlash,
                equipment.attackCrush,
                equipment.attackMagic,
                equipment.attackRanged,
                equipment.defenceStab,
                equipment.defenceSlash,
                equipment.defenceCrush,
                equipment.defenceMagic,
                equipment.defenceRanged,
                equipment.meleeStrength,
                equipment.rangedStrength,
                equipment.magicDamage,
                equipment.prayer
            )
        }
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
        "runecrafting", "runecraft" -> 20
        "hunter" -> 21
        "construction", "contruction" -> 22
        "combat" -> 3
        else -> throw IllegalArgumentException("Illegal skill name: $name")
    }

    private fun getEquipmentSlots(slot: String, id: Int? = null): EquipmentSlots {
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
            // For hats that requires hair removal
            "nohair" -> {
                equipSlot = 0
                equipType = 11
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
            "torso" -> {
                equipSlot = 4
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
            else -> throw IllegalArgumentException("Illegal equipment slot: $slot, $id")
        }
        return EquipmentSlots(equipSlot, equipType)
    }

    private data class Metadata(
        val id: Int = -1,
        val name: String = "",
        val examine: String? = null,
        val tradeable: Boolean = false,
        val weight: Double = 0.0,
        val tradeable_on_ge: Boolean = false,
        val cost: Int = 0,
        val lowalch: Int = 0,
        val highalch: Int = 0,
        val buy_limit: Int? = null,
        val equipment: Equipment? = null
    )

    private data class EquipmentSlots(val slot: Int, val secondary: Int)

    private data class Equipment(
        @JsonProperty("equip_slot") val equipSlot: String? = null,
        @JsonProperty("equip_sound") val equipSound: Int? = -1,
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
        @JsonProperty("render_animations") val renderAnimations: renderAnimations? = null,
        @JsonProperty("attackSounds") val attackSounds: IntArray? = null,
        @JsonProperty("skill_reqs") val skillReqs: Array<SkillRequirement>? = null
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Equipment
            if (equipSlot != other.equipSlot) return false
            if (equipSound != other.equipSound) return false
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
            } else if (other.renderAnimations != null) return false

            if (attackSounds != null) return false

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
            result = 31 * result + (renderAnimations?.getAsArray().contentHashCode())
            result = 31 * result + (skillReqs?.contentHashCode() ?: 0)
            return result
        }
    }

    private data class renderAnimations (
        @JsonProperty("standAnimId") val standAnimId: Int = 0 ,
        @JsonProperty("turnOnSpotAnim") val turnOnSpotAnim: Int = 0 ,
        @JsonProperty("walkForwardAnimId") val walkForwardAnimId: Int = 0 ,
        @JsonProperty("walkBackwardsAnimId") val walkBackwardsAnimId: Int = 0 ,
        @JsonProperty("walkLeftAnimId") val walkLeftAnimId: Int = 0 ,
        @JsonProperty("walkRightAnimId") val walkRightAnimId: Int = 0 ,
        @JsonProperty("runAnimId") val runAnimId: Int = 0

    ) {
        fun getAsArray(): IntArray {
            val renderAnimList = listOf(standAnimId, turnOnSpotAnim, walkForwardAnimId, walkBackwardsAnimId, walkLeftAnimId, walkRightAnimId, runAnimId)
            return renderAnimList.toIntArray()
        }
    }

    private data class SkillRequirement(
        @JsonProperty("skill") val skill: String?,
        @JsonProperty("level") val level: Int?
    )

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
