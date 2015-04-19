package me.johnking.zsignlobby.packet;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;
import me.johnking.zsignlobby.sign.SignData;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Marco on 04.10.2014.
 */
public class ClusterUpdatePacket implements NetworkEvent{

    private ArrayList<UpdateTypePacket> types = new ArrayList<>();

    @Override
    public CodecResult write(PacketWriter packetWriter) {
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader packetReader) {
        byte size = packetReader.readByte();
        for(int i = 0; i < size; i++) {
            UpdateTypePacket packet = new UpdateTypePacket();
            packet.read(packetReader);
            types.add(packet);
        }
        return CodecResult.OK;
    }

    public HashMap<Byte, ArrayList<SignData>> getSigns() {
        HashMap<Byte, ArrayList<SignData>> signs = new HashMap<>();
        for(UpdateTypePacket packet : types) {
            signs.put(packet.getID(), packet.getSigns());
        }
        return signs;
    }

    public int getSize() {
        return types.size();
    }
}
