package me.johnking.zsignadapter;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.mysql.MySQL;
import net.fusemc.zcore.mysql.MySQLDBType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Created by Marco on 03.10.2014.
 */
public class ZSignAdapter extends JavaPlugin{

    private static ZSignAdapter instance;
    private ServerListener listener;
    private boolean joinable;
    private boolean registered;
    private byte type;

    @Override
    public void onEnable() {
        instance = this;

        PacketRegistry.registerPacket(ServerChangePacket.class, 879632087);
        this.listener = new ServerListener(this);

        Bukkit.getPluginManager().registerEvents(this.listener, this);
        ZNetworkPlugin.getInstance().registerEvent("serversigns_serverrequest", this.listener);
    }

    public static void setJoinable(boolean joinable) {
        instance.joinable = joinable;
        instance.sendUpdatePacket();
    }

    public static void register(String type) {
        if(instance.registered) {
            return;
        }
        boolean success = false;
        try (Connection connection = ZCore.getNetworkConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM sign_controller WHERE name = ?;");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                instance.type = rs.getByte(1);
                success = true;
                instance.joinable = true;
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "Signtype " + type + " is not registered in database!");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        instance.registered = success;
        if(success) {
            instance.sendUpdatePacket();
        }
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public boolean isAvailable() {
        return isJoinable() && isRegistered();
    }

    public boolean isServerFull() {
        return getServer().getOnlinePlayers().length == getServer().getMaxPlayers();
    }

    public void sendUpdatePacket() {
        ZNetworkPlugin.getInstance().sendEvent("serversigns_serverchange", new ServerChangePacket(type, getServer().getOnlinePlayers().length, getServer().getMaxPlayers(), joinable));
    }

    public void sendQuitPacket() {
        ZNetworkPlugin.getInstance().sendEvent("serversigns_serverchange", new ServerChangePacket(type, getServer().getOnlinePlayers().length - 1, getServer().getMaxPlayers(), joinable));
    }
}
