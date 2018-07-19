package com.onaple.epicboundaries;

import com.onaple.epicboundaries.event.CopyWorldEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorldAction {
    static Map<String, String> playersToTransfer = new HashMap<>();

    /**
     * Transfer the player into a world
     * @param player Player to transfer
     * @param world World to which transfer the player
     */
    public void transferPlayerToWorld(Player player, World world) {
        player.transferToWorld(world, world.getSpawnLocation().getPosition());
    }

    /**
     * Try to consume the player transfer queue to transfer players to a given world
     * @param worldName Name of the world to transfer player(s) to
     */
    public void consumePlayerTransferQueue(String worldName) {
        for (Map.Entry<String, String> entry : playersToTransfer.entrySet()) {
            if (entry.getValue().equals(worldName)) {
                EpicBoundaries.getLogger().info("world name matches");
                Sponge.getServer().loadWorld(worldName).ifPresent(world -> {
                    EpicBoundaries.getLogger().info("world loaded !");
                    Sponge.getServer().getPlayer(entry.getKey()).ifPresent(player -> {
                        EpicBoundaries.getLogger().info("player retrieved !");
                        transferPlayerToWorld(player, world);
                        EpicBoundaries.getLogger().info("player transfered !");
                    });
                });
                playersToTransfer.remove(entry.getKey());
            }
        }
    }

    /**
     * Copy a world
     * @param worldProperties Properties of the world to copy
     * @param newWorldName Name of the new world
     */
    public void copyWorld(WorldProperties worldProperties, String newWorldName) {
        EpicBoundaries.getLogger().info("Creating duplicate world ".concat(newWorldName).concat("..."));
        CompletableFuture<Optional<WorldProperties>> futureCopiedWorld = Sponge.getServer().copyWorld(worldProperties, newWorldName);
        futureCopiedWorld.thenAcceptAsync(propertiesOpt -> {
            propertiesOpt.ifPresent(properties -> {
                EventContext context = EventContext.builder().build();
                Cause cause = Cause.builder().append(EpicBoundaries.getPluginContainer()).build(context);
                CopyWorldEvent copyEvent = new CopyWorldEvent(cause, properties);
                Sponge.getEventManager().post(copyEvent);
            });
        });
    }

    /**
     * Add a player and a world name to the transfer queue so the player can be transfered after copy is finished
     * @param playerName Name of the player waiting for transfer
     * @param worldName Name of the world to transfer the player to
     */
    public void addPlayerToTransferQueue(String playerName, String worldName) {
        playersToTransfer.put(playerName, worldName);
    }
}
