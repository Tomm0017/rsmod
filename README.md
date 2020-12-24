# RS Mod
[![revision: 193][rev-badge]][patch]

**RS Mod** is a highly flexible user-friendly game server for use with the OSRS client. Implemented in a modular way, the framework allows developers to make and create any sort of plugin they wish without having to
modify the core game module. Due to the plugin capabilities, even owners without programming experience can just have others make plugins for them and simply drop them into the plugins module to be automatically loaded on the next startup!

<details>
  <summary><a href="https://github.com/rsmod/rsmod">RS Mod v2.0</a> supersedes this!!</summary>

  ```
  This repo is entirely separate from the main project which is effectively **DEAD**; as such
  you should not expect a great deal of contributions, advancement, or support with this base!
  While you can usually find me in the RS Mod discord, their channels will ultimately be for
  RS Mod v2.0 and variations from the main repo should see you coming to me for help if using
  this codebase. Just shoot me a DM.
  ```
</details>

## Installation

#### 1) Open IntelliJ
- If you do not have IntelliJ, you can download it from here: https://www.jetbrains.com/idea/download/other.html

- Rs Mod also uses Kotlin and will require installation of the plugin: https://plugins.jetbrains.com/plugin/6954-kotlin/versions/stable/96647

_Note| due to a regression in KotlinScript functionalities in the newest versions of the plugin it is <u>**imperative**</u> you install a version below 1.4.20!_

#### 2) Import the project into IntelliJ

##### -- Using git Revision Control (Recommended)
- In your IntelliJ window, go to the top-left menu bar and navigate to ``File -> New -> Project from Version Control...``
- Enter the project's git repo URL: https://github.com/bmyte/rsmod.git
    - _Note| this will clone the master repo into a local one on your machine and REQUIRES; it is recommended to become familiar with aspects of git revision control for the purposes of maintaining your own changes and even setting up your own remote repo allowing for changes to be stored and synced using services like GitHub_
- Give the project a bit of time to clone the repo and create and index its files

##### -- Downloading the source (for those who do not want to install git)

- Download this repository
- Extract the repository on your desktop (or directory of choice)
    - _Note| make sure you use ``Extract here`` and *not* ``Extract to rs-mod-master\``, unless you know what you're doing (it can lead to silly mistakes when setting up your project)_
- In your IntelliJ window, go to the top-left menu bar and navigate to ``File -> New -> Project from Existing Sources...``
- In the ``Import Project`` window, select ``Import project from external model`` -> ``Gradle``
- In the next window you want to select the following and unselect anything else:
    - Select ``Create separate module per source set``
    - Select ``Use default gradle wrapper (recommended)``
    - In the ``Global Gradle settings`` section:
    - If ``Offline work`` is selected, unselect it
- Give the project a bit of time to create and index its files

#### 3) Install RS Mod
- On the top-right there should be a box ``Add Configuration...``, click on the box
- On the top-left of the ``Run/Debug Configurations`` window, click on the ``+`` button
- Select ``Gradle`` from the drop-down menu
- In the Unnamed Gradle task, you should now fill in the ``Configuration``
    - ``Gradle project`` click the folder button on its right side and select the ``:game`` option
    - ``Tasks`` set value to ``install``
    - ``Arguments`` set value to ```-x test```
- Now hit the ``Apply`` and then ``Ok`` button
- Next to the new button that should appear where the ``Add Configuration...`` was previously,
there should be a green ``run`` button, click on that and let the installation begin.

#### 4) RSA key setup
RSA is a method to stop man-in-the-middle (MITM) attacks on packets. RS Mod has this method enabled by default,
no two servers should use the same private key so you must create your own:

- After the ``install`` task completes, it will print out a message on the IntelliJ console
- Once your key is created, you will have to follow the instructions on the terminal/command prompt
    - You need to copy the public key you are given and replace those in your client
    - In your client, you can find the text `BigInteger("10001")` which will usually be found with the variable you need to replace with the RSA public key generated

#### 5) Setup the revision
Now you're ready to start choosing the direction of your server!

<details>
  <summary>A note about revisions</summary>

  ```
  RS Mod was originally developed/released in an experimental state most compatible with
  the revision 180/181 protocols and is archived for posterity and known stability(ish)
  in fully working state in this repo's `archived-181` branch. Even this master remains
  solely for the purposes of educating aspiring OSRSPS devs to protocols, mechanisms, and 
  practiced implementations of such in order to learn from. Once RS Mod v2.0 releases, you
  should highly consider adopting it over this base as there are inherent issues Tomm saw
  which needed to be reworked.
  ```
