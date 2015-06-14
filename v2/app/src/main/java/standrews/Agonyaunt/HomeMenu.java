package standrews.Agonyaunt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;


/** This class represents the main menu
 * @author Jiachun Liu
 * @author Teng Li
 * @author Patomporn Loungvara
 */
public class HomeMenu extends Activity {
    //private final int READ_TIMEOUT = 10000; /* milliseconds */
    //private final int CONNECTION_TIMEOUT = 15000; /* milliseconds */
    private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build());
             StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog().penaltyDeath().build());
        }

		setContentView(R.layout.activity_home_menu);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(this.getResources().getString(R.string.tab_Personal));
        tabSpec.setContent(R.id.tabPersonal);
        tabSpec.setIndicator(this.getResources().getString(R.string.tab_Personal));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(this.getResources().getString(R.string.tab_AIPatient));
        tabSpec.setContent(R.id.tabAIPatients);
        tabSpec.setIndicator(this.getResources().getString(R.string.tab_AIPatient));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(this.getResources().getString(R.string.tab_Train));
        tabSpec.setContent(R.id.tabTrain);
        tabSpec.setIndicator(this.getResources().getString(R.string.tab_Train));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(this.getResources().getString(R.string.tab_Update));
        tabSpec.setContent(R.id.tabUpdate);
        tabSpec.setIndicator(this.getResources().getString(R.string.tab_Update));
        tabHost.addTab(tabSpec);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String calDate = sharedPref.getString(Util.KEY_DATE, "00000000");
        if (Util.alarmBooted(calDate) && sharedPref.contains(Util.KEY_AGE) && sharedPref.contains(Util.KEY_GENDER)) {
            // manage alarm
            Log.i("Track", "start Alarm");
            Intent i = new Intent(this.getBaseContext(), MyAlarmManager.class);
            startService(i);
		}

        Button btnTrainFrequencyInterventionNet = (Button) findViewById(R.id.btnTrainFrequencyInterventionNeuralNetwork);
        Button btnTrainSlotInterventionNet = (Button) findViewById(R.id.btnTrainSlotInterventionNeuralNetwork);
        Button btnTrainSelectSequenceNet = (Button) findViewById(R.id.btnTrainSelectSequenceNeuralNet);
        Button btnTrainSelectGoalNet = (Button) findViewById(R.id.btnTrainSelectGoalNeuralNetwork);
        Button btnTrainSelectBehaviourNet = (Button) findViewById(R.id.btnTrainSelectBehaviourNeuralNetworks);

        if (Util.checkNetwork(this.getBaseContext())) {
            btnTrainFrequencyInterventionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainFrequencyInterventionNeuralNet().execute();
                }
            });
            btnTrainSlotInterventionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainSlotInterventionNeuralNet().execute();
                }
            });
            btnTrainSelectSequenceNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainSelectSequenceNeuralNet().execute();
                }
            });
            btnTrainSelectGoalNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainSelectGoalNeuralNet().execute();
                }
            });
            btnTrainSelectBehaviourNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainSelectBehaviourNeuralNet().execute();
                }
            });
        } else {
            btnTrainFrequencyInterventionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
            btnTrainSlotInterventionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
            btnTrainSelectSequenceNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
            btnTrainSelectGoalNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
            btnTrainSelectBehaviourNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
                }
            });
        }

	}


    /**
     * Background Async Task to train neural network
     * */
    class TrainFrequencyInterventionNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the frequency intervention neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            Util.trainNet(Util.TRAIN_FREQUENCY_INTERVENTION_NET_URL);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Frequency Intervention neural networks training done!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }

    /**
     * Background Async Task to train neural network
     * */
    class TrainSlotInterventionNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the slot intervention neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            Util.trainNet(Util.TRAIN_SLOT_INTERVENTION_NET_URL);
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Slot Intervention neural network training done!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }

    /**
     * Background Async Task to train neural network
     * */
    class TrainSelectSequenceNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the select sequence neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            Util.trainNet(Util.TRAIN_SELECT_SEQUENCE_NET_URL);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Select sequence neural network training done!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }


    /**
     * Background Async Task to train neural network
     * */
    class TrainSelectGoalNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the select group question goal neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            Util.trainNet(Util.TRAIN_SELECT_GOAL_NET_URL);
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Select group question goal neural network training done!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }


    /**
     * Background Async Task to train neural network
     * */
    class TrainSelectBehaviourNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the select group question behaviour neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            Util.trainNet(Util.TRAIN_SELECT_BEHAVIOUR_NET_URL);
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Select group question behaviour network training done!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }

    }

    /**
     * Background Async Task to update neural network
     * */
    class updateFrequencyInterventionNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            Util.loadNet(HomeMenu.this, Util.FREQUENCY_INTERVENTION_NET_URL, Util.FREQUENCY_INTERVENTION_NET_EG);
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Toast.makeText(HomeMenu.this, "Frequency intervention networks updated!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Background Async Task to update neural network
     * */
    class updateSlotInterventionNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            Util.loadNet(HomeMenu.this, Util.SLOT_INTERVENTION_NET_URL, Util.SLOT_INTERVENTION_NET_EG);
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Toast.makeText(HomeMenu.this, "Slot intervention networks updated!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Background Async Task to update neural network
     * */
    class updateSelectSequenceNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            Util.loadNet(HomeMenu.this, Util.SELECT_SEQUENCE_NET_URL, Util.SELECT_SEQUENCE_NET_EG);
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Toast.makeText(HomeMenu.this, "Select sequence networks updated!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Background Async Task to update neural network
     * */
    class updateSelectGoalNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            Util.loadNet(HomeMenu.this, Util.SELECT_GOAL_NET_URL, Util.SELECT_GOAL_NET_EG);
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Toast.makeText(HomeMenu.this, "Select group question goal networks updated!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Background Async Task to update neural network
     * */
    class updateSelectBehaviourNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            Util.loadNet(HomeMenu.this, Util.SELECT_BEHAVIOUR_NET_URL, Util.SELECT_BEHAVIOUR_NET_EG);
            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            Toast.makeText(HomeMenu.this, "Select group question behaviour networks updated!", Toast.LENGTH_SHORT).show();
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

    /** Starts a demo chat
     * @param view	The Android view
     */
    public void Intervention(View view) {
        Toast.makeText(this, "Notification has appeared! Click on it to begin",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    public void showAllPatients(View view){
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            Intent intent = new Intent(this, AllPatients.class);
            startActivity(intent);
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewPatient(View view){
        Intent intent = new Intent(this, NewPatientActivity.class);
        startActivity(intent);
    }

    public void updateFrequencyInterventionNet(View view){
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            new updateFrequencyInterventionNet().execute();
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateSlotInterventionNet(View view){
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            new updateSlotInterventionNet().execute();
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateSelectSequenceNet(View view) {
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            new updateSelectSequenceNet().execute();
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateSelectGoalNet(View view){
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            new updateSelectGoalNet().execute();
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }

    }
    public void updateSelectBehaviourNet(View view){
        //check internet connection
        if (Util.checkNetwork(this.getBaseContext())) {
            new updateSelectBehaviourNet().execute();
        } else {
            Toast.makeText(HomeMenu.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }

    }

}
