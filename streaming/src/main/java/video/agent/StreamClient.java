package video.agent;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import video.agent.ui.SingleVideoDisplayWindow;
import video.handler.StreamFrameListener;

public class StreamClient {

	private final static Dimension dimension = new Dimension(320,240);
	private final static SingleVideoDisplayWindow displayWindow = new SingleVideoDisplayWindow("Stream example",dimension);
	protected final static Logger logger = LoggerFactory.getLogger(StreamClient.class);
    public static final String DEFAULT_HOST = "localhost";

    public void run(String hostname) {
		//setup the videoWindow
		displayWindow.setVisible(true);
		
		//setup the connection
		logger.info("setup dimension :{}",dimension);
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(),dimension);
		clientAgent.connect(new InetSocketAddress(hostname, 20000));
	}
	
	
	protected static class StreamFrameListenerIMPL implements StreamFrameListener{
		private volatile long count = 0;
		@Override
		public void onFrameReceived(BufferedImage image) {
			logger.info("frame received :{}",count++);
			displayWindow.updateImage(image);			
		}
		
	}
	

}
