package gg.rsmod.plugins.content.mechanics.ships

import gg.rsmod.game.model.Tile

enum class TripType(val destination : String, val varp : Int, val varpDelay : Int, val boardingRegion: Int, val dstTile : Tile, val sailCost : Int) {
    PORTSARIM_ENTRANA(
            destination ="Entrana",
            varp = 1,
            varpDelay = 13,
            boardingRegion = 12082,
            dstTile = Tile(2834, 3332,1 ),
            sailCost = 30
    ),
    ENTRANA_PORTSARIM(
            destination ="Port Sarim",
            varp = 2,
            varpDelay = 13,
            boardingRegion = 11316,
            dstTile = Tile(2956, 3143,1 ),
            sailCost = 30
    ),
    CRANDOR_PORTSARIM(
            destination ="Crandor",
            varp = 3,
            varpDelay = 9,
            boardingRegion = 12082,//notcorrect
            dstTile = Tile(3032, 3217,1 ),//notcorrect
            sailCost = 30
    ),
    PORTSARIM_CRANDOR(
            destination ="Port Sarim",
            varp = 4,
            varpDelay = 9,
            boardingRegion = 12082,
            dstTile = Tile(3032, 3217,1 ),
            sailCost = 30
    ),
    PORTSARIM_KARAMAJA(
            destination ="Karamaja",
            varp = 5,
            varpDelay = 8,
            boardingRegion = 12082,
            dstTile = Tile(2956, 3143,1 ),
            sailCost = 30
            ),
    KARMAJA_PORTSARIM(
            destination ="Port Sarim",
            varp = 6,
            varpDelay = 8,
            boardingRegion = 11825,
            dstTile = Tile(3032, 3217,1 ),
            sailCost = 30
    ),
    ARDOUGNE_BRIMHAVEN(
            destination ="Brimhaven",
            varp = 7,
            varpDelay = 3,
            boardingRegion = 10547,
            dstTile = Tile(2775, 3234,1 ),
            sailCost = 30
    ),
    BRIMHAVEN_ARDOUGNE(
            destination ="Ardougne",
            varp = 8,
            varpDelay = 3,
            boardingRegion = 11058,
            dstTile = Tile(2683, 3268,1 ),
            sailCost = 30
    ),
    KHAZARD_SHIPYARD(
            destination ="Shilo Yard of Karmaja",
            varp = 11,
            varpDelay = 15,
            boardingRegion = 10545,
            dstTile = Tile(2683, 3268,1 ),
            sailCost = 30
    ),
    SHIPYARD_KHAZARD(
            destination ="Port Khazard",
            varp = 12,
            varpDelay = 15,
            boardingRegion = 11823,
            dstTile = Tile(2674, 3141,1 ),
            sailCost = 30
    ),
    PORTSARIM_VOIDKNIGHT(
            destination ="Void Knights' Outpost",
            varp = 14,
            varpDelay = 10,
            boardingRegion = 12082,
            dstTile = Tile(2956, 3143,1 ),
            sailCost = 30
    ),
    VOIDKNIGHT_PORTSARIM(
            destination ="Port Sarim",
            varp = 15,
            varpDelay = 10,
            boardingRegion = 11825,
            dstTile = Tile(3032, 3217,1 ),
            sailCost = 30
    ),
    FELDIP_CAIRN(
            destination ="Cairn Isle",
            varp = 16,
            varpDelay = 8,
            boardingRegion = 12082,
            dstTile = Tile(2956, 3143,1 ),
            sailCost = 30
    ),
    CAIRN_FELDIP(
            destination ="Feldip Hills",
            varp = 17,
            varpDelay = 8,
            boardingRegion = 11825,
            dstTile = Tile(3032, 3217,1 ),
            sailCost = 30
    ),
    PORTSARIM_KOUREND(
            destination ="Great Kourend",
            varp = 16,
            varpDelay = 8,
            boardingRegion = 12082,
            dstTile = Tile(1824, 3695,1 ),
            sailCost = 30
    ),
    KOUREND_PORTSARIM(
            destination ="Port Sarim",
            varp = 17,
            varpDelay = 8,
            boardingRegion = 7225,
            dstTile = Tile(3032, 3217,1 ),
            sailCost = 30
    )

}