package com.rushteamc.RTMCPlugin.ChatManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventListener implements Listener
{
	public EventListener()
	{
		;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatFirst(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		event.setFormat( ChatManager.format(ChatManager.BaseFormat , player.getName(), "%s", player.getWorld().getName() , "%s") );
	}
}
