package com.liberty.robot.controllers;

import com.liberty.robot.communication.wakeUp.WakePacket;
import com.liberty.robot.communication.wakeUp.WakeUpSerial;
import com.liberty.robot.helpers.WakeHelper;
import com.liberty.robot.messages.GenericRequest;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;

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
            error(this, "init error : " + e.getMessage());
        }
    }

    private void onMessageReceived(WakePacket wakePacket) {
        info(this,"received packet : " + wakePacket);
    }

    private void onDestroy() {
        try {
            serial.close();
        } catch(Exception e) {
            error(this, "onDestroy error : " + e.getMessage());
        }
    }

    public void send(GenericRequest message) {
        info("[ArduinoController]  send method called with : " + message);
        WakePacket wp = WakeHelper.convert(message);
        try {
            serial.flash(); // clean rx buffer from gabige
            serial.wakeTX(wp);
        } catch(Exception e) {
            error(this, "[ArduinoController] sendToArduino error : " + e.getMessage());
        }
    }
}
