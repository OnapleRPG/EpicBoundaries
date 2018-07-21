package com.onaple.epicboundaries.service;

import java.util.List;
import java.util.Optional;

public interface IInstanceService {
    boolean apparate(String worldName, String playerName);
    int apparate(String worldName, List<String> playerName);
    Optional<String> createInstance(String worldToCopy, String playerName);
}
