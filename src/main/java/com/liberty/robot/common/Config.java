package com.liberty.robot.common;

/**
 * Created by Dmytro_Kovalskyi on 11.06.2015.
 */
public class Config {
    public static boolean VOICE_LOG_ENABLED = true;
    public static boolean VOICE_RECORDING_ENABLED = true;
    public static boolean SPEAKERS_ENABLED = true;
    public static String SERVER_IP = "127.0.0.1";
    public static int VOICE_BUFFER_SIZE = 1024;

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
