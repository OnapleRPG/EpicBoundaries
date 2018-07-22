
# EpicBoundaries   [![Build Status](https://travis-ci.org/OnapleRPG/EpicBoundaries.svg?branch=master)](https://travis-ci.org/OnapleRPG/EpicBoundaries) ![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=EpicBoundaries&metric=alert_status)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Introduction  
EpicBoundaries is a Sponge Minecraft plugin that enables the use of **world instances**.
Use the included commands to replicate a world to use as an instance which will disappear
once unused, or include this as a dependency to your own plugin to use the instances features.

## Get started
The plugin is designed to work with Sponge API 7.0.0 (Minecraft 1.12).
To install it, just drag and drop the jar file into the mods folder on your server.

## Commands
* **/apparate *world x y z* [*player*]** : Teleport a player or the command executor to the given world to a given position.  
Permission : *epicboundaries.command.apparate*
* **/create-instance *world-to-copy x y z* [*player*]** : Copy a given world to create an instance that will be removed when nobody use it.
Specify the X, Y and Z coordinates to teleport the specified player or the command executor.  
Permission : *epicboundaries.command.createinstance*

## Services
* **IInstanceService** : Give access to the instance manipulation commands to a plugin
    * *boolean* **apparate(*String worldName*, *String playerName*, *Vector3d position*)** : Teleport a *player* to a *world* at a given *position*. Returns true if the player was teleported.
    * *int* **apparate(*String worldName*, *List<String> playerNames*, *Vector3d position*)** : Teleport a *group of players* to a *world* at a given *position*. Returns the number of player successfully teleported.
    * *Optional<String>* **createInstance(*String worldToCopy*, *String playerName*, *Vector3d position*)** : Create an temporary instance from a *world* and try to teleport the *player* to this instance at the specified *position*.
