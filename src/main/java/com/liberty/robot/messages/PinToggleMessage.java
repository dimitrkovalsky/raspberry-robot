package com.liberty.robot.messages;

import com.sun.istack.internal.NotNull;

/**
 * User: Dimitr
 * Date: 21.11.2015
 * Time: 14:20
 */
public class PinToggleMessage {
    @NotNull
    private Integer pinNumber;

    public Integer getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(Integer pinNumber) {
        this.pinNumber = pinNumber;
    }
}
