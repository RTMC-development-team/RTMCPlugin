package com.rushteamc.RTMCPlugin.sync;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rushteamc.RTMCPlugin.adminChat.adminChatMain;
import com.rushteamc.RTMCPlugin.sync.message.*;

public class syncListener extends Thread
{
	public static final String PIPE_PATH = "/dev/shm/RTMCPlugin/pipes/server_";

	private ServerSocket connectionSocket;
	private Socket dataSocket;
	private int port;

	private boolean running = true;
	private int pipeNum = 2;
	private File fd;
	private InputStream inputStream;
	private ObjectInputStream objectInputStream;
	
	public syncListener(int port) throws IOException
	{
		pipeNum = port;
		setDaemon(true);

		fd = new File(PIPE_PATH + pipeNum);
		if( !fd.mkdirs() )
			;

		if(fd.exists())
			fd.delete();
		/*while( fd.exists() )
		{
			pipeNum++;
			fd = new File(PIPE_PATH + pipeNum);
		}*/

		try
		{
			Process p=Runtime.getRuntime().exec("mkfifo --mode=666 " + PIPE_PATH + pipeNum);
			p.waitFor();
		}
		catch(InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void kill()
	{
		running = false;
		try { // Extremly ugly way to force FileInputStream(PIPE_PATH + pipeNum) to unblock... 
			OutputStream outputStream = new FileOutputStream(PIPE_PATH + pipeNum);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject( null );
		} catch(IOException e) {
			e.printStackTrace();
		}
		this.interrupt();
		if( fd.exists() )
			fd.delete();
	}
	
	public void run()
	{
		System.out.println("[RTMCPlugin][SYNC] Listening on pipe " + PIPE_PATH + pipeNum );
		while(running)
		{
			try {
				if(objectInputStream==null)
				{
					try {
						inputStream = new FileInputStream(PIPE_PATH + pipeNum);
						objectInputStream = new ObjectInputStream(inputStream);
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				message msg = (message)objectInputStream.readObject();
				switch(msg.type)
				{
				case CHAT_PUBLIC:
					publicChat pcm = (publicChat)msg;
					// Bukkit.getServer().broadcastMessage( String.format(pcm.format,pcm.playerName,pcm.message) );
					System.out.println("[RTMCPlugin][SYNC][listener] Sending message: " + String.format(pcm.format,pcm.playerName,pcm.message));
					String str = String.format(pcm.format,pcm.playerName,pcm.message);
					for(Player p : Bukkit.getOnlinePlayers()){
						p.sendMessage(str);
					}
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
				// System.out.println("[RTMCPlugin][SYNC] Cought IOException, trying to reconnect!");
				try {
					inputStream = new FileInputStream(PIPE_PATH + pipeNum);
					objectInputStream = new ObjectInputStream(inputStream);
				} catch(IOException e2) {
					e2.printStackTrace();
					running = false;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("[RTMCPlugin][SYNC][listener] Listener closed! Please reopen by typing \"/rtmcplugin sync reset\" in the console.");
		if(objectInputStream != null)
			try {objectInputStream.close();} catch (IOException e) {}
		if(inputStream != null)
			try {inputStream.close();} catch (IOException e) {}
		fd = new File(PIPE_PATH + pipeNum);
		if( fd.exists() )
			fd.delete();

	}
}
