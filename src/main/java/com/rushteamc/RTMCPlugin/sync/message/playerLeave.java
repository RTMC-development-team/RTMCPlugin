package com.rushteamc.RTMCPlugin.sync.message;

public class playerLeave extends message
{
	private static final long serialVersionUID = 7334395069147426921L;
	public String playerName;
	public String message;
	
	public playerLeave(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.PLAYER_LEAVE;
	}
}
