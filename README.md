# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Configuring The Project
- TODO

## Creating Your First Script 

- Creating your first script is super simple! Scripts are written in **Kotlin**. 
Here are the instructions for how you would create a few different scripts.
    
    I want to create a Command
    -
    A command is an input received when a player types something in their chatbox, 
    prefixed with `::`. For example `::food`.

        @JvmStatic   // 1
        @ScanPlugins // 2
        fun register(r: PluginRepository) { // 3
            r.bindCommand("food") { // 4
                it.player().getInventory().add(392, 100) // 5                    
            }    
        }
    
    This is a pretty simple script. Let's go over the lines of code that are 
    labelled 
    
    1. `@JvmStatic` this annotation tells the compiler that the function needs
    an additional static method, which can then be read by our reflection logic.
    2. `@ScanPlugins`: this annotation allows our reflection logic to find any 
    functions that should register plugins upon server launch.
    3. `fun register`: `fun` is a Kotlin keyword to define a function. 
    The use of `register` as the method name where plugins are registered,
    is highly recommended and the convention should be followed for consistency.
    4. `r.bindCommand`: `r` is specified as a parameter on the method as a 
    `PluginRepository`, which is where all plugins are registered to.
    The `PluginRepository` has all the methods which can be used to register 
    plugins; all these registration methods begin with the prefix `bind`.
    `bindCommand` is one of the many registration methods, which tells the repository
    to store the command `food` and execute the logic we give it once a player
    uses the command.
    5. This is pretty straightforward. We get the inventory for the player and
    then add item with id 392 with an amount of 100. So now you have 100 Manta ray!
    
    I want to create a Scheduled Task
    -
    A scheduled task is logic that can take more than a single tick to complete.
    Some examples are skills that are continuous, dialogs that wait for input,
    or cutscenes.
    
        @JvmStatic
        @ScanPlugins
        fun register(r: PluginRepository) {
            r.bindObject(id = 0, opt = 1) { // 1
                it.suspendable { // 2
                    it.player().message("Start scheduled logic.") // 3
                    it.wait(2) // 4
                    it.player().message("Finished scheduled logic.") // 5
                }
            }
        }
    
    This script is pretty similar to the last, except we have a new piece of 
    important code. When you want to create a piece of code that will be 
    scheduled at a certain point, you want to surround the code with `Plugin.suspendable`
    (in this case, `it.suspendable { ... }`). 
    
    1. `bindObj`: this is the bind method for attaching a plugin to an object action.
    You can see, in `Kotlin`, we are able to 'label' the parameters. In this case, 
    we set the `object id` to `0` and `opt` to `1` 
    2. `it.suspendable`: exposes the code that follows as being `suspendable`, 
    which means it can be paused at any point. `it` is the auto-assigned name
    that `Kotlin` gives unnamed variables in logic such as lambdas. `it`, in this case, 
    is a `Plugin`, which holds the `suspendable` method.
    3. We send a message to the player's chatbox immediately.
    4. We tell the `Plugin` to `wait`, or suspend, for `2` game cycles. One game cycle is 
    usually represented by 600 milliseconds. 
    5. After `2` game cycles have gone by, the player will receive the last message.
    
    And that's it! You're on your way to creating all sorts of crazy scripts now.
    
    I want to create a Dialog
    -
    Creating a dialog script is similar to that of the `scheduled task` script. 
    We will use the `npcDialog` functionality in this example, which is targeted 
    for the `OSRS` version of `RS Mod`.
    
        @JvmStatic
        @ScanPlugins
        fun register(r: PluginRepository) {
            r.bindObject(id = 0, opt = 1) {
                it.suspendable {
                    it.npcDialog(message = "This is a test dialog!", npc = 3078, animation = 588, title = "Man") // 1
                    it.npcDialog(message = "This is the second test dialog!", npc = 3078, animation = 588, title = "Man") // 2
                }
            }
        }
        
    1. `npcDialog` is defined in the `plugin.kt` file in the core `plugins`
     package. It can change depending on what version of `RS Mod` you are using,
     but the format and conventions should be similar. This method is `suspendable`
     and will pause any further code until the dialog is continued by the player.
     
     Short and sweet! That's all you need for a basic dialog script. 

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
- You can report them on our forums https://rsmod.gg or contact us directly 
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
* #### OS-Scape
    - The idea behind certain features such as TimerSystem, AttributeSystem and Services
    - The idea behind user-friendly plugin binding
* #### Kris
    - Some information on player GPI
    - Sharing #168 examines