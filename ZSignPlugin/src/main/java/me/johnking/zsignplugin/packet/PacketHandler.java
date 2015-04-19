package me.johnking.zsignplugin.packet;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworklib.packet.ext.ServerStatusChangeEvent;
import me.johnking.fusenet.FuseNet;
import me.johnking.fusenet.network.event.BroadcastEvent;
import me.johnking.fusenet.network.event.NetworkListener;
import me.johnking.zsignplugin.SignUpdater;
import me.johnking.zsignplugin.signs.SignServer;
import me.johnking.zsignplugin.signs.TypeContainer;

/**
 * Created by Marco on 03.10.2014.
 */
public class PacketHandler implements NetworkListener{

    private SignUpdater updater;

    public PacketHandler(SignUpdater updater) {
        PacketRegistry.registerPacket(ClusterUpdatePacket.class, 1294371234);
        PacketRegistry.registerPacket(ServerChangePacket.class, 879632087);
        PacketRegistry.registerPacket(StartServerPacket.class, 91872498);
        this.updater = updater;
    }

    public void sendClusterPacket(ClusterUpdatePacket packet) {
        FuseNet.getNetworkManager().sendEvent("serversigns_clusterupdate", packet);
    }

    @Override
    public void onEvent(BroadcastEvent event) {
        if(event.getName().equals("serversigns_serverchange") && event.getData() instanceof ServerChangePacket) {
            ServerChangePacket packet = (ServerChangePacket) event.getData();
            TypeContainer container = updater.getType(packet.getType());
            if(container == null) {
                return;
            }
            SignServer server = container.getServer(event.getSender());
            server.getServer().setData(packet);
        } else if (event.getName().equals("serversigns_signrequest")) {
            if(updater.isEnabled()) {
                sendClusterPacket(updater.getFullClusterPacket());
            }
        } else if (event.getName().equals("minecraft_server_stopped") && event.getData() instanceof ServerStatusChangeEvent) {
            ServerStatusChangeEvent packet = (ServerStatusChangeEvent) event.getData();
            SignServer server = updater.getServer(packet.getServerId());
            if(server == null) {
                return;
            }
            server.getServer().setJoinable(false);
        }
    }
}
