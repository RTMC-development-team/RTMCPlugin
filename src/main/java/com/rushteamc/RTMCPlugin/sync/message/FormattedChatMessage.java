package com.rushteamc.RTMCPlugin.sync.message;

import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;

public class FormattedChatMessage implements Message
{
	private static final long serialVersionUID = 1L;
	
	private String format;
	private String playername;
	private String playerworld;
	private String message;
	
	public FormattedChatMessage(String format, Player player, String message)
	{
		this(format, player.getName(), player.getWorld().getName(), message);
	}
	
	public FormattedChatMessage(String format, String playername, String worldname, String message)
	{
		this.format = format;
		this.playername = playername;
		this.playerworld = worldname;
		this.message = message;
	}

	@Override
	public void execute()
	{
		ChatManager.sendMessageWithouSync(ChatManager.format(format, playername, playerworld, message));
	}
	
}
