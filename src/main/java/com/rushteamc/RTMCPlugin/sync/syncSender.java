package com.rushteamc.RTMCPlugin.sync;

import java.io.*;
import java.util.ArrayList;

import com.rushteamc.RTMCPlugin.sync.message.Message;

public class syncSender extends Thread
{
	private Boolean running;
	private String filename;
	
	private OutputStream outputStream;
	private ObjectOutputStream objectOutputStream;
	
	private ArrayList<Message> objList = new ArrayList<Message>();
	
	public syncSender(String filename)
	{
		this.filename = filename;
	}
	
	public void kill()
	{
		running = false;
		this.interrupt();
		close();
	}

	public void sendMessage2(Message msg)
	{
		objList.add(msg);
	}
	
	private boolean open()
	{
		File fd = new File(filename);
		if(!fd.exists())
			return false;
		try {
			outputStream = new FileOutputStream(fd);
			objectOutputStream = new ObjectOutputStream(outputStream);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void close()
	{
		if(objectOutputStream!=null)
			try {
					objectOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		if(objectOutputStream!=null)
			try {
					objectOutputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		objectOutputStream = null;
		outputStream = null;
	}
	
	public void run()
	{
		System.out.println("[RTMCPlugin][SYNC] Trying to send to: " + filename);
		running = true;
		while (running)
		{
			while (!objList.isEmpty())
			{
				if(objectOutputStream==null)
					if(!open())
					{
						System.out.println("[RTMCPlugin][SYNC] Could not send connect to other server! Deleting pending messages from queue..." );
						objList.clear();
						break;
					}
					else
					{
						System.out.println("[RTMCPlugin][SYNC] Connected to: " + filename);
					}
				try {
					//System.out.println("Sending messages..."); 
					// message msg = objList.get(0);
					Message msg = objList.get(0);
					objectOutputStream.writeObject(msg);
					objList.remove(msg);
				} catch (IOException e) {
					e.printStackTrace();
					close();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				running = false;
			}
		}
		System.out.println("[RTMCPlugin][SYNC] Data sender for " + filename + " died! Please reset the plugin or type \"/rtmcplugin sync reset\" in the console to try reconnect." );
	}
	
}
