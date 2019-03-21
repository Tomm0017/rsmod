# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Configuring The Project
There's two ways to launch RS Mod. One is for users who want to look into the code and 
possibly create their own content. The other is for users who simply want to run the server
as fast as possible and log in quick. 

### I want to run the server quickly
- Go to the repository's release page: https://github.com/Tomm0017/rsmod/releases
- Download the latest release
- Extract the archive on your desktop (or anywhere of your preference)
- Open the archive and open the following directory: ``game-$version-number/bin``
- If you are running Windows, run the ``game.bat`` file. 
If you are running Linux or Mac, run the ``game`` file
- The first time to launch it, you will receive a prompt saying that you do not 
have an RSA private key. Enter ``y`` on your terminal/command prompt and wait for
a message stating that your key was created. **Do not close the terminal/command prompt** 
- Once your key is created, you will have to follow the instructions on the terminal/command prompt
    - You need to copy the public keys you are given and replace them in your client
    - In your client, you can find the text ``BigInteger("10001`` which will usually be the
    place where you need to replace both the public keys (the ``"10001"`` key is usually the same)
    - Once you have replaced the keys in the client, you can restart the server and launch your client  
- Skip to step ``5) Choose your revision`` in ``I want to run the server and begin making my own content``
and continue from there

### I want to run the server and begin making my own content

##### 1) Clone/Download the Repository
- Go to the repository page: https://github.com/Tomm0017/rsmod
- Clone or download the repository

##### 2) Open the project in IntelliJ
- If you do not have IntelliJ, you can download it from here: https://www.jetbrains.com/idea/download/
- In your IntelliJ window, go to the top-left menu bar and navigate to ``File -> New -> Project from Existing Sources...``
- Select the RSMod repository which you downloaded on the previous step  
- In the ``Import Project`` window, select ``Import project from external model`` -> ``Gradle``
- You can skip the next window, simply hit the ``Finish`` button
- Give the project a bit of time to create and index its files

##### 3) Install RSMod
- On the top-right there should be a box ``Add Configuration...``, click on the box
- On the top-left of the ``Run/Debug Configurations`` window, click on the ``+`` button
- Select ``Gradle`` from the drop-down menu
- In the Unnamed Gradle task, you should now fill in the ``Configuration``
- ``Gradle project``: click the folder button on its right side and select the ``:game`` option
- ``Tasks``: set value to ``install``
- ``Arguments``: set value to ```-x test```
- Now hit the ``Ok`` button
- Next to the new button that should appear where the ``Add Configuration...`` was previously, 
there should be a green ``run`` button, click on that and let the installation begin.

##### 4) RSA key setup
RSA is a method to stop man-in-the-middle (MITM) attacks on packets. RS Mod has this method enabled by default,
no two servers should use the same private key so you must create your own:

- After the ``install`` task completes, it will print out a message on the IntelliJ console
- The console message explains that you must find ``BigInteger("10001"`` in your client and
replace it the values with the ones printed on the console. If you missed the console messages
you can run the ``install`` task again. 

##### 5) Choose your revision
Now you're ready to start choosing the direction of your server!

- Head to the following archives page and select a revision you want your server to run on:
https://archive.runestats.com/osrs/
- Download whichever archive you want
- Open the archive that you downloaded
- Copy the files in its "cache" and place them in your RS Mod folder ``${rsmod-project}/data/cache``
- Create the folder ``${rsmod-project}/data/xteas/``
- Copy the file ``xteas.json`` and place it in the ``xteas`` folder you just created

##### Troubleshooting
- I receive a ``Revision mismatch for channel`` when trying to log in
    - Find the revision of your **client** (*not cache*)
    - Open ``${rsmod-project}/game.yml``
    - Edit the value for ``revision: 178`` to match your client's revision

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
    2. To use the 'scheduler' we have to wrap the code in ``Player.queue`` 
    (gives the ability to use the ``wait`` method in this case).
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
        * ``itemMessageBox(String message, Int itemId)`` a dialog that shows an item next to it
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