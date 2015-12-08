package com.liberty.robot.helpers;

import com.liberty.robot.common.Config;
import utils.MessageProcessor;
import com.liberty.robot.controllers.VoiceController;
import com.liberty.robot.transmission.TransmissionManger;
import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 15.06.2015.
 */
public class Initializer {
    public void initialize() throws IOException {
        TransmissionManger transmission;
        MessageProcessor controller = new MessageProcessor();
        VoiceController voiceController;
        transmission = new TransmissionManger(Config.SERVER_IP, controller::onMessage);
        voiceController = new VoiceController(Config.SERVER_IP);
        new Thread(transmission).start();
        new Thread(voiceController).start();
        System.in.read();
    }
}
