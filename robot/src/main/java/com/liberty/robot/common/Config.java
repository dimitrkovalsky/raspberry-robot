package com.liberty.robot.common;

import org.apache.commons.lang3.SystemUtils;

import static utils.LoggingUtil.localInfo;

/**
 * Created by Dmytro_Kovalskyi on 11.06.2015.
 */
public class Config {
    public static boolean VOICE_LOG_ENABLED = false;
    public static boolean VOICE_RECORDING_ENABLED = false;
    public static boolean SPEAKERS_ENABLED = false;
    public static String SERVER_IP = "127.0.0.1";
    public static int VOICE_BUFFER_SIZE = 1024;
    public static byte ARDUINO_ADDRESS = 0;  // UART device

    public static String VOICE_SAMPLES_FOLDER;
    public static String PATH_SEPARATOR = SystemUtils.FILE_SEPARATOR;

    static {
        if(SystemUtils.IS_OS_WINDOWS) {
            localInfo(Config.class, "Current OS is Windows");
            VOICE_SAMPLES_FOLDER = "D:\\voices\\";
        }
        else {
            localInfo(Config.class, "Current OS is Linux");
            VOICE_SAMPLES_FOLDER = "/opt/java/voices/";
        }

    }

    public static String show() {
        return "Config{" +
            "VOICE_LOG_ENABLED=" + VOICE_LOG_ENABLED +
            ", VOICE_RECORDING_ENABLED=" + VOICE_RECORDING_ENABLED +
            ", SPEAKERS_ENABLED=" + SPEAKERS_ENABLED +
            ", VOICE_BUFFER_SIZE=" + VOICE_BUFFER_SIZE +
            ", SERVER_IP=" + SERVER_IP +
            '}';
    }
}
