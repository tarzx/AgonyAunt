package standrews.Agonyaunt;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;


/** This class represents the main menu
 * @author Jiachun Liu
 * @author Teng Li
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
//             StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
        }

		setContentView(R.layout.activity_home_menu);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(this.getResources().getString(R.string.tab_personal));
        tabSpec.setContent(R.id.tabPersonal);
        tabSpec.setIndicator(this.getResources().getString(R.string.tab_personal));
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

        if (Util.alarmBooted()) {
            // manage alarm
			mainAlarm();
		}

        Button btnTrainFrequencyInterventionNet = (Button) findViewById(R.id.btnTrainFrequencyInterventionNeuralNetwork);
        Button btnTrainSlotInterventionNet = (Button) findViewById(R.id.btnTrainSlotInterventionNeuralNetwork);
        Button btnTrainSelectSequenceNet = (Button) findViewById(R.id.btnTrainSelectSequenceNeuralNet);
        Button btnTrainSelectGoalNet = (Button) findViewById(R.id.btnTrainSelectGoalNeuralNetwork);
        Button btnTrainSelectBehaviourNet = (Button) findViewById(R.id.btnTrainSelectBehaviourNeuralNetworks);

        if (Util.checkNetwork(this)) {
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
                    showDialog();
                }
            });
            btnTrainSlotInterventionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainSelectSequenceNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainSelectGoalNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainSelectBehaviourNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }

	}

    private void showDialog() {
        // Show the error network
        AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("No Internet Connection!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
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
     * Background Async Task to train intervention frequency neural network
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
     * Background Async Task to train intervention slots neural network
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
     * Background Async Task to train first level question neural network
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
     * Background Async Task to train sub question neural network
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home_menu, menu);
		return true;
	}

	// Starts myProfile
	public void myProfile(View view) {
        if (Util.checkNetwork(this)) {
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        } else {
            showDialog();
        }
	}

	/** Starts a demo chat
	 * @param view	The Android view
	 */
	public void Intervention(View view) {
        if (Util.checkNetwork(this)) {
            Toast.makeText(this, "Notification has appeared! Click on it to begin",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotificationService.class);
            startService(intent);
        } else {
            showDialog();
        }
	}

    public void showAllPatients(View view){
        if (Util.checkNetwork(this)) {
            Intent intent = new Intent(this, AllPatients.class);
            startActivity(intent);
        } else {
            showDialog();
        }
    }

    public void addNewPatient(View view){
        if (Util.checkNetwork(this)) {
            Intent intent = new Intent(this, NewPatientActivity.class);
            startActivity(intent);
        } else {
            showDialog();
        }
    }

    public void updateFrequencyInterventionNet(View view){
        if (Util.checkNetwork(this)) {
            Util.loadNet(this, Util.FREQUENCY_INTERVENTION_NET_URL, Util.FREQUENCY_INTERVENTION_NET_EG);

            Toast.makeText(this, "Frequency intervention networks updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }
    }


    public void updateSlotInterventionNet(View view){
        if (Util.checkNetwork(this)) {
            Util.loadNet(this, Util.SLOT_INTERVENTION_NET_URL, Util.SLOT_INTERVENTION_NET_EG);

            Toast.makeText(this, "Slot intervention networks updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }
    }

    public void updateSelectSequenceNet(View view) {
        if (Util.checkNetwork(this)) {
            Util.loadNet(this, Util.SELECT_SEQUENCE_NET_URL, Util.SELECT_SEQUENCE_NET_EG);

            Toast.makeText(this, "Select sequence networks updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }
    }

    public void updateSelectGoalNet(View view){
        if (Util.checkNetwork(this)) {
            Util.loadNet(this, Util.SELECT_GOAL_NET_URL, Util.SELECT_GOAL_NET_EG);

            Toast.makeText(this, "Select group question goal networks updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }

    }

    public void updateSelectBehaviourNet(View view){
        if (Util.checkNetwork(this)) {
            Util.loadNet(this, Util.SELECT_BEHAVIOUR_NET_URL, Util.SELECT_BEHAVIOUR_NET_EG);

            Toast.makeText(this, "Select group question behaviour networks updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }

    }

	/** Manages alarm */
	public void mainAlarm() {
		Calendar cal_alarm = Calendar.getInstance();
		cal_alarm.set(Calendar.HOUR_OF_DAY, 0);
		cal_alarm.set(Calendar.MINUTE, 0);
		cal_alarm.set(Calendar.SECOND, 0);
		Intent intent = new Intent(this, Alarm.class);
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), 1000 * 60 * 60 * 24, pi);
	}
}
