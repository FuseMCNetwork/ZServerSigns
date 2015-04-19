package me.johnking.zsignlobby.sign;

/**
 * Created by Marco on 04.10.2014.
 */
public class SignData {

    private byte typeID;
    private boolean clear;
    private int slot;
    private boolean vip;

    private String serverID;
    private int currentPlayers;
    private int maxPlayers;

    public SignData(byte typeID, boolean clear, int slot, boolean vip) {
        this.typeID = typeID;
        this.clear = clear;
        this.slot = slot;
        this.vip = vip;
    }

    public byte getTypeID() {
        return typeID;
    }

    public boolean isClear () {
        return clear;
    }

    public int getSlot () {
        return slot;
    }

    public boolean isVip() {
        return vip;
    }

    public String getServerID() {
        return serverID;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
