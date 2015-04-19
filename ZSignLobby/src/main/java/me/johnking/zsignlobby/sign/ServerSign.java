package me.johnking.zsignlobby.sign;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

/**
 * Created by Marco on 04.10.2014.
 */
public class ServerSign {

    private String displayName;
    private Location location;
    private int slot;
    private boolean vip;
    private boolean displayed;
    private String serverID;
    private int currentPlayers;
    private int maxPlayers;

    public ServerSign(String displayName, Location location, int slot, boolean vip) {
        this.displayName = displayName;
        this.location = location;
        this.vip = vip;
        this.slot = slot;
        updateSign();
    }

    public void setData(SignData data) {
        this.displayed = !data.isClear();
        if(this.displayed) {
            this.serverID = data.getServerID();
            this.currentPlayers = data.getCurrentPlayers();
            this.maxPlayers = data.getMaxPlayers();
        }
        updateSign();
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
        updateSign();
    }

    public Location getLocation() {
        return location;
    }

    private void updateSign() {
        BlockState state = location.getBlock().getState();
        if(!(state instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) state;
        if(this.displayed) {
            sign.setLine(0, displayName);
            sign.setLine(1, (vip ? ChatColor.DARK_PURPLE : ChatColor.DARK_GREEN) + "\u25CF \u25CF \u25CF");
            sign.setLine(2, ChatColor.GREEN + "" + currentPlayers + ChatColor.DARK_GRAY + "/" + ChatColor.RED + maxPlayers);
        } else {
            sign.setLine(0, "");
            sign.setLine(1, ChatColor.ITALIC + "Server wird");
            sign.setLine(2, ChatColor.ITALIC + "gesucht!");
        }
        sign.update(true);
    }

    public int getSlot() {
        return slot;
    }

    public boolean isVip() {
        return vip;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public String getServerID() {
        return serverID;
    }
}
