package com.rushteamc.RTMCPlugin.ChatManager;

import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.RTMCPlugin;
import com.rushteamc.RTMCPlugin.sync.Synchronizer;
import com.rushteamc.RTMCPlugin.sync.message.*;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatManager
{
	private static PermissionManager permissions;
	private static FileConfiguration config;
	private static Map<String, String> formats = new Hashtable<String, String>();
	public static final String BaseFormat = "default";
	
	public static void setup(RTMCPlugin rtmcplugin)
	{
		ChatManager.config = rtmcplugin.getConfig();
		ChatManager.permissions = PermissionsEx.getPermissionManager();
		
		rtmcplugin.getServer().getPluginManager().registerEvents(new EventListener(), rtmcplugin);

		addFormats(config.getDefaults().getConfigurationSection("chat.format"));
		addFormats(config.getConfigurationSection("chat.format"));
	}
	
	private static void addFormats(ConfigurationSection configList)
	{
		if(configList==null)
			return;
		Map<String, Object> objs = configList.getValues(false);
		if(objs==null)
			return;
		Iterator<Entry<String, Object>> it = objs.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, Object> obj = it.next();
			if(obj.getValue() instanceof String)
			{
				formats.put(obj.getKey(), ((String)obj.getValue()).replace('&', ChatColor.COLOR_CHAR) );
			}
		}
	}
	
	public static void sendMessage(String msg)
	{
		sendMessageWithouSync(msg);
		Synchronizer.sendMessage(new ChatMessage(msg));
	}
	
	public static void sendMessage(String msg, Player[] players)
	{
		sendMessageWithouSync(msg, players);
		Synchronizer.sendMessage(new ChatMessage_Players(msg, players));
	}
	
	public static void sendMessage(String msg, String[] permmisions)
	{
		sendMessageWithouSync(msg, permmisions);
		Synchronizer.sendMessage(new ChatMessage_Permmissions(msg, permmisions));
	}
	
	public static void sendMessageWithouSync(String msg)
	{
		for(Player player : Bukkit.getOnlinePlayers())
			player.sendMessage(msg);
	}
	
	public static void sendMessageWithouSync(String msg, Player[] players)
	{
		for(Player player : players)
			player.sendMessage(msg);
	}
	
	public static void sendMessageWithouSync(String msg, String[] permmisions)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			boolean permited = true;
			for(String perm : permmisions)
				if(!player.hasPermission(perm))
					permited = false;
			if(permited)
				player.sendMessage(msg);
		}
	}
	
	public static void sendMessageFormatted(String playerName, String worldName, String message)
	{
		// TODO: Throw player not found exception.
		sendMessageWithouSync(format(BaseFormat, playerName, worldName, message));
		Synchronizer.sendMessage(new FormattedChatMessage(BaseFormat, playerName, worldName, message));
	}
	
	public static void sendMessageFormatted(String playerName, String worldName, String message, Player[] players)
	{
		// TODO: Throw player not found exception.
		sendMessageWithouSync(format(BaseFormat, playerName, worldName, message), players);
		Synchronizer.sendMessage(new FormattedChatMessage_Players(BaseFormat, playerName, worldName, message, players));
	}
	
	public static void sendMessageFormatted(String playerName, String worldName, String message, String[] permmisions)
	{
		// TODO: Throw player not found exception.
		sendMessageWithouSync(format(BaseFormat, playerName, worldName, message), permmisions);
		Synchronizer.sendMessage(new FormattedChatMessage_Permmisions(BaseFormat, playerName, worldName, message, permmisions));
	}
	
	public static void sendMessageFormatted(Player player, String message)
	{
		sendMessageWithouSync(format(BaseFormat, player, message));
		Synchronizer.sendMessage(new FormattedChatMessage(BaseFormat, player, message));
	}
	
	public static void sendMessageFormatted(Player player, String message, Player[] players)
	{
		sendMessageWithouSync(format(BaseFormat, player, message), players);
		Synchronizer.sendMessage(new FormattedChatMessage_Players(BaseFormat, player, message, players));
	}
	
	public static void sendMessageFormatted(Player player, String message, String[] permmisions)
	{
		sendMessageWithouSync(format(BaseFormat, player, message), permmisions);
		Synchronizer.sendMessage(new FormattedChatMessage_Permmisions(BaseFormat, player, message, permmisions));
	}
	
	public static String format(String format, Player player, String message)
	{
		return format(format, player.getName(), player.getDisplayName(), player.getWorld().getName(), message);
	}
	
	public static String format(String format, String playerName, String worldName, String message)
	{
		// TODO: Throw player not found exception.
		return format(format, playerName, Bukkit.getPlayer(playerName).getDisplayName() , worldName, message);
	}
	
	public static String format(String format,String playerName, String playerNameString, String worldName, String message)
	{
		PermissionUser user = permissions.getUser(playerName);
		PermissionGroup[] userGroups = user.getGroups(worldName);
		PermissionGroup userGroup = userGroups[0];
		int maxRank = userGroups[0].getRank();
		for(PermissionGroup group : userGroups )
		{
			if(group.getRank() > maxRank)
				userGroup = group;
		}
		if( config.isString("chat.worlds." + worldName ) )
			worldName = config.getString("chat.worlds." + worldName ).replace('&', ChatColor.COLOR_CHAR);
		
		String form = formats.get(format);
		if(form==null)
			return null; // TODO: Throw error
		return form.replace("{WORLD}", worldName + ChatColor.RESET ).replace("{RANK}", userGroup.getName() + ChatColor.RESET).replace("{PLAYERNAME}", playerNameString + ChatColor.RESET).replace("{MESSAGE}", message + ChatColor.RESET);
	}
}
