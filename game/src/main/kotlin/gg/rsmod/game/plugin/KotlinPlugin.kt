package gg.rsmod.game.plugin

import gg.rsmod.game.model.World
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(displayName = "Kotlin Plugin", fileExtension = "kts")
abstract class KotlinPlugin(val r: PluginRepository, val world: World)