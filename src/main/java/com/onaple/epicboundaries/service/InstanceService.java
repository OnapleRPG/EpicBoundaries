package com.onaple.epicboundaries.service;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.WorldAction;
import com.onaple.epicboundaries.commands.CommandAbstract;
import com.onaple.epicboundaries.event.ApparateEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InstanceService implements IInstanceService {
    /**
     * Try to transfer a player to a given world
     * @param worldName Name of the world
     * @param playerName Name of the player
     * @param position Position to transfer the player to
     * @return True if player has been transferred
     */
    @Override
    public boolean apparate(String worldName, String playerName, Vector3d position) {
        return Sponge.getServer().loadWorld(worldName).map(world ->
            Sponge.getServer().getPlayer(playerName).map(player -> {
                Location<World> location = world.getLocation(position);
                ApparateEvent apparateEvent = new ApparateEvent(player,location);
                Sponge.getEventManager().post(apparateEvent);
                if(!apparateEvent.isCancelled()){
                    EpicBoundaries.getWorldAction().transferPlayerToWorld(player,location);
                }
                return true;
            }).orElse(false)
        ).orElse(false);
    }

    /**
     * Try to transfer a group of players to a given world
     * @param worldName Name of the world
     * @param players  players
     * @param position Position to transfer the players to
     * @return Successful transfer count
     */
    @Override
    public int apparate(String worldName, List<Player> players, Vector3d position) {
        return Sponge.getServer().loadWorld(worldName).map(world -> {
            Location<World> location = world.getLocation(position);
            ApparateEvent apparateEvent = new ApparateEvent(EpicBoundaries.getInstance(),location,players);
            Sponge.getEventManager().post(apparateEvent);
            if(!apparateEvent.isCancelled()){
                EpicBoundaries.getWorldAction().transferPlayersToWorld(players,location);
                return players.size();
            }
         return 0;
        }).orElse(0);
    }

    /**
     * Try to create an instance from a world and transfer a player to it
     * @param worldToCopy World to copy
     * @return Optional of world name, if copy successfully initiated
     */
    @Override
    public Optional<UUID> createInstance(World worldToCopy) {
        if (worldToCopy.getName().equals(Sponge.getServer().getDefaultWorldName())) {
            return Optional.empty();
        }

            UUID uuid;
            do {
                uuid = java.util.UUID.randomUUID();

            } while (Sponge.getServer().getWorldProperties(uuid.toString()).isPresent());
            EpicBoundaries.getWorldAction().copyWorld(worldToCopy.getProperties(), uuid.toString());


        return Optional.of(uuid);
    }
}
