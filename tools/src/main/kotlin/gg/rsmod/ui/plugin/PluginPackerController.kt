package gg.rsmod.ui.plugin

import gg.rsmod.tools.plugin.PluginPacker
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginPackerController : Initializable {

    var icons: List<Image>? = null

    lateinit var primaryStage: Stage

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        compilerButton.setOnAction {
            var oldDirectory: File? = null
            while (true) {
                val chooser = DirectoryChooser()
                chooser.title = "Select path to your Kotlin's kotlinc bin folder"

                if (compilerPath.text.isNotBlank()) {
                    val oldPath = Paths.get(compilerPath.text)
                    chooser.initialDirectory = if (Files.exists(oldPath)) oldPath.toFile() else null
                } else if (oldDirectory != null) {
                    chooser.initialDirectory = oldDirectory
                }

                val folder = chooser.showDialog(primaryStage)
                if (folder != null) {
                    if (Files.exists(Paths.get(folder.absolutePath).resolve("kotlinc")) && Files.exists(Paths.get(folder.absolutePath).resolve("kotlinc.bat"))) {
                        compilerPath.text = folder.absolutePath
                        break
                    } else {
                        oldDirectory = folder
                        alertDialog(Alert.AlertType.ERROR, "Error", "That directory is not valid!",
                                "Directory must contain kotlinc & kotlinc.bat files.", primaryStage, icons)
                    }
                } else {
                    break
                }
            }
        }

        gameJarButton.setOnAction {
            val chooser = FileChooser()
            chooser.title = "Select your game.jar file"
            chooser.extensionFilters.add(FileChooser.ExtensionFilter("JAR", "*.jar"))

            if (gameJarPath.text.isNotBlank()) {
                val oldPath = Paths.get(gameJarPath.text)
                chooser.initialDirectory = if (Files.exists(oldPath)) oldPath.toFile() else null
            }

            val file = chooser.showOpenDialog(primaryStage)
            if (file != null) {
                gameJarPath.text = file.absolutePath
            }
        }

        packJarButton.setOnAction {
            val compiler = Paths.get(compilerPath.text)
            val gameJar = Paths.get(gameJarPath.text)
            val sourceFolder = Paths.get(sourcePath.text)
            val outputFolder = Paths.get(outputPath.text)
            val plugin = pluginName.text

            if (compilerPath.text.isBlank() || !Files.exists(compiler) || !Files.isDirectory(compiler)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The Kotlin compiler folder does not exist!",
                        "Directory: ${compilerPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (gameJarPath.text.isBlank() || !Files.exists(gameJar) || Files.isDirectory(gameJar)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The game jar file does not exist!",
                        "File: ${gameJarPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (outputPath.text.isBlank() || !Files.exists(outputFolder) || !Files.isDirectory(outputFolder)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The output folder does not exist!",
                        "Directory: ${outputPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (sourcePath.text.isBlank() || !Files.exists(sourceFolder) || !Files.isDirectory(sourceFolder)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The plugin source folder does not exist!",
                        "Directory: ${sourcePath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (plugin.isBlank()) {
                alertDialog(Alert.AlertType.ERROR, "Error", null,
                        "You have not set a plugin name.", primaryStage, icons)
                return@setOnAction
            }

            PluginPacker().compileBinary(compilerPath = compiler.toAbsolutePath().toString(),
                    gameJar = gameJar.toAbsolutePath().toString(),
                    pluginName = plugin,
                    outputPath = outputFolder,
                    paths = Files.walk(sourceFolder).toList())
        }

        packZipButton.setOnAction {
            val compiler = Paths.get(compilerPath.text)
            val gameJar = Paths.get(gameJarPath.text)
            val sourceFolder = Paths.get(sourcePath.text)
            val outputFolder = Paths.get(outputPath.text)
            val plugin = pluginName.text

            if (compilerPath.text.isBlank() || !Files.exists(compiler) || !Files.isDirectory(compiler)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The Kotlin compiler folder does not exist!",
                        "Directory: ${compilerPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (gameJarPath.text.isBlank() || !Files.exists(gameJar) || Files.isDirectory(gameJar)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The game jar file does not exist!",
                        "File: ${gameJarPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (outputPath.text.isBlank() || !Files.exists(outputFolder) || !Files.isDirectory(outputFolder)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The output folder does not exist!",
                        "Directory: ${outputPath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (sourcePath.text.isBlank() || !Files.exists(sourceFolder) || !Files.isDirectory(sourceFolder)) {
                alertDialog(Alert.AlertType.ERROR, "Error", "The plugin source folder does not exist!",
                        "Directory: ${sourcePath.text}", primaryStage, icons)
                return@setOnAction
            }

            if (plugin.isBlank()) {
                alertDialog(Alert.AlertType.ERROR, "Error", null,
                        "You have not set a plugin name.", primaryStage, icons)
                return@setOnAction
            }

            PluginPacker().compileSource(pluginName = plugin,
                    outputPath = outputFolder,
                    paths = Files.walk(sourceFolder).toList())
        }
    }

    private fun alertDialog(type: Alert.AlertType, title: String, header: String?, context: String, parent: Stage?, icon: List<Image>?) {
        val alert = Alert(type)
        alert.title = title
        alert.headerText = header
        alert.contentText = context
        if (parent != null) {
            alert.initModality(Modality.APPLICATION_MODAL)
            alert.initOwner(parent)
        }
        if (icon != null) {
            val stage = alert.dialogPane.scene.window as Stage
            stage.icons.addAll(icon)
        }
        alert.showAndWait()
    }

    @FXML
    private lateinit var compilerPath: TextField

    @FXML
    private lateinit var compilerButton: Button

    @FXML
    private lateinit var gameJarPath: TextField

    @FXML
    private lateinit var gameJarButton: Button

    @FXML
    private lateinit var sourcePath: TextField

    @FXML
    private lateinit var sourceButton: Button

    @FXML
    private lateinit var outputPath: TextField

    @FXML
    private lateinit var outputButton: Button

    @FXML
    private lateinit var pluginName: TextField

    @FXML
    private lateinit var packJarButton: Button

    @FXML
    private lateinit var packZipButton: Button
}