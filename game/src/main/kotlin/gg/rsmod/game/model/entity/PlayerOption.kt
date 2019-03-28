package gg.rsmod.game.model.entity

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the default player interaction options
 */
enum class PlayerOption(val text: String, val index: Int) {
    ATTACK("Attack", 0),
    CHALLENGE("Challenge", 0),
    FOLLOW("Follow", 2),
    TRADE("Trade with", 3),
    REPORT("Report", 4);

    companion object {
        val values = enumValues<PlayerOption>()

        private val indices = values.associate { it.index to it }

        fun byIndex(index: Int) : PlayerOption? = indices[index]
    }
}