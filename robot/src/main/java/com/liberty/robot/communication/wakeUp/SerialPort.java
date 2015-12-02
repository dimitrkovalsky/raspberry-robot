package com.liberty.robot.communication.wakeUp;

import com.pi4j.io.serial.Serial;
//import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialFactory;
import java.util.Arrays;

import static utils.LoggingUtil.info;

public class SerialPort {
    private final Serial serial;

    public SerialPort() {
        // create an instance of the serial communications class
        serial = SerialFactory.createInstance();
        
        /* якщо не гавнокодити - то лісенер треба було б прикрутити сюди )
        // create and register the serial data listener
        serial.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                // print out the data received to the console
                System.out.print(event.getData());
            }
        });
        */
    }

    public void putByte(byte bt) {
        serial.write(bt);
    }

    public void putbuf(byte[] buf) {
        info(this, "Send to arduino : " + Arrays.toString(buf));
        for (byte bt : buf)
            serial.write(bt);
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

    public void open() throws Exception {
        serial.open(Serial.DEFAULT_COM_PORT, 9600);
    }
}
