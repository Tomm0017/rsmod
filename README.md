# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Configuring The Project
- TODO

## Creating Your First Plugin 

-   Creating your first plugin is super simple! Plugins are written in **KotlinScript**. 
    Here are the instructions for how you would create a few different plugins.

    Before we begin...
    -
    A plugin must be created in a **KotlinScript** file, notated by the .kts extension.
    Plugin files **should always** be defined in lowercase with underscores for separation.
    For example if we want to create a food command, we may make a file named ``food_command.kts``
    
    I want to create a Command
    -
    A command is an input received when a player types something in their chatbox, 
    prefixed with `::`. For example `::food`. We would create a file named (including its extension)
    ``food_command.kts``. It would contain the following code:

        package gg.rsmod.plugins.content.test // 1

        on_command("food") { // 2
            player.inventory.add(391, 28) // 3
            player.message("You have spawned some food.") // 4        
        }
    
    This is a pretty simple plugin. Let's go over the lines of code that are 
    labeled.
    
    1. The package in which the file is located. It's valid to not include a package, however
    if any other plugin uses the same name and doesn't specify a package in a similar fashion,
    there will be conflicting issues, which is why it's good to define the package.
    2. There are loads of ``on_xxx`` methods which you can use to define what your plugin
    does under certain circumstances. These methods can be found in the file 
    ``gg.rsmod.game.plugin.KotlinPlugin``
    3. ``player`` is defined automagically (behind-the-scenes) in a way that our ``.kts`` file 
    can call it without needing to define it anywhere in the file. We then use ``Player.inventory``
    and add the item ``391`` with an amount of ``28``. This command will spawn 28 manta rays in 
    your inventory.
    4. ``player.message`` will send a message to the player's chatbox.
    
    I want to create a "Scheduled" Task
    -
    A scheduled task is logic that can take more than a single tick to complete.
    Some examples are skills that are continuous, dialogs that wait for input,
    or cutscenes.
    
        package gg.rsmod.plugins.content.test
        
        on_obj_option(obj = 1278, option = "cut") { // 1
            player.queue { // 2
                player.animate(879) // 3
                player.message("You swing your axe at the tree.")
                wait(2) // 4
                player.animate(-1) // 5
                player.message("You get some logs.")
            }
        }
    
    This plugin is a bit more tedious to understand, but it's mostly just because 
    you'll need some time to learn the method names for certain features. Let's
    go over the labeled code:
    
    1. This is another one of those ``on_xxx`` methods which we spoke of on the
    last example. This one specifically is to give an action when a player clicks
    on an object. We specify the ``obj`` (object id) and the ``option`` which 
    automagically binds our action to said option. If the option is not found
    on the object, it will throw an error when you start the server.
    2. To use the 'scheduler' we have to wrap the code in ``Player.queue``.
    3. We make our player perform animation ``879``.
    4. Signal the code to wait for 2 **game cycles** (a single game cycle is 600 milliseconds).
    5. After the specified amount of cycles have gone by (in this case, 2 cycles), the rest of
    the code is executed, this includes ``player.animate(-1)`` which will reset the player's
    animation since they have already chopped down the logs!
    
    And that's it! You're on your way to creating all sorts of crazy plugins now.
    
    I want to create a Dialog
    -
    Creating a dialog plugin is similar to that of the `scheduled task` plugin. 
    We will use the `chatNpc` functionality in this example, which is targeted 
    for the `OSRS` version of `RS Mod`.
    
        package gg.rsmod.plugins.content.test
        
        on_npc_option(npc = 401, option = "talk-to") {
            player.queue { // 1
                chatNpc("Hello, adventurer.") // 2
                chatPlayer("Good day, Turael!")
            }
        }
        
    1. Again, we need ``player.queue`` since dialogs require to wait until the player
    clicks on the 'continue' option in the dialog box.
    2. There's several dialog methods we can use, these are some of them:
        * ``chatNpc(String message)``
        * ``chatPlayer(String message)``
        * ``messageBox(String message)`` an empty dialog box with a message
        * ``itemMessageBox(String message, Int itemId)`` a dialog that shows an item next to it`
        * ``options(String... options)`` returns an Int in respect to the option the player chooses
                from this dialog. If a player clicks on the first option, this method returns ``1``.
                 
     Each call to one of the dialog methods will wait until the player 'continues'
     the dialog before the code below it will resume. 
     
     Short and sweet! That's all you need for a basic dialog plugin. 

## FAQ

#### One or more of my plugins stopped working
- When you buy, or create, and use a Plugin **JAR** - the plugin uses code it 
assumes you have on the core game module when it was written. If for some reason 
you move, rename, or completely delete the code that the plugin is using - the 
plugin will stop working.
- Notes:
    - You can add new code to the game module without this being an issue, this
    includes adding code to existing methods and files. However, it's best to 
    avoid writing code to the game module and you should always opt to write a 
    suggestion for the RS Mod creators to add specific features to the official 
    game module.
#### I have done some modifications to the game module. How can I tell if one of my plugins is no longer compatible?
- The only way to check is to download the plugin's source files and run it on 
your server and see if it compiles correctly!
- Notes:
    - You only have to worry about **JAR** plugins suddenly becoming incompatible 
    when you edit the game module code
    - Delete or move the JAR plugin when you add the source plugin, otherwise 
    the plugins will conflict when trying to bind them on server start-up
    - In the future, we will add a tool to check if any of your JAR plugins are
    no longer compatible with your game module
#### I would like a feature added to the core game module
- If you would like a feature added, please contact **rspsmods@gmail.com**
#### I found a bug, where can I report it?
- You can report them on our forums https://rspsmods.com or contact us directly 
through e-mail: **rspsmods@gmail.com**

## Acknowledgments

* ##### [![](https://jitpack.io/v/runelite/runelite.svg)](https://jitpack.io/#runelite/runelite) 
    - Using Cache module from RuneLite
* ##### Graham Edgecombe
    - Using project structure based on Apollo
    - Using StatefulFrameDecoder, AccessMode, DataConstants, DataOrder, DataTransformation, DataType, GamePacket & GamePacketBuilder from Apollo
* ##### Major
    - Using Collision Detection from Apollo 
    - Basic idea behind the Region system (known as Chunk on RS Mod)
* ##### Polar
    - Helpful lad who shares a lot of OSRS knowledge
    - Useful OSRS archives https://archive.runestats.com/osrs/
    - Conversations of how to improve RS Mod 
* ##### Kris
    - Always willing to lend a hand and share some code 
* ##### Bart and Scu11
    - Helped solve an issue with setting up KotlinScript 
* ##### Bart (from original OSS team)
    - The basic idea behind certain features such as TimerSystem, AttributeSystem and Services
    - The basic idea behind user-friendly plugin binding
* ##### Rune-Status
    - Discord members lending a hand and being helpful