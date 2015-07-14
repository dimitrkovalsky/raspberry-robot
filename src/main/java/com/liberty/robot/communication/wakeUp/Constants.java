package com.liberty.robot.communication.wakeUp;

public class Constants {
    //codes for common command:
    public static final byte NOP = 0;    //not operation
    public static final byte ERR = 1;    //error at packet receiving
    public static final byte ECHO = 2;    //translate echo
    public static final byte INFO = 3;    //translate device info
    
    public static final int SLIPFRAME = 255; 
    public static final int CRC_INIT  =  0xDE;  //initial CRC value
    //SLIP protocol
    public static final byte FEND = (byte)0xC0; // Frame End
    public static final byte FESC = (byte)0xDB; // Frame Escape
    public static final byte TFEND = (byte)0xDC; // Transposed Frame End
    public static final byte TFESC = (byte)0xDD; // Transposed Frame Escape
    //
    //code error:
    public static final byte ERR_NO = 0x00;   //no error
    public static final byte ERR_TX = 0x01;   //Rx/Tx error
    public static final byte ERR_BU = 0x02;   //device busy error
    public static final byte ERR_RE = 0x03;   //device not ready error
    public static final byte ERR_PA = 0x04;   //parameters value error
    public static final byte ERR_NR = 0x05;   //no replay
    public static final byte ERR_NC = 0x06;   //no carrier
}
