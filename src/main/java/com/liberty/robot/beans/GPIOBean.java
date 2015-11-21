package com.liberty.robot.beans;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class GPIOBean {
    private boolean turnedOn = false;
    private final GpioController gpio = GpioFactory.getInstance();
    private  GpioPinDigitalOutput redPin = null;
    private  GpioPinDigitalOutput greenPin = null;
    private  GpioPinDigitalOutput yellowPin = null;
    public void init(){
        System.out.println("Initializing GPIOBean");
        redPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19, "MyLED11", PinState.LOW);
        greenPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "MyLED12", PinState.LOW);
        yellowPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "MyLED13", PinState.LOW);

        redPin.setShutdownOptions(true, PinState.LOW);
        greenPin.setShutdownOptions(true, PinState.LOW);
        yellowPin.setShutdownOptions(true, PinState.LOW);
    }

    public  void toggle(){
        try {
            System.out.println("[GPIOBean] toggle : " + !turnedOn);
            turnedOn = !turnedOn;
            yellowPin.setState(turnedOn);//high();
            if(turnedOn){
                greenPin.high();
                redPin.low();
            } else {
                greenPin.low();
                redPin.high();
            }
            System.out.println("YELLOW address" + yellowPin.getPin().getAddress());
            System.out.println("Y : " + yellowPin.getState());
            System.out.println("R : " + redPin.getState());
            System.out.println("G : " + greenPin.getState());
        }   catch(Exception e){
            System.err.println("[GPIOBean] error : "  + e.getMessage());
        }
    }

    public void shutdown(){
        gpio.shutdown();
    }


}
