package com.liberty.robot.controllers;

import com.liberty.robot.common.VoiceConfig;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import utils.VoiceSynthesizer;

import static utils.LoggingUtil.error;

/**
 * Created by Dmytro_Kovalskyi on 10.12.2015.
 */
public class VoiceController {
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static VoiceSynthesizer synthesizer = new VoiceSynthesizer();

    public void synthesize(int id) {
        if(isValidId(id)) {
            synthesizer.synthesize(id, VoiceConfig.VoiceGender.MALE);
        } else {
            error(this, "id should be in range from 0 to " + synthesizer.getCurrentVoiceCounter());
        }
    }

    public void synthesize(String text) {
        try {
            if(!StringUtils.isBlank(text)) {
                synthesizer.synthesize(URLDecoder.decode(text, DEFAULT_ENCODING), VoiceConfig.VoiceGender.MALE);
            } else {
                error(this, "Can not synthesize empty text : " + text);
            }
        } catch(UnsupportedEncodingException e) {
            error(this, e);
        }
    }

    private boolean isValidId(int id) {
        return !(id > synthesizer.getCurrentVoiceCounter() || id < 0);
    }

    public Map<Integer, String> getAvailablePhrases() {
        Map<Integer, String> result = new HashMap<>();
        synthesizer.getPhrases().forEach((phrase, id) -> {
            try {
                result.put(id, URLEncoder.encode(phrase, DEFAULT_ENCODING));
            } catch(UnsupportedEncodingException e) {
                error(this, e);
            }
        });
        return result;
    }
}
