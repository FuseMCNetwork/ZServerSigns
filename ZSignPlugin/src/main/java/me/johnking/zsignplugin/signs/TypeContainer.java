package me.johnking.zsignplugin.signs;

import me.johnking.zsignplugin.packet.UpdateTypePacket;
import me.johnking.zsignplugin.server.Server;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Marco on 03.10.2014.
 */
public class TypeContainer {

    private SignType type;
    private float percent;
    private SignSlot[] signSlots;
    private SignSlot[] vipSignSlots;
    private Set<SignServer> cache = Collections.newSetFromMap(new ConcurrentHashMap<SignServer, Boolean>());

    public TypeContainer(SignType type, int slots, int vipSlots, float percent) {
        this.type = type;
        this.percent = percent;
        setupSlots(this.signSlots = new SignSlot[slots], false);
        setupSlots(this.vipSignSlots = new SignSlot[vipSlots], true);
    }

    private void setupSlots(SignSlot[] slots, boolean vip) {
        for(int i = 0; i < slots.length; i++) {
            slots[i] = new SignSlot(this, i, vip);
        }
    }

    public UpdateTypePacket update() {
        UpdateTypePacket packet = new UpdateTypePacket();
        packet.setID(this.type.getID());
        updateType(packet, signSlots, false);
        updateType(packet, vipSignSlots, true);
        return packet;
    }

    private void updateType(UpdateTypePacket packet, SignSlot[] slots, boolean vip) {
        for(SignSlot signSlot: slots) {
            if(signSlot.shouldServerReplace(percent)) {
                signSlot.setServer(getFreeServer(percent, vip));
            }
            signSlot.addUpdatePacket(packet);
        }
    }

    public UpdateTypePacket getFullUpdatePacket() {
        UpdateTypePacket packet = new UpdateTypePacket();
        packet.setID(this.type.getID());
        for(SignSlot signSlot: signSlots) {
            packet.addSign(signSlot);
        }
        for(SignSlot signSlot: vipSignSlots) {
            packet.addSign(signSlot);
        }
        return packet;
    }

    public SignType getType() {
        return this.type;
    }

    public SignServer getServer(String serverID) {
        for(SignServer server : cache) {
            if(server.getServer().getServerID().equals(serverID)) {
                return server;
            }
        }
        SignServer server = new SignServer(new Server(serverID, this.type.getID()));
        this.cache.add(server);
        return server;
    }

    public Set<SignServer> getCache() {
        return cache;
    }

    private SignServer getFreeServer(float percent, boolean vip) {
        for(SignServer server : cache) {
            if(!server.isUsed() && server.isJoinable() && ((server.isPercentFull(percent) && vip) || (!server.isPercentFull(percent) && !vip))) {
                return server;
            }
        }
        return null;
    }
}
