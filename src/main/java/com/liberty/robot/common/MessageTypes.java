package com.liberty.robot.common;

/**
 * Created by Dmytro_Kovalskyi on 11.04.2015.
 */
public interface MessageTypes {
    byte CONNECTION_ESTABLISHED = 10;
    byte KEY_PRESSED = 11;
    byte PIN_TOGGLE = 20;
    byte SET_SERVO_ANGLE = 21;
    byte STOP_MOVEMENT = 25;
    byte LOGGING_MESSAGE = 90;
    byte EXECUTE_ACTION = 99;
    byte DIODE_BLINK = 100;
}
