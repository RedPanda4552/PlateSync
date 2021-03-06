# PlateSync #

## Introduction ##
PlateSync is a Bukkit plugin that "tethers" adjacent pressure plates together so when one is set off, any nearby plates are activated as well.

## How to Use ##
Nothing fancy. If the plugin is installed, it just works. No configuration, no permissions.

## Installation ##
PlateSync is a Bukkit plugin and is to be used on a Bukkit/Spigot server. Simply drag the plugin jar into your server's plugins folder and you are good to go. The first supported Minecraft version is 1.11.2.

## LWC Compatibility ##
PlateSync is LWC aware! LWC is registered as a soft dependency and if present will be queried about protections before executing any tethering operations.

## Dependencies and Building ##
As you can probably tell this project uses Maven. The Bukkit API is handled through this. However, it also uses EntityLWC as a dependency. EntityLWC is a fork of the original LWC which has since become inactive. If you wish to compile the source yourself and need the plugin to satisfy the dependency, [you can find it here] (https://www.spigotmc.org/resources/lwc-unofficial-entity-locking.2162/).

## Problems, Bugs, All the Not-Fun Stuff ##
If you find a bug, feel free to let me know by creating an issue here on GitHub, on the [PlateSync Page on BukkitDev] (https://dev.bukkit.org/projects/platesync) or by email at [redpanda4552@gmail.com] (mailto:redpanda4552@gmail.com).