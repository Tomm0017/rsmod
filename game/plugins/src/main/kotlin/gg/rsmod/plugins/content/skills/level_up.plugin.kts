package gg.rsmod.plugins.content.skills

import gg.rsmod.game.model.attr.LEVEL_UP_INCREMENT
import gg.rsmod.game.model.attr.LEVEL_UP_SKILL_ID

set_level_up_logic {
    val skill = player.attr[LEVEL_UP_SKILL_ID]!!
    val increment = player.attr[LEVEL_UP_INCREMENT]!!

    /*
     * Calculate the combat level for the player if they leveled up a combat
     * skill.
     */
    if (Skills.isCombat(skill)) {
        player.calculateAndSetCombatLevel()
    }

    /*
     * Show the level-up chatbox interface.
     */
    player.queue {
        if(player.getSkills()[skill].currentLevel == 99) player.graphic(1388)
        levelUpMessageBox(skill, increment)
    }
}