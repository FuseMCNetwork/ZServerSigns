package me.johnking.zsigncontroller.packet;

import com.xxmicloxx.znetworklib.codec.CodecResult;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.codec.PacketReader;
import com.xxmicloxx.znetworklib.codec.PacketWriter;

import java.util.ArrayList;

/**
 * Created by Marco on 03.10.2014.
 */
public class ClusterUpdatePacket implements NetworkEvent {

    private ArrayList<UpdateTypePacket> packets = new ArrayList<>();

    public void addType(UpdateTypePacket packet) {
        packets.add(packet);
    }

    public boolean hasData() {
        return packets.size() != 0;
    }

    @Override
    public CodecResult write(PacketWriter packetWriter) {
        packetWriter.writeByte(packets.size());
        for(UpdateTypePacket packet: packets) {
            packet.write(packetWriter);
        }
        return CodecResult.OK;
    }

    @Override
    public CodecResult read(PacketReader packetReader) {
        return CodecResult.OK;
    }
}
