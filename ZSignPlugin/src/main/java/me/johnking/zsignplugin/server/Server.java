package me.johnking.zsignplugin.server;

import me.johnking.zsignplugin.packet.ServerChangePacket;

/**
 * Created by Marco on 03.10.2014.
 */
public class Server {

    private String serverID;
    private byte type;
    private int currentPlayers;
    private int maxPlayers;
    private boolean joinable;
    private boolean change;

    public Server(String serverID, byte type) {
        this.serverID = serverID;
        this.type = type;
    }

    public synchronized int getCurrentPlayers() {
        return currentPlayers;
    }

    public synchronized int getMaxPlayers() {
        return maxPlayers;
    }

    public synchronized boolean isFull() {
        return currentPlayers == maxPlayers;
    }

    public synchronized byte getType() {
        return type;
    }

    public synchronized void setData(ServerChangePacket packet) {
        this.currentPlayers = packet.getCurrentPlayers();
        this.maxPlayers = packet.getMaxPlayers();
        this.joinable = packet.isJoinable();
        this.change = true;
    }

    public synchronized void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public synchronized boolean isJoinable() {
        return joinable;
    }

    public synchronized String getServerID() {
        return serverID;
    }

    public synchronized boolean hasChanged() {
        boolean result = this.change;
        this.change = false;
        return result;
    }
}
