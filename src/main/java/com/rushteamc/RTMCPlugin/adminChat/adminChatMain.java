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
	private RTMCPlugin main;
	private static final String AdminChatFotmat = "adminchat";
	
	public adminChatMain(RTMCPlugin main)
	{
		this.main = main;
		main.getServer().getPluginManager().registerEvents(new adminChatEventListener(this), main);
		String format = main.getConfig().getString("chat.format.adminchat");
		if(format==null)
			format = "[ADMINCHAT][{PLAYERNAME}]: {MESSAGE}";
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
	
	public void togleAdminChat(String playername)
	{
		System.out.println("[RTMCPlugin][ADMINCHAT] Player " + playername + " togled adminchat...");
		Player player = main.getServer().getPlayer(playername);
		boolean enable = !getAdminChatEnabled(player);
		System.out.println("[RTMCPlugin][ADMINCHAT] Player " + playername + " togled adminchat "+((enable)?"on":"off"));
		player.setMetadata("adminChat", new FixedMetadataValue(main,(enable)));
		player.sendMessage( ChatColor.RED + (enable?"Enabled":"Disabled") + " admin chat.");
	}
	
	public static void sendAdminChatMessage(String playername, String msg)
	{
		ChatManager.sendMessage(ChatManager.format(AdminChatFotmat, Bukkit.getPlayer(playername), msg), new String[]{"RTMCPlugin.adminchat.listen"});
	}
	
	public void sendAdminChat(String playername, String msg)
	{
		System.out.println("[ADMINCHAT]["+playername+"]: "+msg);
		sendAdminChatMessage(playername, msg);
	}
	
}
