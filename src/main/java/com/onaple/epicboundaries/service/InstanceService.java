package com.onaple.epicboundaries.service;

import com.onaple.epicboundaries.WorldAction;
import org.spongepowered.api.Sponge;

import java.util.List;
import java.util.Optional;

public class InstanceService implements IInstanceService {
    /**
     * Try to transfer a player to a given world
     * @param worldName Name of the world
     * @param playerName Name of the player
     * @return True if player has been transferred
     */
    @Override
    public boolean apparate(String worldName, String playerName) {
        return Sponge.getServer().loadWorld(worldName).map(world ->
            Sponge.getServer().getPlayer(playerName).map(player -> {
                WorldAction worldAction = new WorldAction();
                worldAction.transferPlayerToWorld(player, world);
                return true;
            }).orElse(false)
        ).orElse(false);
    }

    /**
     * Try to transfer a group of players to a given world
     * @param worldName Name of the world
     * @param playerNames Names of the players
     * @return Successful transfer count
     */
    @Override
    public int apparate(String worldName, List<String> playerNames) {
        return Sponge.getServer().loadWorld(worldName).map(world -> {
            int playerTransferCount = 0;
            WorldAction worldAction = new WorldAction();
            for(String playerName : playerNames) {
                playerTransferCount += Sponge.getServer().getPlayer(playerName).map(player -> {
                    worldAction.transferPlayerToWorld(player, world);
                    return 1;
                }).orElse(0);
            }
            return playerTransferCount;
        }).orElse(0);
    }

    /**
     * Try to create an instance from a world and transfer a player to it
     * @param worldToCopy World to copy
     * @param playerName Name of the player to transfer
     * @return Optional of world name, if copy successfully initiated
     */
    @Override
    public Optional<String> createInstance(String worldToCopy, String playerName) {
        if (worldToCopy.equals(Sponge.getServer().getDefaultWorldName())) {
            return Optional.empty();
        }
        Sponge.getServer().getWorldProperties(worldToCopy).ifPresent(worldProperties -> {
            String uuid, newWorldName;
            do {
                uuid = java.util.UUID.randomUUID().toString();
                newWorldName = uuid;
            } while (Sponge.getServer().getWorldProperties(newWorldName).isPresent());
            WorldAction worldAction = new WorldAction();
            worldAction.copyWorld(worldProperties, newWorldName);
            worldAction.addPlayerToTransferQueue(playerName, newWorldName);
        });
        return Optional.empty();
    }
}
