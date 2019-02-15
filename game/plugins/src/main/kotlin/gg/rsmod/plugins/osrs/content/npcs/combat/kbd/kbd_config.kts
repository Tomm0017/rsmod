package gg.rsmod.plugins.osrs.content.npcs.combat.kbd

import gg.rsmod.game.model.combat.NpcCombatDef
import gg.rsmod.plugins.osrs.api.cfg.Npcs

set_combat_def(Npcs.KING_BLACK_DRAGON, NpcCombatDef.Builder()
        .setHitpoints(240)
        .setAttackSpeed(3)
        .setDeathAnimation(92)
        .setBlockAnimation(89)
        .setAggressiveRadius(16)
        .setFindAggroTargetDelay(1)
        .setSlayerXp(258.0)
        .setAttackLvl(240)
        .setStrengthLvl(240)
        .setMagicLvl(240)
        .setDefenceStabBonus(70)
        .setDefenceSlashBonus(90)
        .setDefenceCrushBonus(90)
        .setDefenceMagicBonus(80)
        .setDefenceRangedBonus(70)
        .build())