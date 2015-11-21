package ru.dzen.besraznitsy.bluetooth;

/**
 * Created by azaz on 21/11/15.
 */
public class Constants {

    public static final String prefix = "ru.dzen/";
    public static final String NAME = "TEST_NAME";
    public static final String MY_UUID = "TEST_UUID";

    private static Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private Constants() {
    }
}
