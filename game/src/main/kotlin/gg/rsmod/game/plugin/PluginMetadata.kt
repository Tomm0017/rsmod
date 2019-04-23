package gg.rsmod.game.plugin

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains data relevant to a [Plugin].
 *
 * @param propertyFileName the file name that will store the metadata. This should
 * be the <strong>file-name</strong>, not the full path and not including the
 * extension.
 *
 * @param name the name of our plugin. There are no restrictions to what you can
 * name a plugin.
 *
 * @param description a short description of the plugin.
 *
 * @param authors a collection of the plugin authors.
 *
 * @param properties a map of properties that can be accessed by the plugin.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class PluginMetadata(
        @Transient val propertyFileName: String?,
        @JsonProperty("name") val name: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("authors") val authors: Set<String>,
        @JsonProperty("properties") val properties: Map<String, Any>)