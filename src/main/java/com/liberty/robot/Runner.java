package com.liberty.robot;

import com.liberty.robot.common.Config;
import com.liberty.robot.helpers.ConsoleParser;
import com.liberty.robot.helpers.Initializer;
import com.martiansoftware.jsap.JSAPException;
import java.io.IOException;

/**
 * Created by Dmytro_Kovalskyi on 13.05.2015.
 */
public class Runner {
    public static void main(String[] args) throws IOException, JSAPException {
        ConsoleParser consoleParser = new ConsoleParser();
        if(consoleParser.parse(args)) {
            System.out.println(Config.show());
        } else {
            System.exit(1);
        }
        Initializer initializer = new Initializer();
        initializer.initialize();
    }
}
