package com.rushteamc.RTMCPlugin.sync.message;

public class playerDeath extends message
{
	private static final long serialVersionUID = -2467790216573477440L;
	public String playerName;
	public String message;
	
	public playerDeath(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.PLAYER_DEATH;
	}
}
