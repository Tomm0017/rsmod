package gg.rsmod.tools.npc

import com.google.common.base.CharMatcher
import com.google.gson.GsonBuilder
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.combat.NpcCombatDef
import net.runelite.cache.fs.Store
import org.jsoup.Jsoup
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Tom <rspsmods@gmail.com>
 */
class NpcStatsDumper {

    companion object {
        @JvmStatic fun main(vararg args: String) {
            val filestore = Store(Paths.get("./data", "cache").toFile())
            val definitions = DefinitionSet()

            filestore.load()
            definitions.init(filestore)

            NpcStatsDumper().dumpAll(definitions, Paths.get("./data", "cfg", "npc-stats.json"))
        }
    }

    fun dumpAll(definitions: DefinitionSet, output: Path) {
        val defs = hashMapOf<Int, NpcCombatDef>()

        val total = definitions.getCount(NpcDef::class.java)
        npcLoop@ for (i in 0 until total) {
            try {
                val definition = definitions.getNullable(NpcDef::class.java, i) ?: continue
                if (definition.combatLevel > 0) {
                    val name = definition.name.replace(" ", "_")

                    val url = "http://oldschoolrunescape.wikia.com/wiki/$name"
                    val def = fetch(url, definition)
                    if (def != null) {
                        defs[i] = def
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Files.newBufferedWriter(output).use {
            GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
                    .toJson(defs.toList().sortedBy { it.first }.toMap(), it)
        }
    }

    private fun fetch(url: String, definition: NpcDef): NpcCombatDef? {
        val document = Jsoup.connect(url).get()

        val generalData = document.getElementsByClass("pi-item pi-data pi-item-spacing pi-border-color")
        val combatData = document.getElementsByClass("pi-item pi-group pi-border-color")

        var combatLvl = -1
        var hitpoints = -1
        var aggressive = false
        var poison = false
        var meleeMaxHit = -1
        var magicMaxHit = -1
        var rangedMaxHit = -1
        var slayerReq = -1
        var slayerXp = -1.0
        var poisonImmunity = false
        var venomImmunity = false

        val stats = Array(5) { 0 }
        val bonuses = Array(14) { 0 }

        for (value in generalData) {
            val data = value.getElementsByClass("pi-item pi-data pi-item-spacing pi-border-color")
            dataLoop@ for (it in data) {
                val href = it.select("a[href][title]").attr("title").toLowerCase()
                val text = it.getElementsByClass("pi-data-value pi-font").text().trim().toLowerCase()

                try {
                    when (href) {
                        "combat level" -> combatLvl = text.toInt()
                        "hitpoints" -> hitpoints = text.toInt()
                        "aggressiveness" -> aggressive = parseYesOrNo(text)
                        "poisonous" -> poison = parseYesOrNo(text)
                        "monster maximum hit" -> {
                            val maxHits = text.split(",")
                            for (hit in maxHits) {
                                val type = hit.toLowerCase()
                                if (type.contains("melee")) {
                                    meleeMaxHit = CharMatcher.inRange('0', '9').retainFrom(type).toInt() // type.replace("[^\\.0123456789]", "").trim().toInt()
                                } else if (type.contains("dragonfire") || type.contentEquals("magic")) {
                                    magicMaxHit = CharMatcher.inRange('0', '9').retainFrom(type).toInt()
                                } else if (type.contains("range")) {
                                    rangedMaxHit = CharMatcher.inRange('0', '9').retainFrom(type).toInt()
                                } else {
                                    meleeMaxHit = CharMatcher.inRange('0', '9').retainFrom(type).toInt()
                                }
                            }
                        }
                        "slayer" -> {
                            if (!text.all { it.isDigit() }) {
                                continue@dataLoop
                            }
                            val attribute = it.select("a[href][title]")
                            if (attribute.any { it.text().toLowerCase().contains("xp") }) {
                                slayerXp = text.toDouble()
                            } else {
                                slayerReq = text.toInt()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error for npc: ${definition.id}")
                }
            }
        }

        if (combatLvl != definition.combatLevel) {
            return null
        }

        for (value in combatData) {
            val data = value.getElementsByClass("pi-item pi-group pi-border-color")
            for (it in data) {
                val href = it.select("a[href][title]").attr("title").toLowerCase()
                val text = it.getElementsByClass("pi-smart-group-body").text().trim().toLowerCase()

                if (href == "combat") {
                    text.split(" ").forEachIndexed { index, s ->
                        if (s.all { it.isDigit() }) {
                            stats[index] = s.toInt()
                        }
                    }
                } else if (href == "attack") {
                    text.replace("+", "").split(" ").forEachIndexed { index, s ->
                        if (s.all { it.isDigit() }) {
                            bonuses[index] = s.toInt()
                        }
                    }
                } else if (href == "defence") {
                    text.replace("+", "").split(" ").forEachIndexed { index, s ->
                        if (s.all { it.isDigit() }) {
                            bonuses[5 + index] = s.toInt()
                        }
                    }
                } else if (href == "monster's attack bonus") {
                    text.replace("+", "").split(" ").forEachIndexed { index, s ->
                        if (s.all { it.isDigit() }) {
                            bonuses[10 + index] = s.toInt()
                        }
                    }
                } else if (href == "poison") {
                    val split = text.split(" ")
                    if (split[0] == "immune") {
                        poisonImmunity = true
                    }
                    if (split[1] == "immune") {
                        venomImmunity = true
                    }
                }
                //href=attack style, text=melee (crush), magic
            }
        }

        val def = NpcCombatDef(hitpoints = hitpoints, combatLvl = combatLvl, attackLvl = 1, strengthLvl = 1,
                defenceLvl = 1, magicLvl = 1, rangedLvl = 1, meleeMaxHit = meleeMaxHit, magicMaxHit = magicMaxHit,
                rangedMaxHit = rangedMaxHit, attackSpeed = 4, aggressive = aggressive, meleeAnimation = 422,
                magicAnimation = -1, rangedAnimation = -1, poisonChance = if (poison) 0.15 else 0.0,
                poisonImmunity = poisonImmunity, venomImmunity = venomImmunity, slayerReq = slayerReq, slayerXp = slayerXp,
                stats = stats, bonuses = bonuses)

        if (hitpoints != -1) {
            return def
        }
        return null
    }

    private fun parseYesOrNo(text: String): Boolean = if (text.toLowerCase().contains("no")) false else if (text.toLowerCase().contains("yes")) true else throw IllegalArgumentException(text)
}