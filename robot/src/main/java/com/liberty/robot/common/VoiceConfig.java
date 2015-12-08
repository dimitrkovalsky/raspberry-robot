package com.liberty.robot.common;

/**
 * Created by Dmytro_Kovalskyi on 08.12.2015.
 */
public interface VoiceConfig {
    enum  VoiceGender {
        MALE("ermil"), FEMALE("jane");
        private final String name;

        VoiceGender(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }

    enum VoiceEmotion {
        good, neutral, evil, mixed
    }
}
