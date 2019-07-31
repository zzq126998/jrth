package com.lib;

import android.provider.Settings;
import android.util.Log;


public final class CLog{
	

	public static void debug(String msg)
	{
		if(A.DEBUG)Log.v(A.APPPAGE, msg);
	}
	
}