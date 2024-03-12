package gg.rsmod.game.service.cache

import gg.rsmod.util.ServerProperties
import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarStyle
import mu.KotlinLogging
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.file.*
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.zip.ZipInputStream
import kotlin.system.exitProcess

class CacheService {

    companion object {
        private val logger = KotlinLogging.logger {}

        private val dataDir = File("./data/")
        private val cachePath = File(dataDir,"cache/")
        private val xteasPath = File(dataDir,"xteas/")

        @JvmStatic
        fun main(args: Array<String>) {
            val yamlFile = File("./game.example.yml")
            val gameProperties = ServerProperties()
            gameProperties.loadYaml(yamlFile)
            val cacheBuildValue = gameProperties.get<Int>("cache-build")?: 101

            val build = cacheBuildValue
            if (downloadCache(build)) {
                unzip(File(cachePath, "cache.zip").absolutePath)
                File(cachePath, "cache.zip").delete()
                saveXteas(build)
            } else {
                logger.info { "Unable to download https://archive.openrs2.org/caches/${build}/disk.zip" }
                exitProcess(0)
            }
        }

        private fun saveXteas(id: Int) {
            val xteasFile: File = xteasPath.toPath().resolve("xteas.json").toFile()

            logger.info { "Saving Xteas" }

            try {
                xteasPath.mkdirs()

                val url = URL("https://archive.openrs2.org/caches/$id/keys.json")
                val uri: URI = url.toURI()

                xteasFile.createNewFile()
                xteasFile.writeText(uri.toURL().readText())

                logger.info { "Xteas saved successfully" }
            } catch (e: Exception) {
                logger.error { "Unable to write Xteas: $e" }
            }
        }

        private fun downloadCache(build : Int) : Boolean {
            try {
                val url: URL = URI.create("https://archive.openrs2.org/caches/$build/disk.zip").toURL()
                val httpConnection = url.openConnection() as HttpURLConnection
                val completeFileSize = 55179361L

                val input : InputStream = httpConnection.inputStream
                val out = FileOutputStream(File(cachePath,"cache.zip"))

                val data = ByteArray(1024)
                var downloadedFileSize: Long = 0
                var count: Int

                val pb = progress("Downloading Cache [${build}]",completeFileSize)

                while (input.read(data, 0, 1024).also { count = it } != -1) {
                    downloadedFileSize += count.toLong()
                    pb.stepBy(count.toLong())
                    out.write(data, 0, count)
                }
                input.close()
                out.close()
                pb.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

        }

        private fun unzip(zipFilePath: String) {
            ZipInputStream(FileInputStream(zipFilePath)).use { zipIn ->
                var entry = zipIn.nextEntry

                while (entry != null) {
                    val filePath = File(dataDir,entry.name)
                    if (!entry.isDirectory) {
                        extractFile(zipIn, filePath.absolutePath)
                    } else {
                        filePath.mkdir()
                    }
                    zipIn.closeEntry()
                    entry = zipIn.nextEntry
                }
                zipIn.close()
            }
        }

        private fun extractFile(zipIn: ZipInputStream, filePath: String) {
            FileOutputStream(filePath).use { fos ->
                val buffer = ByteArray(1024)
                var bytesRead: Int

                while (zipIn.read(buffer).also { bytesRead = it } != -1) {
                    fos.write(buffer, 0, bytesRead)
                }
                fos.close()
            }

        }

        private fun progress(task: String, amt: Long): ProgressBar {
            return ProgressBar(
                task,
                amt,
                1,
                System.err,
                ProgressBarStyle.ASCII,
                "",
                1,
                true,
                null,
                ChronoUnit.SECONDS,
                0L,
                Duration.ZERO
            )
        }
    }
}