package me.johnking.zsigncontroller.command;

import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import jline.console.ConsoleReader;
import me.johnking.zsigncontroller.SignUpdater;
import me.johnking.zsigncontroller.ZSignController;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Marco on 15.08.2014.
 */
public class CommandHandler {

    private ConsoleReader reader;

    public CommandHandler(ConsoleReader reader){
        this.reader = reader;
    }

    public void handleCommand(String command, String[] args){
        if (command.equalsIgnoreCase("clear")) {
            try {
                reader.clearScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equalsIgnoreCase("exit")) {
            ZNetworkPlugin.getInstance().shutdown();
        } else if (command.equals("disable")) {
            if(ZSignController.getInstance().getSignUpdater().isEnabled()) {
                ZSignController.getInstance().getSignUpdater().setEnabled(false);
                ZSignController.getLogger().log(Level.INFO, "Disabled Signs!");
            } else {
                ZSignController.getLogger().log(Level.INFO, "Signs are already disabled!");
            }
        } else if (command.equals("enable")) {
            SignUpdater updater = ZSignController.getInstance().getSignUpdater();
            if(!updater.isEnabled()) {
                updater.setEnabled(true);
                ZSignController.getLogger().log(Level.INFO, "Enabled Signs!");
                updater.getPacketHandler().sendClusterPacket(updater.getFullClusterPacket());
            } else {
                ZSignController.getLogger().log(Level.INFO, "Signs are already enabled!");
            }
        }
    }
}
