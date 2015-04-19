package me.johnking.zsignlobby.sign;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marco on 04.10.2014.
 */
public class ServerSignType {

    private Set<ServerSign> signs = new HashSet<>();
    private byte type;
    private String name;
    private String displayName;

    public ServerSignType(byte type, String name, String displayName) {
        this.type = type;
        this.name = name;
        this.displayName = displayName;
    }

    public ServerSign newServerSign(Location location, int slot, boolean vip) {
        ServerSign result = new ServerSign(displayName, location, slot, vip);
        signs.add(result);
        return result;
    }

    public ServerSign getServerSign(int slot, boolean vip) {
        for(ServerSign sign : signs) {
            if(sign.getSlot() == slot && sign.isVip() == vip) {
                return sign;
            }
        }
        return null;
    }

    public Set<ServerSign> getSigns() {
        return signs;
    }

    public int getNextSlot(boolean vip) {
        int slot = 0;
        for(int i = 0; i < signs.size(); i++) {
            for(ServerSign sign : signs) {
                if(sign.isVip() == vip && sign.getSlot() == slot) {
                    slot++;
                }
            }
        }
        return slot;
    }

    public byte getTypeID() {
        return type;
    }

    public String getName() {
        return name;
    }
}
