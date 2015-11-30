package com.liberty.robot.beans;

import com.pi4j.system.SystemInfo;
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
    public static final int MAX_SERVO_RANGE = 100;
    public static final int MAX_SERVO_ANGLE = 120;
    public static final int MIN_SERVO_ANGLE = 0;
    public static final int DEFAULT_SERVO_ANGLE = 60;
    private int currentServoAngle = DEFAULT_SERVO_ANGLE;
    private int previousServoAngle = DEFAULT_SERVO_ANGLE;


    public ServoBean() {
        try {
            Gpio.wiringPiSetup();
            Gpio.pinMode(SERVO_PIN_NUMBER, Gpio.PWM_OUTPUT);
            Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
            Gpio.pwmSetClock(1920);
            Gpio.pwmSetRange(200);
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void execute() {
        try {

        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void setServoAngle(int angle) {
        try {
            info("PWM frequency : " + SystemInfo.getClockFrequencyPWM());
            Gpio.pwmWrite(SERVO_PIN_NUMBER, getServoPwmValue(angle));
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void turnLeft(int angle) {
        info("Trying to turn left on " + angle + " degrees");
        if (currentServoAngle - angle < MIN_SERVO_ANGLE)
            currentServoAngle = MIN_SERVO_ANGLE;
        else
            currentServoAngle -= angle;
        updateServoAngle();
    }

    public void turnRight(int angle) {
        info("Trying to turn right on " + angle + " degrees");
        if (currentServoAngle + angle > MAX_SERVO_ANGLE)
            currentServoAngle = MAX_SERVO_ANGLE;
        else
            currentServoAngle += angle;
        updateServoAngle();
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
