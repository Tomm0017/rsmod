package gg.rsmod.plugins.content.npcs.sheep

Sheep.SHEEP_NPCS.forEach { sheep ->
    if (world.definitions.get(NpcDef::class.java, sheep).options.contains("Shear")) {

        on_npc_option(npc = sheep, option = "shear") {
            val npc = player.getInteractingNpc()

            player.resetFacePawn()
            player.faceTile(npc.tile)
            if (!player.inventory.contains(Items.SHEARS)) {
                player.message("You need a set of shears to do this.")
                return@on_npc_option
            }
            player.queue { shear(this, player, npc) }
        }
    }
}

suspend fun shear(it: QueueTask, p: Player, n: Npc) {
    val flee = world.percentChance(15.0)

    p.lock()
    p.animate(893)
    p.playSound(761)
    if (flee) {
        flee(n)
    }
    it.wait(2)
    p.unlock()
    p.playSound(762)
    n.forceChat("Baa!")

    if (!flee) {
        if (p.inventory.hasSpace) {
            p.inventory.add(item = Items.WOOL)
        } else {
            val ground = GroundItem(item = Items.WOOL, amount = 1, tile = Tile(p.tile), owner = p)
            world.spawn(ground)
        }
        p.message("You get some wool.")
        n.queue { transmog_sheep(this, n) }
    } else {
        p.message("The sheep manages to get away from you!")
    }
}

fun flee(n: Npc) {
    val rx = world.random(-n.walkRadius..n.walkRadius)
    val rz = world.random(-n.walkRadius..n.walkRadius)

    val start = n.spawnTile
    val dest = start.transform(rx, rz)

    n.walkTo(dest)
}

suspend fun transmog_sheep(it: QueueTask, n: Npc) {
    n.setTransmogId(if (n.id == 2803) Npcs.SHEEP_2792 else Npcs.SHEEP_2793)
    it.wait(100)
    n.setTransmogId(n.id)
}