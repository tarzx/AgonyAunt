package com.example.agonyaunt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import org.w3c.dom.Text;

/** This class is responsible for the main control loop
 * @author Jiachun Liu
 * @author Abigail Lowe
 * @author Teng Li
 */
public class NotificationReceiverActivity extends Activity implements OnSeekBarChangeListener, TextToSpeech.OnInitListener{

	// Keep track of questions
	public static final String COUNT = "com.example.agonyaunt.COUNT";
	String[] answers;
	int questionId;
	int quesCount;
	String question;

    String selectedControl;

	QuestionManager quesManager;
	public final static String QUESMANAGER0 = NotificationService.QUESMANAGER;
	public static String quesMan;
	public final static String QUESTION = "com.example.agonyaunt.question";
	public final static String ANSWERS = "com.example.agonyaunt.answers";
	public final static String ACTIVITYID = "com.example.agonyaunt.activityId";
	public final static String CONTROLID = "com.example.agonyaunt.controlId";


	public final static String PREAMBLEIDS = "com.example.agonyaunt.preambleIds";
	public final static String QUESTIONID = "com.example.agonyaunt.questionid";
	public ArrayList<String> preambleIds = new ArrayList<String>();
	public ArrayList<String> questionID = new ArrayList<String>();
	Question nxtQ;

    double time1;



    private TextView question1, question2, question3;



    private TextToSpeech tts;
    private boolean ttsInstalled = false;


    String answerInFirstDepthConversation = "";


    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static String url_create_patient = "http://tl29.host.cs.st-andrews.ac.uk/AndroidApp/create_patient.php";

    private static final String TAG_SUCCESS = "success";

	@SuppressLint("NewApi")
	@Override
	// On creation, set up everything
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

//      Test if text-to-speech engine is installed in local machine
        if(isPackageInstalled(getPackageManager(), "com.svox.pico")){
            ttsInstalled = true; // This would be good to have it as a static member
        }

        Log.i("tts installed?", ttsInstalled+"");

//        if (ttsInstalled){
            tts = new TextToSpeech(this, this);
//        }


		quesMan = QUESMANAGER0;
		if (intent != null) {
			if (intent.getIntExtra(COUNT, 0) > 0) {
				// Counter has been sent
				quesCount = intent.getIntExtra(COUNT, 0);
			}
			if (intent.getSerializableExtra(quesMan) != null) {
				// Question manager has been sent
				quesManager = (QuestionManager) intent.getSerializableExtra(quesMan);
			}



			if (quesCount == 0) {
				setContentView(R.layout.results);
                time1 = System.currentTimeMillis();

                Button speakBtn1 = (Button) findViewById(R.id.btnSpeak);
			} else if (quesCount > 0 && quesCount < 3) {
				setContentView(R.layout.result2);
			} else {
				setContentView(R.layout.result3);
                question3 = (TextView) findViewById(R.id.question3);
			}



			if (quesCount > 0 && quesCount < 3 && (intent.getStringExtra(QUESTION) != null)) {
				question = intent.getStringExtra(QUESTION);
				question2 = (TextView) findViewById(R.id.question2);
				question2.setText(question);

			}
			// If this is the first feedback question.
			if (quesCount == 0) {
               question1 = (TextView) findViewById(R.id.question1);

				// Show the first control level question
				if (intent.getStringArrayExtra(ANSWERS) != null) {
					answers = intent.getStringArrayExtra(ANSWERS);
					RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
					RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
					RadioButton radio2 = (RadioButton) findViewById(R.id.radio2);
					RadioButton radio3 = (RadioButton) findViewById(R.id.radio3);
					RadioButton radio4 = (RadioButton) findViewById(R.id.radio4);
					radio0.setText(answers[0]);
					radio1.setText(answers[1]);
					radio2.setText(answers[2]);
					radio3.setText(answers[3]);
					radio4.setText(answers[4]);
				}
			}




			if (intent.getStringArrayListExtra(PREAMBLEIDS) != null) {
				preambleIds = intent.getStringArrayListExtra(PREAMBLEIDS);
			}
			if (intent.getStringArrayListExtra(QUESTIONID) != null) {
				questionID = intent.getStringArrayListExtra(QUESTIONID);
			}

		}

	}






	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int currPos = seekBar.getProgress();
		// Low
		if (currPos >= 0 && currPos < 50) {
			seekBar.setProgress(0);
			questionId = 0;
		}
		// Medium
		if (currPos >= 50 && currPos < 80) {
			seekBar.setProgress(50);
			questionId = 1;
		}
		// High
		if (currPos >= 80 && currPos < 100) {
			seekBar.setProgress(100);
			questionId = 2;
		}
	}




















	/** Gets the questions to ask the user
	 * @param view	Android view
	 * @throws IOException
	 */
