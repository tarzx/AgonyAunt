package standrews.Agonyaunt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/** This class contains the main Alarm, which is used to trigger 
 the other alarm everyday at 00:00:00 
 @author Jiachun Liu
 @author Teng Li
 @author Patomporn Loungvara
 */
public class Alarm extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mainAlarm();
		return super.onStartCommand(intent, flags, startId);
	}
	
	/** This method starts a daily alarm */
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
        int frequency = sharedPref.getInt("frequency", 0);
        if(sharedPref.getInt(Util.KEY_FREQ, 0) == 0){
            frequency = SelectRecommendation.selectFrequency(this.getBaseContext(), ctlLv, age, gender);
            editor.putInt(Util.KEY_FREQ, frequency);
            editor.apply();

            Log.w("Suggested frequency by inter net", frequency+"");
        }

		ArrayList<Integer> nextTimes = Util.calculateNextTimes(frequency, slots);

		Calendar cal_alarm = Calendar.getInstance();
		cal_alarm.set(Calendar.HOUR_OF_DAY, 0);
		cal_alarm.set(Calendar.MINUTE, 0);
		cal_alarm.set(Calendar.SECOND, 0);

		PendingIntent[] pi = new PendingIntent[nextTimes.size()];
		Intent intent = new Intent(this, NotificationService.class);
		for (int i = 0; i < nextTimes.size(); i++) {
			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			pi[i] = PendingIntent.getService(this, i, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis() + nextTimes.get(i) * 60 * 1000, pi[i]);
            Log.i("Next time: ", getDate(cal_alarm.getTimeInMillis() + nextTimes.get(i) * 60 * 1000, "dd/MM/yyyy hh:mm:ss.SSS"));
		}
	}



	/** Gets a date
	 * @param milliSeconds		Time in milliseconds
	 * @param dateFormat		The format to get the date in
	 * @return 					Date in specified format	
	 */
	public String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}
