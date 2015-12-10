package com.liberty.robot.messages;

/**
 * Created by Dmytro_Kovalskyi on 10.12.2015.
 */
public class VoiceSynthesizeMessage {
    private Integer phraseId;
    private String text;

    public Integer getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(Integer phraseId) {
        this.phraseId = phraseId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
