package standrews.Agonyaunt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/** This class is responsible for the main control loop
 * @author Jiachun Liu
 * @author Abigail Lowe
 * @author Teng Li
 * @author Patomporn Loungvara
 */
public class NotificationReceiverActivity extends Activity implements TextToSpeech.OnInitListener {
    // Progress Dialog
    private ProgressDialog pDialog;
    private SeekBar bar;
    private TextView counter, qTypeShow, qTypeText, qTypeBar, answerPrefix;
    private EditText answerText;

    // Manage SharePreference
    private SharedPreferences sharedPref;
    private String pid;
    private int ctlLv, age, set_slot, set_freq, frequency;
    private boolean gender;
    private int[] slots;

    // Manage Question
    private QuestionManager qManager;
    private SequenceQuestion seqQ;
    private ArrayList<Question> listQ;
    private ArrayList<String> listA;

    // Keep track of questions
    private int quesCount = 0;
    private Question curQuestion;

    // Speak
    private TextToSpeech tts;
    private boolean ttsInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll().penaltyLog().penaltyDeath().build());
//        }

        // set Intent
        Intent intent = getIntent();

        // Get Info
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        pid = sharedPref.getString(Util.KEY_PID, "");
        ctlLv = sharedPref.getInt(Util.KEY_CONTROL_LEVEL, 0);
        age = Integer.parseInt(sharedPref.getString(Util.KEY_AGE, "0"));
        gender = (sharedPref.getString(Util.KEY_GENDER, "").equals("0"));
        set_slot = sharedPref.getInt(Util.KEY_SET_SLOT, 0);
        set_freq = sharedPref.getInt(Util.KEY_SET_FREQ, 0);
        frequency = sharedPref.getInt(Util.KEY_FREQ, 0);
        slots = new int[Util.NUM_SLOTS];
        for (int i=0; i<slots.length; i++) {
            if (sharedPref.getBoolean(Util.KEY_CHECKBOX + i, false)) {
                slots[i] = 1;
            }
        }

        // Manage Question
        qManager = new QuestionManager(this.getResources());
        seqQ = new SequenceQuestion(this.getBaseContext(), ctlLv, age, gender, set_slot, set_freq);
        listQ = new ArrayList<Question>();
        listA = new ArrayList<String>();

        if (intent != null) {
            if (intent.getIntExtra(Util.KEY_COUNT, 0) > 0) {
                // Counter has been sent
                quesCount = intent.getIntExtra(Util.KEY_COUNT, 0);
            }
            if (intent.getIntExtra(Util.KEY_SEQ, 0) != 0) {
                // Counter has been sent
                seqQ.setSeq(intent.getIntExtra(Util.KEY_SEQ, 0));
            }
            if (intent.getSerializableExtra(Util.KEY_QUESTION) != null) {
                // Counter has been sent
                listQ = (ArrayList<Question>) intent.getSerializableExtra(Util.KEY_QUESTION);
            }
            if (intent.getSerializableExtra(Util.KEY_ANSWER) != null) {
                // Counter has been sent
                listA = (ArrayList<String>) intent.getSerializableExtra(Util.KEY_ANSWER);
            }
            seqQ.setQuestionList(listQ);
            seqQ.setAnswerList(listA);

            // Set Question
            if (quesCount<seqQ.getNumQ()) {
                // set Sequence
                if (quesCount == 1) {
                    //int seq = SelectRecommendation.selectSequence(this.getBaseContext(), seqQ.getPreCtlLv(), age, gender);
                    int seq = 5;
                    seqQ.setSeq(seq);
                }

                // get Current Question
                curQuestion = qManager.generateQuestion(this.getBaseContext(), ctlLv, age, gender, set_slot, set_freq, seqQ, quesCount);
                seqQ.setQuestion(curQuestion);

                if (curQuestion.getQuestionType().equals(questionType.CONTROL_LEVEL)) {
                    // Control Level

                    // Components
                    setContentView(R.layout.result_type_bar);
                    bar = (SeekBar) findViewById(R.id.rateBar);
                    counter = (TextView) findViewById(R.id.rateCounter);
                    setActionBar(false);

                    qTypeBar = (TextView) findViewById(R.id.questionBar);
                    qTypeBar.setText(curQuestion.getDefinedQuestion());

                    if (quesCount != 0) {
                        // set previous control level
                        bar.setProgress(seqQ.getPreCtlLv());
                    }

                } else if (curQuestion.getQuestionType().equals(questionType.CONVERSATION)) {
                    // Conversation
                    setContentView(R.layout.result_type_text);// Components
                    qTypeText = (TextView) findViewById(R.id.questionText);
                    qTypeText.setText(curQuestion.getDefinedQuestion());
                    answerPrefix = (TextView) findViewById(R.id.answerPrefixText);
                    answerPrefix.setText(curQuestion.getAnswerPrefix());
                    answerText = (EditText) findViewById(R.id.answer);

                } else if (curQuestion.getQuestionType().equals(questionType.SUMMARY)) {
                    // Summary
                    setContentView(R.layout.result_type_show);
                    // Components
                    qTypeShow = (TextView) findViewById(R.id.questionShow);
                    qTypeShow.setText(curQuestion.getDefinedQuestion());

                } else if (curQuestion.getQuestionType().equals(questionType.RATE_QUESTION) ||
                        curQuestion.getQuestionType().equals(questionType.RATE_FREQUENCY) ||
                        curQuestion.getQuestionType().equals(questionType.RATE_SLOTS)) {

                    // Rate Intervention
                    setContentView(R.layout.result_type_bar);
                    bar = (SeekBar) findViewById(R.id.rateBar);
                    counter = (TextView) findViewById(R.id.rateCounter);
                    setActionBar(true);

                    qTypeBar = (TextView) findViewById(R.id.questionBar);
                    qTypeBar.setText(curQuestion.getDefinedQuestion());
                }

                setSpeaker();
            }
        }

    }

    /** Gets the questions to ask the user
     * @param view	Android view
     * @throws IOException
     */
    // When click on the next button in question page
    public void nextQuestion(View view) throws IOException {
        //check internet connection
        if (Util.checkNetwork(this)) {
            boolean isNextQ = true;
            Intent intent = new Intent(this, NotificationReceiverActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            if (quesCount < seqQ.getNumQ()) {
                if (curQuestion.getQuestionType().equals(questionType.CONTROL_LEVEL)) {
                    // Control Level
                    if (bar.getProgress() == 0) {
                        isNextQ = false;
                    } else {
                        if (quesCount == 0) {
                            ctlLv = (bar.getProgress());
                            seqQ.setPreCtlLv(ctlLv);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(Util.KEY_CONTROL_LEVEL, seqQ.getPreCtlLv());
                            editor.apply();

                        } else {
                            seqQ.setPostCtlLv(bar.getProgress());
                            new recordSequence().execute();
                        }
                        seqQ.setAnswer(quesCount, String.valueOf(bar.getProgress()));
                    }

                } else if (curQuestion.getQuestionType().equals(questionType.CONVERSATION)) {
                    // Conversation
                    seqQ.setAnswer(quesCount, answerText.getText().toString());

                } else if (curQuestion.getQuestionType().equals(questionType.SUMMARY)) {
                    // Summary

                } else if (curQuestion.getQuestionType().equals(questionType.RATE_QUESTION)) {
                    // Rate Question
                    seqQ.setAnswer(quesCount, String.valueOf(bar.getProgress()));
                    new recordGroupQuestion().execute();

                } else if (curQuestion.getQuestionType().equals(questionType.RATE_FREQUENCY)) {
                    // Rate Intervention
                    seqQ.setAnswer(quesCount, String.valueOf(bar.getProgress()));
                    new recordFrequency().execute();

                } else if (curQuestion.getQuestionType().equals(questionType.RATE_SLOTS)) {
                    // Rate Intervention
                    seqQ.setAnswer(quesCount, String.valueOf(bar.getProgress()));
                    new recordSlots().execute();
                }

                if (isNextQ) {
                    quesCount++;
                } else {
                    Toast.makeText(this, "Please answer the question!", Toast.LENGTH_SHORT).show();
                }
            }

            if (quesCount == seqQ.getNumQ()) {
                // Show the finish information
                AlertDialog alertDialog = new AlertDialog.Builder(NotificationReceiverActivity.this).create();
                alertDialog.setTitle("App Information");
                alertDialog.setMessage("Thank you. Talk to you later.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationReceiverActivity.this.finish();
                    }
                });
                alertDialog.show();
            } else {
                // Next question
                if (isNextQ) {
                    intent.putExtra(Util.KEY_COUNT, quesCount);
                    intent.putExtra(Util.KEY_SEQ, seqQ.getSeq());
                    intent.putExtra(Util.KEY_QUESTION, seqQ.getQuestionList());
                    intent.putExtra(Util.KEY_ANSWER, seqQ.getAnswerList());
                    startActivity(intent);
                }
            }
        } else {
            Toast.makeText(NotificationReceiverActivity.this, "Please connect internet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setActionBar(final boolean allowZero) {
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int position, boolean fromUser) {
                if (!allowZero && position==0) {
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

    private void setSpeaker() {
        //Test if text-to-speech engine is installed in local machine
        if(isPackageInstalled(getPackageManager(), "com.svox.pico")){
            ttsInstalled = true; // This would be good to have it as a static member
        }
        Log.i("tts installed?", ttsInstalled+"");
        tts = new TextToSpeech(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Closes activity. Goes back to activity which launched it
        this.finish();
    }

    /**
     * Background Async Task to add Preference (Frequency) table record
     * */
    class recordFrequency extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(intent);
            pDialog = new ProgressDialog(NotificationReceiverActivity.this);
            pDialog.setMessage("Creating frequency table record..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating sub table record
         * */
        protected String doInBackground(String... args) {

            double rate = (double) bar.getProgress()/(Util.MAX_RATE-Util.MIN_RATE);
            manageInfo.recordFrequency(pid, ctlLv, frequency, rate);

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            pDialog.dismiss();
            Toast.makeText(NotificationReceiverActivity.this, "Frequency intervention has been saved", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * Background Async Task to add Preference (Slot) table record
     * */
    class recordSlots extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(intent);
            pDialog = new ProgressDialog(NotificationReceiverActivity.this);
            pDialog.setMessage("Creating slots table record..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating sub table record
         * */
        protected String doInBackground(String... args) {

            double rate = (double) bar.getProgress()/(Util.MAX_RATE-Util.MIN_RATE);
            manageInfo.recordSlot(pid, ctlLv, slots, rate);

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            pDialog.dismiss();
            Toast.makeText(NotificationReceiverActivity.this, "Slot intervention has been saved", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * Background Async Task to add Group Question table record
     * */
    class recordGroupQuestion extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(intent);
            pDialog = new ProgressDialog(NotificationReceiverActivity.this);
            pDialog.setMessage("Creating group question table record..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating sub table record
         * */
        protected String doInBackground(String... args) {

            double rate = (double) bar.getProgress()/(Util.MAX_RATE-Util.MIN_RATE);
            int prevG = curQuestion.getPrevGroup();
            int group = curQuestion.getGroup();
            manageInfo.recordGroupQuestion(pid, ctlLv, prevG, group, rate);

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            pDialog.dismiss();
            Toast.makeText(NotificationReceiverActivity.this, "Group question has been saved", Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * Background Async Task to add Sequence table record
     * */
    class recordSequence extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(intent);
            pDialog = new ProgressDialog(NotificationReceiverActivity.this);
            pDialog.setMessage("Creating sequence table record..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating sub table record
         * */
        protected String doInBackground(String... args) {

            double rate;
            if (seqQ.getPostCtlLv()<seqQ.getPreCtlLv()) {
                rate = ((double) seqQ.getPostCtlLv()-Util.MIN_CTRL /seqQ.getPreCtlLv()-Util.MIN_CTRL) * 0.5;
            } else {
                rate = 0.5;
                if (seqQ.getPreCtlLv()!=Util.MAX_CTRL) {
                    rate += ((((double) seqQ.getPostCtlLv()-seqQ.getPreCtlLv())/(Util.MAX_CTRL -seqQ.getPreCtlLv())) * 0.5);
                }
            }
            manageInfo.recordSequence(pid, ctlLv, seqQ.getSeq(), rate);

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done

            pDialog.dismiss();
            Toast.makeText(NotificationReceiverActivity.this, "Sequence has been saved", Toast.LENGTH_SHORT).show();
        }

    }

    // Code below is for Text to Speech
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setPitch(1f);
            tts.setSpeechRate(0.8f);
            int result = tts.setLanguage(new Locale("en", "GB"));

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                Log.i("Selected language support", "Yes support");
                String anything = "";
                speakOut(anything);
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut(String text) {
        Log.i("Speak Out", text);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public static boolean isPackageInstalled(PackageManager pm, String packageName) {
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public void clickSpeakShow(View view){
        speakOut(qTypeShow.getText().toString());
    }

    public void clickSpeakText(View view){
        speakOut(qTypeText.getText().toString());
    }

    public void clickSpeakBar(View view){
        speakOut(qTypeBar.getText().toString());
    }
}
