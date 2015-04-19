package me.johnking.zsigncontroller.packet;

import com.xxmicloxx.znetworklib.PacketRegistry;
import com.xxmicloxx.znetworklib.codec.NetworkEvent;
import com.xxmicloxx.znetworklib.packet.ext.ServerStatusChangeEvent;
import com.xxmicloxx.znetworkplugin.EventListener;
import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import me.johnking.zsigncontroller.SignUpdater;
import me.johnking.zsigncontroller.signs.SignServer;
import me.johnking.zsigncontroller.signs.TypeContainer;

/**
 * Created by Marco on 03.10.2014.
 */
public class PacketHandler implements EventListener{

    private SignUpdater updater;

    public PacketHandler(SignUpdater updater) {
        PacketRegistry.registerPacket(ClusterUpdatePacket.class, 1294371234);
        PacketRegistry.registerPacket(ServerChangePacket.class, 879632087);
        PacketRegistry.registerPacket(StartServerPacket.class, 91872498);
        this.updater = updater;
    }

    public void sendClusterPacket(ClusterUpdatePacket packet) {
        ZNetworkPlugin.getInstance().sendEvent("serversigns_clusterupdate", packet);
    }

    @Override
    public void onEventReceived(String event, String sender, NetworkEvent networkEvent) {
        if(event.equals("serversigns_serverchange") && networkEvent instanceof ServerChangePacket) {
            ServerChangePacket packet = (ServerChangePacket) networkEvent;
            TypeContainer container = updater.getType(packet.getType());
            if(container == null) {
                return;
            }
            SignServer server = container.getServer(sender);
            server.getServer().setData(packet);
        } else if (event.equals("serversigns_signrequest")) {
            if(updater.isEnabled()) {
                sendClusterPacket(updater.getFullClusterPacket());
            }
        } else if (event.equals("minecraft_server_stopped") && networkEvent instanceof ServerStatusChangeEvent) {
            ServerStatusChangeEvent packet = (ServerStatusChangeEvent) networkEvent;
            SignServer server = updater.getServer(packet.getServerId());
            if(server == null) {
                return;
            }
            server.getServer().setJoinable(false);
        }
    }
}
