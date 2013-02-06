package com.rushteamc.RTMCPlugin.sync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import com.rushteamc.RTMCPlugin.RTMCPlugin;
import com.rushteamc.RTMCPlugin.sync.message.message;

public class syncMain
{
	//private int syncside = 0;
	public int syncReceiverPort;
	public int[] syncTransmitterPorts = new int[0];
	
	//private final static int PORT_0 = 1234;
	//private final static int PORT_1 = 1235;
	
	private syncListener listener;
	private Socket clientSocket;
	private ObjectOutputStream oos;
	
	public syncMain(RTMCPlugin main)
	{
		FileConfiguration config = main.getConfig();

		if(config.isInt("sync.ports.own"))
			syncReceiverPort = (int)config.get("sync.ports.own");

		if(config.isInt("sync.ports.others"))
			syncTransmitterPorts = new int[] {(int)config.get("sync.ports.others")};
		else if(config.isList("sync.ports.others"))
		{
			ArrayList list = (ArrayList)config.get("sync.ports.others");
			syncTransmitterPorts = new int[ list.size() ];
			for( int i = 0 ; i < list.size() ; i++ )
			{
				Object obj = list.get(i);
				if( obj instanceof Integer )
				{
					syncTransmitterPorts[i] = (int)obj;
				}
				else if( obj instanceof String )
				{
					syncTransmitterPorts[i] = Integer.parseInt((String)obj);
				}
				else
				{
					System.out.println("[RTMCPlugin][SYNC] Invalid class of type: " + list.get(i).getClass().getCanonicalName() );
				}
			}
		}
		
		/*
		if(config.isInt("sync.side"))
			syncside = (int)config.get("sync.side");
		
		if(syncside<0)
			syncside = 0;
		else if(syncside>1)
			syncside = 1;
		
		switch(syncside)
		{
		case 0:
			syncReceiverPort = PORT_0;
			syncTransmitterPort = PORT_1;
			break;
		case 1:
			syncReceiverPort = PORT_1;
			syncTransmitterPort = PORT_0;
		}
		*/
		
		try {
			listener = new syncListener(syncReceiverPort);
			listener.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		main.getServer().getPluginManager().registerEvents(new eventListener(this, config), main);

		try {
			openSocket();
			System.out.println("[RTMCPlugin][SYNC] Connected to port " + syncTransmitterPorts[0] );
		} catch (IOException e) {
			System.out.println("[RTMCPlugin][SYNC] Could not connect to other server!");
			try {
				if( oos != null)
				{
					oos.close();
					oos = null;
				}
				if( clientSocket != null)
				{
					clientSocket.close();
					clientSocket = null;
				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		
	}

	public void resetIn()
	{
		closeListener();
		try {
			listener = new syncListener(syncReceiverPort);
			listener.start();
		} catch (IOException e) {
			System.out.println("[RTMCPlugin][SYNC] Failed to reset input connection!");
			e.printStackTrace();
			return;
		}
		System.out.println("[RTMCPlugin][SYNC] Reseted input connection!");
	}

	public void resetOut()
	{
		try {
			openSocket();
		} catch (IOException e) {
			System.out.println("[RTMCPlugin][SYNC] Failed to reset output connection!");
			e.printStackTrace();
			return;
		}
		System.out.println("[RTMCPlugin][SYNC] Reseted output connection!");
	}
	
	public void unload()
	{
		System.out.println("[RTMCPlugin][SYNC] Clossing listening thread...");
		closeListener();
		System.out.println("[RTMCPlugin][SYNC] Closing writing ports...");
		closeSocket();
	}
	
	private void closeListener()
	{
		if(listener != null)
			listener.kill();
		try {
			if( oos != null)
				oos.close();
			if( clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			;
		}
		try {
			if( listener != null && listener.isAlive() )
			{
				listener.join();
				listener = null;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void closeSocket()
	{
		try {
			if( oos != null)
			{
				oos.close();
			}
		} catch (IOException e) {
			;
		}
		try {
			if( clientSocket != null)
			{
				clientSocket.close();
			}
		} catch (IOException e) {
			;
		}
		oos = null;
		clientSocket = null;
	}
	
	private void openSocket() throws IOException
	{
		closeSocket();
		System.out.println("[RTMCPlugin][SYNC] Connecting to port "+syncTransmitterPorts[0]+"...");
		InetAddress host = InetAddress.getByName("localhost");
		System.out.println("[RTMCPlugin][SYNC] Connecting to port "+syncTransmitterPorts[0]+"...");
		clientSocket = new Socket(host, syncTransmitterPorts[0]);
		System.out.println("[RTMCPlugin][SYNC] Connecting to port "+syncTransmitterPorts[0]+"...");
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
	}
	
	public void sendMessage(message message)
	{
		try {
			if(oos==null)
				openSocket();
			oos.writeObject( message );
			oos.flush();
		} catch (IOException e1) {
			try {
				openSocket();
				oos.writeObject( message );
				oos.flush();
			} catch (IOException e2) {
				System.out.println("[RTMCPlugin][SYNC] Could not connect to other server!");
			}
		}
	}
	
}
