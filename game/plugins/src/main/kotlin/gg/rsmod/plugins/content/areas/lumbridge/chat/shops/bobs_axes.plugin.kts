package gg.rsmod.plugins.content.areas.lumbridge.chat.shops

spawn_npc(npc = Npcs.BOB_2812, x = 3230, z = 3203, walkRadius = 4, height = 0)

    on_npc_option(Npcs.BOB_2812, "trade") {
        player.openShop("Bob's Brilliant Axes.")
    }

    on_npc_option(npc = Npcs.BOB_2812, option = "repair") {
        player.queue { chatNpc("You don't have anything I can repair.", animation = 575) }
    }

    on_npc_option(Npcs.BOB_2812, "talk-to") {
        player.queue {
            when(this.options("Give me a quest!", "Have you anything to sell?", "Can you repair my items for me?", title = "Select an Option")) {
                1 -> {
                    this.chatPlayer("Give me a quest!")
                    this.chatNpc("Get yer own!")
                }

                2 -> {
                    this.chatPlayer("Have you anything to sell?")
                    this.chatNpc("Yes! I buy and sell axes! Take your pick (or axe)!")
                    this.player.openShop("Bob's Brilliant Axes.")
                }
                3 -> {
                    this.chatPlayer("Can you repair my items for me?")
                    this.chatNpc("Of course I'll repair it, though the materials may cost<br>you. Just hand me the item and I'll have a look.")
                }
            }
        }
    }