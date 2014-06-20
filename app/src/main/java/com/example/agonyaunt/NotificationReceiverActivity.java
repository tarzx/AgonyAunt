package com.example.agonyaunt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
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
	String uname;
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
	// New neural net
	Net net = new Net(this);

    private TextView question1, question2, question3;



    private TextToSpeech tts;
    private boolean ttsInstalled = false;

	@SuppressLint("NewApi")
	@Override
	// On creation, set up everything
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

        if(isPackageInstalled(getPackageManager(), "com.svox.pico")){
            ttsInstalled = true; // This would be good to have it as a static member
        }

        Log.i("tts installed?", ttsInstalled+"");

        if (ttsInstalled){
            tts = new TextToSpeech(this, this);
        }


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
                Button speakBtn1 = (Button) findViewById(R.id.btnSpeak);
			} else if (quesCount < 3) {
				setContentView(R.layout.result2);
			} else {
				setContentView(R.layout.result3);
                question3 = (TextView) findViewById(R.id.question3);
			}
			if (quesCount < 3 && (intent.getStringExtra(QUESTION) != null)) {
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
		// Get user name
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        What is trying to do here???
		uname = sharedPref.getString("userName", "");
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
	public void nextQuestion(View view) throws IOException {

		// Total questions
		int totalQs = quesManager.getTotalQs();

		if (quesCount < totalQs) {
			Intent intent = new Intent(this, NotificationReceiverActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Ask the parent question
			if (quesCount == 0) {
//                The selected variable is a string which conclude a number from 1-5
				String selected = getSelected();
//                Preamble 前言 (Just a translation)
				preambleIds.add(selected);
				intent.putExtra(PREAMBLEIDS, preambleIds);
				// Get random parent
				int index = quesManager.getRandomIndex();
				quesManager.setParent_index(index);
				// Ask it
				nxtQ = quesManager.getTherapeuticQ(index);
				quesManager.setRecPar(nxtQ);

				// Ask the subquestion
			} else {

				EditText answer_text = (EditText) findViewById(R.id.answer);
				String answer = answer_text.getText().toString();
				preambleIds.add(answer);
				intent.putExtra(PREAMBLEIDS, preambleIds);
				quesManager.getRecPar().setAnswer(answer);

				if (quesCount == 1) {
					net.load(quesManager.getRecPar().getID());
					// Get the parent
					Question parent = quesManager.getRecPar();
					// Get the next question
					nxtQ = quesManager.getNextFromNet(net, parent);
					// Enhance if needed
					QuestionEnhancer qe = new QuestionEnhancer(this);
					nxtQ = qe.receive(nxtQ, answer);
					quesManager.setSub_index(net.getOutput());
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

		// If final question get user's rating
		else if (quesCount == totalQs) {
			Intent intent = getIntent();
			SeekBar rate_bar = (SeekBar) findViewById(R.id.rate_bar);
			String user_rate = Integer.toString(rate_bar.getProgress());
			preambleIds.add(user_rate);
			// Update rate
			Rater rate = new Rater(this);
			rate.update(quesManager.getParent_index(),
					quesManager.getSub_index(), rate_bar.getProgress(), false);
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
			return "1";
		}

		if (radio1.isChecked()) {
			return "2";
		}

		if (radio2.isChecked()) {
			return "3";
		}

		if (radio3.isChecked()) {
			return "4";
		}

		if (radio4.isChecked()) {
			return "5";
		}

		return "";
	}

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
