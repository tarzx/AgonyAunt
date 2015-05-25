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
		boolean[] slots = new boolean[MyPreferences.numCheckboxes];
        double[] slotsFromNeuralNet;
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		for (int c = 0; c < MyPreferences.numCheckboxes; c++) {
			String key = MyPreferences.checkBoxesKey + c;
			slots[c] = sharedPref.getBoolean(key, false);
		}


        boolean b_temp = false;
        for (boolean slot : slots) {
            b_temp |= slot;
        }
        if (!b_temp) {
            InterventionSlotsNet interSlotsNet = new InterventionSlotsNet(Alarm.this);
            slotsFromNeuralNet = interSlotsNet.computeSlots();
            for (int i = 0; i < slots.length; i++){
                if (slotsFromNeuralNet[i] == 1){
                    slots[i] = true;


                }

                Log.w("from net to slots", slots[i] + "");
            }
        }


//        Here decide the intervention frequency
//        Frequency could be user's setting, if no setting, then use neural net suggestion.
        InterventionNet interNet = new InterventionNet(Alarm.this);

		int frequency = sharedPref.getInt("frequency", 0);
        if(frequency == 0){
            frequency = interNet.computeFrequency();
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
			System.out.println(getDate(cal_alarm.getTimeInMillis() + nextTimes.get(i) * 60 * 1000, "dd/MM/yyyy hh:mm:ss.SSS"));
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
