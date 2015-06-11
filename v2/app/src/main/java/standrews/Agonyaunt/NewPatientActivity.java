package standrews.Agonyaunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
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
import java.util.List;

/** This class handles New Patient
 * @author Teng
 * @author Patomporn Loungvara
 */
public class NewPatientActivity extends Activity {
    public final static int numCheckboxes = Util.NUM_SLOTS;

    // Progress Dialog
    private ProgressDialog pDialog;

    EditText patientName, patientAge;
    RadioButton rbMale, rbFemale;
    CheckBox[] checkBoxes = new CheckBox[numCheckboxes];
    SeekBar bar;
    TextView counter;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new_patient);
        super.onCreate(savedInstanceState);
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
//        }

        patientName = (EditText) findViewById(R.id.NewNameText);
        patientAge = (EditText) findViewById(R.id.NewAgeText);
        rbMale = (RadioButton) findViewById(R.id.NewrbMale);
        rbFemale = (RadioButton) findViewById(R.id.NewrbFemale);
        bar = (SeekBar) findViewById(R.id.NewFrequencyBar);
        counter = (TextView) findViewById(R.id.NewFrequencyCounter);
        checkBoxes[0] = (CheckBox) findViewById(R.id.NewCheckBox0);
        checkBoxes[1] = (CheckBox) findViewById(R.id.NewCheckBox1);
        checkBoxes[2] = (CheckBox) findViewById(R.id.NewCheckBox2);
        checkBoxes[3] = (CheckBox) findViewById(R.id.NewCheckBox3);
        checkBoxes[4] = (CheckBox) findViewById(R.id.NewCheckBox4);
        checkBoxes[5] = (CheckBox) findViewById(R.id.NewCheckBox5);
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


        Button btnCreatePatient = (Button) findViewById(R.id.btnCreatePatient);

        if (Util.checkNetwork(this)) {
            btnCreatePatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // creating new patient in background thread
                    new CreateNewPatient().execute();
                }
            });
        } else {
            btnCreatePatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NewPatientActivity.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    /**
     * Background Async Task to Create new patient
     * */
    class CreateNewPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPatientActivity.this);
            pDialog.setMessage("Creating Patient..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating patient
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

            int success = manageInfo.createPatient(name, age, gender, set_slot, slots, set_frequency, frequencies);

            if (success == 1) {
                // successfully created patient
                Intent i = new Intent(getApplicationContext(), AllPatients.class);
                startActivity(i);

                // closing this screen
                finish();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
