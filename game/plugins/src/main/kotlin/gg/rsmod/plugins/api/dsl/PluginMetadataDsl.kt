package gg.rsmod.plugins.api.dsl

import gg.rsmod.game.plugin.KotlinPlugin
import gg.rsmod.game.plugin.PluginMetadata
import gg.rsmod.plugins.api.PluginMetadataBuilder

fun KotlinPlugin.load_metadata(init: PluginMetadataDsl.Builder.() -> Unit) {
    val builder = PluginMetadataDsl.Builder()
    init(builder)
    load_metadata(builder.build())
}

object PluginMetadataDsl {

    @DslMarker
    annotation class PluginMetadataMarker

    @PluginMetadataMarker
    class Builder {

        private val metadataBuilder = PluginMetadataBuilder()

        /**
         * @see [PluginMetadata.propertyFileName]
         */
        var propertyFileName: String = ""
            set(value) {
                metadataBuilder.setPropertyFileName(value)
            }

        /**
         * @see [PluginMetadata.name]
         */
        var name: String = ""
            set(value) {
                metadataBuilder.setName(value)
            }

        /**
         * @see [PluginMetadata.authors]
         */
        var author: String = ""
            set(value) {
                metadataBuilder.addAuthor(value)
            }

        /**
         * @see [PluginMetadata.description]
         */
        var description: String = ""
            set(value) {
                metadataBuilder.setDescription(value)
            }

        internal fun build(): PluginMetadata = metadataBuilder.build()

        /**
         * @see [PluginMetadata.authors]
         */
        fun authors(init: AuthorBuilder.() -> Unit) {
            val authors = mutableSetOf<String>()
            val builder = AuthorBuilder(authors)
            init(builder)
            metadataBuilder.setAuthors(authors)
        }

        /**
         * @see [PluginMetadata.description]
         */
        fun description(init: DescriptionBuilder.() -> Unit) {
            val builder = DescriptionBuilder()
            init(builder)
            metadataBuilder.setDescription(builder.description.toString())
        }

        /**
         * @see [PluginMetadata.properties]
         */
        fun properties(vararg properties: Pair<String, *>) {
            properties.forEach { pair ->
                metadataBuilder.addProperty(pair.first, pair.second!!)
            }
        }
    }

    @PluginMetadataMarker
    class AuthorBuilder(private val authors: MutableSet<String>) {

        infix fun add(author: String) {
            authors.add(author)
        }

        infix fun add(authors: Array<String>) {
            this.authors.addAll(authors)
        }

        operator fun String.unaryPlus() = add(this)
    }

    @PluginMetadataMarker
    class DescriptionBuilder {

        var description = StringBuilder()

        infix fun add(line: String) {
            description.append(line)
        }

        operator fun String.unaryPlus() = add(this)
    }
}