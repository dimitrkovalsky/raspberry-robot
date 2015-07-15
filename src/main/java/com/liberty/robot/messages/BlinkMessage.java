package com.liberty.robot.messages;

import com.liberty.robot.common.MessageTypes;

/**
 * Created by Dmytro_Kovalskyi on 15.07.2015.
 */
public class BlinkMessage extends GenericMessage {

    public BlinkMessage() {
        super(MessageTypes.DIODE_BLINK);
    }

    @Override
    public Object getData() {
        return "red";
    }
}
