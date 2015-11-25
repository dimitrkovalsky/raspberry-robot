package com.liberty.robot.beans;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.info;

/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class GPIOBean {
    public static final int MAX_PIN_NUMBER = 29;
    public static final int SERVO_PIN_NUMBER = 1;
    public static final int MAX_SERVO_RANGE = 100;
    public static final int MAX_SERVO_ANGLE = 180;
    public static final int MIN_SERVO_ANGLE = 0;
    public static final int DEFAULT_SERVO_ANGLE = 90;
    private int currentServoAngle = DEFAULT_SERVO_ANGLE;
    private boolean turnedOn = false;
    private final GpioController gpio = GpioFactory.getInstance();
    private Map<Integer, GpioPinDigitalOutput> pins = new HashMap<>();
    private GpioPinDigitalOutput redPin = null;
    private GpioPinDigitalOutput greenPin = null;
    private GpioPinDigitalOutput yellowPin = null;
    //    private GpioPinDigitalOutput pwm = null;


    public void init() {
        System.out.println("Initializing GPIOBean");
        redPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "MyLED11", PinState.LOW);
        greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "MyLED12", PinState.LOW);
        yellowPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED13", PinState.LOW);

        redPin.setShutdownOptions(true, PinState.LOW);
        greenPin.setShutdownOptions(true, PinState.LOW);
        yellowPin.setShutdownOptions(true, PinState.LOW);

        Gpio.wiringPiSetup();
        SoftPwm.softPwmCreate(SERVO_PIN_NUMBER, 0, MAX_SERVO_RANGE);
        updateServoAngle();
    }

    private void updateServoAngle() {
        SoftPwm.softPwmWrite(SERVO_PIN_NUMBER, getServoPwmValue());
    }

    private int getServoPwmValue() {
        int pwmValue = currentServoAngle * MAX_SERVO_RANGE / MAX_SERVO_ANGLE;
        info(this, "angle : " + currentServoAngle + " pwmValue : " + pwmValue);
        return pwmValue;
    }


    public void moveForward() {
        yellowPin.high();
        redPin.low();
        greenPin.high();
        showStatus();
    }

    public void moveBackwards() {
        yellowPin.high();
        redPin.high();
        greenPin.low();
        showStatus();
    }

    public void stopMovement() {
        info(this, "Trying to stop movement");
        yellowPin.low();
        redPin.low();
        greenPin.low();
        showStatus();
        currentServoAngle = DEFAULT_SERVO_ANGLE;
        updateServoAngle();
    }

    private void testPwm() throws InterruptedException {
        Gpio.wiringPiSetup();

        // softPwmCreate(int pin, int value, int range)
        // the range is set like (min=0 ; max=100)
        SoftPwm.softPwmCreate(SERVO_PIN_NUMBER, 0, 100);

        int counter = 0;
        while (counter < 3) {
            // fade LED to fully ON
            for (int i = 0; i <= 100; i++) {
                // softPwmWrite(int pin, int value)
                // This updates the PWM value on the given pin. The value is
                // checked to be in-range and pins
                // that haven't previously been initialized via softPwmCreate
                // will be silently ignored.
                SoftPwm.softPwmWrite(SERVO_PIN_NUMBER, i);
                Thread.sleep(25);
            }

            // fade LED to fully OFF
            for (int i = 100; i >= 0; i--) {
                SoftPwm.softPwmWrite(SERVO_PIN_NUMBER, i);
                Thread.sleep(25);
            }
            counter++;
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

    private void showStatus() {
        info("Y : " + yellowPin.getState() + "\t" +
                "R : " + redPin.getState() + "\t" +
                "G : " + greenPin.getState());
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
        }
        catch (Exception e) {
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

    public void shutdown() {
        gpio.shutdown();
    }


}
