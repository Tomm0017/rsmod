package gg.rsmod.plugins.content.mechanics.ships


data class GangPlank(val objId: Int, val walkToTile:Tile, val moveToTile :Tile, val boardShip : Boolean, val speakTo : String)

private val Planks = setOf(
        GangPlank(Objs.GANGPLANK_2081, Tile(2956, 3143), Tile(2956, 3143,1),true, "Customer officer"),
        GangPlank(Objs.GANGPLANK_2082,Tile(2956, 3146),Tile(2956, 3146),false, ""),
        GangPlank(Objs.GANGPLANK_2083,Tile(3032, 3217),Tile(3032, 3217, 1), true , "sailors"),
        GangPlank(Objs.GANGPLANK_2084,Tile(3029, 3217),Tile(3029, 3217), false, ""),
        GangPlank(Objs.GANGPLANK_2085,Tile(2683, 3268),Tile(2683, 3268, 1), true, "Customer officer"),
        GangPlank(Objs.GANGPLANK_2086,Tile(2683, 3271),Tile(2683, 3271), false, ""),
        GangPlank(Objs.GANGPLANK_2087,Tile(2775, 3234),Tile(2775, 3234, 1), true, "Captain Barnaby"),
        GangPlank(Objs.GANGPLANK_2088,Tile(2772, 3234),Tile(2772, 3234), false, ""),
        GangPlank(Objs.GANGPLANK_2412,Tile(2834, 3231),Tile(2834, 3231, 1), false, ""),
        GangPlank(Objs.GANGPLANK_2413,Tile(3048, 3234),Tile(3048, 3234), false, ""),
        GangPlank(Objs.GANGPLANK_2414,Tile(2834, 3332),Tile(2834, 3332, 1), false, ""),
        GangPlank(Objs.GANGPLANK_2415,Tile(2834, 3335),Tile(2834, 3335), false, ""),
        GangPlank(Objs.GANGPLANK_17396,Tile(2998, 3032),Tile(2998, 3032, 1), false, ""),
        GangPlank(Objs.GANGPLANK_17397,Tile(3001, 3032),Tile(3001, 3032), false, ""),
        GangPlank(Objs.GANGPLANK_17402,Tile(2834, 3141),Tile(2674, 3141, 1), false, ""),
        GangPlank(Objs.GANGPLANK_17403,Tile(2674, 3144),Tile(2674, 3144), false, "")
)


Planks.forEach { plank ->
    on_obj_option(plank.objId, "Cross") {
        player.queue(TaskPriority.STRONG) {
            player.walkTo(plank.walkToTile, MovementQueue.StepType.FORCED_WALK, detectCollision = false)
            wait(2)
            if(plank.boardShip) {
                player.message("You board the ship.")
                player.message("You must speak to the "+ plank.speakTo +" it will set sail.")
            }
            player.moveTo(plank.moveToTile)
        }
    }
}