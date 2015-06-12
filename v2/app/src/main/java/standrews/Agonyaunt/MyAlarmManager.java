package standrews.Agonyaunt;

import android.app.PendingIntent;
import android.app.AlarmManager;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** This class use to manage Alarm
 @author Patomporn Loungvara
 */
public class MyAlarmManager extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainAlarm();
        this.stopSelf();
    }

    /** Manages alarm */
    public void mainAlarm() {

        boolean[] slots = new boolean[Util.NUM_SLOTS];

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        int ctlLv = sharedPref.getInt(Util.KEY_CONTROL_LEVEL, 1);
        int age = Integer.parseInt(sharedPref.getString(Util.KEY_AGE, "0"));
        boolean gender = (sharedPref.getString(Util.KEY_GENDER, null).equals("0"));

        if (sharedPref.getInt(Util.KEY_SET_SLOT, 0) == 0) {
            ArrayList<Integer> slotsFromNeuralNet = SelectRecommendation.selectTimeSlot(this.getBaseContext(), ctlLv, age, gender);
            for (int i = 0; i < slots.length; i++){
                if (slotsFromNeuralNet.contains(i)){
                    slots[i] = true;
                    editor.putBoolean(Util.KEY_CHECKBOX + i, true);
                    editor.apply();

                }

                Log.w("from net to slots", slots[i] + "");
            }
        } else {
            for (int i=0; i<slots.length; i++) {
                slots[i] = (sharedPref.getBoolean(Util.KEY_CHECKBOX + i, false));
            }
        }

        // Here decide the intervention frequency
        // Frequency could be user's setting, if no setting, then use neural net suggestion.
        int frequency = sharedPref.getInt(Util.KEY_FREQ, 0);
        if(sharedPref.getInt(Util.KEY_SET_FREQ, 0) == 0){
            frequency = SelectRecommendation.selectFrequency(this.getBaseContext(), ctlLv, age, gender);
            editor.putInt(Util.KEY_FREQ, frequency);
            editor.apply();

            Log.w("Suggested frequency by inter net", frequency + "");
        }

        ArrayList<Integer> nextTimes = Util.calculateNextTimes(frequency, slots);

        for (int i = 0; i < nextTimes.size(); i++) {
            final int id = (int)Math.floor(System.currentTimeMillis()/(24*60*60*1000))*10 + i;
            final int hour = (int)Math.floor(nextTimes.get(i)/60)%24;
            final int min = nextTimes.get(i) - (hour*60);

            Calendar cal_alarm = Calendar.getInstance();
            cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
            cal_alarm.set(Calendar.MINUTE, min);
            cal_alarm.set(Calendar.SECOND, 0);

            if (cal_alarm.getTimeInMillis() > System.currentTimeMillis()) {
                android.app.AlarmManager am = (android.app.AlarmManager) this.getSystemService(this.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getBroadcast(this.getBaseContext(), id, new Intent(this, MyAlarm.class), PendingIntent.FLAG_CANCEL_CURRENT);
                am.set(android.app.AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pi);
                Log.i("Next time: ", id + " " + Util.getDate(cal_alarm.getTimeInMillis(), "dd/MM/yyyy HH:mm:ss.SSS"));
            } else {
                Log.i("Last time: ", Util.getDate(cal_alarm.getTimeInMillis(), "dd/MM/yyyy HH:mm:ss.SSS"));
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        editor.putString(Util.KEY_DATE, sdf.format(new Date()));
        editor.apply();

        Log.i("Cal Date", sdf.format(new Date()) + "");
    }

}
