package gg.rsmod.tools.item

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

object ItemDataDump {

    private val logger = KotlinLogging.logger {  }

    private const val JSON_ENDPOINT = "https://www.osrsbox.com/osrsbox-db/items-complete.json"

    private val FILE_OUTPUT = Paths.get(".", "data", "cfg", "items.yml")

    private val weapons = hashMapOf<Int, Weapon>()

    @JvmStatic fun main(vararg args: String) {
        val client = OkHttpClient()

        val json = read(client, JSON_ENDPOINT)
        val parsed = parse(json)

        toGameFormat(FILE_OUTPUT, parsed.values.toSortedSet(Comparator.comparingInt { it.id }).toTypedArray())
    }

    private fun read(client: OkHttpClient, endpoint: String): String {
        var data = ""

        val request = Request.Builder().url(endpoint).build()
        client.newCall(request).execute().use { response ->
            data = response.body()!!.string()
        }

        return data
    }

    private fun parse(json: String): Map<Int, ItemMetadata> = GsonBuilder().create().fromJson<Map<Int, ItemMetadata>>(json, object : TypeToken<Map<Int, ItemMetadata>>() {}.type)

    private fun toGameFormat(output: Path, metadata: Array<ItemMetadata>) {
        Files.newBufferedReader(Paths.get(".", "data", "cfg", "weapons.json")).use { reader ->
            val weapons = Gson().fromJson<Array<Weapon>>(reader, object : TypeToken<Array<Weapon>>() {}.type)
            weapons.forEach { weapon ->
                this.weapons[weapon.item] = weapon
            }
        }

        Files.newBufferedWriter(output).use { writer ->
            val mapper = ObjectMapper(YAMLFactory())
            mapper.propertyNamingStrategy = PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES
            mapper.writeValue(writer, metadata.map { toOutputMetadata(it) }.toTypedArray())
        }
    }

    private fun toOutputMetadata(data: ItemMetadata): OutputMetadata = OutputMetadata(data.id, data.name, data.examine, data.tradeable, data.weight, if (data.equipment != null) transformEquipmentMetadata(data) else null)

    private fun transformEquipmentMetadata(input: ItemMetadata): OutputMetadata.EquipmentMetadata {
        val equipment = input.equipment!!

        return OutputMetadata.EquipmentMetadata(equipment.slot, weapons[input.id]?.type ?: -1, equipment.attackSpeed, equipment.attackStab,
                equipment.attackSlash, equipment.attackCrush, equipment.attackMagic, equipment.attackRanged,
                equipment.defenceStab, equipment.defenceSlash, equipment.defenceCrush, equipment.defenceMagic,
                equipment.defenceRanged, equipment.meleeStrength, equipment.rangedStrength, equipment.magicDamage,
                equipment.prayer, equipment.skillReqs, weapons[input.id]?.animations)
    }

    private data class Weapon(val item: Int, val type: Int, val animations: IntArray)

    private data class OutputMetadata(@SerializedName("id") val id: Int,
                                      @SerializedName("name") val name: String,
                                      @SerializedName("examine") val examine: String?,
                                      @SerializedName("tradeable") val tradeable: Boolean,
                                      @SerializedName("weight") val weight: Double,
                                      @SerializedName("equip") val equipment: EquipmentMetadata?) {

        internal data class EquipmentMetadata(@SerializedName("equip_slot") val equipSlot: String? = null,
                                              @SerializedName("weapon_type") val weaponType: Int = -1,
                                              @SerializedName("attack_speed") val attackSpeed: Int = -1,

                                              @SerializedName("attack_stab") val attackStab: Int = 0,
                                              @SerializedName("attack_slash") val attackSlash: Int = 0,
                                              @SerializedName("attack_crush") val attackCrush: Int = 0,
                                              @SerializedName("attack_magic") val attackMagic: Int = 0,
                                              @SerializedName("attack_ranged") val attackRanged: Int = 0,

                                              @SerializedName("defence_stab") val defenceStab: Int = 0,
                                              @SerializedName("defence_slash") val defenceSlash: Int = 0,
                                              @SerializedName("defence_crush") val defenceCrush: Int = 0,
                                              @SerializedName("defence_magic") val defenceMagic: Int = 0,
                                              @SerializedName("defence_ranged") val defenceRanged: Int = 0,

                                              @SerializedName("melee_strength") val meleeStrength: Int = 0,
                                              @SerializedName("ranged_strength") val rangedStrength: Int = 0,
                                              @SerializedName("magic_damage") val magicDamage: Int = 0,
                                              @SerializedName("prayer") val prayer: Int = 0,

                                              @SerializedName("skill_reqs") val skillReqs: Array<SkillRequirement>? = null,
                                              @SerializedName("render_anims") val renderAnimations: IntArray?)
    }

    private data class ItemMetadata(@SerializedName("id") val id: Int,
                                    @SerializedName("name") val name: String,
                                    @SerializedName("members") val members: Boolean?,
                                    @SerializedName("tradeable") val tradeable: Boolean,
                                    @SerializedName("tradeable_on_ge") val tradeableOnGe: Boolean?,
                                    @SerializedName("stackable") val stackable: Boolean?,
                                    @SerializedName("noted") val noted: Boolean?,
                                    @SerializedName("noteable") val noteable: Boolean?,
                                    @SerializedName("linked_id") val linkedId: Int?,
                                    @SerializedName("equipable") val equipable: Boolean?,
                                    @SerializedName("cost") val cost: Int?,
                                    @SerializedName("lowalch") val lowAlch: Int?,
                                    @SerializedName("highalch") val highAlch: Int?,
                                    @SerializedName("weight") val weight: Double,
                                    @SerializedName("buy_limit") val buyLimit: Int?,
                                    @SerializedName("quest_item") val questItem: Any?,
                                    @SerializedName("release_date") val releaseDate: String?,
                                    @SerializedName("examine") val examine: String?,
                                    @SerializedName("url") val url: String?,
                                    @SerializedName("equipment") val equipment: EquipmentMetadata?) {

        internal data class EquipmentMetadata(@SerializedName("attack_stab") val attackStab: Int,
                                              @SerializedName("attack_slash") val attackSlash: Int,
                                              @SerializedName("attack_crush") val attackCrush: Int,
                                              @SerializedName("attack_magic") val attackMagic: Int,
                                              @SerializedName("attack_ranged") val attackRanged: Int,

                                              @SerializedName("defence_stab") val defenceStab: Int,
                                              @SerializedName("defence_slash") val defenceSlash: Int,
                                              @SerializedName("defence_crush") val defenceCrush: Int,
                                              @SerializedName("defence_magic") val defenceMagic: Int,
                                              @SerializedName("defence_ranged") val defenceRanged: Int,

                                              @SerializedName("melee_strength") val meleeStrength: Int,
                                              @SerializedName("ranged_strength") val rangedStrength: Int,
                                              @SerializedName("magic_damage") val magicDamage: Int,
                                              @SerializedName("prayer") val prayer: Int,

                                              @SerializedName("slot") val slot: String?,
                                              @SerializedName("attack_speed") val attackSpeed: Int,

                                              @SerializedName("skill_reqs") val skillReqs: Array<SkillRequirement>?)
    }

    private data class SkillRequirement(@SerializedName("skill") val skill: String,
                                        @SerializedName("level") val level: Int)
}