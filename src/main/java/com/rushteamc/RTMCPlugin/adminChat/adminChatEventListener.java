package com.rushteamc.RTMCPlugin.adminChat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class adminChatEventListener implements Listener
{
	public adminChatEventListener()
	{
		;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChatSeccond(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if( adminChatMain.getAdminChatEnabled(player) )
		{
			adminChatMain.sendAdminChat(player.getName(), event.getMessage());
			event.setCancelled(true);
		}
	}
}
