package com.liberty.robot.communication.wakeUp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WakePacket {
    private Byte address = 0;
    private Byte command = 0;
    private List<Byte> data = null;
    private Byte CodeErr = 0;
    // for RX logic
    private boolean flagFESC = false; // for byte stuffing
    private RxState state = RxState.BEGIN;
    private byte rxdataCnt = 0;
    private boolean rxAddressIsPresent = false;
    //

    public Byte getAddress() {
        return address;
    }

    public void setAddress(Byte address) {
        this.address = address;
    }

    public Byte getCommand() {
        return command;
    }

    public void setCommand(Byte command) {
        this.command = command;
    }

    public List<Byte> getData() {
        return data;
    }

    public int getDataCount() {
        return data.size();
    }

    public void setData(List<Byte> value) throws Exception {
        if (value.size() > Constants.SLIPFRAME)
            throw new Exception("data size bigger then SLIPFRAME");
        this.data = value;
    }
    
    public void setData(byte [] value) throws Exception {
        List<Byte> bytes = new ArrayList<>();
        for(byte b : value){
            bytes.add(b);
        }
        setData(bytes);
    }

    public Byte getCodeErr() {
        return CodeErr;
    }

    // --------------------- CRC ------------------------
    private int do_crc8(int b, int crc) {
        for (int i = 0; i < 8; b = b >> 1, i++) {
            if (((b ^ crc) & 1) == 1) {
                crc = ((crc ^ 0x18) >> 1) | 0x80;
            } else {
                crc = (crc >> 1) & 0x7F;
            }
        }
        return crc;
    }

    private List<Byte> translateCharSLIP(Byte ch) {
        List<Byte> result = new ArrayList<Byte>();
        if (ch == Constants.FEND) {
            result.add(Constants.FESC);
            result.add(Constants.TFEND);
        } else if (ch == Constants.FESC) {
            result.add(Constants.FESC);
            result.add(Constants.TFESC);
        } else {
            result.add(ch);
        }
        return result;
    }

    private int performCRCcalculation() {
        int crc = Constants.CRC_INIT;
        crc = do_crc8(Constants.FEND, crc);
        if (rxAddressIsPresent) crc = do_crc8(this.getAddress(), crc);
        crc = do_crc8(this.getCommand(), crc);
        crc = do_crc8(this.getDataCount(), crc);
        for (Byte bt : this.getData()) {
            crc = do_crc8(bt, crc);
        }
        return crc;
    }

    public List<Byte> getTXbuf() {
        List<Byte> bufTX = new ArrayList<Byte>();

        bufTX.add(Constants.FEND); // start packet
        bufTX.addAll(translateCharSLIP((byte) (this.getAddress() | 0x80))); // set
                                                                            // address
        bufTX.addAll(translateCharSLIP(this.getCommand())); // set command
        bufTX.addAll(translateCharSLIP((byte) this.getDataCount()));
        for (Byte bt : this.getData()) {
            bufTX.addAll(translateCharSLIP(bt));
        }
        bufTX.addAll(translateCharSLIP((byte) performCRCcalculation()));

        return bufTX;
    }
    
    public boolean setRXbyte(byte rcv) throws Exception {
        
        boolean wakePacketIsReceived = false;
        
        if (rcv == Constants.FEND) state = RxState.BEGIN;

        // byte stuffing
        if(rcv == Constants.FESC && flagFESC == false) {
            flagFESC = true;
            return false;
        }
        if (flagFESC == true) {
            flagFESC = false;
            if (rcv == Constants.TFEND) rcv = Constants.FEND;
            else if (rcv == Constants.TFESC) rcv = Constants.FESC;
        }
        // end byte stuffing
        switch(state) {
        case  BEGIN:
            if(rcv == Constants.FEND) {
                state = RxState.STARTPACKET;
            }
            break;
        case STARTPACKET:
            if((rcv & 0x80) != 0) {
                rxAddressIsPresent = true;
                state = RxState.ADDRESS;
                this.setAddress((byte)(rcv & 0x7F));
            } else {
                state = RxState.COMMAND;
                this.setAddress((byte)0);
                this.setCommand(rcv);
            }
            break;
        case ADDRESS:
            state = RxState.COMMAND;
            this.setCommand(rcv);
            break;
        case COMMAND: // receive CntData
            data.clear();
            state = (rcv != 0) ? RxState.DATA : RxState.CRC;
            rxdataCnt = rcv;
            if(rxdataCnt > Constants.SLIPFRAME) { // err: packet is very long
                throw new Exception("Received WakeUp packet is very long");
            }
            break; 
        case DATA:
            data.add(rcv);
            if (data.size() == rxdataCnt) {
                state = RxState.CRC;
            }
            break;
        case CRC:
            this.CodeErr = (rcv == performCRCcalculation()) ? Constants.ERR_NO : Constants.ERR_TX;
            state = RxState.BEGIN;
            wakePacketIsReceived = true;
            break;
        }
       return wakePacketIsReceived;
    }

}
