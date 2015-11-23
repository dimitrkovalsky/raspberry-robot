package utils;


import com.liberty.robot.helpers.EventListener;
import com.liberty.robot.messages.GenericRequest;

import java.util.HashMap;
import java.util.Map;

import static utils.LoggingUtil.localInfo;


/**
 * Created by Dmytro_Kovalskyi on 28.10.2015.
 */
public class EventBus {
    private static Map<String, EventListener> listeners = new HashMap<>();

    public static void subscribe(String name, EventListener listener) {
        listeners.put(name, listener);
    }

    public static void unSubscribe(String name) {
        listeners.remove(name);
    }

    public static void fireEvent(GenericRequest data) {
        localInfo(EventBus.class, "event retrieved send to " + listeners.size() + " listeners");
        listeners.values().forEach(l -> l.onMessage(data));
    }
}
