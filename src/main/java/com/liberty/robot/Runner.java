package com.liberty.robot;

import com.liberty.robot.common.Config;
import com.liberty.robot.helpers.ConsoleParser;
import com.martiansoftware.jsap.JSAPException;
import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class Runner {
    public static void main(String[] args) throws IOException, JSAPException {
        ConsoleParser consoleParser = new ConsoleParser();
        if(consoleParser.parse(args)) {
            System.out.println("Success");
            System.out.println(Config.show());
        } else {
            System.exit(1);
        }
//        TransmissionManger transmission;
//        GPIOController controller = new GPIOController();
//        VoiceController voiceController;
//        if(args != null && args.length > 0) {
//            String ip = args[0];
//            transmission = new TransmissionManger(ip, controller::onMessage);
//            voiceController = new VoiceController(ip);
//        } else {
//            transmission = new TransmissionManger(controller::onMessage);
//            voiceController = new VoiceController();
//        }
//        new Thread(transmission).start();
//        new Thread(voiceController).start();
//        System.in.read();

    }


}
