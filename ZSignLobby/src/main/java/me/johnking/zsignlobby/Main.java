package me.johnking.zsignlobby;

import me.johnking.zsignlobby.sign.SignListener;
import me.johnking.zsignlobby.sign.SignUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Marco on 04.10.2014.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private SignUpdater updater;
    private SignListener listener;

    @Override
    public void onEnable() {
        instance = this;

        this.updater = new SignUpdater();
        this.listener = new SignListener(this.updater);

        Bukkit.getPluginManager().registerEvents(listener, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,  this.updater, 10L, 5L);
    }

    public static Main getInstance() {
        return instance;
    }
}
