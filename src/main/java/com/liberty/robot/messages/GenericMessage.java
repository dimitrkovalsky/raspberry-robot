package com.liberty.robot.messages;

/**
 * Created by Dmytro_Kovalskyi on 11.07.2014.
 */
abstract public class GenericMessage {

    public GenericMessage(int messageType) {
        this.messageType = messageType;
    }

    protected int messageType;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    
    public abstract Object getData(); 
}
