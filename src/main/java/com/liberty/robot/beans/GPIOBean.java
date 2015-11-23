package com.liberty.robot.beans;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;

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
    private boolean turnedOn = false;
    private final GpioController gpio = GpioFactory.getInstance();
    private Map<Integer, GpioPinDigitalOutput> pins = new HashMap<>();
    private GpioPinDigitalOutput redPin = null;
    private GpioPinDigitalOutput greenPin = null;
    private GpioPinDigitalOutput yellowPin = null;

    public void init() {
        System.out.println("Initializing GPIOBean");
        redPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, "MyLED11", PinState.LOW);
        greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "MyLED12", PinState.LOW);
        yellowPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "MyLED13", PinState.LOW);

        redPin.setShutdownOptions(true, PinState.LOW);
        greenPin.setShutdownOptions(true, PinState.LOW);
        yellowPin.setShutdownOptions(true, PinState.LOW);
    }

    public void toggle() {
        try {
            System.out.println("[GPIOBean] toggle : " + !turnedOn);
            turnedOn = !turnedOn;
            yellowPin.setState(turnedOn);//high();
            if (turnedOn) {
                greenPin.high();
                redPin.low();
            }
            else {
                greenPin.low();
                redPin.high();
            }
            System.out.println("YELLOW address" + yellowPin.getPin().getAddress());
            System.out.println("Y : " + yellowPin.getState());
            System.out.println("R : " + redPin.getState());
            System.out.println("G : " + greenPin.getState());
        }
        catch (Exception e) {
            System.err.println("[GPIOBean] error : " + e.getMessage());
        }
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
        info(this, " Changed state of pin#" + pinNumber + " address " + pin.getPin().getAddress() + "into " + pin
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
        if (isValidPin(address)) {
            error(this, "Address should be in range [0; 29]");
            return Optional.empty();
        }
        Pin pin = new PinImpl(RaspiGpioProvider.NAME, address, "Pin#" + address,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
        info(this, "Pin #" + address + " was created");
        return Optional.of(pin);
    }

    private boolean isValidPin(int address) {return address < 0 || address > MAX_PIN_NUMBER;}

    public void shutdown() {
        gpio.shutdown();
    }


}
