package com.rushteamc.RTMCPlugin.sync.message;

public class playerJoin extends message
{
	private static final long serialVersionUID = -8904376194212898561L;
	public String playerName;
	public String message;
	
	public playerJoin(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.PLAYER_JOIN;
	}
}
