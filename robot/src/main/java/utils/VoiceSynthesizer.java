package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liberty.robot.common.Config;
import com.liberty.robot.common.VoiceConfig;
import com.sun.istack.internal.NotNull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import static utils.LoggingUtil.error;
import static utils.LoggingUtil.localInfo;

/**
 * Created by Dmytro_Kovalskyi on 08.12.2015.
 */
public class VoiceSynthesizer {
    private static final String FILE_FORMAT = "wav";
    private static final String API_KEY = "24bdfb36-8faf-4223-afd6-17a855a6b7c2";
    public static final String HOLDER_PATH = Config.VOICE_SAMPLES_FOLDER + "holder.json";
    public static final String PHRASES_FILE_PATH = "../phrases.txt";
    public static final String RASPBERRY_PHRASES_LOCATION = "/opt/java/phrases.txt";
    private VoiceConfigHolder holder;
    private ObjectMapper mapper = new ObjectMapper();

    public VoiceSynthesizer() {
        init();
    }

    private void init() {
        localInfo(this, "Initialization...");
        loadFiles();
        checkFiles();
    }

    private void loadFiles() {
        try {
            Path voiceFolder = Paths.get(Config.VOICE_SAMPLES_FOLDER);
            if(!Files.exists(voiceFolder)) {
                Files.createDirectories(voiceFolder);
            }
            Path path = Paths.get(HOLDER_PATH);
            if(Files.exists(path)) {
                holder = mapper.readValue(path.toFile(), VoiceConfigHolder.class);
            } else {
                holder = new VoiceConfigHolder();
            }
        } catch(IOException e) {
            error(this, "loadFiles method", e);
            holder = new VoiceConfigHolder();
        }
    }

    private void checkFiles() {
        try {
            List<String> strings;
            URL resource = this.getClass().getResource(PHRASES_FILE_PATH);
            if(resource != null){
                strings = Files.readAllLines(Paths.get(resource.toURI()));
            }  else {
                strings = Files.readAllLines(Paths.get(RASPBERRY_PHRASES_LOCATION));
            }
            List<String> forUpdate = new ArrayList<>();
            for(String phrase : strings) {
                if(phrase.startsWith("//")) {
                    continue;
                }
                String currentPhrase = phrase.toLowerCase();
                Map<String, Integer> phrases = holder.getPhrases();
                Map<Integer, String> ids = holder.getIds();
                if(phrases.containsKey(currentPhrase) && ids
                    .containsKey(phrases.get(currentPhrase))) {
                    if(!Files.exists(Paths.get(ids.get(phrases.get(currentPhrase))))) {
                        forUpdate.add(currentPhrase);
                    }
                } else {
                    forUpdate.add(currentPhrase);
                }
            }
            for(String currentPhrase : forUpdate) {
                localInfo(this, "Trying to load voice for : " + currentPhrase);
                loadAndSave(currentPhrase, VoiceConfig.VoiceGender.MALE);
                //loadAndSave(currentPhrase, VoiceConfig.VoiceGender.FEMALE);
                //synthesizeCached(file);
            }
        } catch(Exception e) {
            error(this, e);
        }
    }

    public void synthesize(Integer id, VoiceConfig.VoiceGender gender) {
        String filename = holder.getIds().get(id);
        if(filename != null) {
            synthesizeCached(filename);
        } else {
            error(this, "File for id : " + id + " does not exists");
        }
    }

    public void synthesize(String text, VoiceConfig.VoiceGender gender) {
        Integer id = holder.getPhrases().get(text.toLowerCase());
        if(id != null) {
            synthesize(id, text, gender);
        } else {
            synthesizeNew(text, gender);
        }
    }

    private void synthesize(Integer id, String text, VoiceConfig.VoiceGender gender) {
        String filename = holder.getIds().get(id);
        if(filename != null) {
            synthesizeCached(filename);
        } else {
            loadAndSynthesize(text, gender);
        }
    }

