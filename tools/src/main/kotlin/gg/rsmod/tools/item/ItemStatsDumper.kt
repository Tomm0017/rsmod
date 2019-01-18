package gg.rsmod.tools.item

import com.google.gson.GsonBuilder
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import gg.rsmod.game.model.item.ItemStats
import net.runelite.cache.fs.Store
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import org.apache.commons.io.FileUtils
import org.apache.logging.log4j.LogManager
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class ItemStatsDumper {

    private val log = LogManager.getLogger(ItemStatsDumper::class.java)

    private val downloadPageURL = "http://oldschoolrunescape.wikia.com/wiki/Special:Statistics"

    companion object {
        @JvmStatic fun main(vararg args: String) {
            val filestore = Store(Paths.get("./data", "cache").toFile())
            val definitions = DefinitionSet()

            filestore.load()
            definitions.init(filestore)

            ItemStatsDumper().dumpAll(definitions, Paths.get("./data", "cfg", "item-stats.json"))
        }
    }

    private fun dumpSpecifics(definitions: DefinitionSet, itemIds: ArrayList<Int>?, bonuses: Boolean = false, attackSpeed: Boolean = false, equipmentInfo: Boolean = false) {
        val dumpedDefinitions = HashMap<Int, ItemStats>()
        val definitionsNotFound = ArrayList<ItemDef>()

        val pages = itemWikiaPages

        val totalItems = definitions.getCount(ItemDef::class.java)
        val itemDefs = arrayListOf<ItemDef>()
        val ids: ArrayList<Int>
        if (itemIds == null) {
            ids = arrayListOf()
            for (i in 0 until totalItems) {
                ids.add(i)
            }
        } else {
            ids = itemIds
        }
        for (i in ids) {
            val item = definitions.getNullable(ItemDef::class.java, i) ?: continue
            val name = item.name.toLowerCase()
            if (name != "null" && !item.isNoted()) {
                val sameName = itemDefs.firstOrNull { it.name.toLowerCase() == name }
                val legit = if (sameName != null) item.chooseLegit(sameName) else null
                if (sameName == null || legit != null && legit != sameName) {
                    if (legit != null && sameName != null) {
                        itemDefs.remove(sameName)
                    }
                    itemDefs.add(item)
                }
            }
        }

        itemDefs.forEach { item ->
            val name = item.name.toLowerCase()
            val text = pages[name]

            // if the item does not exist in the Wikia, add it to the not found list
            if (text == null) {
                definitionsNotFound.add(item)
                return@forEach
            }

            val def = dump(item, text)
            // apply the configurations we just dumped to all of the items of the same name
            dumpedDefinitions[item.id] = def
        }

        if (bonuses) {
            // Note(Tom): dump bonuses here if u want
        }
        if (equipmentInfo) {
            // Note(Tom): dump equipment info here if u want
        }
        if (attackSpeed) {
            // Note(Tom): dump attack speed here if u want
        }

        log.info("Dumped ${dumpedDefinitions.size} items")
        log.info("${definitionsNotFound.size} items could not be found in the wikia")
    }

    private fun dumpAll(definitions: DefinitionSet, outputPath: Path) {
        val dumpedDefinitions = arrayListOf<Any>()
        val definitionsNotFound = ArrayList<ItemDef>()

        val pages = itemWikiaPages

        val totalItems = definitions.getCount(ItemDef::class.java)
        val itemDefs = arrayListOf<ItemDef>()
        for (i in 1 until totalItems) {
            val item = definitions.getNullable(ItemDef::class.java, i) ?: continue
            val name = item.name.toLowerCase()
            if (name != "null" && !item.isNoted()) {
                val sameName = itemDefs.firstOrNull { it.name.toLowerCase() == name }
                val legit = if (sameName != null) item.chooseLegit(sameName) else null
                if (sameName == null || legit != null && legit != sameName) {
                    if (legit != null && sameName != null) {
                        itemDefs.remove(sameName)
                    }
                    itemDefs.add(item)
                }
            }
        }

        itemDefs.forEach { item ->
            val name = item.name.toLowerCase()
            val text = pages[name]

            // if the item does not exist in the Wikia, add it to the not found list
            if (text == null) {
                definitionsNotFound.add(item)
                return@forEach
            }

            val def = dump(item, text)
            // apply the configurations we just dumped to all of the items of the same name
            dumpedDefinitions.add(def)
        }

        FileWriter(outputPath.toFile()).use {
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
            gson.toJson(dumpedDefinitions, it)
        }

        log.info("Dumped ${dumpedDefinitions.size} items")
        log.info("${definitionsNotFound.size} items could not be found in the wikia")
    }

    private fun dump(definition: ItemDef, text: List<String>): ItemStats {
        val builder = WikiDefBuilder()

        val attackBonuses = IntArray(5) { 0 }
        val defensiveBonuses = IntArray(5) { 0 }
        val otherBonuses = IntArray(4) { 0 }

        text.forEach {
            val rawValue = it.trim()
            val value = it.getWikiaValue().trim()
            when {
                rawValue.startsWith("weight") -> {
                    val weight = value.parseWeight()
                    if (weight > 0) {
                        builder.weight = value.parseWeight()
                    }
                }
                rawValue.startsWith("destroy") -> {
                    if (!value.equals("drop", true)) {
                        builder.destroyMessage = value
                    }
                }
                rawValue.startsWith("slot") -> {
                    val equipSlot = parseItemSlot(value)
                    if (value.toLowerCase() == "2h") {
                        builder.equipType = 5
                    }
                    builder.equipSlot = equipSlot
                }
                rawValue.startsWith("aspeed") -> builder.attackSpeed = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("astab=") -> attackBonuses[0] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("aslash=") -> attackBonuses[1] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("acrush=") -> attackBonuses[2] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("amagic=") -> attackBonuses[3] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("arange=") -> attackBonuses[4] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("dstab=") -> defensiveBonuses[0] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("dslash=") -> defensiveBonuses[1] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("dcrush=") -> defensiveBonuses[2] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("dmagic=") -> defensiveBonuses[3] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("drange=") -> defensiveBonuses[4] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("str=") -> otherBonuses[0] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("rstr=") -> otherBonuses[1] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("mdmg=") -> otherBonuses[2] = value.parseWikiaInt()
                rawValue.replace(" ", "").startsWith("prayer=") -> otherBonuses[3] = value.parseWikiaInt()
            }
        }

        builder.item = definition.id
        builder.name = definition.name
        if (builder.equipType == 0) {
            val name = builder.name.toLowerCase()
            if (builder.equipSlot == 0) {
                when {
                    name.contains("full helm") -> builder.equipType = 11
                    name.contains("helm") -> builder.equipType = 8
                    name.contains(" hat") -> builder.equipType = 8
                }
            } else if (builder.equipSlot == 4) {
                when {
                    name.contains("chestplate") -> builder.equipType = 6
                    name.contains("body") && !name.contains("hide") -> builder.equipType = 6
                }
            }
        }
        if (attackBonuses.sum() > 0.0 || defensiveBonuses.sum() > 0.0 || otherBonuses.sum() > 0.0) {
            attackBonuses.forEach { bonus ->
                builder.bonuses.add(bonus)
            }
            defensiveBonuses.forEach { bonus ->
                builder.bonuses.add(bonus)
            }
            otherBonuses.forEach { bonus ->
                builder.bonuses.add(bonus)
            }
        }

        return builder.build()
    }

    /**
     * Get item slots for OSRS
     */
    private fun parseItemSlot(value: String): Int = when (value.toLowerCase()) {
        "body" -> 4
        "legs" -> 7
        "neck", "necklace" -> 2
        "weapon", "2h" -> 3
        "ammo" -> 13
        "cape" -> 1
        "head" -> 0
        "hands" -> 9
        "shield" -> 5
        "feet" -> 10
        "ring" -> 12
        else -> throw RuntimeException("Unhandled item slot: $value")
    }

    private val wikia: Document by lazy {
        log.info("Downloading wikia dump...")

        val archive = File("archive.7z")
        archive.deleteOnExit()
        FileUtils.copyURLToFile(retrieveDownloadURL(), archive)

        log.info("Finished downloading wikia dump! Size: ${archive.length() / 1024 / 1024}Mb")

        val archiveFile = SevenZFile(archive)
        var entry: SevenZArchiveEntry?
        var contents = ""

        archiveFile.use {
            entry = archiveFile.nextEntry
            while (entry != null) {
                if (entry!!.isDirectory) {
                    entry = archiveFile.nextEntry
                    continue
                }

                val contentBytes = ByteArrayOutputStream()

                val buffer = ByteArray(2048)
                var bytesRead = archiveFile.read(buffer)
                while (bytesRead != -1) {
                    contentBytes.write(buffer, 0, bytesRead)
                    bytesRead = archiveFile.read(buffer)
                }

                contents = contentBytes.toString("UTF-8")
                break
            }
        }

        val document = Jsoup.parse(contents, "", Parser.xmlParser())
        log.info("Finished parsing XML into a Jsoup Document")

        document
    }

    private val itemWikiaPages: HashMap<String, List<String>> by lazy {
        wikia.select("page")
                .filter { !it.select("title").text().startsWith("file:", true) }
                .filter { !it.select("title").text().startsWith("update:", true) }
                .filter { !it.select("title").text().startsWith("Module:", true) }
                .filter { !it.children().map { it.tagName() }.contains("redirect") }
                .filter { it.select("text").text().contains("{{Infobox Item") }
                .map { it.select("title").text().trim().toLowerCase() to it.select("text").text().trim().split('|') }
                .toMap() as HashMap
    }

    private fun retrieveDownloadURL(): URL {
        val document = Jsoup.connect(downloadPageURL).get()
        val link = document.select("table.mw-statistics-table")[1].select("a").attr("href")
        return URL(link)
    }

    private fun String.parseWikiaBoolean(): Boolean {
        return if (this.toLowerCase() == "no" || this.toIntOrNull() == 0) false
        else if (this.toLowerCase() == "yes" || this.toIntOrNull() == 1) true
        else this.toLowerCase().toBoolean()
    }

    private fun String.parseWikiaInt() = this.replace("+", "").trim().toIntOrNull() ?: -1

    private fun String.parseWeight() = this.removeSuffix("kg").trim().toDoubleOrNull() ?: 0.0

    private fun String.getWikiaValue(): String {
        val value = this.substring(this.indexOf('=') + 1).trim()

        return if (value.contains("}")) {
            value.substring(0, value.indexOf('}'))
        } else {
            value
        }
    }

    private fun ItemDef.chooseLegit(other: ItemDef): ItemDef? {
        val thisNonNotable = this.noteTemplateId > 0 || this.noteLinkId < 1 // This doesn't have noted form
        val otherNonNotable = other.noteTemplateId > 0 || other.noteLinkId < 1 // Other doesn't have noted form
        val thisPlaceholdable = this.placeholderId > 0 && this.placeholderTemplateId != 14401 // This has a placeholder item
        val otherPlaceholdable = other.placeholderId > 0 && other.placeholderTemplateId != 14401 // This has a placeholder item

        return when {
            thisNonNotable == otherNonNotable && thisPlaceholdable && otherPlaceholdable -> null
            !thisNonNotable && otherNonNotable || thisPlaceholdable && !otherPlaceholdable -> this
            else -> other
        }
    }

    private class WikiDefBuilder {
        var name = ""
        var item = -1
        var weight = 0.0
        var equipSlot = -1
        var equipType = 0
        var attackSpeed = 0
        var bonuses = arrayListOf<Int>()
        var destroyMessage = ""

        fun build(): ItemStats = ItemStats(item, name, weight, equipSlot, equipType, attackSpeed, bonuses)
    }
}