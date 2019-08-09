package gg.rsmod.plugins.content.areas.lumbridge.chat

spawn_npc(npc = Npcs.COOKING_TUTOR, x = 3233, z = 3195, walkRadius = 3, height = 0)

on_npc_option(npc = Npcs.COOKING_TUTOR, option = "talk-to") {
    player.queue { menu() }
}

suspend fun QueueTask.menu() {
    when (options("Can you teach me the basics of cooking please?", "Tell me about different food I can make.", "Goodbye.")) {
        1 -> {
            chatPlayer("Can you teach me the basics of cooking please?", animation = 554)
            chatNpc("The simplest thing to cook is raw meat or fish.", animation = 588)
            chatNpc("Fish can be caught, speak to the fishing tutor south of<br>here in the swamp. Killing cows or chickens will yield<br>raw meat to cook too.", animation = 569)
            itemMessageBox("When you have a full inventory of meat or fish, find a<br>range. Look for this icon on your minimap.", item = 5090, amountOrZoom = 400)
            chatNpc("You can use your own fire... but it's not as effective<br>and you'll burn more. To build a fire use a tinderbox<br>on logs.", animation = 569)
            chatNpc("Once you've found your range, click on it. This will<br>bring up a menu of the food you can cook.", animation = 589)
            itemMessageBox("When you have a full inventory of cooked food, drop<br>the useless burnt food and find a bank. Look for this<br>symbol on your minimap after climbing the stairs of the<br>Lumbridge Castle to the top. There are numerous", item = 5080, amountOrZoom = 400)
            itemMessageBox("banks around the world, all marked with that symbol.", item = 5080, amountOrZoom = 400)
            chatNpc("If you're interested in quests, I heard my friend the<br>cook in Lumbridge Castle is in need of a hand. Just<br>talk to him and he'll set you off.")
            menu()
        }
        2 -> {
            chatPlayer("Tell me about different foods.", animation = 554)
            foodStuffs()
        }
        3 -> chatPlayer("Goodbye.", animation = 588)
    }
}

suspend fun QueueTask.foodStuffs() {
    when (options("Fish and Meat", "Pies and Pizza", "Go back to teaching")) {
        1 -> {
            doubleItemMessageBox("Fish and meat of most varieties can be cooked very<br>simply on either a fire or range, experiment which one<br>works for you.", item1 = Items.RAW_BEEF, item2 = Items.COOKED_MEAT, amount1 = 400, amount2 = 400)
            foodStuffs()
        }
        2 -> {
            doubleItemMessageBox("Use a pot of flour with a bucket of water. You will then<br>get an option to make bread dough, pitta bread dough,<br>pastry dough, or pizza dough. Select pizza or pastry<br>dough.", item1 = Items.BUCKET_OF_WATER, item2 = Items.POT_OF_FLOUR, amount1 = 400, amount2 = 400)
            doubleItemMessageBox("Use the pastry dough with a pie dish then add your<br>filling such as apple or red berries.", item1 = Items.PIE_DISH, item2 = Items.PASTRY_DOUGH, amount1 = 400, amount2 = 400)
            chatNpc("Finally cook your pie by using the unbaked pie on a<br>cooking range. Mmmm...pie.", animation = 568)
            chatNpc("There's pizza too! Find yourself some tomato and<br>cheese, use on the Pizza dough. Cook the pizza on a<br>range then add any other toppings you want, such as<br>anchovies.", animation = 570)
            foodStuffs()
        }
        3 -> menu()
    }
}
