package com.liberty.robot.messages;

import com.liberty.robot.common.MessageTypes;

/**
 * User: dimitr
 * Date: 11.05.2015
 * Time: 11:01
 */
public class KeyPressedMessage extends GenericMessage {
    private int keyCode;

    public KeyPressedMessage() {
        super(MessageTypes.KEY_PRESSED);
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public String toString() {
        return "KeyPressedMessage{" +
                "keyCode=" + keyCode +
                '}';
    }
}
