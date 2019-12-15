package gg.rsmod.plugins.content.combat.specialattack.weapons.melee
import gg.rsmod.game.model.entity.AreaSound
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MagicCombatFormula
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 100

SpecialAttacks.register(intArrayOf(Items.SARADOMIN_SWORD), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    player.animate(id = 1132)
    player.graphic(id = 1213, height = 0)
    target.graphic(1196, 0, 30)
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.10)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
    val landHit = accuracy >= world.randomDouble()
    player.dealHit(target = target, maxHit = maxHit, landHit = landHit, delay = 1)
    if(landHit) {
        val magicAccuracy = MagicCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)
        val landMagicHit = magicAccuracy >= world.randomDouble()
        player.dealHit(target = target, maxHit = 16, landHit = landMagicHit, delay = 1)
    }
}