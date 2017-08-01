package com.example.koko.time.alarms;

/**
 * Created by koko on 2017/5/30.
 */

import com.example.android.directboot.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

/**
 * IntentService to set off an alarm.
 */
public class AlarmIntentService extends IntentService {

    public static final String ALARM_WENT_OFF_ACTION = AlarmIntentService.class.getName()
            + ".ALARM_WENT_OFF";


    public static final String KEY_ALARM_ID = "alarm_id";

    public static final String KEY_ALARM_MONTH = "alarm_month";

    public static final String KEY_ALARM_DATE = "alarm_date";

    public static final String KEY_ALARM_HOUR = "alarm_hour";

    public static final String KEY_ALARM_MINUTE = "alarm_minute";

    public AlarmIntentService() {
        super(AlarmIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        Alarm alarm = AlarmUtil.readAlarm(intent.getExtras());

        NotificationManager notificationManager = context
                .getSystemService(NotificationManager.class);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_fbe_notification)
                        .setCategory(Notification.CATEGORY_ALARM)
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setContentTitle(context.getString(R.string.alarm_went_off, alarm.hour,
                                alarm.minute));
        notificationManager.notify(alarm.id, builder.build());

        AlarmStorage alarmStorage = new AlarmStorage(context);
        alarmStorage.deleteAlarm(alarm);
        Intent wentOffIntent = new Intent(ALARM_WENT_OFF_ACTION);
        wentOffIntent.putExtras(AlarmUtil.writeAlarm(alarm));
        LocalBroadcastManager.getInstance(context).sendBroadcast(wentOffIntent);
    }
}