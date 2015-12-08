package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioTest {
    public static void main(String[] args) throws MalformedURLException {
      start();
    }

    public static void start() throws MalformedURLException {
        final URL resource = Paths.get("D:\\sample.mp3").toUri().toURL();
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}