package com.rushteamc.RTMCPlugin.sync.message;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;

public class ChatMessage_Players implements Message
{
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String[] players;
	
	public ChatMessage_Players(String message, Player[] players)
	{
		this.message = message;
		this.players = new String[players.length];
		for(int i = 0;i<players.length;i++)
			this.players[i] = players[i].getName();
	}
	
	public ChatMessage_Players(String message, String[] players)
	{
		this.message = message;
		this.players = players;
	}

	@Override
	public void execute()
	{
		Player[] playersObj = new Player[players.length];
		for(int i = 0;i<players.length;i++)
			playersObj[i] = Bukkit.getPlayer(players[i]);
		ChatManager.sendMessageWithouSync(message, playersObj);
	}
	
}
