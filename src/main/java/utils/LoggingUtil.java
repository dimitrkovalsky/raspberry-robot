package utils;

import com.liberty.robot.common.MessageTypes;
import com.liberty.robot.messages.GenericRequest;
import com.liberty.robot.messages.LoggingMessage;

import java.util.logging.Level;

/**
 * Created by Dmytro_Kovalskyi on 12.11.2015.
 */
public class LoggingUtil {
    public static void info(String message) {
        System.out.println(message);
        sendLoggingMessage(Level.INFO, message);
    }

    /**
     * Logging without notification send.
     */
    public static void localInfo(String message) {
        System.out.println(message);
    }

    /**
     * Logging without notification send.
     */
    public static void localInfo(Object caller, String message) {
        String finalMessage = "[" + caller.getClass() + "] " + message;
        System.out.println(finalMessage);
    }

    public static void info(Object caller, String message) {
        String finalMessage = "[" + caller.getClass() + "] " + message;
        System.out.println(finalMessage);
        sendLoggingMessage(Level.INFO, finalMessage);
    }

    /**
     * Logging without notification send.
     */
    public static void localError(Object caller, String message) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + message;
        System.err.println(finalMessage);
    }

    public static void error(Object caller, String message) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + message;
        System.err.println(finalMessage);
        sendLoggingMessage(Level.SEVERE, finalMessage);
    }

    public static void error(Object caller, String message, Exception e) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + message + ". " + e.getMessage();
        System.err.println(finalMessage);
        sendLoggingMessage(Level.SEVERE, finalMessage);
    }

    /**
     * Logging without notification send.
     */
    public static void localError(Object caller, String message, Exception e) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + message + ". " + e.getMessage();
        System.err.println(finalMessage);
    }

    /**
     * Logging without notification send.
     */
    public static void localError(Object caller, Exception e) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + e.getMessage();
        System.err.println(finalMessage);
    }

    public static void error(Object caller, Exception e) {
        String finalMessage = "ERROR [" + caller.getClass() + "] " + e.getMessage();
        System.err.println(finalMessage);
        sendLoggingMessage(Level.SEVERE, finalMessage);
    }

    private static void sendLoggingMessage(Level level, String message) {
        LoggingMessage loggingMessage = new LoggingMessage(message, level);
        GenericRequest request = new GenericRequest();
        request.setMessageType(MessageTypes.LOGGING_MESSAGE);
        request.setRequestData(loggingMessage);
        EventBus.fireEvent(request);
    }
}
