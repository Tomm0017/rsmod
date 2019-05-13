package gg.rsmod.plugins.content.skills.fishing.data

/**
 *
 * @author Vashmeed
 *
 * @property toolId The tool required for the specified option
 * @property levelReq The minimum level required to fish
 * @property animationId The animation the player plays when using this option
 * @property bait The bait the fishing spot requires if any
 * @property option The string of the npc option text
 * @property fishType the amount of time that the npc stuns the player for
 */
enum class FishingOption(val toolId: Int, val levelReq: Int, val animationId: Int, val bait: Int?, val option: String, val fishType: Array<Fish>) {
    NET(
            toolId = 303,
            levelReq = 1,
            animationId = 621,
            bait = null,
            option = "net",
            fishType = arrayOf(Fish.SHRIMP, Fish.ANCHOVIE)
    ),
    SMALL_NET(
            toolId = 303,
            levelReq = 1,
            animationId = 621,
            bait = null,
            option = "small net",
            fishType = arrayOf(Fish.SHRIMP, Fish.ANCHOVIE)
    ),
    BAIT(
            toolId = 307,
            levelReq = 5,
            animationId = 622,
            bait = 313,
            option = "bait",
            fishType = arrayOf(Fish.SARDINE, Fish.HERRING)
    ),
    BAIT_EEL(
            toolId = 307,
            levelReq = 5,
            animationId = 622,
            bait = 313,
            option = "bait",
            fishType = arrayOf(Fish.CAVE_EEL)
    ),
    LURE(
            toolId = 309,
            levelReq = 20,
            animationId = 622,
            bait = 314,
            option = "lure",
            fishType = arrayOf(Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH)
    ),
    FISH(
            toolId = 309,
            levelReq = 20,
            animationId = 622,
            bait = 314,
            option = "fish",
            fishType = arrayOf(Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH)
    ),
    USE_ROD(
            toolId = 309,
            levelReq = 20,
            animationId = 622,
            bait = 314,
            option = "use-rod",
            fishType = arrayOf(Fish.TROUT, Fish.SALMON, Fish.RAINBOW_FISH)
    ),
    L_BAIT(
            toolId = 307,
            levelReq = 25,
            animationId = 622,
            bait = 313,
            option = "bait",
            fishType = arrayOf(Fish.PIKE)
    ),
    CAGE(
            toolId = 301,
            levelReq = 40,
            animationId = 619,
            bait = null,
            option = "cage",
            fishType = arrayOf(Fish.LOBSTER)
    ),
    HARPOON(
            toolId = 311,
            levelReq = 35,
            animationId = 618,
            bait = null,
            option = "harpoon",
            fishType = arrayOf(Fish.TUNA, Fish.SWORDFISH)
    ),
    BARB_HARPOON(
            toolId = 10129,
            levelReq = 35,
            animationId = 618,
            bait = null,
            option = "harpoon",
            fishType = arrayOf(Fish.TUNA, Fish.SWORDFISH)
    ),
    CATCH(
            toolId = 10129,
            levelReq = 35,
            animationId = 618,
            bait = null,
            option = "catch",
            fishType = arrayOf(Fish.TUNA, Fish.SWORDFISH)
    ),
    BIG_NET(
            toolId = 305,
            levelReq = 16,
            animationId = 620,
            bait = null,
            option = "big net",
            fishType = arrayOf(Fish.MACKEREL, Fish.COD, Fish.BASS)
    ),
    N_HARPOON(
            toolId = 311,
            levelReq = 76,
            animationId = 618,
            bait = null,
            option = "harpoon",
            fishType = arrayOf(Fish.SHARK)
    ),
    H_NET(
            toolId = 303,
            levelReq = 1,
            animationId = 621,
            bait = null,
            option = "net",
            fishType = arrayOf(Fish.MONKFISH)
    ),
    C_CAGE(
            toolId = 301,
            levelReq = 85,
            animationId = 619,
            bait = 14943,
            option = "cage",
            fishType = arrayOf(Fish.DARK_CRAB)
    );

    companion object {
        val values = enumValues<FishingOption>()
    }
}