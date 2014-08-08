package com.example.agonyaunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** This class control user preferences
 * @author Jiachun Liu
 * @author Teng Li
 */
public class MyPreferences extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static String url_create_patient = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/create_patient.php";
    private static String url_add_to_slots = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/add_to_slots_table.php";

    private static final String TAG_SUCCESS = "success";


	public final static String checkBoxesKey = "checkBoxesKey";
	public final static int numCheckboxes = 6;
	CheckBox[] checkBoxes = new CheckBox[numCheckboxes];

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_preferences);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

		setupActionBar();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("User notification!");
        alertDialog.setMessage("An intervention frequency or slots will be given if you leave the slide bar at 0 or check boxes in blank. Of course, you can also set it as you want.");
        alertDialog.setButton("I got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();

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

		setupSeekBar(sharedPref.getInt("frequency", 0));


        Button btnSavePreferences = (Button) findViewById(R.id.btnSavePreferences);
        btnSavePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SeekBar bar = (SeekBar) findViewById(R.id.frequency_bar);
                int frequency = bar.getProgress();
                if (frequency>0){
                        new SaveFrequencyPreference().execute();
                }

                boolean slotsSelected = false;
                for (int i = 0; i < numCheckboxes; i++){
                    if (checkBoxes[i].isChecked()){
                        slotsSelected = true;
                    }
                }

                if (slotsSelected){
                        new SaveSlotsPreferences().execute();
                }


            }
        });

	}








    /**
     * Background Async Task to save preference slots
     * */
    class SaveSlotsPreferences extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyPreferences.this);
            pDialog.setMessage("Slots preference saving to the database..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating record
         * */
        protected String doInBackground(String... args) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MyPreferences.this);
            SharedPreferences.Editor editor = sharedPref.edit();



            for (int i = 0; i < numCheckboxes; i++) {
                String key = checkBoxesKey + i;
                editor.putBoolean(key, checkBoxes[i].isChecked());
            }
            editor.commit();



            String age = sharedPref.getString("userAge", "0");
            String occupation = sharedPref.getString("userOccupation", "Not set");

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("age", age));
            params.add(new BasicNameValuePair("occupation", occupation));

            String sexF = sharedPref.getString("sex0", "false");
            String sexM = sharedPref.getString("sex1", "false");
            if (sexF.equals("true")){
                params.add(new BasicNameValuePair("gender", "Female"));
            }
            else if (sexM.equals("true")){
                params.add(new BasicNameValuePair("gender", "Male"));
            }



            Boolean slot1 = sharedPref.getBoolean(checkBoxesKey+0, false);
            Boolean slot2 = sharedPref.getBoolean(checkBoxesKey+1, false);
            Boolean slot3 = sharedPref.getBoolean(checkBoxesKey+2, false);
            Boolean slot4 = sharedPref.getBoolean(checkBoxesKey+3, false);
            Boolean slot5 = sharedPref.getBoolean(checkBoxesKey+4, false);
            Boolean slot6 = sharedPref.getBoolean(checkBoxesKey+5, false);


            Log.w("Slots: ", age + " " + occupation + " " +" Fema " + sexF+  " Ma " + sexM + slot1 + " "+ slot2 + " "+ slot3 + " "+ slot4 + " "+ slot5 + " "+ slot6 + " ");
            if (slot1){
                params.add(new BasicNameValuePair("slot1", "1.0"));
            }else{
                params.add(new BasicNameValuePair("slot1", "0.0"));
            }


            if (slot2){
                params.add(new BasicNameValuePair("slot2", "1.0"));
            }else{
                params.add(new BasicNameValuePair("slot2", "0.0"));
            }

            if (slot3){
                params.add(new BasicNameValuePair("slot3", "1.0"));
            }else {
                params.add(new BasicNameValuePair("slot3", "0.0"));
            }

            if (slot4){
                params.add(new BasicNameValuePair("slot4", "1.0"));
            }else {
                params.add(new BasicNameValuePair("slot4", "0.0"));
            }

            if (slot5){
                params.add(new BasicNameValuePair("slot5", "1.0"));
            }else {
                params.add(new BasicNameValuePair("slot5", "0.0"));
            }

            if (slot6){
                params.add(new BasicNameValuePair("slot6", "1.0"));
            }else {
                params.add(new BasicNameValuePair("slot6", "0.0"));
            }








            // getting JSON Object
            // Note that create patient url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_to_slots, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created patient

                } else {
                    // failed to create patient
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            Toast.makeText(MyPreferences.this, "Slots preference has been saved", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
        }

    }















    /**
     * Background Async Task to save preference frequency
     * */
    class SaveFrequencyPreference extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyPreferences.this);
            pDialog.setMessage("Frequency preference saving to the database..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating record
         * */
        protected String doInBackground(String... args) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MyPreferences.this);
            SharedPreferences.Editor editor = sharedPref.edit();

            SeekBar bar = (SeekBar) findViewById(R.id.frequency_bar);
            editor.putInt("frequency", bar.getProgress());


            editor.commit();



            String name = sharedPref.getString("userName", "Not set");
            String age = sharedPref.getString("userAge", "0");
            String occupation = sharedPref.getString("userOccupation", "Not set");
            String interventionFrequency = sharedPref.getInt("frequency", 0) +"";



            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("age", age));
            params.add(new BasicNameValuePair("occupation", occupation));
            params.add(new BasicNameValuePair("interventionFrequency",interventionFrequency));

            String sexF = sharedPref.getString("sex0", "false");
            String sexM = sharedPref.getString("sex1", "false");
            if (sexF.equals("true")){
                params.add(new BasicNameValuePair("gender", "Female"));
            }
            else if (sexM.equals("true")){
                params.add(new BasicNameValuePair("gender", "Male"));
            }

            Log.w("DATA", "name " + name + " age " + age + " occupation " + occupation + " intervention fre " + interventionFrequency + " fe " + sexF + " ma " + sexM);

            // getting JSON Object
            // Note that create patient url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_patient, "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created patient

                } else {
                    // failed to create patient
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            Toast.makeText(MyPreferences.this, "Frequency preference has been saved", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();

        }

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
}
