package com.rushteamc.RTMCPlugin.sync.message;

public class playerKick extends message
{
	private static final long serialVersionUID = 6200026750467662030L;
	public String playerName;
	public String message;
	
	public playerKick(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.PLAYER_KICK;
	}
}
