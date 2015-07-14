package com.liberty.robot.communication.wakeUp;

@FunctionalInterface
public interface WakeUpListener {
    void WakeUpReceived(WakePacket wp);
}
