package com.epam.robot.controllers;

import com.epam.robot.common.ConnectionProperties;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;

public class VoiceController implements Runnable{

    private String address = "127.0.0.1";
    /**
     * Creates a new instance of MicPlayer
     * @param ip
     */
    public VoiceController(String ip) {
        this.address = ip;
    }

    public VoiceController() {
    }

    private void startTransmission() throws Exception {
        InetAddress ipAddress = InetAddress.getByName(address);
        System.out.println("Connecting to " + address + " and port " + ConnectionProperties.VOICE_PORT);
        Socket socket = new Socket(ipAddress, ConnectionProperties.VOICE_PORT);

        Handler handler = new Handler(socket);
        Mixer.Info minfo[] = AudioSystem.getMixerInfo();
        for(Mixer.Info aMinfo : minfo) {
            System.out.println(aMinfo);
        }


        if(AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
                TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(getAudioFormat());
                targetDataLine.start();
                byte tempBuffer[] = new byte[1024];
                int cnt = 0;
                while(true) {
                    targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    handler.sent(tempBuffer);
                }

            } catch(Exception e) {
                System.out.println(" not correct ");
                System.exit(0);
            }
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
            System.err.println("[VoiceController] error : " + e.getMessage());
        }
    }
}

class Handler {
    private Socket socket;
    private OutputStream sout;
    private InputStream sin;

    public Handler(Socket socket) {
        try {
            this.socket = socket;
            sin = socket.getInputStream();
            sout = socket.getOutputStream();
        } catch(Exception e) {
            System.err.println("[TransmissionManger] error : " + e.getMessage());
        }
    }

    public void sent(byte[] content) {
        try {
//            System.out.println("Send bytes : " + Arrays.toString(content));
            sout.write(content);
            sout.flush();
        } catch(Throwable t) {
            System.err.println("Error : " + t.getMessage());
        }
    }


}