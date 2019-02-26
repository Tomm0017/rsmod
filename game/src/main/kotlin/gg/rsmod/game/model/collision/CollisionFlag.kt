package gg.rsmod.game.model.collision

enum class CollisionFlag(private val bit: Int) {

    PAWN_NORTH_WEST(1),

    PAWN_NORTH(2),

    PAWN_NORTH_EAST(3),

    PAWN_EAST(4),

    PAWN_SOUTH_EAST(5),

    PAWN_SOUTH(6),

    PAWN_SOUTH_WEST(7),

    PAWN_WEST(8),

    PROJECTILE_NORTH_WEST(9),

    PROJECTILE_NORTH(10),

    PROJECTILE_NORTH_EAST(11),

    PROJECTILE_EAST(12),

    PROJECTILE_SOUTH_EAST(13),

    PROJECTILE_SOUTH(14),

    PROJECTILE_SOUTH_WEST(15),

    PROJECTILE_WEST(16);

    fun getBitAsShort(): Short = (1 shl bit).toShort()

    companion object {

        val values = enumValues<CollisionFlag>()

        private val pawnFlags = arrayOf(
                PAWN_NORTH_WEST,
                PAWN_NORTH,
                PAWN_NORTH_EAST,
                PAWN_WEST,
                PAWN_EAST,
                PAWN_SOUTH_WEST,
                PAWN_SOUTH,
                PAWN_SOUTH_EAST)

        private val projectileFlags = arrayOf(
                PROJECTILE_NORTH_WEST,
                PROJECTILE_NORTH,
                PROJECTILE_NORTH_EAST,
                PROJECTILE_WEST,
                PROJECTILE_EAST,
                PROJECTILE_SOUTH_WEST,
                PROJECTILE_SOUTH,
                PROJECTILE_SOUTH_EAST)

        fun getFlags(projectiles: Boolean): Array<CollisionFlag> = if (projectiles) projectileFlags() else pawnFlags()

        fun pawnFlags() = pawnFlags

        fun projectileFlags() = projectileFlags
    }
}