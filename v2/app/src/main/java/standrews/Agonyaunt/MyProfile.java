package standrews.Agonyaunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

/** This class represent myProfile
 * @author Jiachun Liu
 * @author Teng Li
 * @author Patomporn Loungvara
 */
public class MyProfile extends Activity {
    public final static int numCheckboxes = Util.NUM_SLOTS;

    // Progress Dialog
    private ProgressDialog pDialog;

    EditText patientName, patientAge;
    RadioButton rbMale, rbFemale;
    CheckBox[] checkBoxes = new CheckBox[numCheckboxes];
    SeekBar bar;
    TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
//        }

        setContentView(R.layout.activity_my_profile);

        // Fields
        patientName = (EditText) findViewById(R.id.MyUsername);
        patientAge = (EditText) findViewById(R.id.MyAge);
        rbMale = (RadioButton) findViewById(R.id.MyrbMale);
        rbFemale = (RadioButton) findViewById(R.id.MyrbFemale);
        bar = (SeekBar) findViewById(R.id.MyFrequencyBar);
        counter = (TextView) findViewById(R.id.MyFrequencyCounter);
        checkBoxes[0] = (CheckBox) findViewById(R.id.MyCheckBox0);
        checkBoxes[1] = (CheckBox) findViewById(R.id.MyCheckBox1);
        checkBoxes[2] = (CheckBox) findViewById(R.id.MyCheckBox2);
        checkBoxes[3] = (CheckBox) findViewById(R.id.MyCheckBox3);
        checkBoxes[4] = (CheckBox) findViewById(R.id.MyCheckBox4);
        checkBoxes[5] = (CheckBox) findViewById(R.id.MyCheckBox5);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Get user input
        String username = sharedPref.getString(Util.KEY_USERNAME, "");
        String age = sharedPref.getString(Util.KEY_AGE, "");
        String gender = sharedPref.getString(Util.KEY_GENDER, "");
        int set_slot = sharedPref.getInt(Util.KEY_SET_SLOT, 0);
        int set_freq = sharedPref.getInt(Util.KEY_SET_FREQ, 0);

        Log.i("System.out", "Username: " + username + " Age: " + age + " Gender: " + (gender.equals("0")?"Male":(gender.equals("1")?"Female":"")));

        // Update variables
        patientName.setText(username);
        patientAge.setText(age);
        rbMale.setChecked(gender.equals("0"));
        rbFemale.setChecked(gender.equals("1"));

        setupActionBar();
        if (set_freq==1) setupSeekBar(sharedPref.getInt(Util.KEY_FREQ, 0));
        if (set_slot==1) {
            for (int i = 0; i < numCheckboxes; i++) {
                String key = Util.KEY_CHECKBOX + i;
                // In get method of sharePref, the second is for default when this value is not existing.
                checkBoxes[i].setChecked(sharedPref.getBoolean(key, false));
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("User notification!");
        alertDialog.setMessage("An intervention frequency or slots will be given if you leave the slide bar at 0 or check boxes in blank. Of course, you can also set it as you want.");
        alertDialog.setButton("I got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile, menu);
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

    public void backButtonProfile(View view) {
        // Closes activity. Goes back to activity which launched it
        this.finish();
    }

    // Set up the {@link android.app.ActionBar}, if the API is available.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    /**
     * Creates the seek bar
     *
     * @param frequency Frequency of notification
     */
    private void setupSeekBar(int frequency) {
        bar.setProgress(frequency);
        counter.setText(Integer.toString(frequency));

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

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

    public void saveProfile(View view) {
        String userName = String.valueOf(patientName.getText());
        String userAge = String.valueOf(patientAge.getText());
        String gender = null;
        if (rbMale.isChecked()){
            gender = "0";
        } else if (rbFemale.isChecked()){
            gender = "1";
        }
        int frequency = bar.getProgress();
        int set_frequency = (int)Math.ceil(frequency/Util.NUM_FREQUENCY);
        int set_slot = 0;
        for (int i = 0; i < numCheckboxes; i++){
            if (checkBoxes[i].isChecked()){
                set_slot = 1;
                break;
            }
        }

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        // editor.put(Key, value)
        editor.putString(Util.KEY_USERNAME, userName);
        editor.putString(Util.KEY_AGE, userAge);
        editor.putString(Util.KEY_GENDER, gender);
        editor.putInt(Util.KEY_SET_FREQ, set_frequency);
        editor.putInt(Util.KEY_FREQ, frequency);
        editor.putInt(Util.KEY_SET_SLOT, set_slot);
        for (int i = 0; i < numCheckboxes; i++) {
            editor.putBoolean(Util.KEY_CHECKBOX + i, checkBoxes[i].isChecked());
        }
        editor.apply();

        //Toast.makeText(MyProfile.this, "Preference has been saved", Toast.LENGTH_SHORT).show();

        new SavePatientInfo().execute();

    }

    /**
     * Background Async Task to save patient info
     */
    class SavePatientInfo extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyProfile.this);
            pDialog.setMessage("Slots preference saving to the database..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating record
         */
        protected String doInBackground(String... args) {
            int success = 0;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MyProfile.this);

            String name = sharedPref.getString(Util.KEY_USERNAME, "");
            String age = sharedPref.getString(Util.KEY_AGE, "");
            String gender = sharedPref.getString(Util.KEY_GENDER, null);
            int set_frequency = sharedPref.getInt(Util.KEY_SET_FREQ, 0);
            int[] frequencies = new int[Util.NUM_FREQUENCY];
            for (int i=0; i<frequencies.length; i++) {
                if (i+1 == sharedPref.getInt(Util.KEY_FREQ, 0)) {
                    frequencies[i] = 1;
                }
            }
            int set_slot = sharedPref.getInt(Util.KEY_SET_SLOT, 0);
            int[] slots = new int[numCheckboxes];
            for (int i=0; i<slots.length; i++) {
                String key = Util.KEY_CHECKBOX + i;
                if (sharedPref.getBoolean(key, false)) {
                    slots[i] = 1;
                }
            }

            if (sharedPref.contains(Util.KEY_PID)) {
                //update existing
                String pid = sharedPref.getString(Util.KEY_PID, "");

                success = manageInfo.updatePatient(pid, name, age, gender);
                if (success == 1) {
                    success = manageInfo.updatePatientPreference(pid, set_slot, slots, set_frequency, frequencies);
                }

            } else {
                //create new
                success = manageInfo.createPatient(name, age, gender, set_slot, slots, set_frequency, frequencies);
                if (success != 0) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Util.KEY_PID, String.valueOf(success));
                    editor.apply();
                }
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            AlertDialog alertDialog = new AlertDialog.Builder(MyProfile.this).create();
            alertDialog.setTitle("User notification!");
            alertDialog.setMessage("Profile has been saved. Now restart the app to apply new settings.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }

}