    private void loadAndSynthesize(String text, VoiceConfig.VoiceGender gender) {
        try {
            String fileName = loadAndSave(text, gender);
            synthesizeCached(fileName);
        } catch(Exception e) {
            error(this, "loadAndSynthesize method", e);
        }
    }

    /**
     * @return Name of file with voice
     */
    private String loadAndSave(String text, VoiceConfig.VoiceGender gender) throws IOException {
        String request = buildRequest(text, gender, VoiceConfig.VoiceEmotion.good);
        InputStream inputStream = executeRequest(request);
        int count = holder.nextCount();
        String fileName = buildFileName(gender, VoiceConfig.VoiceEmotion.good, count);
        saveToFile(fileName, inputStream);
        updateHolder(count, fileName, text);
        return fileName;
    }

    private void updateHolder(int count, String fileName, String text) {
        holder.addId(count, fileName);
        holder.addPhrases(count, text);
        File file = new File(HOLDER_PATH);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, holder);
        } catch(Exception e) {
            error(this, "updateHolder method", e);
        }
    }

    private String buildFileName(VoiceConfig.VoiceGender gender, VoiceConfig.VoiceEmotion emotion, Integer id) {
        return Config.VOICE_SAMPLES_FOLDER + gender.name().toLowerCase() +
            Config.PATH_SEPARATOR + emotion.name().toLowerCase() +
            Config.PATH_SEPARATOR + id + "." + FILE_FORMAT;
    }

    private void synthesizeNew(String text, VoiceConfig.VoiceGender gender) {
        speak(executeRequest(buildRequest(text, gender, VoiceConfig.VoiceEmotion.good)));
    }

    private void synthesizeCached(String fileName) {
        Path path = Paths.get(fileName);
        try {
            if(Files.exists(path)) {
                speak(new FileInputStream(path.toFile()));
            } else {
                error(this, "File with path : " + fileName + " doesn't exist. Please regenerate files");
            }
        } catch(Exception e) {
            error(this, "synthesizeCached method ", e);
        }
    }

    @NotNull
    private String buildRequest(String text, VoiceConfig.VoiceGender gender, VoiceConfig.VoiceEmotion emotion) {
        String url = "";
        try {
            String lang = "ru-RU";
            String textToSpeech = URLEncoder.encode(text, "UTF-8");
            url = "https://tts.voicetech.yandex.net/generate?text=" + textToSpeech
                + "&format=" + FILE_FORMAT + "&lang=" + lang
                + "&speaker=" + gender.getValue()
                + "&emotion=" + emotion.name()
                + "&key=" + API_KEY;
        } catch(UnsupportedEncodingException e) {
            error(this, e);
        }
        return url;
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
        Path targetPath = new File(fileName).toPath();
        if(!Files.exists(targetPath.getParent())) {
            Files.createDirectories(targetPath.getParent());
        }
        Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        stream.close();
    }

    private void speak(InputStream stream) {

        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(stream))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch(Exception e) {
            error(this, e);
        }
    }

    /**
     * Return al available phrases
     *
     * @return id -> phrase
     */
    public Map<String, Integer> getPhrases() {
        return holder.getPhrases();
    }

    public int getCurrentVoiceCounter() {
        return holder.counter;
    }

    public static class VoiceConfigHolder {
        private Map<String, Integer> phrases = new HashMap<>(); // Phrase --> Id
        private Map<Integer, String> ids = new HashMap<>(); // Id --> filename
        private Integer counter = 0;

        public Map<String, Integer> getPhrases() {
            return phrases;
        }

        public void setPhrases(Map<String, Integer> phrases) {
            this.phrases = phrases;
        }

        public Map<Integer, String> getIds() {
            return ids;
        }

        public void setIds(Map<Integer, String> ids) {
            this.ids = ids;
        }

        public void addId(Integer id, String filename) {
            ids.put(id, filename);
        }

        public void addPhrases(Integer id, String phrase) {
            phrases.put(phrase.toLowerCase(), id);
        }

        public Integer nextCount() {
            counter++;
            return counter;
        }

        public Integer getCounter() {
            return counter;
        }

        public void setCounter(Integer counter) {
            this.counter = counter;
        }
    }
}
