package gg.rsmod.ui.plugin

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Hyperlink
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ModalController : Initializable {

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        downloadKotlinCompiler.setOnAction {
            try {
                Desktop.getDesktop().browse(URI(downloadKotlinCompiler.text))
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }

    @FXML
    lateinit var downloadKotlinCompiler: Hyperlink
}