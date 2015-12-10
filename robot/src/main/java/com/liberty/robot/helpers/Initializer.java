package com.liberty.robot.helpers;

import com.liberty.robot.common.Config;
import com.liberty.robot.controllers.VoiceTransmissionController;
import utils.MessageProcessor;
import com.liberty.robot.transmission.TransmissionManger;
import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 15.06.2015.
 */
public class Initializer {
    public void initialize() throws IOException {
        TransmissionManger transmission;
        MessageProcessor controller = new MessageProcessor();
        VoiceTransmissionController voiceTransmissionController;
        transmission = new TransmissionManger(Config.SERVER_IP, controller::onMessage);
        voiceTransmissionController = new VoiceTransmissionController(Config.SERVER_IP);
        new Thread(transmission).start();
        new Thread(voiceTransmissionController).start();
        System.in.read();
    }
}
