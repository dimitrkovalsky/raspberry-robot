package com.liberty.robot.video.agent;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.net.InetSocketAddress;


public class StreamServer {

	/**
	 * @author kerr
	 * @param args
	 */
	public static void main(String[] args) {
		Webcam.setAutoOpenMode(true);
        //System.out.println(Webcam.getWebcams());
		Webcam webcam = Webcam.getDefault();
		Dimension dimension = new Dimension(320, 240);
		webcam.setViewSize(dimension);

		StreamServerAgent serverAgent = new StreamServerAgent(webcam, dimension);
		serverAgent.start(new InetSocketAddress("localhost", 20000));
	}

}
