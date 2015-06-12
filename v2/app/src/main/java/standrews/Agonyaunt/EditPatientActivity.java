package standrews.Agonyaunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** This class handles editing patient information
 * @author Teng
 * @author Patomporn Loungvara
 */
public class EditPatientActivity extends Activity {
    public final static int numCheckboxes = Util.NUM_SLOTS;

    EditText patientName, patientAge;
    Button btnUpdate, btnDelete;
    RadioButton rbMale, rbFemale;
    CheckBox[] checkBoxes = new CheckBox[numCheckboxes];
    SeekBar bar;
    TextView counter;

    String pid;
    String ctlLv;

    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
//        }

        setContentView(R.layout.activity_edit_patient);

        // save button
        btnUpdate = (Button) findViewById(R.id.btnUpdatePatient);
        btnDelete = (Button) findViewById(R.id.btnDeletePatient);

        // getting patient details from intent
        Intent i = getIntent();

        // getting patient id (pid) from intent
        pid = i.getStringExtra(Util.KEY_PID);
        Log.w("What is the value of pid", pid);

        // getting patient control Level from intent
        ctlLv = "1"; //by Default
        Log.w("What is the value of ctlLv", ctlLv);


        // Getting complete patient details in background thread
        new GetPatientDetails().execute();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String userPid = sharedPref.getString(Util.KEY_PID, "");

        if (userPid.equals(pid)) {
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        } else {
            if (Util.checkNetwork(this.getBaseContext())) {
                // save button click event
                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // starting background task to update patient
                        new UpdatePatientDetails().execute();
                    }
                });
            } else {
                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Toast.makeText(EditPatientActivity.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // Delete button click event
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // deleting patient in background thread
                    new DeletePatient().execute();
                }
            });
        }

    }



    /**
     * Background Async Task to Get complete patient details
     * */
    class GetPatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPatientActivity.this);
            pDialog.setMessage("Loading patient details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting patient details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    HashMap<String, String> patientInfo = manageInfo.getPatientInfo(pid, ctlLv);

                    if (patientInfo != null) {
                        Log.d("Track", "Load success");

                        // patient with this pid found
                        // Edit Text
                        patientName = (EditText) findViewById(R.id.EditNameText);
                        patientAge = (EditText) findViewById(R.id.EditAgeText);
                        rbMale = (RadioButton) findViewById(R.id.EditrbMale);
                        rbFemale = (RadioButton) findViewById(R.id.EditrbMale);
                        bar = (SeekBar) findViewById(R.id.EditFrequencyBar);
                        counter = (TextView) findViewById(R.id.EditFrequencyCounter);
                        checkBoxes[0] = (CheckBox) findViewById(R.id.EditCheckBox0);
                        checkBoxes[1] = (CheckBox) findViewById(R.id.EditCheckBox1);
                        checkBoxes[2] = (CheckBox) findViewById(R.id.EditCheckBox2);
                        checkBoxes[3] = (CheckBox) findViewById(R.id.EditCheckBox3);
                        checkBoxes[4] = (CheckBox) findViewById(R.id.EditCheckBox4);
                        checkBoxes[5] = (CheckBox) findViewById(R.id.EditCheckBox5);

                        // display patient data in EditText
                        patientName.setText(patientInfo.get(Util.TAG_NAME));
                        patientAge.setText(patientInfo.get(Util.TAG_AGE));
                        if (patientInfo.get(Util.TAG_GENDER).equals("0") ){
                            rbMale.setChecked(true);
                        } else if (patientInfo.get(Util.TAG_GENDER).equals("1")){
                            rbFemale.setChecked(true);
                        }

                        ArrayList<Integer> slots =Util.getSlots(patientInfo);
                        for (int i=0; i<checkBoxes.length; i++) {
                            checkBoxes[i].setChecked(slots.contains(i+1));
                        }

                        Integer frequency = Util.getFrequency(patientInfo);
                        bar.setProgress(frequency);
                        counter.setText(Integer.toString(frequency));

                        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar bar, int position, boolean fromUser) {
                                if (position==0) {
                                    counter.setText("");
                                } else {
                                    counter.setText(Integer.toString(position));
                                }
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
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save patient Details
     * */
    class UpdatePatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPatientActivity.this);
            pDialog.setMessage("Saving patient ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving patient
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = patientName.getText().toString();
            String age = patientAge.getText().toString();
            String gender = null;
            if (rbMale.isChecked()){
                gender = "0";
            } else if (rbFemale.isChecked()){
                gender ="1";
            }
            int set_frequency = 0;
            int[] frequencies = new int[Util.NUM_FREQUENCY];
            for (int i=0; i<frequencies.length; i++) {
                if (i+1 == bar.getProgress()) {
                    frequencies[i] = 1;
                    set_frequency = 1;
                }
            }
            Log.w("Frequency: ", String.valueOf(bar.getProgress()));

            int set_slot = 0;
            int[] slots = new int[numCheckboxes];
            for (int i=0; i<slots.length; i++) {
                if (checkBoxes[i].isChecked()) {
                    slots[i] = 1;
                    set_slot = 1;
                }
            }

            int success = manageInfo.updatePatient(pid, name, age, gender);
            if (success == 1) {
                success = manageInfo.updatePatientPreference(pid, set_slot, slots, set_frequency, frequencies);
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about patient update
                    setResult(100, i);
                    finish();
                }
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once patient updated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete patient
     * */
    class DeletePatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPatientActivity.this);
            pDialog.setMessage("Deleting patient...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting patient
         */
        protected String doInBackground(String... args) {
            int success = manageInfo.deletePatient(pid);
            if (success == 1) {
                // patient successfully deleted
                // notify previous activity by sending code 100
                Intent i = getIntent();
                // send result code 100 to notify about patient deletion
                setResult(100, i);
                finish();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once patient deleted
            pDialog.dismiss();

        }

    }
}
