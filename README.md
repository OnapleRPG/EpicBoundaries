# EpicBoundaries  ![Build Status](https://github.com/OnapleRPG/EpicBoundaries/actions/workflows/gradle.yml/badge.svg) ![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.onaple%3AEpicBoundaries&metric=reliability_rating) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![sponge version](https://img.shields.io/badge/sponge-7.2.0-blue.svg)](https://www.spongepowered.org/)

## Introduction  
EpicBoundaries is a Sponge Minecraft plugin that enables the use of **world instances**.
Use the included commands to replicate a world to use as an instance which will disappear
once unused, or include this as a dependency to your own plugin to use the instances features.

## Get started
The plugin is designed to work with Sponge API 7.0.0 (Minecraft 1.12).
To install it, just drag and drop the jar file into the mods folder on your server.

### Commands
* **/apparate *world x y z* [*player*]** : Teleport a player or the command executor to the given world to a given position.  
Permission : *epicboundaries.command.apparate*
* **/create-instance *world-to-copy x y z* [*player*]** : Copy a given world to create an instance that will be removed when nobody use it.
Specify the X, Y and Z coordinates to teleport the specified player or the command executor.  
Permission : *epicboundaries.command.createinstance*

### Services
* **IInstanceService** : Give access to the instance manipulation commands to a plugin
    * *boolean* **apparate(*String worldName*, *String playerName*, *Vector3d position*)** : Teleport a *player* to a *world* at a given *position*. Returns true if the player was teleported.
    * *int* **apparate(*String worldName*, *List<String> playerNames*, *Vector3d position*)** : Teleport a *group of players* to a *world* at a given *position*. Returns the number of player successfully teleported.
    * *Optional<String>* **createInstance(*String worldToCopy*, *String playerName*, *Vector3d position*)** : Create an temporary instance from a *world* and try to teleport the *player* to this instance at the specified *position*.

## Preparing worlds

By default, your server will have the following worlds:  
- "**world**" for your overworld  
- "**DIM-1**" for the nether, if you generated it  
- "**DIM1**" for the end, if you generated it  

If you want to add a world from a single player save to your server, copy your save folder within your server "world" folder. You can delete the DIM1 and DIM-1 within it.  

In case the world you want to add is a copy of an existing world on your server, you will need an extra step.  
Server world names are handled by the *level.dat* file, and you will need to edit it to avoid duplicates.  
> Edit the *level.dat* of the world you want to import, not your main world's *level.dat*  

You cannot edit it with a text editor, but with some kind of NBT data editor. [Here is one online](https://irath96.github.io/webNBT/).  
Change the *LevelName* with something unique.  

#### *Special case, like copying "the end"* (hopefully you don't need it)  
In case you want to copy the end, you will have to get rid of some of the *level.dat* data: The enderdragon comes with UUID, unique identifier that cannot exist twice. You will need to delete or change those UUID.  
