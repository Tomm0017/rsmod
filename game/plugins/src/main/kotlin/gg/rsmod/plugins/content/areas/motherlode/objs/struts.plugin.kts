package gg.rsmod.plugins.content.areas.motherlode.objs

import gg.rsmod.plugins.content.areas.motherlode.WaterCircuit.run_water_wheel
import kotlin.random.Random

on_obj_option(Objs.BROKEN_STRUT, "Hammer"){
    player.queue {
        val broken_strut = player.getInteractingGameObj()
        val attempts = Random.nextInt(1, 6)

        repeat(attempts) {
            player.animate(id = 3971, delay = 10)
            player.playSound(1786, 1, 10)
            wait(3)
            if (it == attempts - 1) {
                player.message("great succeesss")
                run_water_wheel(world, broken_strut)
            }
        }
    }
}
