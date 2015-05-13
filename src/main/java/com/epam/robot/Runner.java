package com.epam.robot;

import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        Receiver receiver = new Receiver();
        new Thread(receiver).start();
        System.in.read();
    }
}
