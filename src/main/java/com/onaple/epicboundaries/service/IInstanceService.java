package com.onaple.epicboundaries.service;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInstanceService {
    boolean apparate(String worldName, String playerName, Vector3d position);

    int apparate(String worldName, List<Player> players, Vector3d position);

    Optional<UUID> createInstance(World worldToCopy);
}
