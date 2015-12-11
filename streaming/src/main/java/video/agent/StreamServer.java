package video.agent;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import java.awt.*;
import java.net.InetSocketAddress;


public class StreamServer {

    public static final String DEFAULT_HOST = "localhost";

    public void run(String hostname) {
        System.out.println("Running Stream server for : " + hostname);
        Webcam.setDriver(new V4l4jDriver());
        Webcam.setAutoOpenMode(true);
        System.out.println("Available cams" + Webcam.getWebcams());
        Webcam webcam = Webcam.getDefault();
        System.out.println("Webcam : " + webcam.getName());
        System.out.println("Webcam : " + webcam);
        Dimension dimension = new Dimension(320, 240);
        webcam.setViewSize(dimension);

        StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
        serverAgent.start(new InetSocketAddress(hostname, 20000));
    }

}
