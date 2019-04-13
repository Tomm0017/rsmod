# RS Mod
**RS Mod** is a server that is highly flexible and user-friendly. We allow the 
developer to make and create any sort of plugin they wish without having to 
modify the core game module. People without developing experience can have 
others make plugins for them and simply drop them into the Plugins module 
and it'll automatically load on the next server startup! 

## Usage
You can learn how to create plugins here: [RS Mod Wiki](https://github.com/Tomm0017/rsmod/wiki/Creating-Plugins)

## Installation
There's two ways to launch RS Mod. One is for users who want to look into the code and 
possibly create their own content. The other is for users who simply want to run the server
as fast as possible and log in quick. 

### I want to run the server quickly
- Go to the repository's release page: [Releases](https://github.com/Tomm0017/rsmod/releases)
- Download the latest release (note: *not* the source code package)
- Extract the archive on your desktop (or anywhere of your preference)
- Open the extracted folder and open the following directory: ``game-${version-number}/bin``
- If you are running Windows, run the ``game.bat`` file. 
If you are running Linux or Mac, run the ``game`` file
- The first time you launch it, you will receive a prompt stating that you do not 
have an RSA private key. Enter ``y`` on your terminal/command prompt and wait for
a message stating that your key was created. **Do not close the terminal/command prompt** 
- Once your key is created, you will have to follow the instructions on the terminal/command prompt
    - You need to copy the public keys you are given and replace them in your client
    - In your client, you can find the text ``BigInteger("10001`` which will usually be the
    place where you need to replace both the public keys (the ``"10001"`` key is usually the same)
    - Once you have replaced the keys in the client, you can restart the server and launch your client  
- The server release comes with a cache and XTEA keys you can use. Current revision: ``179``

### I want to run the server and begin making my own content

#### 1) Clone/Download the Repository
- Go to the repository page: [RS Mod](https://github.com/Tomm0017/rsmod)
- Clone or download the repository
- Extract the repository in your desktop (or anywhere else you prefer)
    - Note: make sure you use ``Extract here`` and *not* ``Extract to rs-mod-master\``, unless
    you know what you're doing (it can lead to silly mistakes when setting up your project)

#### 2) Open the project in IntelliJ
- If you do not have IntelliJ, you can download it from here: https://www.jetbrains.com/idea/download/
- In your IntelliJ window, go to the top-left menu bar and navigate to ``File -> New -> Project from Existing Sources...``
- Select the RSMod repository which you downloaded on the previous step  
- In the ``Import Project`` window, select ``Import project from external model`` -> ``Gradle``
- In the next window you want to select the following and unselect anything else:
    * Select ``Create separate module per source set``
    * Select ``Use default gradle wrapper (recommended)``
    * In the ``Global Gradle settings`` section:
        * If ``Offline work`` is selected, unselect it
- Give the project a bit of time to create and index its files

#### 3) Install RSMod
- On the top-right there should be a box ``Add Configuration...``, click on the box
- On the top-left of the ``Run/Debug Configurations`` window, click on the ``+`` button
- Select ``Gradle`` from the drop-down menu
- In the Unnamed Gradle task, you should now fill in the ``Configuration``
    - ``Gradle project`` click the folder button on its right side and select the ``:game`` option
    - ``Tasks`` set value to ``install``
    - ``Arguments`` set value to ```-x test```
- Now hit the ``Ok`` button
- Next to the new button that should appear where the ``Add Configuration...`` was previously, 
there should be a green ``run`` button, click on that and let the installation begin.

#### 4) RSA key setup
RSA is a method to stop man-in-the-middle (MITM) attacks on packets. RS Mod has this method enabled by default,
no two servers should use the same private key so you must create your own:

- After the ``install`` task completes, it will print out a message on the IntelliJ console
- The console message explains that you must find ``BigInteger("10001"`` in your client and
replace it and another value near it, with the ones printed on the console. If you missed the 
console messages you can run the ``install`` task again. 

#### 5) Choose your revision
Now you're ready to start choosing the direction of your server!

- Go to the archives page and select a revision you want your server to run on:
https://archive.runestats.com/osrs/
- Download whichever archive you want
- Open the archive that you downloaded
- Copy the files in its "cache" folder and place them in your RS Mod folder ``${rsmod-project}/data/cache``
- Create the folder ``${rsmod-project}/data/xteas/``
- Copy the file ``xteas.json`` and place it in the ``xteas`` folder you just created

#### 6) Run the Server
This step is similar to step ``3) Install RSMod``

- On the top-right there should be a box ``Add Configuration...``, click on the box
- On the top-left of the ``Run/Debug Configurations`` window, click on the ``+`` button
- Select ``Gradle`` from the drop-down menu
- In the Unnamed Gradle task, you should now fill in the ``Configuration``
    - ``Gradle project`` click the folder button on its right side and select the ``:game`` option
    - ``Tasks`` set value to ``run``
- Now hit the ``Ok`` button
- Next to the new button that should appear where the other configuration was previously, 
there should be a green ``run`` button, click on that and the server should begin to run.


## Troubleshooting
- *Where can I get a client?*
    - You can get a client from https://www.rune-server.ee/runescape-development/rs2-server/downloads/684206-179-rsmod-release.html
- *I receive a* ``Bad session id`` *message on the log-in screen*
    - This means the RSA keys on the client do not match the ones created on the server.
    You should try to follow the steps in ``4) RSA key setup`` again. 
- *I receive a* ``Revision mismatch for channel`` *console message when trying to log in*
    - Find the revision of your **client** (*not cache*)
    - Open ``${rsmod-project}/game.yml``
    - Edit the value for ``revision: 179`` to match your client's revision
- *I receive a* ``error_game_js5connect`` *error on the client console*
    - You need to launch the server first and *then* the client
- *When following ``2) Open the project in IntelliJ`` my IntelliJ throws the error ``Build model 'org.jetbrains.plugins.gradle.model.ExternalProject' for root project 'gg.rsmod'``*
    - This appears to be an issue that can be solved by upgrading your IntelliJ    
    
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
- If you would like a feature added, you can create a Pull Request on GitHub or contact **rspsmods@gmail.com**
#### I found a bug, where can I report it?
- You can report them as Issues on GitHub or contact **rspsmods@gmail.com**

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
* ##### Sini
    - Advice on improving RS Mod's infrastructure
* ##### Kris
    - Always willing to lend a hand and share some code 
* ##### Bart and Scu11
    - Helped solve an issue with setting up KotlinScript 
* ##### Bart (from original OSS team)
    - The basic idea behind certain features such as TimerSystem, AttributeSystem and Services
    - The basic idea behind user-friendly plugin binding
* ##### Rune-Status
    - Discord members lending a hand and being helpful
