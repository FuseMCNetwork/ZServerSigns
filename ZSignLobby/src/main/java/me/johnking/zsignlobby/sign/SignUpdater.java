package me.johnking.zsignlobby.sign;

import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import me.johnking.zsignlobby.packet.PacketHandler;
import me.johnking.zsignlobby.storage.StorageController;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.mysql.MySQL;
import net.fusemc.zcore.mysql.MySQLDBType;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Marco on 04.10.2014.
 */
public class SignUpdater implements Runnable {

    private ConcurrentLinkedQueue<HashMap<Byte, ArrayList<SignData>>> queue = new ConcurrentLinkedQueue<>();
    private Set<ServerSignType> types = Collections.newSetFromMap(new ConcurrentHashMap<ServerSignType, Boolean>());
    private PacketHandler handler;

    public SignUpdater() {
        this.handler = new PacketHandler(this);

        loadServerTypes();
        loadSigns();

        ZNetworkPlugin.getInstance().registerEvent("serversigns_clusterupdate", this.handler);
        ZNetworkPlugin.getInstance().sendEvent("serversigns_signrequest", null);
    }

    private void loadServerTypes() {
        try (Connection connection = ZCore.getNetworkConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT id, name, display_name FROM sign_controller;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                byte id = rs.getByte(1);
                String name = rs.getString(2);
                String displayName = rs.getString(3);
                types.add(new ServerSignType(id, name, displayName));
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSigns() {
        for(ServerSignType type : types) {
            StorageController.loadServerSigns(type);
        }
    }

    @Override
    public void run() {
        if(queue.size() > 100) {
            queue.clear();
            ZNetworkPlugin.getInstance().sendEvent("serversigns_signrequest", null);
        }
        int size = queue.size();
        for(int i = 0; i < size; i++) {
            HashMap<Byte, ArrayList<SignData>> signs = queue.poll();
            if(signs.size() == 0) {
                queue.clear();
                for(ServerSignType type : types) {
                    for(ServerSign sign : type.getSigns()) {
                        sign.setDisplayed(false);
                    }
                }
                return;
            }
            for(Map.Entry<Byte, ArrayList<SignData>> entry : signs.entrySet()) {
                ServerSignType type = getTypeFromID(entry.getKey());
                if(type == null) {
                    continue;
                }
                for(SignData data : entry.getValue()) {
                    ServerSign sign = type.getServerSign(data.getSlot(), data.isVip());
                    if(sign == null) {
                        continue;
                    }
                    sign.setData(data);
                }
            }
        }
    }

    public void addToQueue(HashMap<Byte, ArrayList<SignData>> signs) {
        queue.add(signs);
    }

    public PacketHandler getPacketHandler() {
        return handler;
    }

    public ServerSignType getTypeFromID(byte id) {
        for(ServerSignType type : types) {
            if(type.getTypeID() == id) {
                return type;
            }
        }
        return null;
    }

    public ServerSignType getTypeFromName(String name) {
        for(ServerSignType type : types) {
            if(type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public ServerSign getServerSign(Location location) {
        for(ServerSignType type : types) {
            for(ServerSign sign : type.getSigns()) {
                if(sign.getLocation().equals(location)) {
                    return sign;
                }
            }
        }
        return null;
    }

    public void removeServerSign(ServerSign serverSign) {
        for(ServerSignType type : types) {
            if(type.getSigns().contains(serverSign)) {
                type.getSigns().remove(serverSign);
            }
        }
        StorageController.removeServerSign(serverSign);
    }

    public void addServerSign(Location location, ServerSignType type, boolean vip) {
        int slot = type.getNextSlot(vip);
        ServerSign serverSign = type.newServerSign(location, slot, vip);
        StorageController.addServerSign(serverSign, type.getTypeID());
        ZNetworkPlugin.getInstance().sendEvent("serversigns_signrequest", null);
    }
}
