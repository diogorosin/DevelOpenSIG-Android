package br.com.developen.sig.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Jobs.scheduleJob(context);

    }

}