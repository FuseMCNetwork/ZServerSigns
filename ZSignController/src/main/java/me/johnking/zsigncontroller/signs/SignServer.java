package me.johnking.zsigncontroller.signs;

import me.johnking.zsigncontroller.server.Server;

/**
 * Created by Marco on 03.10.2014.
 */
public class SignServer {

    private Server server;
    private boolean used;

    public SignServer (Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isJoinable() {
        return this.server.isJoinable() && !this.server.isFull();
    }

    public boolean isPercentFull(float percent) {
        return server.getCurrentPlayers() >= server.getMaxPlayers() * percent / 100;
    }
}
