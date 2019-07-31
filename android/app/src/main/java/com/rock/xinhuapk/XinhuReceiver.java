package com.rock.xinhuapk;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lib.Rock;


public class XinhuReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action   = intent.getAction();
        Bundle bundle   = intent.getExtras();

        Rock.Toast(context, "xinumy："+action+"");

        if(action.equals(Xinhu.ACTION_OPENSERVER)){
           // Intent it	 	= new Intent(context, CoreService.class);
           // context.startService(it);
        }
    }
}
