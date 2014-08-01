package com.example.agonyaunt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.agonyaunt.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teng Li on 09/07/2014.
 */

public class NewPatientActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    EditText patientName, patientAge, patientOccupation, patientInterFrequency;
    RadioButton rbMale, rbFemale;

    //    private static String url_create_patient = "http://10.0.2.2:8888/AndroidApp/create_patient.php";
    private static String url_create_patient = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/create_patient.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        patientName = (EditText) findViewById(R.id.newTextName);
        patientAge = (EditText) findViewById(R.id.newTextAge);
        patientOccupation = (EditText) findViewById(R.id.newTextOccupation);
        patientInterFrequency = (EditText) findViewById(R.id.newTextInterventionFrequency);
        rbMale = (RadioButton) findViewById(R.id.radioBtnNewMale);
        rbFemale = (RadioButton) findViewById(R.id.radioBtnNewFemale);


        Button btnCreatePatient = (Button) findViewById(R.id.btnCreatePatient);

        btnCreatePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating new patient in background thread
                new CreateNewPatient().execute();
            }
        });
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
            String name = patientName.getText().toString();
            String age = patientAge.getText().toString();
            String occupation = patientOccupation.getText().toString();
            String interventionFrequency = patientInterFrequency.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("age", age));
            params.add(new BasicNameValuePair("occupation", occupation));
            params.add(new BasicNameValuePair("interventionFrequency",interventionFrequency ));

            if (rbMale.isChecked()){
                params.add(new BasicNameValuePair("gender", "Male"));
            }
            else if (rbFemale.isChecked()){
                params.add(new BasicNameValuePair("gender", "Female"));
            }

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
                    Intent i = new Intent(getApplicationContext(), AllPatients.class);
                    startActivity(i);

                    // closing this screen
                    finish();
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
            pDialog.dismiss();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
