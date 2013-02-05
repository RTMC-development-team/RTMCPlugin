package com.rushteamc.RTMCPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.rushteamc.RTMCPlugin.adminChat.adminChatMain;
import com.rushteamc.RTMCPlugin.sync.syncMain;
import com.rushteamc.RTMCPlugin.sync.message.meChat;

public class RTMCPlugin extends JavaPlugin
{
	public syncMain sync;
	public adminChatMain adminChat;
	
	public void onLoad()
	{
		;
	}
	
	public void onEnable()
	{
		sync = new syncMain(this);
		adminChat = new adminChatMain(this);
		//Bukkit.addFakeOnline("test");
		//getServer().addFakeOnline("test1");
		//getServer().addFakeOnline("test2");
		//getServer().addFakeOnline("test3");
	}
	
	public void onDisable()
	{
		sync.unload();
		//getServer().removeFakeOnline("test1");
		//getServer().removeFakeOnline("test2");
		//getServer().removeFakeOnline("test3");
	}
	
	private String joinArguments(String[] args)
	{
		String msg = "";
		for(String arg : args)
			msg += arg + " ";
		if(msg.length()>0)
			msg = msg.substring(0,msg.length()-1);
		return msg;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		switch(cmd.getName())
		{
		case "rtmctest":
			//sync.sendTestPacket( joinArguments(args) );
			return true;
		case "rtmcsync":
			int i = -1;
			do
			{
				i++;
				if(i>=args.length)
				{
					sender.sendMessage("usage:\n" + cmd.getUsage() );
					return true;
				}
			} while( args[i].equals("") );
			switch(args[i])
			{
			case "reset":
				do
				{
					i++;
					if(i>=args.length)
					{
						sender.sendMessage("usage:\n" + cmd.getUsage() );
						return true;
					}
				} while( args[i].equals("") );
				switch(args[i])
				{
				case "in":
					if(sync == null)
						sync = new syncMain(this);
					else
						sync.resetIn();
					break;
				case "out":
					if(sync == null)
						sync = new syncMain(this);
					else
						sync.resetOut();
					break;
				default:
					sender.sendMessage(args[i] + " is not a valid action for reload, usage:\n" + cmd.getUsage() + " [in|out]" );
				}
				break;
			default:
				sender.sendMessage(args[i] + " is not a valid action, usage:\n" + cmd.getUsage() );
			}
			return true;
		case "atoggle":
			adminChat.togleAdminChat(sender.getName());
			return true;
		case "amsg":
			adminChat.sendAdminChat(sender.getName(), joinArguments(args) );
			return true;
		case "me":
			meChat message = new meChat(sender.getName(), joinArguments(args) );
			sync.sendMessage(message);
			return false;
		case "msg":
			//adminChat.sendAdminChat(sender.getName(), joinArguments(args) );
			return false;
		case "reply":
			//adminChat.sendAdminChat(sender.getName(), joinArguments(args) );
			return false;
		case "mail":
			//adminChat.sendAdminChat(sender.getName(), joinArguments(args) );
			return false;
		}
		return false;
	}
	
}
