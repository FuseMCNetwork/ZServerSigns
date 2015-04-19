package me.johnking.zsignadapter;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;

/**
 * Created by Marco on 03.10.2014.
 */
public class ServerChangePacket implements NetworkEvent{

    private byte type;
    private int currentPlayers;
    private int maxPlayers;
    private boolean joinable;

    public ServerChangePacket() {

    }

    public ServerChangePacket(byte type, int currentPlayers, int maxPlayers, boolean joinable) {
        this.type = type;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.joinable = joinable;
    }

    @Override
    public CodecResult write(PacketWriter packetWriter) {
        packetWriter.writeByte(type);
        packetWriter.writeInt(currentPlayers);
        packetWriter.writeInt(maxPlayers);
        packetWriter.writeBoolean(joinable);
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader packetReader) {
        this.type = packetReader.readByte();
        this.currentPlayers = packetReader.readInt();
        this.maxPlayers = packetReader.readInt();
        this.joinable = packetReader.readBoolean();
        return CodecResult.OK;
    }

    public byte getType() {
        return type;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isJoinable() {
        return joinable;
    }
}
