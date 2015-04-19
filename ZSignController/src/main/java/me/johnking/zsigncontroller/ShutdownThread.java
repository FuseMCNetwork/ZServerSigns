package me.johnking.zsigncontroller;

/**
 * Created by Marco on 22.08.2014.
 */
public class ShutdownThread extends Thread{

    public void run(){
        try {
            ZSignController.getInstance().stop();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                ZSignController.getInstance().getReader().getTerminal().restore();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
