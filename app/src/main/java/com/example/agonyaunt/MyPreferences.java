package com.example.agonyaunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/** This class control user preferences
 * @author Jiachun Liu
 */
public class MyPreferences extends Activity {

	public final static String checkBoxesKey = "checkBoxesKey";
	public final static int numCheckboxes = 6;
	CheckBox[] checkBoxes = new CheckBox[numCheckboxes];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_preferences);

		setupActionBar();

		checkBoxes[0] = (CheckBox) findViewById(R.id.checkBox0);
		checkBoxes[1] = (CheckBox) findViewById(R.id.CheckBox1);
		checkBoxes[2] = (CheckBox) findViewById(R.id.CheckBox2);
		checkBoxes[3] = (CheckBox) findViewById(R.id.CheckBox3);
		checkBoxes[4] = (CheckBox) findViewById(R.id.CheckBox4);
		checkBoxes[5] = (CheckBox) findViewById(R.id.CheckBox5);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		for (int i = 0; i < numCheckboxes; i++) {
			String key = checkBoxesKey + i;
//            In get method of sharePref, the second is for default when this value is not existing.
			checkBoxes[i].setChecked(sharedPref.getBoolean(key, false));
		}

		setupSeekBar(sharedPref.getInt("frequency", 1));

	}

	// Set up the {@link android.app.ActionBar}, if the API is available.
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void backButton(View view) {
		// Closes activity. Goes back to activity which launched it
		this.finish();
	}

	/** Saves the data
	 * @param view	The Android view
     *              click on the save button in my preference activity
	 */
	public void savePreference(View view) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();

		SeekBar bar = (SeekBar) findViewById(R.id.frequency_bar);
		editor.putInt("frequency", bar.getProgress());

		for (int i = 0; i < numCheckboxes; i++) {
			String key = checkBoxesKey + i;
			editor.putBoolean(key, checkBoxes[i].isChecked());
		}
		editor.commit();
		Toast.makeText(this, "Preference has been saved", Toast.LENGTH_SHORT).show();
	}

	/** Creates the seek bar
	 * @param frequency		Frequency of notification
	 */
	private void setupSeekBar(int frequency) {
		SeekBar bar = (SeekBar) findViewById(R.id.frequency_bar);
		final TextView counter = (TextView) findViewById(R.id.frequency_counter);

		bar.setProgress(frequency);
		counter.setText(Integer.toString(frequency));

		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar bar, int position, boolean fromUser) {
				counter.setText(Integer.toString(position));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

		});
	}
}
