package com.rushteamc.RTMCPlugin.adminChat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class adminChatEventListener implements Listener
{
	private adminChatMain adminchatmain;
	
	public adminChatEventListener(adminChatMain adminchatmain)
	{
		this.adminchatmain = adminchatmain;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChatSeccond(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		if( adminChatMain.getAdminChatEnabled(player) )
		{
			adminchatmain.sendAdminChat(player.getName(), event.getMessage());
			event.setCancelled(true);
		}
	}
}
