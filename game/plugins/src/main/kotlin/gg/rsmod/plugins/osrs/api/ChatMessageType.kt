package gg.rsmod.plugins.osrs.api

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class ChatMessageType(val id: Int) {
    SERVER(0),
    PUBLIC_MOD(1),
    PUBLIC(2),
    RECEIVE_PRIVATE(3),
    RECEIVE_TRADE_REQUEST(4),
    FRIEND_STATUS(5),
    SEND_PRIVATE(6),
    RECEIVE_PRIVATE_FROM_MOD(7),
    CLAN_CHAT(9),
    CLAN_CHAT_INFO(11),
    SEND_TRADE_REQUEST(12),
    SUBMIT_ABUSE_REPORT(26),
    EXAMINE_ITEM(27),
    EXAMINE_NPC(28),
    EXAMINE_OBJECT(29),
    ADD_FRIEND(30),
    ADD_IGNORE(31),
    AUTO_CHAT(90),
    GAME(99),
    TRADE(101),
    DUEL(103),
    FILTERED(109)
}