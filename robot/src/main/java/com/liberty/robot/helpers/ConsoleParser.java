package com.liberty.robot.helpers;

import com.liberty.robot.common.Config;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

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

            Switch helpOpt = new Switch("help")
                .setShortFlag('h')
                .setLongFlag("help");
            helpOpt.setHelp("Show help information");

            Switch voiceLog = new Switch("voice-log")
                .setShortFlag('v')
                .setLongFlag("voice-log");
            helpOpt.setHelp("Enable voice logging");

            FlaggedOption ipOpt = new FlaggedOption("ip")
                .setStringParser(JSAP.STRING_PARSER)
                .setLongFlag("ip")
                .setShortFlag('i')
                .setRequired(true)
                .setDefault("127.0.0.1");
            ipOpt.setHelp("Use to set appropriate ip for concrete server");

            jsap.registerParameter(speakersOpt);
            jsap.registerParameter(microphoneOpt);
            jsap.registerParameter(ipOpt);
            jsap.registerParameter(helpOpt);
            jsap.registerParameter(voiceLog);
        } catch(JSAPException e) {
            System.err.println("[ConsoleParser] init error : " + e.getMessage());
        }
    }

    public boolean parse(String[] args) {
        JSAPResult config = jsap.parse(args);
        boolean result = false;
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
            if(config.getBoolean("help")) {
                System.out.println(jsap.getHelp());
            } else {
                Config.SPEAKERS_ENABLED = config.getBoolean("speakers");
                Config.VOICE_RECORDING_ENABLED = config.getBoolean("microphone");
                Config.SERVER_IP = config.getString("ip");
                Config.VOICE_LOG_ENABLED = config.getBoolean("voice-log");
                result = true;
            }
        }
        return result;
    }
}
