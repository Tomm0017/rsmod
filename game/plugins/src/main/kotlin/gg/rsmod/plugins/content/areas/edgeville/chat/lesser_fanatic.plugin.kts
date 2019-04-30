package gg.rsmod.plugins.content.areas.edgeville.chat

on_npc_option(npc = Npcs.LESSER_FANATIC, option = "talk-to") {
    player.queue { chat(this) }
}

suspend fun chat(it: QueueTask) {
    it.chatNpc("Hello!", animation = 567)
    options(it)
}

suspend fun options(it: QueueTask) {
    when (it.options("Who are you?", "I have a question about my Achievement Diary", "Bye!")) {
        1 -> who_are_you(it)
        2 -> question(it)
        3 -> bye(it)
    }
}

suspend fun who_are_you(it: QueueTask) {
    it.chatPlayer("Who are you?", animation = 554)
    it.chatNpc("I'm Jeffery, the taskmaster for the Wilderness<br>Achievement Diary.", animation = 589)
    what_is_achievement_diary(it)
}

suspend fun question(it: QueueTask) {
    it.chatPlayer("I have a question about my Achievement diary.", animation = 554)
    when (it.options("What is the Achievement Diary?", "What are the rewards?", "How do I claim the rewards?", "Bye!")) {
        1 -> what_is_achievement_diary(it)
        2 -> what_are_rewards(it)
        3 -> claim_rewards(it)
        4 -> bye(it)
    }
}

suspend fun what_is_achievement_diary(it: QueueTask) {
    it.chatPlayer("What is the Achievement Diary?", animation = 554)
    it.chatNpc("It's a diary that helps you keep track of particular<br>achievements. In the Wilderness it can help you<br>discover some quite useful things. Eventually, with<br>enough exploration, the inhabitants will reward you.", animation = 591)
    it.chatNpc("You can see the list of tasks on the side-panel.", animation = 588)
    options(it)
}

suspend fun what_are_rewards(it: QueueTask) {
    it.chatNpc("Well, there are four different Wilderness Swords, which<br>match up with the four levels of difficulty. Each has the<br>same rewards as the previous level and some additional<br>benefits too... which tier of rewards would you like to", animation = 570)
    it.chatNpc("know more about?", animation = 567)
    when (it.options("Easy Rewards.", "Medium Rewards.", "Hard Rewards.", "Elite Rewards.")) {
        1 -> easy_rewards(it)
        2 -> medium_rewards(it)
        3 -> hard_rewards(it)
        4 -> elite_rewards(it)
    }
}

suspend fun easy_rewards(it: QueueTask) {
    it.chatPlayer("Tell me more about the Easy rewards please!", animation = 588)
    it.chatNpc("If you complete all of the easy tasks in the Wilderness,<br>you can speak to Lundail at the Mage Arena every day<br>to receive 10 free runes and you can teleport to either<br>Edgeville or Ardougne from the Wilderness lever.", animation = 570)
    it.chatPlayer("Thanks!", animation = 567)
}

suspend fun medium_rewards(it: QueueTask) {
    it.chatPlayer("Tell me more about the Medium rewards please!", animation = 588)
    it.chatNpc("In addition to the easy rewards, Mandrith will give you<br>a 20% discount off entry to the Resource Area, you'll<br>cut Ents faster, you can access a shortcut in the deep<br>Wilderness dungeon. You can also own 4 ecumenical", animation = 570)
    it.chatNpc("keys at once and receive 20 runes a day from Lundail.", animation = 567)
    it.chatPlayer("Thanks!", animation = 567)
}

suspend fun hard_rewards(it: QueueTask) {
    it.chatPlayer("Tell me more about the Hard rewards please!", animation = 588)
    it.chatNpc("In addition to the easy and medium benefits, the sword<br>can teleport you to the Fountain of Rune once per<br>day, you'll get 50% more lava shards when grinding<br>scales and even cheaper access to the Resource Area.", animation = 570)
    it.chatNpc("You can own 5 ecumenical keys, access a shortcut to<br>Lava Dragon Isle and Lundail will give you 30 free<br>runes each day. You can also control which Obelisk you<br>want to travel to.", animation = 570)
    it.chatPlayer("Thanks!", animation = 567)
}

suspend fun elite_rewards(it: QueueTask) {
    it.chatPlayer("Tell me more about the Elite rewards please!", animation = 588)
    it.chatNpc("In addition to the previous tiers of rewards, access to<br>the Resource Area is free and the sword will now give<br>you unlimited teleports to the Fountain of Rune. All<br>dragon bone drops in the Wilderness become noted and", animation = 570)
    it.chatNpc("Lundail will give you 50 free runes per day and the<br>rate at which you catch Dark Crabs is increased.", animation = 568)
    it.chatPlayer("Thanks!", animation = 567)
}

suspend fun claim_rewards(it: QueueTask) {
    it.chatPlayer("How do I claim the rewards?", animation = 554)
    it.chatNpc("Just complete the tasks in the Wilderness so they're<br>ticked off, then come and speak to me for your<br>rewards.", animation = 590)
    options(it)
}

suspend fun bye(it: QueueTask) {
    it.chatPlayer("Bye!", animation = 567)
    it.chatNpc("See you later.", animation = 567)
}