</details>
<br>

- Go to the archives page and select the revision you want your server to run on:
https://archive.runestats.com/osrs/
- Download whichever archive you want
- Copy the files in its "cache" folder and place them in your RS Mod folder ``${rsmod-project}/data/cache``
- Create the folder ``${rsmod-project}/data/xteas/``
- Copy the file ``xteas.json`` and place it in the ``xteas`` folder you just created

_Note| As indicated by the revision badge, the master branch of this repo is currently on 193, and you should always work to try and match client and cache when catering to a specific revision. Also a fair amount of work is required to change revisions as the protocols and interfaces change from revision to revision._

#### 6) Run the Server
This step is similar to step ``3) Install RS Mod``

- On the top-right click on the configurations box and select ``Edit Configurations...``
- On the top-left of the ``Run/Debug Configurations`` window, click on the ``+`` button
- Select ``Gradle`` from the drop-down menu
- In the Unnamed Gradle task, you should now fill in the ``Configuration``
    - ``Gradle project`` click the folder button on its right side and select the ``:game`` option
    - ``Tasks`` set value to ``run``
- Now hit the ``Apply`` and then ``Ok`` button
- Next to the new button that should appear where the other configuration was previously,
there should be a green ``run`` button, click on that and the server should begin to run.

## Creating Content
You can learn how to get started here: [RS Mod Wiki](https://github.com/Tomm0017/rsmod/wiki/Creating-Plugins)

## Troubleshooting
- *Where can I get a client?*
    - You can get a client from the same archive file you download for the cache but will require decompiling the `deob-gamepack.jar` into useable source in order to set the RSA and hostname for connecting to your server.
- *I receive a* ``Bad session id`` *message on the log-in screen*
    - This means the RSA keys on the client do not match the ones created on the server.
    You should try to follow the steps in ``4) RSA key setup`` again.
- *I receive a* ``Revision mismatch for channel`` *console message when trying to log in*
    - Your client revision must match the one indicated in the ``${rsmod-project}/game.yml`` configuration file
    - This repo is currently set for ``revision: 193`` so match your client's revision to that.
- *I receive a* ``error_game_js5connect`` *error on the client console*
    - You need to launch the server first and *then* the client
- I receive a `java.lang.NoClassDefFoundError: Could not initialize class class_name_here` when trying to log into the client
    - The RSA key you copied to the client should not include a newline (`\n`) at the end - remove it.

## FAQ

#### One or more of my plugins stopped working
- When you use a plugin, the plugin uses code it assumes you have in the core game module
when it was written and compiled. If for some reason you move, rename, or completely delete
the code that the plugin is using, the plugin will fail/stop working.

  _Note| You can add new code to the game module without this being an issue, this
    includes adding code to existing methods and files. However, it's best to
    avoid writing code to the game module and you should always opt to write a
    suggestion for the RS Mod creators to add specific features to the official
    game module or at very least for compatibility not rename, remove, or move game
    module code._

#### I have done some modifications to the game module. How can I tell if one of my plugin is no longer compatible?
- The only way to check is to download the plugin's source files and run it on your server and see if it compiles correctly!
<details>
  <summary>Notes</summary>

  ```
  - You only have to worry about **JAR** plugins suddenly becoming incompatible when you edit
    core game module code
  - Delete or move the plugin `.jar` when you add the source plugin, otherwise the plugins
    will conflict when trying to bind them on server start-up
  - In the future, we will add a tool to check if any of your JAR plugins are no longer
    compatible with your game module
  ```
</details>


#### I would like a feature added to the core game module
- If you would like a feature added, you can create a Pull Request on GitHub

#### I found a bug, where can I report it?
- You can report them as Issues on GitHub

## Acknowledgments

* ##### Tomm
    - For all his hard work on [RS Mod](https://github.com/Tomm0017/rsmod/)
    - His dedication to the OSRSPS community and producing a quality Open Source emulation server for it
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

[patch]: https://oldschool.runescape.wiki/w/2020_Christmas_event
[rev-badge]: https://img.shields.io/badge/revision-193-success
