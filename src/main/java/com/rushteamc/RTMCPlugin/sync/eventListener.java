package com.rushteamc.RTMCPlugin.sync;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;
import com.rushteamc.RTMCPlugin.sync.message.*;

public class eventListener implements Listener
{
	public String format;
	
	public eventListener(FileConfiguration config)
	{
		format = config.getString("chat.format.default").replace('&', ChatColor.COLOR_CHAR);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onAsyncPlayerChatSeccond(AsyncPlayerChatEvent event)
	{
		// publicChat message = new publicChat(event.getFormat(),event.getPlayer().getDisplayName(),event.getMessage());
		// syncmain.sendMessage(message);
		Player player = event.getPlayer();
		Synchronizer.sendMessage( new FormattedChatMessage(ChatManager.BaseFormat, player, event.getMessage()) );
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		//playerJoin message = new playerJoin(event.getPlayer().getDisplayName(),event.getJoinMessage());
		//syncmain.sendMessage(message);
		Synchronizer.sendMessage( new ChatMessage(event.getJoinMessage()) );
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		//playerLeave message = new playerLeave(event.getPlayer().getDisplayName(),event.getQuitMessage());
		//syncmain.sendMessage(message);
		Synchronizer.sendMessage( new ChatMessage(event.getQuitMessage()) );
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event)
	{
		//playerKick message = new playerKick(event.getPlayer().getDisplayName(),event.getLeaveMessage());
		//syncmain.sendMessage(message);
		Synchronizer.sendMessage( new ChatMessage(event.getLeaveMessage()) );
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		//playerDeath message = new playerDeath(event.getEntity().getDisplayName(),event.getDeathMessage());
		//syncmain.sendMessage(message);
		Synchronizer.sendMessage( new ChatMessage(event.getDeathMessage()) );
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled())
			return;
		
		String command = event.getMessage();
		if (command == null)
			return;

		if (command.toLowerCase().startsWith("/me ")) {
			Synchronizer.sendMessage(new FormattedChatMessage("Me", event.getPlayer(), command.substring(command.indexOf(" ")).trim()));
		} else if (command.toLowerCase().startsWith("/pex ")) {
			;
		}
	}
	
}
