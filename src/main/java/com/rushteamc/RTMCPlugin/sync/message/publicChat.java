package com.rushteamc.RTMCPlugin.sync.message;

public class publicChat extends message
{
	private static final long serialVersionUID = -5000195932812807688L;
	public String playerName;
	public String message;
	public String format;
	
	public publicChat(String format, String playerName, String message)
	{
		this.playerName = playerName;
		this.format = format;
		this.message = message;
		type = messageType.CHAT_PUBLIC;
	}
	
}
