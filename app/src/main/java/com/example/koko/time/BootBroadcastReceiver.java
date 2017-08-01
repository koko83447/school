package com.example.koko.time;

/**
 * Created by koko on 2017/5/30.
 */

import com.example.android.directboot.alarms.Alarm;
import com.example.android.directboot.alarms.AlarmStorage;
import com.example.android.directboot.alarms.AlarmUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.UserManagerCompat;
import android.util.Log;

/**
 * BroadcastReceiver that receives the following implicit broadcasts:
 * <ul>
 *     <li>Intent.ACTION_BOOT_COMPLETED</li>
 *     <li>Intent.ACTION_LOCKED_BOOT_COMPLETED</li>
 * </ul>
 *
 * To receive the Intent.ACTION_LOCKED_BOOT_COMPLETED broadcast, the receiver needs to have
 * <code>directBootAware="true"</code> property in the manifest.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean bootCompleted;
        String action = intent.getAction();
        Log.i(TAG, "Received action: " + action + ", user unlocked: " + UserManagerCompat
                .isUserUnlocked(context));
        if (BuildCompat.isAtLeastN()) {
            bootCompleted = Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action);
        } else {
            bootCompleted = Intent.ACTION_BOOT_COMPLETED.equals(action);
        }
        if (!bootCompleted) {
            return;
        }
        AlarmUtil util = new AlarmUtil(context);
        AlarmStorage alarmStorage = new AlarmStorage(context);
        for (Alarm alarm : alarmStorage.getAlarms()) {
            util.scheduleAlarm(alarm);
        }
    }
}
