package com.liberty.robot.beans;

import com.liberty.robot.helpers.Direction;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import utils.RobotParams;

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
    public static final int MAX_SERVO_ANGLE = RobotParams.MIN_STEERING_ANGLE;
    public static final int MIN_SERVO_ANGLE = RobotParams.MAX_STEERING_ANGLE;
    public static final int DEFAULT_SERVO_ANGLE = RobotParams.CENTER_STEERING_ANGLE;
    private int currentServoAngle = DEFAULT_SERVO_ANGLE;
    private int previousServoAngle = DEFAULT_SERVO_ANGLE;
    private ArduinoBean arduinoBean;

    public ServoBean() {
        try {
            Gpio.wiringPiSetup();
            Gpio.pinMode(SERVO_PIN_NUMBER, Gpio.PWM_OUTPUT);
            Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
            Gpio.pwmSetClock(1920);
            Gpio.pwmSetRange(200);
            arduinoBean = new ArduinoBean();
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void execute() {
        try {
            byte angle = 0;
            arduinoBean.setSteering(angle);
        }
        catch (Exception e) {
            error(this, e);
        }
    }


    public void setServoAngle(byte angle) {
        try {
            arduinoBean.setSteering(angle);
        }
        catch (Exception e) {
            error(this, e);
        }
    }

    public void moveForward(){
        arduinoBean.send(Direction.FORWARD, (byte)currentServoAngle);
    }

    public void moveBackward(){
        arduinoBean.send(Direction.BACKWARD, (byte)currentServoAngle);
    }

    public void stop(){
        arduinoBean.send(Direction.STOP, (byte)currentServoAngle);
    }

    public void turnLeft(int angle) {
        if (currentServoAngle - angle < MIN_SERVO_ANGLE)
            currentServoAngle = MIN_SERVO_ANGLE;
        else
            currentServoAngle -= angle;
        info("Current ANGLE : " + currentServoAngle + " degrees");
        arduinoBean.setSteering((byte) currentServoAngle);
    }

    public void turnRight(int angle) {
        info("Trying to turn right on " + angle + " degrees");
        if (currentServoAngle + angle > MAX_SERVO_ANGLE)
            currentServoAngle = MAX_SERVO_ANGLE;
        else
            currentServoAngle += angle;
        info("Current ANGLE : " + currentServoAngle + " degrees");
        arduinoBean.setSteering((byte) currentServoAngle);
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
