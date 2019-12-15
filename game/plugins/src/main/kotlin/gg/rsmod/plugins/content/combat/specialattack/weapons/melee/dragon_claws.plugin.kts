package gg.rsmod.plugins.content.combat.specialattack.weapons.melee

import gg.rsmod.plugins.content.combat.dealExactHit
import gg.rsmod.plugins.content.combat.dealHit
import gg.rsmod.plugins.content.combat.formula.MeleeCombatFormula
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks

val SPECIAL_REQUIREMENT = 50

SpecialAttacks.register(intArrayOf(Items.DRAGON_CLAWS), SPECIAL_REQUIREMENT, ExecutionType.EXECUTE_ON_ATTACK) {
    val maxHit = MeleeCombatFormula.getMaxHit(player, target, specialAttackMultiplier = 1.0)
    val accuracy = MeleeCombatFormula.getAccuracy(player, target, specialAttackMultiplier = 1.0)

    player.animate(id = 5283)
    player.graphic(id = 1171, height = 0)

    val firstHitDamage =  clawsHit(player, target, maxHit, accuracy, 0, 1)
    val secondHitDamage =  clawsHit(player, target, maxHit, accuracy, firstHitDamage, 2)
    val thirdHitDamage = clawsHit(player, target, maxHit, accuracy, secondHitDamage, 3)
    clawsHit(player, target, maxHit, accuracy, when(secondHitDamage) { 0 -> thirdHitDamage else -> (secondHitDamage+1)/2}, 4)
}

fun clawsHit(player : Player , target : Pawn, maxHit : Int, accuracy : Double,  lastHitDamage : Int, hitNumber : Int) : Int {
    var hitDamage = world.random(maxHit)
    var landHit = accuracy >= world.randomDouble()

    if(lastHitDamage != 0) {
        hitDamage = when(hitNumber) { 4 -> lastHitDamage
            else -> (lastHitDamage)/2
        }
        landHit = true
    } else if(lastHitDamage == 0 && hitNumber == 4)
        hitDamage = (hitDamage*1.5).toInt()

    val delay = (hitNumber-1)/2
    player.dealExactHit(target = target, exactHit = hitDamage, landHit = landHit, delay = delay)

    return when(landHit) {true-> hitDamage else -> 0}
}