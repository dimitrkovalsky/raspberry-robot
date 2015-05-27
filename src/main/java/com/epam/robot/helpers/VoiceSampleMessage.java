package com.epam.robot.helpers;

import com.epam.robot.common.MessageTypes;
import com.epam.robot.messages.GenericMessage;

/**
 * Created by Dmytro_Kovalskyi on 27.05.2015.
 */
public class VoiceSampleMessage extends GenericMessage {
    private byte[] bytes;

    public VoiceSampleMessage() {
        setMessageType(MessageTypes.VOICE_SAMPLE);
    }

    public VoiceSampleMessage(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
