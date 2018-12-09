package gg.rsmod.game.fs

import net.runelite.cache.ConfigType
import net.runelite.cache.IndexType
import net.runelite.cache.definitions.EnumDefinition
import net.runelite.cache.definitions.NpcDefinition
import net.runelite.cache.definitions.VarbitDefinition
import net.runelite.cache.definitions.loaders.EnumLoader
import net.runelite.cache.definitions.loaders.NpcLoader
import net.runelite.cache.definitions.loaders.VarbitLoader
import net.runelite.cache.fs.Store
import org.apache.logging.log4j.LogManager

/**
 * Holds all definitions that we need from our [Store].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DefinitionSet {

    companion object {
        private val logger = LogManager.getLogger(DefinitionSet::class.java)
    }

    /**
     * A [HashMap] holding all definitions with their [Class] as key.
     */
    private val defs = hashMapOf<Class<*>, Array<*>>()

    @Throws(RuntimeException::class)
    fun init(store: Store) {
        /**
         * Load [IndexType.CONFIGS] definitions.
         */
        val configs = store.getIndex(IndexType.CONFIGS)!!

        /**
         * Load [Varp]s.
         */
        val varpArchive = configs.getArchive(ConfigType.VARPLAYER.id)!!
        val varpFiles = varpArchive.getFiles(store.storage.loadArchive(varpArchive)!!).files
        val varps = arrayListOf<VarpDefinition>().apply {
            varpFiles.forEach { add(VarpDefinition()) }
        }
        defs[VarpDefinition::class.java] = varps.toTypedArray()

        logger.info("Loaded ${varps.size} varp definitions.")

        /**
         * Load [VarbitDef]s.
         */
        val varbitArchive = configs.getArchive(ConfigType.VARBIT.id)!!
        val varbitFiles = varbitArchive.getFiles(store.storage.loadArchive(varbitArchive)!!).files
        val varbits = arrayListOf<VarbitDefinition>()
        for (file in varbitFiles) {
            val loader = VarbitLoader()
            val def = loader.load(file.fileId, file.contents)
            varbits.add(def)
        }
        defs[VarbitDefinition::class.java] = varbits.toTypedArray()

        logger.info("Loaded ${varbits.size} varbit definitions.")

        /**
         * Load [EnumDefinition]s.
         */
        val enumArchive = configs.getArchive(ConfigType.ENUM.id)!!
        val enumFiles = enumArchive.getFiles(store.storage.loadArchive(enumArchive)!!).files
        val enums = arrayListOf<EnumDefinition>()
        for (file in enumFiles) {
            val loader = EnumLoader()
            val def = loader.load(file.fileId, file.contents)
            enums.add(def)
        }
        defs[EnumDefinition::class.java] = enums.toTypedArray()

        logger.info("Loaded ${enums.size} enum definitions.")

        /**
         * Load [NpcDefinition]s.
         */
        val npcArchive = configs.getArchive(ConfigType.NPC.id)!!
        val npcFiles = npcArchive.getFiles(store.storage.loadArchive(npcArchive)!!).files
        val npcs = arrayListOf<NpcDefinition>()
        for (file in npcFiles) {
            val loader = NpcLoader()
            val def = loader.load(file.fileId, file.contents)
            npcs.add(def)
        }
        defs[NpcDefinition::class.java] = npcs.toTypedArray()

        logger.info("Loaded ${npcs.size} npc definitions.")
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(type: Class<T>): Array<T> {
        return defs[type]!! as Array<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(type: Class<T>, id: Int): T {
        return (defs[type]!!)[id] as T
    }

    fun getCount(type: Class<*>) = defs[type]!!.size
}