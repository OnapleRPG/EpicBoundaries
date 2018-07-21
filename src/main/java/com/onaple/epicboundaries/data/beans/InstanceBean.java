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
    private long lastExit;

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

    public long getLastExit() {
        return lastExit;
    }

    public void setLastExit(long lastExit) {
        this.lastExit = lastExit;
    }

    public InstanceBean(int id, String worldName, int playerCount, long lastExit) {
        this.id = id;
        this.worldName = worldName;
        this.playerCount = playerCount;
        this.lastExit = lastExit;
    }

    public InstanceBean(String worldName, int playerCount, long lastExit) {
        this.worldName = worldName;
        this.playerCount = playerCount;
        this.lastExit = lastExit;
    }
}
