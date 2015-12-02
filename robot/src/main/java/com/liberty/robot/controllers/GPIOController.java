package com.liberty.robot.controllers;

import com.liberty.robot.beans.GPIOBean;
import com.liberty.robot.common.MessageTypes;
import com.liberty.robot.devices.DistanceMonitor;
import com.liberty.robot.helpers.Executable;
import com.liberty.robot.helpers.JsonHelper;
import com.liberty.robot.messages.GenericRequest;
import com.liberty.robot.messages.KeyPressedMessage;
import com.liberty.robot.messages.PinToggleMessage;
import com.liberty.robot.messages.SetAngleMessage;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;


/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class GPIOController {
    private static final Byte ARDUINO_ADDRESS = 0;
    private GPIOBean gpioBean;
    private ArduinoController arduinoController;

    public GPIOController() {
        init(true);
    }

    public void onMessage(GenericRequest message) {
        info("[GPIOController] onMessage : " + message);
        switch(message.getMessageType()) {
            case MessageTypes.KEY_PRESSED:
                KeyPressedMessage keyPressedMessage = JsonHelper
                    .convertEntity(message.getRequestData(), KeyPressedMessage.class);
                onKeyPressed(keyPressedMessage);
                break;
            case MessageTypes.PIN_TOGGLE:
                PinToggleMessage pinMessage = JsonHelper
                    .convertEntity(message.getRequestData(), PinToggleMessage.class);
                onPinToggleMessage(pinMessage);
                break;
            case MessageTypes.STOP_MOVEMENT:
                executeWithCatch(() -> gpioBean.stopMovement());
                break;
            case MessageTypes.EXECUTE_ACTION:
                executeWithCatch(() -> gpioBean.execute());
                break;
            case MessageTypes.SET_SERVO_ANGLE:
                SetAngleMessage angleMessage = JsonHelper
                    .convertEntity(message.getRequestData(), SetAngleMessage.class);
                executeWithCatch(() -> gpioBean.setServoAngle(angleMessage.getAngle()));
                break;
            default:
                error(this, "Unrecognized message type : " + message.getMessageType());
        }

    }

    private void onKeyPressed(KeyPressedMessage message) {
        switch(message.getKeyCode()) { // Up
            case 38:
                executeWithCatch(() -> gpioBean.moveForward());
                break;
            case 40:
                executeWithCatch(() -> gpioBean.moveBackwards());
                break;
            case 39:
                executeWithCatch(() -> gpioBean.turnLeft(10));
                break;
            case 37:
                executeWithCatch(() -> gpioBean.turnRight(10));
                break;
            default:
                error(this, "Unrecognized key code : " + message.getKeyCode());
        }
    }

    public void onPinToggleMessage(PinToggleMessage message) {
        executeWithCatch(() -> gpioBean.togglePin(message.getPinNumber()));
    }

    private void executeWithCatch(Executable executable) {
        try {
            if(gpioBean != null) {
                executable.execute();
            } else {
                error(this, " GPIOBean was not initialized");
            }
        } catch(Exception e) {
            error(this, e);
        }
    }

    private void init(boolean useGpio) {
        try {
            if(useGpio) {
                info("Running GPIOBean initialization");
                gpioBean = new GPIOBean();
                gpioBean.init();
                //new Thread(this::run).start();
            }
        } catch(Exception e) {
            error(this, "Can't initialize GPIOBean");
        }
    }

    private void run() {
       info("Running distance monitoring");
        Pin echoPin = RaspiPin.GPIO_04; // PI4J custom numbering (pin 11)
        Pin trigPin = RaspiPin.GPIO_05; // PI4J custom numbering (pin 7)
        DistanceMonitor monitor = new DistanceMonitor(echoPin, trigPin);
        while(true) {
            try {
                System.out.printf("%1$d,%2$.3f%n", System.currentTimeMillis(), monitor.measureDistance());
            } catch(DistanceMonitor.TimeoutException e) {
                System.err.println(e);
            }

            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                System.err.println("Interrupt during trigger");
            }
        }
    }

}
