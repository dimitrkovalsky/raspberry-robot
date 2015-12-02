package com.liberty.robot.communication.wakeUp;

public enum RxState {
    BEGIN,
    STARTPACKET,
    ADDRESS,
    COMMAND,
    DATA,
    CRC
}
