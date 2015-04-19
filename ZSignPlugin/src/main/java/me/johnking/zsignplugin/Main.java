package me.johnking.zsignplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.johnking.fusenet.FuseNet;
import me.johnking.fusenet.mysql.MySQLData;
import me.johnking.fusenet.plugin.Plugin;
import me.johnking.fusenet.plugin.PluginData;
import me.johnking.fusenet.scheduler.ScheduleUnit;
import me.johnking.zsignplugin.command.SignCommand;
import me.johnking.zsignplugin.packet.ClusterUpdatePacket;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by Marco on 19.10.2014.
 */
@PluginData(name = "SignPlugin", version = "1.0.0")
public class Main extends Plugin {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Main instance;
    private SignUpdater updater;

    @Override
    public void onEnable() {
        instance = this;

        File file = new File("mysql.json");
        String input = FileHelper.stringFromFile(file);
        MySQLData data = gson.fromJson(input, MySQLData.class);
        this.updater = new SignUpdater(this, data);
        this.getLogger().log(Level.INFO, "Done!");

        FuseNet.getCommandManager().registerCommand(this, new SignCommand());
        FuseNet.getScheduler().scheduleSyncRepeatingTask(this, updater, 2, ScheduleUnit.TICKS);
    }

    @Override
    public void onDisable() {
        this.updater.getPacketHandler().sendClusterPacket(new ClusterUpdatePacket());
    }

    public SignUpdater getSignUpdater() {
        return updater;
    }

    public static Main getInstance() {
        return instance;
    }
}
