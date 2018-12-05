package gg.rsmod.net.codec.game

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class GameDecoderState {
    OPCODE,
    LENGTH,
    PAYLOAD
}