package me.johnking.zsignplugin.signs;

/**
 * Created by Marco on 03.10.2014.
 */
public class SignType {

    private byte id;
    private String name;

    public SignType(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    public byte getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
