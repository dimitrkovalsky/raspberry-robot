package com.liberty.robot.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.liberty.robot.beans.GPIOBean;
import com.liberty.robot.communication.wakeUp.WakePacket;
import com.liberty.robot.communication.wakeUp.WakeUpSerial;
import com.liberty.robot.messages.GenericMessage;
import com.liberty.robot.messages.KeyPressedMessage;

/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class GPIOController {
    private static final Byte ARDUINO_ADDRESS = 0;
    private GPIOBean gpioBean;

    public GPIOController() {
        init(false);
    }

    public void onMessage(KeyPressedMessage message) {
        System.out.println("[GPIOController] onMessage : " + message);
        if(gpioBean != null) {
            if(message.getKeyCode() == 38) { // Up
                gpioBean.toggle();
            }
        } else {
            System.out.println("[GPIOController] GPIOBean was not initialized");
        }
        sendToArduino(message);
    }
    
    private void sendToArduino(KeyPressedMessage message) {
        WakePacket wp = convert(message);
        WakeUpSerial serial = new WakeUpSerial();
        try {
            //TODO open and close command do in other place 
            serial.open();
            serial.flash(); // clean rx buffer from gabige
            serial.addWakeUpListener(w -> System.out.println(w));
            serial.wakeTX(wp);
            serial.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private <T extends GenericMessage> WakePacket convert(T message) {
        WakePacket wp = new WakePacket();
        wp.setAddress(ARDUINO_ADDRESS);
        wp.setCommand((byte)message.getMessageType());
        byte[] bytes = toBytes(message.getData());
        try {
            wp.setData(bytes);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return wp;
    }
    
    private byte[] toBytes(Object object){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] result = null;
        try {
          out = new ObjectOutputStream(bos);   
          out.writeObject(object);
          result = bos.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
          try {
            if (out != null) {
              out.close();
            }
          } catch (IOException ex) {
            // ignore close exception
          }
          try {
            bos.close();
          } catch (IOException ex) {
            // ignore close exception
          }
        }
        return result;
    }

    private void init(boolean useGpio) {
        try {
            if(useGpio) {
                gpioBean = new GPIOBean();
                gpioBean.init();
            }
        } catch(Exception e) {
            System.out.println("Can't initialize GPIOBean");
        }
    }
}
