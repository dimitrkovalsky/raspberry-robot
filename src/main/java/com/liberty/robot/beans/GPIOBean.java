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
    private  GpioPinDigitalOutput pin7 = null;
    private  GpioPinDigitalOutput pin25 = null;
    public void init(){
        System.out.println("Initializing GPIOBean");
        pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "MyLED", PinState.LOW);
        pin25 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "MyLED25", PinState.LOW);

        pin7.setShutdownOptions(true, PinState.LOW);
        pin25.setShutdownOptions(true, PinState.LOW);
    }

    public  void toggle(){
        try {

            System.out.println("[GPIOBean] toggle : " + !turnedOn);
            turnedOn = !turnedOn;

            pin7.toggle();
            pin25.toggle();

        }   catch(Exception e){
            System.err.println("[GPIOBean] error : "  + e.getMessage());
        }
    }

    public void shutdown(){
        gpio.shutdown();
    }


}
