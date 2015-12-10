package com.liberty.robot.controllers;

import com.liberty.robot.common.Config;
import com.liberty.robot.common.ConnectionProperties;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class VoiceTransmissionController implements Runnable {
    private SourceDataLine speakersDataLine;
    private TargetDataLine microphoneDataLine;
    private String address = "127.0.0.1";
    private Handler handler;

    /**
     * Creates a new instance of MicPlayer
     *
     * @param ip
     */
    public VoiceTransmissionController(String ip) {
        this.address = ip;
    }

    public VoiceTransmissionController() {
    }

    private void startTransmission() throws Exception {
        InetAddress ipAddress = InetAddress.getByName(address);
        System.out.println("Connecting to " + address + " and port " + ConnectionProperties.VOICE_PORT);
        Socket socket = new Socket(ipAddress, ConnectionProperties.VOICE_PORT);
        init();
        handler = new Handler(socket, this::toSpeaker);
        if(Config.VOICE_RECORDING_ENABLED) {
            startRecording(handler);
        }
    }

    private void init() {
        if(Config.SPEAKERS_ENABLED) {
            initSpeakers();
        }
        if(Config.VOICE_RECORDING_ENABLED) {
            initMicrophone();
        }
    }

    private void startRecording(Handler handler) {
        try {

            byte tempBuffer[] = new byte[Config.VOICE_BUFFER_SIZE];
            while(true) {
                microphoneDataLine.read(tempBuffer, 0, tempBuffer.length);
                handler.sent(tempBuffer);
            }

        } catch(Exception e) {
            System.out.println("[VoiceTransmissionController] startRecording error : " + e.getMessage());

        }
    }

    private void initMicrophone() {
        Mixer.Info minfo[] = AudioSystem.getMixerInfo();

        for(Mixer.Info aMinfo : minfo) {
            System.out.println(aMinfo);
        }
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
            microphoneDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            System.out.println("Data Line : " + microphoneDataLine.getLineInfo());
            microphoneDataLine.open(getAudioFormat());
            microphoneDataLine.start();

        } catch(Exception e) {
            System.out.println("[VoiceTransmissionController] not correct : " + e.getMessage());
            System.out.println("MICROPHONE doesn't supported");
        }
    }

    private void initSpeakers() {
        try {
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
            speakersDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakersDataLine.open(getAudioFormat());
            speakersDataLine.start();
            System.out.println("[VoiceTransmissionController] speakers initialized");
        } catch(Exception e) {
            System.err.println("[VoiceTransmissionController] speakers init error : " + e.getMessage());
        }
    }

    private void toSpeaker(byte soundbytes[]) {
        if(Config.VOICE_LOG_ENABLED) {
            System.out.println("Voice received " + Arrays.toString(soundbytes));
        }
        try {
            speakersDataLine.write(soundbytes, 0, soundbytes.length);
//            speakersDataLine.drain();
        } catch(Exception e) {
            System.out.println("Not working in speakers " + e.getMessage());
        }

    }


    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    @Override
    public void run() {
        try {
            startTransmission();
        } catch(Exception e) {
            System.err.println("[VoiceTransmissionController] error : " + e.getMessage());
        }
    }
}

class Handler {
    private Socket socket;
    private OutputStream sout;
    private InputStream sin;
    private Consumer<byte[]> onRead;

    public Handler(Socket socket, Consumer<byte[]> onRead) {
        try {
            this.socket = socket;
            sin = socket.getInputStream();
            sout = socket.getOutputStream();
            this.onRead = onRead;
            if(Config.SPEAKERS_ENABLED) {
                new Thread(this::read).start();
            }
        } catch(Exception e) {
            System.err.println("[Handler] error : " + e.getMessage());
        }
    }

    public void sent(byte[] content) {
        try {
            if(Config.VOICE_LOG_ENABLED) {
                System.out.println("Recorded voice : " + Arrays.toString(content));
            }
            sout.write(content);
            sout.flush();
        } catch(Throwable t) {
            System.err.println("Error : " + t.getMessage());
        }
    }

    private void read() {
        try {
            System.out.println("[Handler] Wait for messages");
            byte[] buffer = new byte[Config.VOICE_BUFFER_SIZE];
            while(true) {
                sin.read(buffer);
                System.out.println("Received data : " + Arrays.toString(buffer));
                onRead.accept(buffer);

            }
        } catch(Throwable t) {
            System.err.println("Error t : " + t.getMessage());
        }
    }


}