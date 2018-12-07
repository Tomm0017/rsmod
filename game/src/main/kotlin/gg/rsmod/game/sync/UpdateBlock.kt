package gg.rsmod.game.sync

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class UpdateBlock(val value: Int) {

    APPEARANCE(0x1),

    ANIMATION(0x0),

    GFX(0x0),

    CHAT(0x0),

    FACE_TILE(0x0),

    FACE_PAWN(0x0),

    MOVEMENT(0x0),

    CONTEXT_MENU(0x0),

    FORCE_MOVEMENT(0x0),

    HITMARK(0x0),

    FORCE_CHAT(0x0);
}