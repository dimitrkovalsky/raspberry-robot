package com.liberty.robot.messages;

/**
 * Created by Dmytro_Kovalskyi on 11.07.2014.
 */
public class GenericRequest {

    public GenericRequest() {
    }

    protected int messageType;
    protected Object requestData;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(Object requestData) {
        this.requestData = requestData;
    }

    @Override
    public String toString() {
        return "GenericRequest{" +
                "messageType=" + messageType +
                ", requestData=" + requestData +
                '}';
    }
}
