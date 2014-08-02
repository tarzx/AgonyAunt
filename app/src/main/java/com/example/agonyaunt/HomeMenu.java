package com.example.agonyaunt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** This class represents the main menu
 * @author Jiachun Liu
 * @author Teng Li
 */
public class HomeMenu extends Activity {

    public static final String TRAIN_INTERVENTION_FREQUENCY_NET_URL = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/neuralNetFactory/runInterventionFrequencyNet.php";
    public static final String INTERVENTION_FREQUENCY_NET_URL = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/neuralNetFactory/neuralNetIntervention.eg";
    public static final String TRAIN_FIRST_LEVEL_QUESTION_NET_URL = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/neuralNetFactory/runFirstLevelQuestionNet.php";
    public static final String FIRST_LEVEL_QUESTION_NET_URL = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/neuralNetFactory/neuralNetFirstLevelQuestion.eg";
    public static final String TRAIN_SUB_QUESTION_NET_URL = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/neuralNetFactory/runSubQuestionNet.php";


    private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

        tabSpec = tabHost.newTabSpec("firstLevelQuestion");
        tabSpec.setContent(R.id.tabFirstLevelQuestion);
        tabSpec.setIndicator("First Level");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("subQuestion");
        tabSpec.setContent(R.id.tabSubQuestion);
        tabSpec.setIndicator("Sub Level");
        tabHost.addTab(tabSpec);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

//        Util class used
		if (Util.alarmBooted()) {
//            manage alarm
			mainAlarm();
		}



        Button btnTrainInterventionFrequencyNet = (Button) findViewById(R.id.btnTrainInterventionFrequencyNeuralNetworks);
        btnTrainInterventionFrequencyNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TrainInterventionNeuralNet().execute();

            }
        });

        Button btnTrainFirstLevelQuestionNet = (Button) findViewById(R.id.btnTrainFirstLevelQuestionNeuralNetwork);
        btnTrainFirstLevelQuestionNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TrainFirstLevelQuestionNeuralNet().execute();
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

        protected void onProgressUpdate(Integer... progress){
            super.onProgressUpdate(String.valueOf(progress));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(progress[0]);
        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TRAIN_INTERVENTION_FREQUENCY_NET_URL);
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

        protected void onProgressUpdate(Integer... progress){
            super.onProgressUpdate(String.valueOf(progress));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(progress[0]);
        }


        /**
         * Invoke the training jar in server
         * */
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(TRAIN_FIRST_LEVEL_QUESTION_NET_URL);
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

	// Starts myPreferences
	public void preferences(View view) {
		Intent intent = new Intent(this, MyPreferences.class);
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
        Intent intent = new Intent(this, AllPatients.class);
        startActivity(intent);
    }

    public void addNewPatient(View view){
        Intent intent = new Intent(this, NewPatientActivity.class);
        startActivity(intent);
    }


    public void updateInterventionNet(View view){
        String fileName = "neuralNetIntervention.eg";

        URL url = null;
        try {
            url = new URL(INTERVENTION_FREQUENCY_NET_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            URLConnection connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File neuralNet = new File(this.getApplicationContext().getFilesDir(), fileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(neuralNet);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte buffer[] = new byte[1024];
        int dataSize;
        int loadedSize = 0;
        Log.w("My Track from here", "Intervention update");
        try {
            while ((dataSize = inputStream.read(buffer)) != -1) {
                loadedSize += dataSize;
                outputStream.write(buffer, 0, dataSize);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "Intervention frequency net updated!",
                Toast.LENGTH_SHORT).show();
    }



    public void updateFirstLevelQuestionNet(View view){
        String fileName = "neuralNetFirstLevelQuestion.eg";

        URL url = null;
        try {
            url = new URL(FIRST_LEVEL_QUESTION_NET_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            URLConnection connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File neuralNet = new File(this.getApplicationContext().getFilesDir(), fileName);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(neuralNet);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte buffer[] = new byte[1024];
        int dataSize;
        int loadedSize = 0;
        Log.w("My Track from here", "First level question update");
        try {
            while ((dataSize = inputStream.read(buffer)) != -1) {
                loadedSize += dataSize;
                outputStream.write(buffer, 0, dataSize);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "First level question net updated!",
                Toast.LENGTH_SHORT).show();

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
