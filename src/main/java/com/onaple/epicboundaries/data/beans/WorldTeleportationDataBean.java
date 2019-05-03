package com.onaple.epicboundaries.data.beans;

import com.flowpowered.math.vector.Vector3d;

public class WorldTeleportationDataBean {
    private Vector3d position;
    private String worldName;
    private String playerName;

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public WorldTeleportationDataBean(Vector3d position, String worldName, String playerName) {
        this.position = position;
        this.worldName = worldName;
        this.playerName = playerName;
    }
}
