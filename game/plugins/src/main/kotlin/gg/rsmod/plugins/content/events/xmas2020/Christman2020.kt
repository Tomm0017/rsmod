package gg.rsmod.plugins.content.events.xmas2020

object Christman2020 {
    /**
     * 1,2,3 talking to Scrubfoot, 5 when completed
     * 6,7 talking to generals, 8 accusing them of not knowing XMAS
     * 10(+1) after talking about every option with generals
     * 11(+1) after rolling boulder
     * 12 turned in all items
     * 14 final words with generals
     * 15 COMPLETE
     */
    const val OVERALL_PROGRESS_VARBIT = 11718

    /**
     * 1 after talking about option
     * 2 after completing associated task
     * 3 after turn-in of associated items
     */
    const val TREES_PROGRESS_VARBIT = 11724 // trees -> boulder
    const val GIFTS_PROGRESS_VARBIT = 11725 // gifts -> chest
    const val DECOR_PROGRESS_VARBIT = 11726 // decor -> fireflies
    const val FOODS_PROGRESS_VARBIT = 11727 // foods -> cauldron


}