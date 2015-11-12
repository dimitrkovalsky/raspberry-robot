package utils;

/**
 * Created by Dmytro_Kovalskyi on 12.11.2015.
 */
public class LoggingUtil {
    public static void info(String message) {
        System.out.println(message);
    }

    public static void info(Object caller, String message) {
        System.out.println("[" + caller.getClass() + "] " + message);
    }

    public static void error(Object caller, String message) {
        System.err.println("ERROR [" + caller.getClass() + "] " + message);
    }

    public static void error(Object caller, Exception e) {
        System.err.println("ERROR [" + caller.getClass() + "] " + e.getMessage());
    }
}
