package com.liberty.robot.helpers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class Scope {

    private TargetDataLine line;
    private double frequency = 440D;
    private int cycles = 10;
    private double samplesTaken;
    private BufferedImage buf;
    private volatile boolean active;
    private final Object lock;
    private Thread thread;
    private Consumer<VoiceSampleMessage> consumer;

    public Scope(Consumer<VoiceSampleMessage> consumer) {
        this(getDefaultTargetDataLine());
        this.consumer = consumer;
    }

    private Scope(TargetDataLine input) {
        //  super();
        //setPreferredSize(new Dimension(700, 256));
        line = input;
        try {
            line.open(getDefaultAudioFormat(), line.getBufferSize());
        } catch(Throwable t) {
            t.printStackTrace();
        }
        line.start();
        frequency = 100D;
        cycles = 5;
        System.out.println("Sample rate  : " + line.getFormat().getSampleRate());
        samplesTaken = line.getFormat().getSampleRate() / frequency * cycles;
        System.out.println("Sample taken  : " + samplesTaken);
        lock = new Object();
        buf = new BufferedImage(1024, 256, BufferedImage.TYPE_INT_RGB);
        Graphics g = buf.getGraphics();
        g.clearRect(0, 0, 1024, 256);
        g.dispose();
        active = false;
        thread = new ScopeReader();
    }

    private static AudioFormat getDefaultAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    private static TargetDataLine getDefaultTargetDataLine() {
        try {
            DataLine.Info linfo = new DataLine.Info(TargetDataLine.class, getDefaultAudioFormat());
            return (TargetDataLine) AudioSystem.getLine(linfo);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start() {
        if(!active) {
            synchronized(lock) {
                active = true;
                thread = new ScopeReader();
                thread.start();
            }
        }
    }

    public void stop() {
        if(active) {
            active = false;
            try {
                thread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        active = false;
    }

    public void paint(Graphics g) {
        g.drawImage(buf, 0, 0, null);
    }

    private class ScopeReader extends Thread {

        public void run() {
            synchronized(lock) {
                int samples = (int) Math.round(samplesTaken * 1.5);
                byte[] sample = new byte[samples];
                AffineTransform scale = AffineTransform.getScaleInstance(700D / samplesTaken, 1);
                AffineTransform translate = AffineTransform.getTranslateInstance(0, 127);
                scale.concatenate(translate);
                BufferedImage buf = new BufferedImage(1024, 256, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = (Graphics2D) buf.getGraphics();
                g.transform(scale);
                line.start();
                while(active) {
                    line.read(sample, 0, samples);

                    consumer.accept(new VoiceSampleMessage(sample));
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            line.stop();
            line.flush();
        }
    }


}
