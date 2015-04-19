package me.johnking.zsigncontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxmicloxx.znetworklib.packet.core.GeneralRequest;
import com.xxmicloxx.znetworkplugin.ResultHandler;
import com.xxmicloxx.znetworkplugin.ZNativePlugin;
import com.xxmicloxx.znetworkplugin.ZNetworkPlugin;
import jline.console.ConsoleReader;
import me.johnking.zsigncontroller.command.CommandHandler;
import me.johnking.zsigncontroller.command.CommandLineReader;
import me.johnking.zsigncontroller.mysql.MySQL;
import me.johnking.zsigncontroller.mysql.MySQLData;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Marco on 02.10.2014.
 */
public class ZSignController implements ZNativePlugin{

    private static Logger logger;

    private static ZSignController instance;
    private ConsoleReader reader;
    private ShutdownThread shutdownThread;
    private String name;
    private Gson gson;
    private CommandLineReader commandReader;
    private CommandHandler handler;
    private SignUpdater updater;

    public static void main(String[] args) {
        instance = new ZSignController();
        instance.enable();
    }

    public void start() {
        //////////////////////////////////////////////////////
        File file = new File("mysql.json");
        String input = FileHelper.stringFromFile(file);
        MySQLData data = gson.fromJson(input, MySQLData.class);
        this.updater = new SignUpdater(data);
        logger.log(Level.INFO, "Done!");
        this.updater.start();
        //////////////////////////////////////////////////////
    }

    public void stop() {
        //////////////////////////////////////////////////////
        logger.log(Level.INFO, "Disabling all signs!");
        this.updater.setEnabled(false);
        //////////////////////////////////////////////////////
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ZSignController.getLogger().log(Level.INFO, "Disabling network-connection ... ");
        ZNetworkPlugin.getInstance().onDisable();
        ZSignController.getLogger().log(Level.INFO, "Done! Thank you and goodbye!");
    }

    public void enable() {
        new ZNetworkPlugin(this);
        ZNetworkPlugin.getInstance().onEnable();
        
        try {
            this.reader = new ConsoleReader(System.in, System.out);
        } catch (IOException e){
            System.out.println("Could not load console reader!");
            System.exit(2);
        }
        this.handler = new CommandHandler(this.reader);
        this.commandReader = new CommandLineReader(this.handler, this.reader);

        logger = createLogger();

        name = ZNetworkPlugin.getInstance().getConnectionName();
        if(name == null || name.equals("")){
            logger.log(Level.SEVERE, "Could not load my name!");
            System.exit(1);
        }
        gson = new GsonBuilder().setPrettyPrinting().create();
        this.shutdownThread = new ShutdownThread();
        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
        start();
    }

    @Override
    public void shutdown() {
        Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        try {
            stop();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                this.reader.getTerminal().restore();
            } catch (Exception e){
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public void handleRequest(GeneralRequest generalRequest, ResultHandler resultHandler) {

    }

    private static Logger createLogger(){
        Logger logger = Logger.getLogger("ZSignController");
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;
    }

    public ConsoleReader getReader() {
        return reader;
    }

    public static ZSignController getInstance() {
        return instance;
    }

    public static Logger getLogger(){
        return logger;
    }

    public SignUpdater getSignUpdater() {
        return updater;
    }
}
