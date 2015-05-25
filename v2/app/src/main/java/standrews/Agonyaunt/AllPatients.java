package standrews.Agonyaunt;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/** This class handles dates
 * @author Teng
 * @author Patomporn Loungvara
 */
public class AllPatients extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> patientsList;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
        }

        setContentView(R.layout.activity_all_patients);

        //HashMap for ListView
        patientsList = new ArrayList<HashMap<String, String>>();

        // Loading data in Background Thread
        new LoadAllPatients().execute();

        // Get listView
        ListView lv = getListView();

        // on selecting single patient
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
                in.putExtra(Util.KEY_PID, pid);

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
            patientsList = manageInfo.getAllPatientsInfo();

            if (patientsList == null) {
                Log.e("Track", " fail");

                // no patients found
                // Launch Add New Patient Activity
                Intent i = new Intent(getApplicationContext(),
                        NewPatientActivity.class);
                // Closing all previous activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
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
                            R.layout.list_patient_item,
                            new String[]{Util.TAG_PID, Util.TAG_NAME, Util.TAG_AGE, Util.TAG_GENDER},
                            new int[]{R.id.pid, R.id.listNameText, R.id.listAgeText, R.id.listGenderText}
                    );
                    // updating listView
                    setListAdapter(adapter);
                }
            });

        }


    }
}
