package com.liberty.robot.helpers;

import com.liberty.robot.common.Config;
import com.liberty.robot.communication.wakeUp.WakePacket;
import com.liberty.robot.messages.GenericRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Dmytro_Kovalskyi on 15.07.2015.
 */
public class WakeHelper {
    public static <T extends GenericRequest> WakePacket convert(T message) {
        WakePacket wp = new WakePacket();
        wp.setAddress(Config.ARDUINO_ADDRESS);
        wp.setCommand((byte) message.getMessageType());
        byte[] bytes = new byte[]{};// TODO: FIX toBytes(message.getData());
        try {
            wp.setData(bytes);
        } catch(Exception e) {
            System.err.println("[WakeHelper] convert error : " + e.getMessage());
        }
        return wp;
    }

    public static byte[] toBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] result = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            result = bos.toByteArray();
        } catch(IOException e) {
            System.err.println("[WakeHelper] toBytes error : " + e.getMessage());
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch(IOException e) {
                System.err.println("[WakeHelper] toBytes error : " + e.getMessage());
            }
        }
        return result;
    }
}
