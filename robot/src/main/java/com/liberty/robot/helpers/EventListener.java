package com.liberty.robot.helpers;


import com.liberty.robot.messages.GenericRequest;

/**
 * Created by Dmytro_Kovalskyi on 28.10.2015.
 */
public interface EventListener {
    void onMessage(GenericRequest message);
}
