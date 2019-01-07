package gg.rsmod.ui.plugin

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import javax.swing.SwingUtilities

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PluginPackerUI : Application() {

    fun start() {
        launch()
    }

    override fun start(primaryStage: Stage) {
        val loader = FXMLLoader(PluginPackerUI::class.java.getResource("/ui/plugin/ui.fxml"))
        primaryStage.title = "RS Mod Tool - Plugin Packer"
        //primaryStage.icons.addAll() // TODO(Tom): add favicons :)
        primaryStage.scene = Scene(loader.load())
        primaryStage.isResizable = true
        primaryStage.show()

        val controller = loader.getController<PluginPackerController>()
        controller.primaryStage = primaryStage

        SwingUtilities.invokeAndWait {
            primaryStage.scene.stylesheets.clear()
            primaryStage.scene.stylesheets.add(PluginPackerUI::class.java.getResource("/ui/styles/modena/modena.css").toURI().toString())
        }

        primaryStage.setOnCloseRequest {
            Platform.exit()
            System.exit(0)
        }
    }
}