package com.liberty.robot.controllers;

import com.liberty.robot.beans.ServoBean;
import com.liberty.robot.model.Robot;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;
import utils.RobotParams;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;

/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class MovementController {
    ServoBean servoBean = new ServoBean();
    public static final int MAX_PIN_NUMBER = 29;

    private final GpioController gpio = GpioFactory.getInstance();
    private Map<Integer, GpioPinDigitalOutput> pins = new HashMap<>();
    private GpioPinDigitalOutput redPin = null;
    private GpioPinDigitalOutput greenPin = null;
    private GpioPinDigitalOutput yellowPin = null;
    private Robot robot = new Robot();

    public void init() {
        System.out.println("Initializing GPIOBean");
        redPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED11", PinState.LOW);
        greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED12", PinState.LOW);
        yellowPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED13", PinState.LOW);

        redPin.setShutdownOptions(true, PinState.LOW);
        greenPin.setShutdownOptions(true, PinState.LOW);
        yellowPin.setShutdownOptions(true, PinState.LOW);
        initializeRobotModel();
    }

    private void initializeRobotModel() {
        robot.setSpeed(0);
        robot.setSteering(RobotParams.CENTER_STEERING_ANGLE);
        sendControlSignals();
    }

    /**
     * Send control signals to different controllers corresponding to robot model.
     */
    private void sendControlSignals() {
        servoBean.setServoAngle(robot.getSteering());
    }

    public void execute() {
        info(this, "Executes any action");
        servoBean.execute();
        info(this, "Execution completed");
    }

    public void moveForward() {
        yellowPin.high();
        redPin.high();
        greenPin.low();
    }

    public void moveBackwards() {
        yellowPin.high();
        redPin.low();
        greenPin.high();
    }

    public void stopMovement() {
        info(this, "Trying to stop movement");
        yellowPin.low();
        redPin.low();
        greenPin.low();
        servoBean.stop();
    }

    public void turnLeft(int angle) {
        robot.setSteering(RobotParams.MIN_STEERING_ANGLE);
        sendControlSignals();
    }

    public void turnRight(int angle) {
        robot.setSteering(RobotParams.MAX_STEERING_ANGLE);
        sendControlSignals();
    }

    public void setServoAngle(int angle) {
        robot.setSteering(angle);
        sendControlSignals();
    }

    public void togglePin(int pinNumber) {
        if (!isValidPin(pinNumber)) {
            error(this, "Address should be in range [0; 29]");
            return;
        }
        GpioPinDigitalOutput pin = pins.get(pinNumber);
        if (pin == null) {
            Optional<Pin> pinOptional = createPin(pinNumber);
            if (initializePin(pinOptional)) {
                error(this, "Can not initialize pin# " + pinNumber);
            }
        }
        changeState(pinNumber);
    }

    private void changeState(int pinNumber) {
        GpioPinDigitalOutput pin = pins.get(pinNumber);
        if (pin == null) {
            error(this, "Pin#" + pinNumber + " was not initialized");
            return;
        }
        pin.toggle();
        info(this, " Changed state of pin#" + pinNumber + " address " + pin.getPin().getAddress() + " into " + pin
                .getState());
    }

    private boolean initializePin(Optional<Pin> pin) {
        try {
            pin.ifPresent(p -> pins.put(p.getAddress(), gpio.provisionDigitalOutputPin(p, PinState.LOW)));
            return pin.isPresent();
        } catch (Exception e) {
            error(this, "error in initializePin method", e);
        }
        return false;
    }

    private Optional<Pin> createPin(int address) {
        if (!isValidPin(address)) {
            error(this, "Address should be in range [0; 29]");
            return Optional.empty();
        }
        Pin pin = new PinImpl(RaspiGpioProvider.NAME, address, "Pin#" + address,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
        info(this, "Pin #" + address + " was created");
        return Optional.of(pin);
    }

    private boolean isValidPin(int address) {
        return address >= 0 || address <= MAX_PIN_NUMBER;
    }

    private void shutdown() {
        gpio.shutdown();
    }

    /**
     * Updates robot model and sends control;2 signals
     */
    public void updateRobot(Robot robot) {
        this.robot = robot;
        sendControlSignals();
    }
}
