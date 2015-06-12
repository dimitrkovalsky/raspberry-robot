package com.liberty.robot.controllers;

import com.liberty.robot.beans.GPIOBean;
import com.liberty.robot.messages.KeyPressedMessage;

/**
 * Created by Dmytro_Kovalskyi on 25.05.2015.
 */
public class GPIOController {
    private GPIOBean gpioBean;

    public GPIOController() {
        init(false);
    }

    public void onMessage(KeyPressedMessage message) {
        System.out.println("[GPIOController] onMessage : " + message);
        if(gpioBean != null) {
            if(message.getKeyCode() == 38) { // Up
                gpioBean.toggle();
            }
        } else {
            System.out.println("[GPIOController] GPIOBean was not initialized");
        }
    }

    private void init(boolean useGpio) {
        try {
            if(useGpio) {
                gpioBean = new GPIOBean();
                gpioBean.init();
            }
        } catch(Exception e) {
            System.out.println("Can't initialize GPIOBean");
        }
    }
}
