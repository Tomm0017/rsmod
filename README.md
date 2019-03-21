# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Configuring The Project
- TODO

## Creating Your First Script 

- Creating your first script is super simple! Scripts are written in **KotlinScript**. 
Here are the instructions for how you would create a few different scripts.
    
    I want to create a Command
    -
    A command is an input received when a player types something in their chatbox, 
    prefixed with `::`. For example `::food`.

        TODO: add code example for new kotlin script plugins
    
    This is a pretty simple script. Let's go over the lines of code that are 
    labelled 
    
    I want to create a Scheduled Task
    -
    A scheduled task is logic that can take more than a single tick to complete.
    Some examples are skills that are continuous, dialogs that wait for input,
    or cutscenes.
    
        TODO: add code example for new kotlin script plugins
    
    This script is pretty similar to the last, except we have a new piece of 
    important code. When you want to create a piece of code that will be 
    scheduled at a certain point, you want to surround the code with `Plugin.suspendable`
    (in this case, `suspendable { ... }`). 
        
    And that's it! You're on your way to creating all sorts of crazy scripts now.
    
    I want to create a Dialog
    -
    Creating a dialog script is similar to that of the `scheduled task` script. 
    We will use the `npcDialog` functionality in this example, which is targeted 
    for the `OSRS` version of `RS Mod`.
    
        TODO: add code example for new kotlin script plugins
     
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