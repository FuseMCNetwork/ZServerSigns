package me.johnking.zsignplugin.command;

import me.johnking.fusenet.command.Command;
import me.johnking.fusenet.command.TabCompleter;
import me.johnking.zsignplugin.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 19.10.2014.
 */
public class SignCommand extends Command implements TabCompleter{

    public SignCommand() {
        super("sign");
    }

    @Override
    public boolean onCommand(String[] args) {
        if(args.length == 0) {
            System.out.println("Usage: /sign <enable:disable>");
            return true;
        }
        if(args[0].equalsIgnoreCase("enable")) {
            if(Main.getInstance().getSignUpdater().isEnabled()) {
                System.out.println("Signs are already enabled!");
            } else {
                Main.getInstance().getSignUpdater().setEnabled(true);
                System.out.println("Enabled Signs!");
            }
        } else if (args[0].equalsIgnoreCase("disable")) {
            if(!Main.getInstance().getSignUpdater().isEnabled()) {
                System.out.println("Signs are already disabled!");
            } else {
                Main.getInstance().getSignUpdater().setEnabled(false);
                System.out.println("Disabled Signs!");
            }
        } else {
            System.out.println("Usage: /sign <enable:disable>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        ArrayList<String> results = new ArrayList<>();
        if(args.length == 1) {
            String begin = args[0] == null ? "" : args[0];
            if("enable".startsWith(begin)) {
                results.add("enable");
            }
            if ("disable".startsWith(begin)) {
                results.add("disable");
            }
        }
        return results;
    }
}
