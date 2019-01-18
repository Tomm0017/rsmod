package gg.rsmod.tools.npc

import com.google.gson.GsonBuilder
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.NpcDef
import gg.rsmod.game.model.NpcCombatDef
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
                    val document = Jsoup.connect(url).get()

                    val multiple = document.getElementsByClass("switch-infobox")
                    if (multiple != null && multiple.isNotEmpty()) {
                        val items = multiple[0].getElementsByClass("item")
                        for (element in items) {
                            val values = element.getElementsByClass("pi-item pi-data pi-item-spacing pi-border-color")

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

                            for (value in values) {
                                val data = value.getElementsByClass("pi-item pi-data pi-item-spacing pi-border-color")
                                data.forEach {
                                    val href = it.select("a[href][title]").attr("title").toLowerCase()
                                    val text = it.getElementsByClass("pi-data-value pi-font").text().trim().toLowerCase()

                                    try {
                                        when (href) {
                                            "combat level" -> combatLvl = text.toInt()
                                            "hitpoints" -> hitpoints = text.toInt()
                                            "aggressiveness" -> aggressive = parseYesOrNo(text)
                                            "poisonous" -> poison = parseYesOrNo(text)
                                            "monster maximum hit" -> {
                                                if (text.any { !Character.isDigit(it) }) {
                                                    println("non digit: $text")
                                                } else {
                                                    meleeMaxHit = text.toInt()
                                                }
                                            }
                                            "slayer" -> if (it.select("a[href][title]").any { it.text().toLowerCase().contains("xp") }) slayerXp = text.toDouble() else slayerReq = text.toInt()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            if (combatLvl != definition.combatLevel) {
                                continue@npcLoop
                            }

                            val def = NpcCombatDef(hitpoints = hitpoints, combatLvl = combatLvl, attackLvl = 1, strengthLvl = 1,
                                    defenceLvl = 1, magicLvl = 1, rangedLvl = 1, meleeMaxHit = meleeMaxHit, magicMaxHit = magicMaxHit,
                                    rangedMaxHit = rangedMaxHit, aggressive = aggressive, poisonChance = if (poison) 0.15 else 0.0,
                                    poisonImmunity = poisonImmunity, venomImmunity = venomImmunity, slayerReq = slayerReq, slayerXp = slayerXp.toDouble(),
                                    stats = Array(5) { 0 }, bonuses = Array(14) { 0 })

                            if (hitpoints != -1) {
                                defs[i] = def
                                println(def)
                            }
                        }
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

    private fun parseYesOrNo(text: String): Boolean = if (text.toLowerCase().contains("no")) false else if (text.toLowerCase().contains("yes")) true else throw IllegalArgumentException(text)
}