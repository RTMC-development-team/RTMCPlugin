package com.rushteamc.RTMCPlugin.sync.message;

public class meChat extends message
{
	private static final long serialVersionUID = -4165098980383195775L;
	public String playerName;
	public String message;
	
	public meChat(String playerName, String message)
	{
		this.playerName = playerName;
		this.message = message;
		type = messageType.CHAT_ADMIN;
	}
	
}
