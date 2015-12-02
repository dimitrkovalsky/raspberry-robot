package com.liberty.robot.messages;

/**
 * User: dimitr
 * Date: 11.05.2015
 * Time: 11:01
 */
public class KeyPressedMessage {
    private int keyCode;

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
