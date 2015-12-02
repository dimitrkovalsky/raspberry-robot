package com.liberty.robot.beans;

import com.liberty.robot.controllers.ArduinoController;
import com.liberty.robot.helpers.Direction;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;

/**
 * User: Dimitr
 * Date: 26.11.2015
 * Time: 13:55
 */
public class ServoBean {
    public static final int SERVO_PIN_NUMBER = 1;
    public static final int MAX_SERVO_RANGE = 0;
    public static final int MAX_SERVO_ANGLE = 156;
    public static final int MIN_SERVO_ANGLE = 54;
    public static final int DEFAULT_SERVO_ANGLE = 105;
    private int currentServoAngle = DEFAULT_SERVO_ANGLE;
    private int previousServoAngle = DEFAULT_SERVO_ANGLE;
    private ArduinoController arduinoController;

    public ServoBean() {
        try {
            Gpio.wiringPiSetup();
            Gpio.pinMode(SERVO_PIN_NUMBER, Gpio.PWM_OUTPUT);
            Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
            Gpio.pwmSetClock(1920);
            Gpio.pwmSetRange(200);
            arduinoController = new ArduinoController();
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void execute() {
        try {
            byte angle = 0;
            arduinoController.send(angle);
        }
        catch (Exception e) {
            error(this, e);
        }
    }


    public void setServoAngle(byte angle) {
        try {
//            info("PWM frequency : " + SystemInfo.getClockFrequencyPWM());
//            Gpio.pwmWrite(SERVO_PIN_NUMBER, getServoPwmValue(angle));
            arduinoController.send(angle);
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void moveForward(){
        arduinoController.send(Direction.FORWARD, (byte)currentServoAngle);
    }

    public void moveBackward(){
        arduinoController.send(Direction.BACKWARD, (byte)currentServoAngle);
    }

    public void stop(){
        arduinoController.send(Direction.STOP, (byte)currentServoAngle);
    }

    public void turnLeft(int angle) {
//        info("Trying to turn left on " + angle + " degrees");
        if (currentServoAngle - angle < MIN_SERVO_ANGLE)
            currentServoAngle = MIN_SERVO_ANGLE;
        else
            currentServoAngle -= angle;
//        updateServoAngle();
        //byte bAngle = 0;
        info("Current angle : " + angle + " degrees");
        arduinoController.send((byte) currentServoAngle);
    }

    public void turnRight(int angle) {
        info("Trying to turn right on " + angle + " degrees");
        if (currentServoAngle + angle > MAX_SERVO_ANGLE)
            currentServoAngle = MAX_SERVO_ANGLE;
        else
            currentServoAngle += angle;
//        updateServoAngle();
        info("Current angle : " + angle + " degrees");
        arduinoController.send((byte) currentServoAngle);
    }

    private void updateServoAngle() {
        try {

            int previousValue = getServoPwmValue(previousServoAngle);
            int newValue = getServoPwmValue();
            if (previousValue <= newValue) {
                for (int i = previousValue; i <= newValue; i++) {
                    SoftPwm.softPwmWrite(SERVO_PIN_NUMBER, i);
                    System.out.println("WRITE : " + i);
                }
            }
            else {
                for (int i = newValue; i >= previousValue; i--) {
                    SoftPwm.softPwmWrite(SERVO_PIN_NUMBER, i);
                    System.out.println("WRITE : " + i);
                }
            }
            info(this, "angle : " + currentServoAngle + " pwmValue : " + newValue);
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    private int getServoPwmValue() {
        return getServoPwmValue(currentServoAngle);
    }

    private int getServoPwmValue(int angle) {
        int pwmValue = angle * MAX_SERVO_RANGE / MAX_SERVO_ANGLE;
        info("Trying to set into : " + angle + " angle with value : " + pwmValue);
        return pwmValue;
    }
}
