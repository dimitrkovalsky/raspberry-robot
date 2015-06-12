package com.liberty.robot.helpers;

import com.liberty.robot.common.Config;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

/**
 * Created by Dmytro_Kovalskyi on 12.06.2015.
 */
public class ConsoleParser {
    private JSAP jsap;

    public ConsoleParser() {
        init();
    }

    private void init() {
        try {
            jsap = new JSAP();
            Switch speakersOpt = new Switch("speakers")
                .setShortFlag('s')
                .setLongFlag("speakers");
            speakersOpt.setHelp("Play voice from server using speakers");

            Switch microphoneOpt = new Switch("microphone")
                .setShortFlag('m')
                .setLongFlag("microphone");
            microphoneOpt.setHelp("Record voice from microphone and sent into server");

            UnflaggedOption ipOpt = new UnflaggedOption("ip")
                .setStringParser(JSAP.STRING_PARSER)
                .setRequired(true)
                .setDefault("127.0.0.1")
                .setGreedy(true);
            ipOpt.setHelp("Use to set appropriate ip for concrete server");

            jsap.registerParameter(speakersOpt);
            jsap.registerParameter(microphoneOpt);
            jsap.registerParameter(ipOpt);
        } catch(JSAPException e) {
            System.err.println("[ConsoleParser] init error : " + e.getMessage());
        }
    }

    public boolean parse(String[] args) {
        JSAPResult config = jsap.parse(args);
        boolean result;
        if(!config.success()) {

            System.err.println();

            for(java.util.Iterator errs = config.getErrorMessageIterator();
                errs.hasNext(); ) {
                System.err.println("Error: " + errs.next());
            }
            System.err.println();

            System.err.println(jsap.getUsage());
            System.err.println();
            System.err.println(jsap.getHelp());
            result = false;
        } else {
            Config.SPEAKERS_ENABLED = config.getBoolean("speakers");
            Config.VOICE_RECORDING_ENABLED = config.getBoolean("microphone");
            Config.SERVER_IP = config.getString("ip");
            result = true;
        }
        return result;
    }
}
