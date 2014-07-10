package com.example.agonyaunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditPatientActivity extends Activity {

    EditText patientName, patientAge, patientOccupation;
    Button btnUpdate, btnDelete;
    RadioButton rbMale, rbFemale;

    String pid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();




    // single patient url
//    private static final String url_patient_details = "http://10.0.2.2:8888/AndroidApp/get_patient_details.php";
    private static final String url_patient_details = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/get_patient_details.php";

    // url to update patient
//    private static final String url_update_patient = "http://10.0.2.2:8888/AndroidApp/update_patient.php";
    private static final String url_update_patient = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/update_patient.php";

    // url to delete patient
//    private static final String url_delete_patient = "http://10.0.2.2:8888/AndroidApp/delete_patient.php";
    private static final String url_delete_patient = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/delete_patient.php";


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENT = "patient";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_OCCUPATION = "occupation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);


        // save button
        btnUpdate = (Button) findViewById(R.id.btnUpdatePatient);
        btnDelete = (Button) findViewById(R.id.btnDeletePatient);

        // getting patient details from intent
        Intent i = getIntent();

        // getting patient id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
        Log.w("What is the value of pid", pid);

        // Getting complete patient details in background thread
        new GetPatientDetails().execute();

        // save button click event
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update patient
                new UpdatePatientDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting patient in background thread
                new DeletePatient().execute();
            }
        });
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
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        Log.e("What is the value of pid",pid);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_PID, pid));



                        // getting patient details by making HTTP request
                        // Note that patient details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(url_patient_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Patient Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        Log.e("The success valuse is", success+"");
                        if (success == 1) {
                            // successfully received patient details
                            JSONArray patientObj = json.getJSONArray(TAG_PATIENT); // JSON Array

                            // get first patient object from JSON Array
                            JSONObject patient = patientObj.getJSONObject(0);

                            Log.e("Json object patient: ", patient.toString());

                            // patient with this pid found
                            // Edit Text
                            patientName = (EditText) findViewById(R.id.editTextName);
                            patientAge = (EditText) findViewById(R.id.editTextAge);
                            patientOccupation = (EditText) findViewById(R.id.editTextOccupation);
                            rbMale = (RadioButton) findViewById(R.id.radioBtnMale);
                            rbFemale = (RadioButton) findViewById(R.id.radioBtnFemale);

                            // display patient data in EditText
                            patientName.setText(patient.getString(TAG_NAME));
                            patientAge.setText(patient.getString(TAG_AGE));
                            patientOccupation.setText(patient.getString(TAG_OCCUPATION));

                            if (patient.getString(TAG_GENDER).equals("Male") ){
                                rbMale.setChecked(true);
                            }else if (patient.get(TAG_GENDER).equals("Female")){
                                rbFemale.setChecked(true);
                            }

                        }else{
                            // patient with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
            String occupation = patientOccupation.getText().toString();
            String gender = null;
            if (rbMale.isChecked()){
                gender = "Male";
            }else if (rbFemale.isChecked()){
                gender ="Female";
            }

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_AGE, age));
            params.add(new BasicNameValuePair(TAG_GENDER, gender));
            params.add(new BasicNameValuePair(TAG_OCCUPATION, occupation));

            // sending modified data through http request
            // Notice that update patient url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_patient,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about patient update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update patient
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
            // dismiss the dialog once patient uupdated
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

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting patient details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_patient, "POST", params);

                // check your log for json response
                Log.d("Delete patient", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // patient successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about patient deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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


//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            getMenuInflater().inflate(R.menu.edit_patient, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            // Handle action bar item clicks here. The action bar will
//            // automatically handle clicks on the Home/Up button, so long
//            // as you specify a parent activity in AndroidManifest.xml.
//            int id = item.getItemId();
//            if (id == R.id.action_settings) {
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
    }
}
