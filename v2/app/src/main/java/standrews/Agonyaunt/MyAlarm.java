package standrews.Agonyaunt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.IBinder;
import android.preference.PreferenceManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

/** This class contains the main Alarm, which is used to trigger 
 the other alarm everyday at 00:00:00 
 @author Jiachun Liu
 @author Teng Li
 @author Patomporn Loungvara
 */
public class MyAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Util.alarmBooted(context)) {
            // manage alarm
            Log.i("Track", "start Alarm");
            Intent ami = new Intent(context, MyAlarmManager.class);
            context.startService(ami);
        }

        Log.d("Track", "Alarm Recieved!");
        Intent i = new Intent(context, NotificationService.class);
        context.startService(i);
    }

}
