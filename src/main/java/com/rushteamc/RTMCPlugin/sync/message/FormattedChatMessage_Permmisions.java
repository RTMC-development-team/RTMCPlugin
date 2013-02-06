package com.rushteamc.RTMCPlugin.sync.message;

import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;

public class FormattedChatMessage_Permmisions implements Message
{
	private static final long serialVersionUID = 1L;
	
	private String format;
	private String playername;
	private String playerworld;
	private String message;
	private String[] permmisions;
	
	public FormattedChatMessage_Permmisions(String format, Player player, String message, String[] permmisions)
	{
		this(format, player.getName(),player.getWorld().getName(), message, permmisions);
	}
	
	public FormattedChatMessage_Permmisions(String format, String playername, String worldname, String message, String[] permmisions)
	{
		this.format = format;
		this.playername = playername;
		this.playerworld = worldname;
		this.message = message;
		this.permmisions = permmisions;
	}

	@Override
	public void execute() {
		ChatManager.sendMessageWithouSync(ChatManager.format(format, playername, playerworld, message), permmisions);
	}
}
