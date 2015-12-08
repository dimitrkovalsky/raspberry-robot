package utils;

import com.liberty.robot.common.Config;
import com.liberty.robot.common.VoiceConfig;
import com.sun.istack.internal.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import static utils.LoggingUtil.error;

/**
 * Created by Dmytro_Kovalskyi on 08.12.2015.
 */
public class VoiceSynthesizer {
    private static final String API_KEY = "24bdfb36-8faf-4223-afd6-17a855a6b7c2";
    private Map<String, String> cached = new HashMap<>(); // Map <Phrase --> filename>

    public static void main(String[] args) throws Exception {
        String apiKey = "24bdfb36-8faf-4223-afd6-17a855a6b7c2";
        String text = URLEncoder.encode("Привет Сергей, си шарп - это не язык программирования?", "UTF-8");
        String lang = "ru-RU";
        String url = "https://tts.voicetech.yandex.net/generate?text=" + text + "&format=wav&lang=" + lang
            + "&speaker=ermil&emotion=evil&key=" + apiKey + "&robot=false";
        System.out.println(url);


// Get the response
        // play(executeRequest(url));

        System.in.read();
    }

    public void synthesize(String text) {
        String filename = cached.get(text);
        if(filename != null) {
            synthesizeCached(text, filename);
        } else {
            synthesizeNew(text);
        }
    }

    private void synthesizeNew(String text) {

    }

    private void synthesizeCached(String text, String fileName) {

    }

    @NotNull
    private String buildRequest(String text, VoiceConfig.VoiceGender gender, VoiceConfig.VoiceEmotion emotion) {
        String url = "";
        try {
            String lang = "ru-RU";
            String textToSpeech = URLEncoder.encode(text, "UTF-8");
            url = "https://tts.voicetech.yandex.net/generate?text=" + textToSpeech
                + "&format=wav&lang=" + lang
                + "&speaker=" + gender.getValue()
                + "&emotion=" + emotion.name()
                + "&key=" + API_KEY;
        } catch(UnsupportedEncodingException e) {
            error(this, e);
        }
        return url;
    }

    private void play(InputStream stream)
        throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(stream));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    private InputStream executeRequest(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            return response.getEntity().getContent();
        } catch(Exception e) {
            error(null, e);
        }
        return null;
    }

    private void saveToFile(String fileName, InputStream stream) throws IOException {
        Path targetPath = new File(Config.VOICE_SAMPLES_FOLDER + fileName).toPath();

        Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        stream.close();
    }

    private void speak(InputStream stream) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(stream))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        }
    }
}
