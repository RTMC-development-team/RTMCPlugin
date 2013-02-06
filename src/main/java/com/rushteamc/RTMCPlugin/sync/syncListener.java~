package com.rushteamc.RTMCPlugin.sync;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;

import com.rushteamc.RTMCPlugin.adminChat.adminChatMain;
import com.rushteamc.RTMCPlugin.sync.message.*;

public class syncListener extends Thread
{
	private ServerSocket connectionSocket;
	private Socket dataSocket;
	private boolean running = true;
	private int port;
	
	public syncListener(int port) throws IOException
	{
		setDaemon(true);
		this.port = port;
		connectionSocket = new ServerSocket(port);
		System.out.println("[RTMCPlugin][SYNC] Listening on port " + port );
	}
	
	public void kill()
	{
		running = false;
		try {
			dataSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(running)
		{
			try {
				dataSocket = connectionSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(dataSocket.getInputStream( ));
				message msg = (message)ois.readObject();
				dataSocket.close();
				switch(msg.type)
				{
				case CHAT_PUBLIC:
					publicChat pcm = (publicChat)msg;
					Bukkit.getServer().broadcastMessage( String.format(pcm.format,pcm.playerName,pcm.message) );
					break;
				case CHAT_ADMIN:
					adminChat amc = (adminChat)msg;
					adminChatMain.sendAdminChatMessage(amc.playerName, amc.message);
					break;
				case CHAT_ME:
					meChat mc = (meChat)msg;
					Bukkit.getServer().broadcastMessage( "* " + mc.playerName + " " + mc.message );
					break;
				case PLAYER_JOIN:
					playerJoin pj = (playerJoin)msg;
					Bukkit.getServer().broadcastMessage( pj.message );
					break;
				case PLAYER_LEAVE:
					playerLeave pl = (playerLeave)msg;
					Bukkit.getServer().broadcastMessage( pl.message );
					break;
				case PLAYER_KICK:
					playerKick pk = (playerKick)msg;
					Bukkit.getServer().broadcastMessage( pk.message );
					break;
				case PLAYER_DEATH:
					playerDeath pd = (playerDeath)msg;
					Bukkit.getServer().broadcastMessage( pd.message );
					break;
				default:
					System.out.println("[RTMCPlugin][SYNC] Received message with unknown type!");
				}
			} catch (IOException e) {
				try {
					if(connectionSocket != null)
					{
						try {
							connectionSocket.close();
						} catch (IOException e1) {}
						connectionSocket = null;
					}
					connectionSocket = new ServerSocket(port);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
