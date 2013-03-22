package com.rushteamc.RTMCPlugin.adminChat;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.rushteamc.RTMCPlugin.RTMCPlugin;
import com.rushteamc.RTMCPlugin.ChatManager.ChatManager;

public class adminChatMain
{
	private static RTMCPlugin rtmcplugin;
	private static final String AdminChatFotmat = "adminchat";
	
	public adminChatMain(RTMCPlugin main)
	{
		;
	}
	
	public static void init(RTMCPlugin rtmcplugin)
	{
		adminChatMain.rtmcplugin = rtmcplugin;
		rtmcplugin.getServer().getPluginManager().registerEvents(new adminChatEventListener(), rtmcplugin);
	}
	
	public static boolean getAdminChatEnabled(Player player)
	{
		List<MetadataValue> values = player.getMetadata("adminChat");  
		for(MetadataValue value : values)
		{
			if(value.getOwningPlugin().getDescription().getName().equals("RTMCPlugin"))
			{
				return value.asBoolean();
			}
		}
		return false;
	}
	
	public static void togleAdminChat(String playername)
	{
		Player player = Bukkit.getPlayer(playername);
		boolean enable = !getAdminChatEnabled(player);
		System.out.println("[RTMCPlugin][ADMINCHAT] Player " + playername + " turned adminchat "+((enable)?"on":"off"));
		player.setMetadata("adminChat", new FixedMetadataValue(rtmcplugin,(enable)));
		player.sendMessage( ChatColor.RED + (enable?"Enabled":"Disabled") + " admin chat.");
	}
	
	public static void sendAdminChatMessage(String playername, String msg)
	{
		ChatManager.sendMessage(ChatManager.format(AdminChatFotmat, Bukkit.getPlayer(playername), msg), new String[]{"RTMCPlugin.adminchat.listen"});
	}
	
	public static void sendAdminChat(String playername, String msg)
	{
		sendAdminChatMessage(playername, msg);
	}
	
}
