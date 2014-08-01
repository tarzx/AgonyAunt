package com.example.agonyaunt;

import android.annotation.TargetApi;
import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.agonyaunt.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Teng Li on 09/07/2014.
 */

public class AllPatients extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> patientsList;

    // url to get all patients list
//    private static String url_all_patients = "http://10.0.2.2:8888/AndroidApp/get_all_patients.php";
    private static String url_all_patients = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/get_all_patients.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PATIENTS = "patients";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE = "age";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_OCCUPATION = "occupation";
    private static final String TAG_INTERFREQUENCY = "interventionFrequency";




    // patients JSONArray
    JSONArray patients = null;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients);

// StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

    // Hashmap for ListView
        patientsList = new ArrayList<HashMap<String, String>>();

        // Loading data in Background Thread
        new LoadAllPatients().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single patient
        // launching Edit Patient Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), EditPatientActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });


    }


    // Response from Edit Patient Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted patient
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    /**
     * Background Async Task to Load all patient by making HTTP Request
     * */
    class LoadAllPatients extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllPatients.this);
            pDialog.setMessage("Loading data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        /**
         * getting All patients from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_patients, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Patients: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // patients found
                    // Getting Array of Patients
                    patients = json.getJSONArray(TAG_PATIENTS);

                    // looping through All Patient
                    for (int i = 0; i < patients.length(); i++) {
                        JSONObject c = patients.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String age = c.getString(TAG_AGE);
                        String gender = c.getString(TAG_GENDER);
                        String occupation = c.getString(TAG_OCCUPATION);
                        String interventionFrequency = c.getString(TAG_INTERFREQUENCY);



                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_AGE, age);
                        map.put(TAG_GENDER, gender);
                        map.put(TAG_OCCUPATION, occupation);
                        map.put(TAG_INTERFREQUENCY, interventionFrequency);

                        // adding HashList to ArrayList
                        patientsList.add(map);
                    }
                } else {
                    // no patients found
                    // Launch Add New Patient Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewPatientActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all patients
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllPatients.this, patientsList,
                            R.layout.list_patient_item, new String[]{TAG_PID,
                            TAG_NAME, TAG_AGE, TAG_GENDER, TAG_OCCUPATION, TAG_INTERFREQUENCY},
                            new int[]{R.id.pid, R.id.textPatientName, R.id.textAge, R.id.textGender, R.id.textPatientOccupation, R.id.textInterventionFrequency}
                    );
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }


    }
}
