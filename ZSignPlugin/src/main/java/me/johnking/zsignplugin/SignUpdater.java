package me.johnking.zsignplugin;

import me.johnking.fusenet.FuseNet;
import me.johnking.fusenet.mysql.MySQL;
import me.johnking.fusenet.mysql.MySQLData;
import me.johnking.zsignplugin.packet.ClusterUpdatePacket;
import me.johnking.zsignplugin.packet.PacketHandler;
import me.johnking.zsignplugin.packet.UpdateTypePacket;
import me.johnking.zsignplugin.signs.SignServer;
import me.johnking.zsignplugin.signs.SignType;
import me.johnking.zsignplugin.signs.TypeContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Created by Marco on 03.10.2014.
 */
public class SignUpdater implements Runnable{

    private final Main main;
    private Set<TypeContainer> types = Collections.newSetFromMap(new ConcurrentHashMap<TypeContainer, Boolean>());
    private MySQL mySQL;
    private PacketHandler packetHandler;
    private boolean enabled = true;
    private boolean send;

    public SignUpdater(Main main, MySQLData data) {
        this.main = main;
        this.main.getLogger().log(Level.INFO, "Loading settings from database!");
        this.mySQL = new MySQL(data);
        this.packetHandler = new PacketHandler(this);
        FuseNet.getNetworkManager().registerListener(this.main, "serversigns_serverchange", this.packetHandler);
        FuseNet.getNetworkManager().registerListener(this.main, "serversigns_signrequest", this.packetHandler);
        FuseNet.getNetworkManager().registerListener(this.main, "minecraft_server_stopped", this.packetHandler);

        loadSigns();
        FuseNet.getNetworkManager().sendEvent("serversigns_serverrequest", null);
    }

    private void loadSigns() {
        try (Connection connection = mySQL.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT id, name, slots, vip_slots, percent FROM sign_controller;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte id = rs.getByte(1);
                String name = rs.getString(2);
                int slots = rs.getInt(3);
                int vipSlots = rs.getInt(4);
                float percent = rs.getFloat(5);
                SignType type = new SignType(id, name);
                types.add(new TypeContainer(type, slots, vipSlots, percent));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ClusterUpdatePacket getFullClusterPacket() {
        ClusterUpdatePacket packet = new ClusterUpdatePacket();
        for(TypeContainer container : types) {
            packet.addType(container.getFullUpdatePacket());
        }
        return packet;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    @Override
    public void run() {
        if(isEnabled()) {
            ClusterUpdatePacket packet = new ClusterUpdatePacket();
            for (TypeContainer container : types) {
                UpdateTypePacket typePacket = container.update();
                if (typePacket.hasData()) {
                    packet.addType(typePacket);
                }
            }
            if (packet.hasData()) {
                packetHandler.sendClusterPacket(packet);
            }
        } else {
            if(send) {
                //send packet to clear all signs
                this.packetHandler.sendClusterPacket(new ClusterUpdatePacket());
                send = false;
            }
        }
    }

    public SignServer getServer(String serverID) {
        for(TypeContainer container : types) {
            for(SignServer server : container.getCache()) {
                if(server.getServer().getServerID().equals(serverID)) {
                    return server;
                }
            }
        }
        return null;
    }

    public synchronized boolean isEnabled() {
        return enabled;
    }

    public synchronized void setEnabled(boolean enabled) {
        this.enabled = enabled;
        //send packet to clear all signs
        send = !enabled;
    }

    public TypeContainer getType(byte id) {
        for(TypeContainer container : types) {
            if (container.getType().getID() == id) {
                return container;
            }
        }
        return null;
    }
}
