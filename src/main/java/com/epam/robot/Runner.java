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
        VoiceController voiceController;
        if(args != null && args.length > 0) {
            String ip = args[0];
            transmission = new TransmissionManger(ip, controller::onMessage);
            voiceController = new VoiceController(ip);
        } else {
            transmission = new TransmissionManger(controller::onMessage);
            voiceController = new VoiceController();
        }
        new Thread(transmission).start();
        new Thread(voiceController).start();
        System.in.read();
    }


}
