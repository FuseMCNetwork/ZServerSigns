package me.johnking.zsignadapter;

import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworkplugin.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Marco on 03.10.2014.
 */
public class ServerListener implements Listener, EventListener{

    private ZSignAdapter adapter;

    public ServerListener(ZSignAdapter adapter) {
        this.adapter = adapter;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e){
        if(adapter.isAvailable() && !adapter.isServerFull()){
            return;
        }
        e.disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(!adapter.isAvailable() || Bukkit.getOnlinePlayers().length > adapter.getServer().getMaxPlayers()){
            e.getPlayer().kickPlayer(ChatColor.RED + "Server is full!");
            return;
        }
        adapter.sendUpdatePacket();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(adapter.isRegistered()){
            adapter.sendQuitPacket();
        }
    }

    @Override
    public void onEventReceived(String event, String sender, NetworkEvent networkEvent) {
        if(!adapter.isAvailable()) {
            return;
        }
        if(event.equals("serversigns_serverrequest")) {
            adapter.sendUpdatePacket();
        }
    }
}
