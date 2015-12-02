package com.liberty.robot.communication.wakeUp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WakeUpSerial extends SerialPort {
    
    private List<WakeUpListener> listeners = new
     CopyOnWriteArrayList<WakeUpListener>();
    

    public void addWakeUpListener(WakeUpListener listener) {
        listeners.add(listener); 
    }

    public void removeWakeUpListener(WakeUpListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(WakePacket wp) { 
        for (WakeUpListener listener : listeners) {
            listener.WakeUpReceived(wp);
        }
     }


    public void wakeTX(WakePacket wp) throws Exception {
        for (byte bt : wp.getTXbuf()) {
            putByte(bt);
        }
    }
    
    public WakePacket  WakeRX() throws Exception {
        WakePacket wp = new WakePacket();
        boolean isReady;
        do {
            isReady = wp.setRXbyte(getByte());
        } while (isReady == false);
        notifyListeners(wp);
        return wp;
    }
    
    //TODO worker where:  do { WakeRX(); } while(true);

}

