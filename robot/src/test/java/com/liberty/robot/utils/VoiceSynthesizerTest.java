package com.liberty.robot.utils;

import com.liberty.robot.common.VoiceConfig;
import org.junit.Test;
import utils.VoiceSynthesizer;

/**
 * Created by Dmytro_Kovalskyi on 09.12.2015.
 */
public class VoiceSynthesizerTest {
    @Test
    public void test() throws Exception {
        VoiceSynthesizer synthesizer = new VoiceSynthesizer();
//        synthesizer.getPhrases().keySet().forEach(id -> {
//            synthesizer.synthesize(id, VoiceConfig.VoiceGender.FEMALE);
//            try {
//                Thread.sleep(2000);
//            } catch(InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
        synthesizer.synthesize("Привет, человек, я - робот. Я умею ездить по офису и разговаривать.", VoiceConfig.VoiceGender.MALE);
        System.in.read();
    }
}
