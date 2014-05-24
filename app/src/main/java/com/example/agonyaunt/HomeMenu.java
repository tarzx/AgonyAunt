package com.example.agonyaunt;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/** This class represents the main menu
 * @author Jiachun Liu
 */
public class HomeMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_menu);
		if (Util.alarmBooted()) {
			mainAlarm();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_menu, menu);
		return true;
	}

	// Starts myProfile
	public void myProfile(View view) {
		Intent intent = new Intent(this, MyProfile.class);
		startActivity(intent);
	}

	// Starts myPreferences
	public void preferences(View view) {
		Intent intent = new Intent(this, MyPreferences.class);
		startActivity(intent);
	}

	/** Starts a demo chat
	 * @param view	The Android view
	 */
	public void Intervention(View view) {
		Toast.makeText(this, "Notification has appeared! Click on it to begin",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, NotificationService.class);
		startService(intent);
	}

	/** Manages alarm */
	public void mainAlarm() {
		Calendar cal_alarm = Calendar.getInstance();
		cal_alarm.set(Calendar.HOUR_OF_DAY, 0);
		cal_alarm.set(Calendar.MINUTE, 0);
		cal_alarm.set(Calendar.SECOND, 0);
		Intent intent = new Intent(this, Alarm.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(),
				1000 * 60 * 60 * 24, pi);
	}
}
