package me.johnking.zsignplugin.packet;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;
import me.johnking.zsignplugin.signs.SignSlot;

import java.util.ArrayList;

/**
 * Created by Marco on 03.10.2014.
 */
public class UpdateTypePacket implements NetworkEvent{

    private byte id;
    private ArrayList<SignSlot> servers = new ArrayList<>();

    public void setID(byte id) {
        this.id = id;
    }

    public void addSign(SignSlot signSlot) {
        servers.add(signSlot);
    }

    public boolean hasData() {
        return servers.size() != 0;
    }

    public CodecResult write(PacketWriter packetWriter) {
        packetWriter.writeByte(id);
        packetWriter.writeInt(servers.size());
        for(SignSlot signSlot : servers) {
            packetWriter.writeBoolean(signSlot.isOccupied());
            packetWriter.writeInt(signSlot.getSlot());
            packetWriter.writeBoolean(signSlot.isVip());
            if(signSlot.isOccupied()) {
                packetWriter.writeString(signSlot.getServer().getServer().getServerID());
                packetWriter.writeInt(signSlot.getServer().getServer().getCurrentPlayers());
                packetWriter.writeInt(signSlot.getServer().getServer().getMaxPlayers());
            }
        }
        return CodecResult.OK;
    }

    public CodecResult read(PacketReader packetReader) {
        return CodecResult.OK;
    }
}
