package me.johnking.zsignlobby.packet;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;
import me.johnking.zsignlobby.sign.SignData;

import java.util.ArrayList;

/**
 * Created by Marco on 04.10.2014.
 */
public class UpdateTypePacket implements NetworkEvent{

    private ArrayList<SignData> signs = new ArrayList<>();
    private byte id;

    @Override
    public CodecResult write(PacketWriter packetWriter) {
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader packetReader) {
        id = packetReader.readByte();
        int size = packetReader.readInt();
        for(int i = 0; i < size; i++) {
            SignData data = new SignData(id, !packetReader.readBoolean(), packetReader.readInt(), packetReader.readBoolean());
            if(!data.isClear()) {
                data.setServerID(packetReader.readString());
                data.setCurrentPlayers(packetReader.readInt());
                data.setMaxPlayers(packetReader.readInt());
            }
            signs.add(data);
        }
        return CodecResult.OK;
    }

    public ArrayList<SignData> getSigns() {
        return signs;
    }

    public byte getID() {
        return id;
    }
}
