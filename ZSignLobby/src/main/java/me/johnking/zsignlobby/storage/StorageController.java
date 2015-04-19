package me.johnking.zsignlobby.storage;

import me.johnking.zsignlobby.Main;
import me.johnking.zsignlobby.sign.ServerSign;
import me.johnking.zsignlobby.sign.ServerSignType;
import me.michidk.DKLib.FileHelper;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Marco on 04.10.2014.
 */
public class StorageController {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loadServerSigns(ServerSignType type) {
        String input = FileHelper.stringFromFile(new File(Main.getInstance().getDataFolder(), "signs.json"));
        StorageSign[] signs = gson.fromJson(input, StorageSign[].class);
        if(signs == null) {
            return;
        }
        for(StorageSign sign : signs) {
            if(sign.getType() == type.getTypeID()) {
                sign.addToServerSigns(type);
            }
        }
    }

    public static void removeServerSign(ServerSign serverSign) {
        File file = new File(Main.getInstance().getDataFolder(), "signs.json");
        String input = FileHelper.stringFromFile(file);
        StorageSign[] signs = gson.fromJson(input, StorageSign[].class);
        ArrayList<StorageSign> list = new ArrayList<>();
        for(StorageSign sign : signs) {
            if(!(serverSign.getLocation().toVector().equals(sign.getLocation()) && serverSign.getLocation().getWorld().getName().equals(sign.getWorld()))) {
                list.add(sign);
            }
        }
        StorageSign[] storage = list.toArray(new StorageSign[list.size()]);
        String output = gson.toJson(storage);
        FileHelper.stringToFile(file, output);
    }

    public static void addServerSign(ServerSign serverSign, byte type) {
        File file = new File(Main.getInstance().getDataFolder(), "signs.json");
        String input = FileHelper.stringFromFile(file);
        StorageSign[] signs = gson.fromJson(input, StorageSign[].class);
        ArrayList<StorageSign> list = new ArrayList<>();
        for(StorageSign sign : signs) {
            list.add(sign);
        }
        list.add(new StorageSign(serverSign, type));
        StorageSign[] storage = list.toArray(new StorageSign[list.size()]);
        String output = gson.toJson(storage);
        FileHelper.stringToFile(file, output);
    }
}
