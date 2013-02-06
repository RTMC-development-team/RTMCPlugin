package com.rushteamc.RTMCPlugin.sync.message;

public class adminChat extends message
{
	private static final long serialVersionUID = 3004895120031064061L;
	public String playerName;
	public String message;
	
	public adminChat(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.CHAT_ADMIN;
	}
	
}
