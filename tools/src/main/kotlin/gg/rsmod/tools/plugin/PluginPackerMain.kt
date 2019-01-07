package gg.rsmod.tools.plugin

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Options
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

/**
 * @author Tom <rspsmods@gmail.com>
 */
object PluginPackerMain {

    @JvmStatic fun main(vararg args: String) {
        val packer = PluginPacker()

        if (args.isNotEmpty()) {
            val options = Options()
            //"C:/Program Files/kotlinc/bin/"
            //"game/build/libs/game-0.0.1.jar"
            options.addOption("c", true, "The path to your Kotlin compiler")
            options.addOption("g", true, "The path to your game distribution jar")
            options.addOption("s", true, "The path to the plugin files you would like to pack")
            options.addOption("p", true, "The name you would like to give your packed plugin")

            val parser = DefaultParser()
            val commands = parser.parse(options, args)

            val pluginName = commands.getOptionValue('p')
            val source = Paths.get(commands.getOptionValue('s'))
            val output = Paths.get(".", "plugins").resolve("$pluginName.tmp")

            val success = packer.compileBinary(
                    compilerPath = commands.getOptionValue('c'),
                    gameJar = commands.getOptionValue('g'),
                    plugin = output,
                    paths = Files.walk(source).toList())
            if (success) {
                try {
                    packer.zipFiles(output = Paths.get(".", "plugins").resolve("$pluginName.jar"),
                            paths = Files.walk(output).toList(), removeParent = output.toString().replace("\\", "/"))
                } finally {
                    output.toFile().deleteRecursively()
                }
            }
        } else {
            // TODO(Tom): launch gui
        }
    }
}