package gg.rsmod.game.plugin

import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Tom <rspsmods@gmail.com>
 */
@KotlinScript(
        displayName = "Kotlin Plugin",
        fileExtension = "kts"
)
abstract class KotlinPlugin(val r: PluginRepository)