//    When click on the next button in question page
	public void nextQuestion(View view) throws IOException {

		// Total questions, just return an int variable in QuestionManager class
		int totalQs = quesManager.getTotalQs();

		if (quesCount < totalQs) {
			Intent intent = new Intent(this, NotificationReceiverActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Ask the parent question
			if (quesCount == 0) {
//                The selected variable is a string which conclude a number from 1-5
				selectedControl = getSelected();

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("controlLevel", selectedControl).commit();

//                Preamble
//                preambleIds store the answer of first question
				preambleIds.add(selectedControl);
				intent.putExtra(PREAMBLEIDS, preambleIds);

//                Compute the response time and get the first level question from neural net
                double time2 = System.currentTimeMillis();
                double responseTime = time2 - time1;

                int conversationDepth = sharedPref.getInt("conversationDepth", 1);
                Log.w("Track the conversation depth--first time", conversationDepth +"");
                if (conversationDepth > 2 || conversationDepth < 1){
                    conversationDepth = 1;
                }
                Log.w("Track the conversation depth after if", conversationDepth +"");
                FirstLevelQuestionNet firstLevelQuestionNet = new FirstLevelQuestionNet(NotificationReceiverActivity.this);
                double[] input = {Double.parseDouble(selectedControl), responseTime/1000, conversationDepth};


//
                conversationDepth++;
                editor.putInt("conversationDepth", conversationDepth).commit();
                Log.w("Track the conversation depth after ++", conversationDepth +"");


                Log.w("selected and time ", ""+ input[0] + " " + input[1]);

                int index = firstLevelQuestionNet.computeFirstQuestion(input);
                Log.w("The index of question", index +"");
                index = index -1;
                Log.w("The minus 1 index of question", index +"");
                if (index > quesManager.getNUM_QUESTIONS()-1){
                    index = quesManager.getNUM_QUESTIONS()-1;
                }
                if (index < 0){
                    index = 0;
                }

                Log.w("The final index of question", index +"");
                Log.w("My test about selected and response time", selectedControl + " <-se && time-> " + responseTime);
//                Here to choose which group of question to ask first, the group need to decided by the
//                answer to the first question

				quesManager.setParent_index(index);
				// Ask it
				nxtQ = quesManager.getTherapeuticQ(index);
				quesManager.setRecPar(nxtQ);


                answerInFirstDepthConversation = sharedPref.getString("answerInFirstDepthConversation", "empty");
//
////                // Enhance if needed
                QuestionEnhancer questionEnhancer = new QuestionEnhancer(this);
//                    The answer for question is used by QuestionEnhancer
                nxtQ = questionEnhancer.receive(nxtQ, answerInFirstDepthConversation);



                Log.w("Answer in first depth conversation", answerInFirstDepthConversation);


				// Ask the sub question
			} else {

				EditText answer_text = (EditText) findViewById(R.id.answer);
				String answer = answer_text.getText().toString();

//                preambleIds store the answer for next question
				preambleIds.add(answer);
				intent.putExtra(PREAMBLEIDS, preambleIds);

				quesManager.getRecPar().setAnswer(answer);

				if (quesCount == 1) {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("answerInFirstDepthConversation", answer).commit();

//
                    Log.w("Answer in first depth conversation -> Q count = 1", answer);
                    SubQuestionNet subQuestionNet = new SubQuestionNet(NotificationReceiverActivity.this);

                    String uAge = sharedPref.getString("userAge", "0.0");
                    selectedControl = sharedPref.getString("controlLevel", "0.0");
                    Log.w("The selected control", selectedControl);
                    Log.w("The current age for sub question net", uAge);
                    double[] input = {Double.parseDouble(selectedControl), Double.parseDouble("0." + uAge)};
                    Log.w("Control level and age", " " + input[0] + " " + input[1]);

                    int subIndex = subQuestionNet.computeSubQuestion(input);



//                    compute the index of sub question and use Q manager to set the sub index
					// Get the parent
					Question parent = quesManager.getRecPar();
					// Get the next question

                    nxtQ = quesManager.getSubFromSubQuestionNet(parent, subIndex);



					// Enhance if needed
					QuestionEnhancer questionEnhancer = new QuestionEnhancer(this);
//                    The answer for question is used by QuestionEnhancer
					nxtQ = questionEnhancer.receive(nxtQ, answer);
                    quesManager.setSub_index(subIndex);
				}
				
				questionID.add("0");
				intent.putExtra(QUESTIONID, questionID);
			}
			quesCount++;
			intent.putExtra(COUNT, quesCount);
			intent.putExtra(quesMan, quesManager);
			intent.putExtra(QUESTION, quesManager.getRecPar().getContent());
			startActivity(intent);

		}

		// If final question, then get user's rating
		else if (quesCount == totalQs) {
			Intent intent = getIntent();
			SeekBar rate_bar = (SeekBar) findViewById(R.id.rate_bar);
			String user_rate = Integer.toString(rate_bar.getProgress());
			preambleIds.add(user_rate);
			// Update rate
			Rater rate = new Rater(this);
			rate.update(quesManager.getParent_index(), quesManager.getSub_index(), rate_bar.getProgress(), false);
			rate.getBest();
			intent.putExtra(PREAMBLEIDS, preambleIds);
			this.finish();
		}




	}
































	/** Finds out if an intervention should be sent
	 * @return true		If there should be an intervention sent
	 * @return false	If there should not be an intervention sent
	 */
	public boolean toSendIntervention() {

		int freqCount = 0;
		int freq = 3;

		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		freqCount = sharedPref.getInt((getString(R.string.freqCount)), 0);
		System.out.println("freqCount " + freqCount);

		if (freqCount < freq) {
			return true;
		}
		return false;
	}

	/** Gets available time
	 * @param time		The time
	 * @return nextTime	The next time
	 */
	public int checkAvailableTimes(int time) {

		int low = 0;
		int high = 20;
		int nextTime = time;

		if (time >= low && time < high) {
			System.out.println("busy tiiiimes!");
			nextTime += high;
		}
		return nextTime;
	}

	/** Get which radio button is selected
	 * @return	String representing which option was picked
	 */
	public String getSelected() {
		RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
		RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);
		RadioButton radio2 = (RadioButton) findViewById(R.id.radio2);
		RadioButton radio3 = (RadioButton) findViewById(R.id.radio3);
		RadioButton radio4 = (RadioButton) findViewById(R.id.radio4);

		if (radio0.isChecked()) {
			return "0.1";
		}

		if (radio1.isChecked()) {
			return "0.3";
		}

		if (radio2.isChecked()) {
			return "0.5";
		}

		if (radio3.isChecked()) {
			return "0.7";
		}

		if (radio4.isChecked()) {
			return "0.9";
		}

		return "0.0";
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
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {

        Log.i("System Out", text);

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

    public void clickSpeakBtn1(View view){
        speakOut(question1.getText().toString());
    }

    public void clickSpeakBtn2(View view){
        speakOut(question2.getText().toString());
    }

    public void clickSpeakBtn3(View view){
        speakOut(question3.getText().toString());
    }




}
