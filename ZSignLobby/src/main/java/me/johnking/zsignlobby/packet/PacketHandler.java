package me.johnking.zsignlobby.packet;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworkplugin.EventListener;
import me.johnking.zsignlobby.sign.SignUpdater;

/**
 * Created by Marco on 04.10.2014.
 */
public class PacketHandler implements EventListener {

    private SignUpdater scheduler;

    public PacketHandler(SignUpdater scheduler) {
        PacketRegistry.registerPacket(ClusterUpdatePacket.class, 1294371234);
        this.scheduler = scheduler;
    }

    @Override
    public void onEventReceived(String event, String sender, NetworkEvent networkEvent) {
        if(event.equals("serversigns_clusterupdate") && networkEvent instanceof ClusterUpdatePacket) {
            ClusterUpdatePacket packet = (ClusterUpdatePacket) networkEvent;
            scheduler.addToQueue(packet.getSigns());
        }
    }
}
