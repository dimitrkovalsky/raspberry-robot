package utils;
/**
 * Created by Dmytro_Kovalskyi on 03.12.2015.
 */

import java.beans.PropertyVetoException;
import java.util.Locale;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

import static utils.LoggingUtil.error;

public class VoiceSynthesizer {
    SynthesizerModeDesc desc;
    Synthesizer synthesizer;
    Voice voice;

    public VoiceSynthesizer() {
        try {
            init("kevin16");
        } catch(Exception e) {
            error(this, "Error of VoiceSynthesizer initialization ", e);
        }
    }

    public void init(String voiceName)
        throws EngineException, AudioException, EngineStateError,
        PropertyVetoException {
        if(desc == null) {

            System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            desc = new SynthesizerModeDesc(Locale.US);
            Central.registerEngineCentral
                ("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(desc);
            synthesizer.allocate();
            synthesizer.resume();
            SynthesizerModeDesc smd =
                (SynthesizerModeDesc) synthesizer.getEngineModeDesc();
            Voice[] voices = smd.getVoices();
            Voice voice = null;
            for(Voice voice1 : voices) {
                if(voice1.getName().equals(voiceName)) {
                    voice = voice1;
                    break;
                }
            }
            synthesizer.getSynthesizerProperties().setVoice(voice);
        }

    }

    public void terminate() throws EngineException, EngineStateError {
        synthesizer.deallocate();
    }

    public void doSpeak(String speakText)
        throws EngineException, AudioException, IllegalArgumentException,
        InterruptedException {
        synthesizer.speakPlainText(speakText, null);
        synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

    }

    private static VoiceSynthesizer voiceSynthesizer = new VoiceSynthesizer();

    public static void speak(String message){
        try {
            voiceSynthesizer.doSpeak(message);
        } catch(EngineException e) {
            e.printStackTrace();
        } catch(AudioException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        VoiceSynthesizer.speak("dot NET is death");
    }
}
