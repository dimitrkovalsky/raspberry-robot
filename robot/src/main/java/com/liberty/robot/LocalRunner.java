package com.liberty.robot;

import com.liberty.robot.common.Config;
import com.liberty.robot.helpers.ConsoleParser;
import com.liberty.robot.helpers.Initializer;

import java.io.IOException;

/**
 * User: Dimitr
 * Date: 21.11.2015
 * Time: 18:19
 */
public class LocalRunner {
    public static void main(String[] args) throws IOException {
        String[] params = {"--ip", "localhost"};
        ConsoleParser consoleParser = new ConsoleParser();
        if(consoleParser.parse(params)) {
            System.out.println(Config.show());
        } else {
            System.exit(1);
        }
        Initializer initializer = new Initializer();
        initializer.initialize();
    }
}
