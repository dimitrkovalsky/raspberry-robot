package com.liberty.robot.video.agent;

import java.net.SocketAddress;

public interface IStreamClientAgent {
	public void connect(SocketAddress streamServerAddress);
	public void stop();
}
