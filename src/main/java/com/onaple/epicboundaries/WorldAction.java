package com.onaple.epicboundaries;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class WorldAction {
    /**
     * Transfer the player into a world
     * @param player Player to transfer
     * @param world World to which transfer the player
     */
    public static void transferPlayerToWorld(Player player, World world) {
        EpicBoundaries.getLogger().info("transferPlayer");
        player.transferToWorld(world, world.getSpawnLocation().getPosition());
    }

    /**
     * Copy a world and then transfer the given player to it
     * @param player Player to transfer
     * @param worldProperties Properties of the world to copy
     * @param newWorldName Name of the new world
     */
    public static void copyWorldAndTransferPlayer(Player player, WorldProperties worldProperties, String newWorldName) {
        final Player immutablePlayer = player;
        CompletableFuture<Optional<WorldProperties>> futureCopiedWorld = Sponge.getServer().copyWorld(worldProperties, newWorldName);
        futureCopiedWorld.thenAccept(propertiesOpt -> {
            if (propertiesOpt.isPresent()) {
                EpicBoundaries.getLogger().info("This triggers");
                Optional<World> worldOpt = Sponge.getServer().loadWorld(propertiesOpt.get());
                EpicBoundaries.getLogger().info("This doesn't trigger !");
                worldOpt.ifPresent(world -> {
                    WorldAction.transferPlayerToWorld(immutablePlayer, world);
                });
            }
        });
    }
}
