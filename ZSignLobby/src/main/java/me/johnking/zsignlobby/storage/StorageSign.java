package me.johnking.zsignlobby.storage;

import me.johnking.zsignlobby.sign.ServerSign;
import me.johnking.zsignlobby.sign.ServerSignType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Created by Marco on 04.10.2014.
 */
public class StorageSign {

    private String world;
    private Vector location;
    private byte type;
    private int slot;
    private boolean vip;

    public StorageSign(ServerSign serverSign, byte type) {
        this.world = serverSign.getLocation().getWorld().getName();
        this.location = serverSign.getLocation().toVector();
        this.type = type;
        this.slot = serverSign.getSlot();
        this.vip = serverSign.isVip();
    }

    public void addToServerSigns(ServerSignType type) {
        World world = Bukkit.getWorld(this.world);
        if(world == null) {
            return;
        }
        Location location = this.location.toLocation(world);
        type.newServerSign(location, slot, vip);
    }

    public byte getType() {
        return type;
    }

    public Vector getLocation() {
        return location;
    }

    public String getWorld() {
        return world;
    }
}
