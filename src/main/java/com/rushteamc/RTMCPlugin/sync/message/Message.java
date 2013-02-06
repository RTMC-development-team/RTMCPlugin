package com.rushteamc.RTMCPlugin.sync.message;

import java.io.Serializable;

public interface Message extends Serializable
{
	public void execute();
}
