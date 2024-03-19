package gg.rsmod.game.service.cache

import dev.openrune.cache.tools.CacheUpdater

import gg.rsmod.util.ServerProperties
import java.io.*

class CacheService {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val yamlFile = File("./game.example.yml")
            val gameProperties = ServerProperties()
            gameProperties.loadYaml(yamlFile)
            val cacheBuildValue = gameProperties.get<Int>("cache-build") ?: 101
            CacheUpdater.init(cacheBuildValue, File("./data/"))
        }
    }
}