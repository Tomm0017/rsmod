package gg.rsmod.game.model.droptable

data class NpcDropTableDef(
        val rolls: Int,
        val always_table: DropTable,
        val common_table: DropTable,
        val uncommon_table: DropTable,
        val rare_table: DropTable,
        val veryrare_table: DropTable
) {
    companion object {
        private val DEFAULT_ALWAYS_TABLE = DropTable("""
        {
            "percentage": 0.0,
            "items": []
        }
    """.trimIndent())
        private val DEFAULT_ROLLS = 0
        private val DEFAULT_COMMON_TABLE = DropTable("""
        {
            "percentage": 0.0,
            "items": []
        }
    """.trimIndent())
        private val DEFAULT_UNCOMMON_TABLE = DropTable("""
        {
            "percentage": 0.0,
            "items": []
        }
    """.trimIndent())
        private val DEFAULT_RARE_TABLE = DropTable("""
        {
            "percentage": 0.0,
            "items": []
        }
    """.trimIndent())
        private val DEFAULT_VERYRARE_TABLE = DropTable("""
        {
            "percentage": 0.0,
            "items": []
        }
    """.trimIndent())


        val DEFAULT = NpcDropTableDef(
                rolls = DEFAULT_ROLLS,
                always_table = DEFAULT_ALWAYS_TABLE,
                common_table = DEFAULT_COMMON_TABLE,
                uncommon_table = DEFAULT_UNCOMMON_TABLE,
                rare_table = DEFAULT_RARE_TABLE,
                veryrare_table = DEFAULT_VERYRARE_TABLE
        )
    }
}