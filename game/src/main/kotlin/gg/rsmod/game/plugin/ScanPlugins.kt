package gg.rsmod.game.plugin

/**
 * An annotation that indicates a function should be invoked while scanning through the
 * plugin path.
 *
 * @author Tom <rspsmods@gmail.com>
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScanPlugins