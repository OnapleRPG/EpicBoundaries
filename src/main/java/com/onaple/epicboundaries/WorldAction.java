package com.onaple.epicboundaries;

import com.onaple.epicboundaries.data.beans.InstanceBean;
import com.onaple.epicboundaries.event.CopyWorldEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class WorldAction {
    private static Map<String, String> playersToTransfer = new HashMap<>();

    /**
     * Transfer the player into a world
     * @param player Player to transfer
     * @param world World to which transfer the player
     */
    public void transferPlayerToWorld(Player player, World world) {
        EpicBoundaries.getInstanceDao().updateInstancePlayerCount(player.getWorld().getName(), -1);
        player.transferToWorld(world, world.getSpawnLocation().getPosition());
        EpicBoundaries.getInstanceDao().updateInstancePlayerCount(world.getName(), 1);
    }

    /**
     * Try to consume the player transfer queue to transfer players to a given world
     * @param worldName Name of the world to transfer player(s) to
     */
    public void consumePlayerTransferQueue(String worldName) {
        for (Map.Entry<String, String> entry : playersToTransfer.entrySet()) {
            if (entry.getValue().equals(worldName)) {
                Sponge.getServer().loadWorld(worldName).ifPresent(world -> {
                    Sponge.getServer().getPlayer(entry.getKey()).ifPresent(player -> {
                        transferPlayerToWorld(player, world);
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
        SpongeExecutorService minecraftExecutor = Sponge.getScheduler().createSyncExecutor(EpicBoundaries.getPluginContainer());
        CompletableFuture<Optional<WorldProperties>> futureCopiedWorld = Sponge.getServer().copyWorld(worldProperties, newWorldName);
        futureCopiedWorld.thenAcceptAsync(propertiesOpt -> {
            propertiesOpt.ifPresent(properties -> {
                registerInstance(properties.getWorldName());
                EventContext context = EventContext.builder().build();
                Cause cause = Cause.builder().append(EpicBoundaries.getPluginContainer()).build(context);
                CopyWorldEvent copyEvent = new CopyWorldEvent(cause, properties);
                Sponge.getEventManager().post(copyEvent);
            });
        }, minecraftExecutor);
    }

    /**
     * Register an instance for a world
     * @param worldName Name of the new instance/world
     */
    private void registerInstance(String worldName) {
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
        long time = currentTimestamp.getTime()/1000;
        InstanceBean newInstance = new InstanceBean(worldName, 0, (int)time);
        EpicBoundaries.getInstanceDao().addInstance(newInstance);
    }

    /**
     * Add a player and a world name to the transfer queue so the player can be transfered after copy is finished
     * @param playerName Name of the player waiting for transfer
     * @param worldName Name of the world to transfer the player to
     */
    public void addPlayerToTransferQueue(String playerName, String worldName) {
        playersToTransfer.put(playerName, worldName);
    }

    /**
     * Remove all instances considered as deprecated
     */
    public void removeDeprecatedInstances() {
        List<InstanceBean> instances = EpicBoundaries.getInstanceDao().getDeprecatedInstances();
        for (InstanceBean instance : instances) {
            String worldName = instance.getWorldName();
            if (!Sponge.getServer().getDefaultWorldName().equals(worldName)) {
                deleteWorld(worldName);
            }
        }
        EpicBoundaries.getInstanceDao().removeInstances(instances);
    }

    /**
     * Delete a given world
     * @param worldName World name
     */
    private void deleteWorld(String worldName) {
        SpongeExecutorService minecraftExecutor = Sponge.getScheduler().createSyncExecutor(EpicBoundaries.getPluginContainer());
        Sponge.getServer().getWorld(worldName).ifPresent(world -> {
            if (!world.getPlayers().isEmpty()) {
                EpicBoundaries.getLogger().warn("Attempting to unload and delete a non empty world !");
                return;
            }
            Sponge.getServer().unloadWorld(world);
        });
        EpicBoundaries.getLogger().info("Deleting world ".concat(worldName).concat("..."));
        Sponge.getServer().getWorldProperties(worldName).ifPresent(properties -> {
            CompletableFuture<Boolean> worldDeletion = Sponge.getServer().deleteWorld(properties);
            worldDeletion.thenAcceptAsync(deletion -> {

            }, minecraftExecutor);
        });
    }
}