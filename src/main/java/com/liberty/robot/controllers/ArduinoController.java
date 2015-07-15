package com.liberty.robot.controllers;

import com.liberty.robot.communication.wakeUp.WakePacket;
import com.liberty.robot.communication.wakeUp.WakeUpSerial;
import com.liberty.robot.helpers.WakeHelper;
import com.liberty.robot.messages.GenericMessage;

/**
 * Created by Dmytro_Kovalskyi on 15.07.2015.
 */
public class ArduinoController {
    private WakeUpSerial serial;

    public ArduinoController() {
        init();
    }

    public void init() {
        try {
            serial = new WakeUpSerial();
            serial.open();
            serial.addWakeUpListener(this::onMessageReceived);
        } catch(Exception e) {
            System.err.println("[ArduinoController] init error : " + e.getMessage());
        }
    }

    private void onMessageReceived(WakePacket wakePacket) {
        System.out.println("[ArduinoController] received packet : " + wakePacket);
    }

    private void onDestroy() {
        try {
            serial.close();
        } catch(Exception e) {
            System.err.println("[ArduinoController] onDestroy error : " + e.getMessage());
        }
    }

    public void send(GenericMessage message) {
        System.out.println("[ArduinoController]  send method called with : " + message);
        WakePacket wp = WakeHelper.convert(message);
        try {
            serial.flash(); // clean rx buffer from gabige
            serial.wakeTX(wp);
        } catch(Exception e) {
            System.err.println("[ArduinoController] sendToArduino error : " + e.getMessage());
        }
    }
}
