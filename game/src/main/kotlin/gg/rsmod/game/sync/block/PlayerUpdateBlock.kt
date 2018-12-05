package gg.rsmod.game.sync.block

import gg.rsmod.game.sync.SyncBlock

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class PlayerUpdateBlock(private val mask: Int) : SyncBlock {

    APPEARANCE(0x1),

    ANIMATION(0x10),

    GFX(0x400),

    CHAT(0x40),

    FACE_TILE(0x2),

    FACE_PAWN(0x8),

    MOVEMENT(0x1000),

    CONTEXT_MENU(0x200),

    FORCE_MOVEMENT(0x100),

    HITMARK(0x80),

    FORCE_CHAT(0x4);

    override fun getMask(): Int = mask
}