package com.rushteamc.RTMCPlugin.sync;

import java.io.*;

import org.bukkit.configuration.file.FileConfiguration;

import com.rushteamc.RTMCPlugin.RTMCPlugin;
import com.rushteamc.RTMCPlugin.sync.message.Message;

public class Synchronizer
{
	private static String pipe_prefix;
	private static int numServers;
	private static int serverID;
	private static syncListener listeners[];
	private static syncSender senders[];
	
	public Synchronizer(RTMCPlugin main)
	{
		;
	}
	
	public static void init(RTMCPlugin rtmcplugin)
	{
		FileConfiguration config = rtmcplugin.getConfig();
		
		if(config.isString("sync.path"))
			pipe_prefix = (String)config.get("sync.path");
		else
			pipe_prefix = "/dev/shm/RTMCPlugin/pipes/server";
		
		if(config.isInt("sync.servers"))
			numServers = (int)config.get("sync.servers");
		else if(config.isString("sync.servers"))
			numServers = Integer.parseInt((String)config.get("sync.servers"));
		else
			numServers = 0;
		
		if(numServers > 1)
		{
			listeners = new syncListener[numServers-1];
			senders = new syncSender[numServers-1];
			
			serverID = 0;
			while(new File(pipe_prefix + "_" + String.valueOf(serverID) + "_" + String.valueOf(numServers-1)).exists())
				serverID++;
			if(serverID == numServers-1)
				if(new File(pipe_prefix + "_" + String.valueOf(serverID) + "_0").exists())
					serverID++;
			if(serverID>=numServers)
			{
				System.out.println("[RTMCPlugin][SYNC] All server IDs in use! Could not setup synchronizer.");
				// TODO: Do some better error handling here. Pipes should all pipes should be removed and recreated by the servers. This way the unused pipes are not recreated and can be used by this server.
				numServers = 0;
				listeners = new syncListener[0];
				senders = new syncSender[0];
				return;
			}
			
			int i;
			for(i = 0;i<serverID;i++)
			{
				listeners[i] = new syncListener(pipe_prefix + "_" + String.valueOf(serverID) + "_" + String.valueOf(i) );
				listeners[i].start();
			}
			for(i=serverID+1;i<numServers;i++)
			{
				listeners[i-1] = new syncListener(pipe_prefix + "_" + String.valueOf(serverID) + "_" + String.valueOf(i) );
				listeners[i-1].start();
			}
			
			for(i = 0;i<serverID;i++)
			{
				senders[i] = new syncSender(pipe_prefix + "_" + String.valueOf(i) + "_" + String.valueOf(serverID) );
				senders[i].start();
			}
			for(i = serverID+1;i<numServers;i++)
			{
				senders[i-1] = new syncSender(pipe_prefix + "_" + String.valueOf(i) + "_" + String.valueOf(serverID) );
				senders[i-1].start();
			}
		}
		else
		{
			listeners = new syncListener[0];
			senders = new syncSender[0];
		}
		
		rtmcplugin.getServer().getPluginManager().registerEvents(new eventListener(config), rtmcplugin);
	}
	
	public static void unload()
	{
		for(syncListener listener : listeners)
			listener.kill();
		for(syncListener listener : listeners)
			try {
				listener.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		for(syncSender sender : senders)
			sender.kill();
		for(syncSender sender : senders)
			try {
				sender.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	public static void sendMessage(Message formattedMessage)
	{
		for(syncSender sender : senders)
			// TODO: Do null pointer check (and solve null pointers...)
			sender.sendMessage2(formattedMessage);
	}
	
}
