package com.rushteamc.RTMCPlugin.sync.message;

import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;

public class FormattedChatMessage_Players implements Message
{
	private static final long serialVersionUID = 1L;
	
	private String format;
	private String playername;
	private String playerworld;
	private String message;
	private Player[] players;
	
	public FormattedChatMessage_Players(String format, Player player, String message, Player[] players)
	{
		this(format, player.getName(),player.getWorld().getName(), message, players);
	}
	
	public FormattedChatMessage_Players(String format, String playername, String worldname, String message, Player[] players)
	{
		this.format = format;
		this.playername = playername;
		this.playerworld = worldname;
		this.message = message;
		this.players = players;
	}

	@Override
	public void execute() {
		ChatManager.sendMessageWithouSync(ChatManager.format(format, playername, playerworld, message), players);
	}
}
