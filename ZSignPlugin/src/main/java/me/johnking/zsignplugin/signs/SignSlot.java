package me.johnking.zsignplugin.signs;

import me.johnking.fusenet.FuseNet;
import me.johnking.zsignplugin.packet.StartServerPacket;
import me.johnking.zsignplugin.packet.UpdateTypePacket;

/**
 * Created by Marco on 03.10.2014.
 */
public class SignSlot {

    private TypeContainer type;
    private int slot;
    private boolean vip;
    private SignServer server;
    private boolean update;

    public SignSlot(TypeContainer type, int slot, boolean vip) {
        this.type = type;
        this.slot = slot;
        this.vip = vip;
    }

    public void setServer(SignServer server) {
        if(this.server == null && server == null) {
            return;
        }
        if(this.server != null) {
            this.server.setUsed(false);
            if(server == null) {
                //start new server //ownpacket
                if(!this.vip) {
                    FuseNet.getNetworkManager().sendEvent("serversigns_startserver", new StartServerPacket(this.server.getServer().getServerID()));
                }
            }
        }
        if(server != null) {
            server.setUsed(true);
        }
        this.server = server;
        this.update = true;
    }

    public SignServer getServer() {
        return server;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isOccupied() {
        return this.server != null;
    }

    public boolean shouldServerReplace(float percent) {
        return this.server == null || !this.server.isJoinable() || (this.server.isPercentFull(percent) && !vip);
    }

    private boolean shouldUpdate() {
        boolean result = this.update;
        this.update = false;
        return result || (this.server != null && server.getServer().hasChanged());
    }

    public void addUpdatePacket(UpdateTypePacket packet) {
        if(shouldUpdate()) {
            packet.addSign(this);
        }
    }

    public boolean isVip() {
        return vip;
    }
}
