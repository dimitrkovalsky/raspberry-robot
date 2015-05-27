package com.epam.robot;

import com.epam.robot.controllers.GPIOController;
import com.epam.robot.controllers.VoiceController;
import com.epam.robot.transmission.TransmissionManger;
import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        TransmissionManger transmission;
        GPIOController controller = new GPIOController();
        if(args != null && args.length > 0) {
            transmission = new TransmissionManger(args[0], controller::onMessage);
        } else {
            transmission = new TransmissionManger(controller::onMessage);
        }
        new Thread(transmission).start();
        VoiceController voiceController = new VoiceController(transmission);
        System.in.read();
    }


}
