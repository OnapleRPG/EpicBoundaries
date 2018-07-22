package com.onaple.epicboundaries.service;

import com.flowpowered.math.vector.Vector3d;

import java.util.List;
import java.util.Optional;

public interface IInstanceService {
    boolean apparate(String worldName, String playerName, Vector3d position);
    int apparate(String worldName, List<String> playerName, Vector3d position);
    Optional<String> createInstance(String worldToCopy, String playerName, Vector3d position);
}
