package com.onaple.epicboundaries.data.beans;

public class InstanceBean {
    /**
     * Id of the instance object
     */
    private int id;
    /**
     * Name of the replicated world
     */
    private String worldName;
    /**
     * Player count inside the instance
     */
    private int playerCount;
    /**
     * Timestamp of the last player exit (used to delete the instance after a while)
     */
    private int lastExit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getLastExit() {
        return lastExit;
    }

    public void setLastExit(int lastExit) {
        this.lastExit = lastExit;
    }
}
