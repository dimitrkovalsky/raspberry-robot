package com.epam.robot.controllers;

import com.epam.robot.helpers.Scope;
import com.epam.robot.helpers.VoiceSampleMessage;
import com.epam.robot.transmission.TransmissionManger;

/**
 * Created by Dmytro_Kovalskyi on 27.05.2015.
 */
public class VoiceController {
    private Scope scope;
    private TransmissionManger transmissionManger;

    public VoiceController(TransmissionManger transmissionManger) {
        this.transmissionManger = transmissionManger;
        scope = new Scope(this::onSample);
        scope.start();
    }

    private void onSample(VoiceSampleMessage sample) {
        transmissionManger.send(sample);
    }
}
