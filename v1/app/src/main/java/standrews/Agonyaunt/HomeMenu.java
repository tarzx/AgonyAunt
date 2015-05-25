package standrews.Agonyaunt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("personal");

        tabSpec.setContent(R.id.tabPersonal);
        tabSpec.setIndicator("Personal");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("patients");
        tabSpec.setContent(R.id.tabAIPatients);
        tabSpec.setIndicator("AI Patients");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("interventionSlots");
        tabSpec.setContent(R.id.tabInterventionSlots);
        tabSpec.setIndicator("Slots");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Questions");
        tabSpec.setContent(R.id.tabQuestions);
        tabSpec.setIndicator("Questions");
        tabHost.addTab(tabSpec);

//        Util class used
        boolean mainAlarmTrigger = Util.alarmBooted();

		if (mainAlarmTrigger) {
//            manage alarm

			mainAlarm();
		}

        Button btnTrainInterventionFrequencyNet = (Button) findViewById(R.id.btnTrainInterventionFrequencyNeuralNetworks);
        Button btnTrainFirstLevelQuestionNet = (Button) findViewById(R.id.btnTrainFirstLevelQuestionNeuralNetwork);
        Button btnTrainSubQuestionNet = (Button) findViewById(R.id.btnTrainSubQuestionNeuralNetwork);
        Button btnTrainInterventionSlotsNet = (Button) findViewById(R.id.btnTrainInterventionSlotsNeuralNetwork);

        if (myClickHandler()) {
            btnTrainInterventionFrequencyNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainInterventionNeuralNet().execute();
                }
            });
            btnTrainFirstLevelQuestionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainFirstLevelQuestionNeuralNet().execute();
                }
            });
            btnTrainSubQuestionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainSubQuestionNeuralNet().execute();
                }
            });
            btnTrainInterventionSlotsNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new TrainInterventionSlotsNeuralNet().execute();
                }
            });
        } else {
            btnTrainInterventionFrequencyNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainFirstLevelQuestionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainSubQuestionNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
            btnTrainInterventionSlotsNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }

	}
    public boolean myClickHandler() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
    private void showDialog() {
    //            Show the error network
        AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage("No Internet Connection!");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }








    /**
     * Background Async Task to train intervention frequency neural network
     * */
    class TrainInterventionNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the intervention frequency neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

//        protected void onProgressUpdate(Integer... progress){
//            super.onProgressUpdate(Arrays.toString(progress));
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setProgress(progress[0]);
//        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(Util.TRAIN_INTERVENTION_FREQUENCY_NET_URL);
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    in.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Intervention frequency neural network training done!");
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
    class TrainInterventionSlotsNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the intervention slots neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

//        protected void onProgressUpdate(Integer... progress){
//            super.onProgressUpdate(String.valueOf(progress));
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setProgress(progress[0]);
//        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(Util.TRAIN_INTERVENTION_SLOTS_NET_URL);
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    in.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Intervention slots neural network training done!");
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
    class TrainFirstLevelQuestionNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the first level question neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

//        protected void onProgressUpdate(Integer... progress){
//            super.onProgressUpdate(String.valueOf(progress));
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setProgress(progress[0]);
//        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(Util.TRAIN_FIRST_LEVEL_QUESTION_NET_URL);
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    in.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("First level question neural network training done!");
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
    class TrainSubQuestionNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeMenu.this);
            pDialog.setMessage("Training the sub question neural network..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

//        protected void onProgressUpdate(Integer... progress){
//            super.onProgressUpdate(String.valueOf(progress));
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setProgress(progress[0]);
//        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(Util.TRAIN_SUB_QUESTION_NET_URL);
                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                    in.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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

//            Show the finish information
            AlertDialog alertDialog = new AlertDialog.Builder(HomeMenu.this).create();
            alertDialog.setTitle("Server Information");
            alertDialog.setMessage("Sub question neural network training done!");
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
        if (myClickHandler()) {
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        } else {
            showDialog();
        }
	}

	// Starts myPreferences
	public void preferences(View view) {
        if (myClickHandler()) {
            Intent intent = new Intent(this, MyPreferences.class);
            startActivity(intent);
        } else {
            showDialog();
        }
	}

	/** Starts a demo chat
	 * @param view	The Android view
	 */
	public void Intervention(View view) {
        if (myClickHandler()) {
            Toast.makeText(this, "Notification has appeared! Click on it to begin",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, NotificationService.class);
            startService(intent);
        } else {
            showDialog();
        }
	}


    public void showAllPatients(View view){
        if (myClickHandler()) {
            Intent intent = new Intent(this, AllPatients.class);
            startActivity(intent);
        } else {
            showDialog();
        }
    }

    public void addNewPatient(View view){
        if (myClickHandler()) {
            Intent intent = new Intent(this, NewPatientActivity.class);
            startActivity(intent);
        } else {
            showDialog();
        }
    }








    public void updateInterventionNet(View view){
        if (myClickHandler()) {
            Util.updateNetFile(this.getApplicationContext(), Util.INTERVENTION_FREQUENCY_NET_EG, Util.INTERVENTION_FREQUENCY_NET_URL);

            Toast.makeText(this, "Intervention frequency net updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }
    }






    public void updateInterventionSlotsNet(View view) {
        if (myClickHandler()) {
            Util.updateNetFile(this.getApplicationContext(), Util.INTERVENTION_SLOTS_NET_EG, Util.INTERVENTION_SLOTS_NET_URL);

            Toast.makeText(this, "Intervention slots net updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }
    }



    public void updateFirstLevelQuestionNet(View view){
        if (myClickHandler()) {
            Util.updateNetFile(this.getApplicationContext(), Util.FIRST_LEVEL_QUESTION_NET_EG, Util.FIRST_LEVEL_QUESTION_NET_URL);

            Toast.makeText(this, "First level question net updated!", Toast.LENGTH_SHORT).show();
        } else {
            showDialog();
        }

    }

    public void updateSubQuestionNet(View view){
        if (myClickHandler()) {
            Util.updateNetFile(this.getApplicationContext(), Util.SUB_QUESTION_NET_EG, Util.SUB_QUESTION_NET_URL);

            Toast.makeText(this, "Sub question net updated!", Toast.LENGTH_SHORT).show();
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
