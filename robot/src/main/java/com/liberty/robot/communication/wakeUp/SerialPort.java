package com.liberty.robot.communication.wakeUp;

import com.pi4j.io.serial.Serial;
//import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import java.util.Arrays;

import static utils.LoggingUtil.info;

public class SerialPort {
    private final Serial serial;

    public SerialPort() {
        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();
    }

    public void putByte(byte bt) {
        serial.write(bt);
    }

    public void putbuf(byte[] buf) {
        info(this, "Send to arduino : " + Arrays.toString(buf));
        for (byte bt : buf)
            serial.write(bt);
//        serial.write(buf);
    }

    public byte getByte() throws Exception {
        return (byte) serial.read();
    }

    public void flash() {
        serial.flush();
    }

    public void close() throws Exception {
        serial.close();
    }

    public void open(SerialDataListener onEvent) throws Exception {
        serial.open(Serial.DEFAULT_COM_PORT, 9600);
        //serial.addListener(onEvent);
    }
}
