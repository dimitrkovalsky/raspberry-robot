package com.liberty.robot.beans;

import com.liberty.robot.communication.wakeUp.SerialPort;
import com.liberty.robot.communication.wakeUp.WakePacket;
import com.liberty.robot.helpers.Direction;
import com.liberty.robot.helpers.WakeHelper;
import com.liberty.robot.messages.GenericRequest;
import com.pi4j.io.serial.SerialDataEvent;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;

/**
 * Created by Dmytro_Kovalskyi on 15.07.2015.
 */
public class ArduinoBean {
    private SerialPort serial;

    public ArduinoBean() {
        init();
    }

    public void init() {
        try {
            serial = new SerialPort();
            serial.open(this::onEvent);
            // serial.addWakeUpListener(this::onMessageReceived);
        } catch(Exception e) {
            error(this, "init error : " + e.getMessage());
        }
    }

    private void onEvent(SerialDataEvent serialDataEvent) {
        info(this, "received packet : " + serialDataEvent.getData());
    }

    public byte[] createCommand(byte angle) {
        byte[] command = {'$', 'M', 0x03, 0x00, 1, 127, angle, 0};
//                / 0 / 0x24,  // '$'
//            / 1 / 0x4D,  // 'M'
//            / 2 / 0x03,  // 3 bytes
//            / 3 / 0x00,  // Message type = Rover driving
//            / 4 / Moving_direction,
//            / 5 / Moving_speed,
//            / 6 / Turn_angle,
//            / 7 / 0x0 };  // Checksum (XOR)

        // Calculate checksum (XOR) from bytes [2..6]
        byte checksum = 0;

        for(byte i = 2; i < 7; i++)  // [2..6]
        {
            checksum ^= command[i];  // XOR
        }

        command[7] = checksum;  // Save checksum

        return command;
    }

    public void setSteering(byte angle) {
        byte[] command = createCommand(angle);
        serial.putBuffer(command);
    }

    public void send(Direction direction, byte angle) {
        byte[] command = createCommand(direction, angle);
        serial.putBuffer(command);
    }

    private byte[] createCommand(Direction direction, byte angle) {
        byte[] command = {'$', 'M', 0x03, 0x00, 1, 127, angle, 0};

        switch(direction) {
            case FORWARD:
                command[4] = 1;
                break;
            case BACKWARD:
                command[4] = 2;
                break;
            case STOP:
                command[4] = 0;
                break;
        }

        byte checksum = 0;
        for(byte i = 2; i < 7; i++) {
            checksum ^= command[i];
        }
        command[7] = checksum;
        return command;

    }

    private void onMessageReceived(WakePacket wakePacket) {
        info(this, "received packet : " + wakePacket);
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
//            serial.flash(); // clean rx buffer from gabige
//            serial.wakeTX(wp);
        } catch(Exception e) {
            error(this, "[ArduinoController] sendToArduino error : " + e.getMessage());
        }
    }
}
