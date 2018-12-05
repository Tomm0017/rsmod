# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Getting Started
#### Configuring The Project
- TODO

#### Launching The Server
- Before you can launch the server, you will need to create a few files:

        game.yml: holds information on general game information
        packets.yml: holds information on the packets you want to support
        
    Luckily, both of these files have a ```.example.yml``` file in the root 
    directory, which you can simply copy and remove the ```.example``` extension.
    The ```packets.example.yml``` file contains the structure of packets for `OSRS` ```#172```

#### Creating Your First Script 
- Creating your first script is super simple! Here are the instructions for how 
    you would create a few different scripts. Scripts are written in **Kotlin**.
    
    I want to create a ``Command``
    -
    A command is an input through normal chat messages when the message is 
    prefixed with `::`, such as `::food`.

        @JvmStatic   // 1
        @ScanPlugins // 2
        fun register(r: PluginRepository) { // 3
            r.bindCommand("food") { // 4
                it.player().getInventory().add(392, 100) // 5                    
            }    
        }
    
    This is a pretty simple script. Let's go over the lines of code that are 
    labelled:
    1.  `@JvmStatic` this annotation tells the compiler that the function needs
    an additional static method, which can then be read by our reflection logic.
    2.  `@ScanPlugins`: this annotation allows our reflection logic to find any 
    functions that should register plugins upon server launch.
    3.  `fun register`: `fun` is a Kotlin keyword to begin a function. 
    The use of `register` as the method name where plugins are registered
    is highly recommended and the convention should be followed for consistency.
    4.  `r.bindCommand`: `r` is specified as a parameter on the method as a 
    `PluginRepository`, which is where all plugins are registered to.
    The `PluginRepository` has all the methods which can be used to register 
    plugins; all these registration methods begin with the prefix `bind`.
    `bindCommand` is one of the many registration methods, which tells the repository
    to store the command `food` and execute the logic we give it once a player
    uses a command.
    5.  This is pretty straightforward. We get the inventory for the player and
    then add item with id 392 with an amount of 100. So now you have 100 Manta ray!
    
    I want to create a ``Scheduled Task``
    -
    A scheduled task is logic that can take more than a single tick to complete.
    Some examples are skills that are continuous, dialogues that wait for input,
    or cutscenes.
    
        @JvmStatic
        @ScanPlugins
        fun register(r: PluginRepository) {
            r.bindObject(id = 0, opt = 1) { // 1
                GlobalScope.launch(it.dispatcher) { // 2
                    it.player().message("Start scheduled logic.") // 3
                    it.wait(1) // 4
                    it.player.message("Finished scheduled logic.") // 5
                }
            }
        }
    
    This script can seem a bit more complex, but it just needs to follow a simple
    structure and the rest of the code is code you would normally use.
    1.  TODO
    2.  TODO
    3.  TODO
    4.  TODO
    5.  TODO

## FAQ
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