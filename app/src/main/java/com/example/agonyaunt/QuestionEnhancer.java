package com.example.agonyaunt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.content.Context;

/** This class changes question content
 * @author Abigail Lowe
 */
public class QuestionEnhancer {

	Context context;
	// Represent a new question
	Question new_question;
	// Store last answer
	String prev_answer;
	// Store all emotions
	ArrayList<String> emotions = new ArrayList<String>();
	int index;
	String other_replace = "";


	public QuestionEnhancer(Context context) {
		this.context = context;
	}

	/**  Get a question from the app
	 * @param question		The question to enhance
	 * @param answer		The answer to the previous question
	 * @return new_question	The enhanced question
	 */
	public Question receive(Question question, String answer) {
		// If there's content to replace
		if (question.getContent().contains("[x]")
				|| question.getContent().contains("[y]")) {
			if (answer != null) {
				answer = answer.replaceAll("\\s+","");
				consultEmotions();
				// Add the new answer to the list of emotions
				if (!emotions.contains(answer))
					emotions.add(answer);
				// Save
				new_question = question;
				prev_answer = answer;
				// We have to reword if the user didn't enter anything
				if (prev_answer.equals(""))
					prev_answer = "like that";
				// Replace part of the new question with the previous answer
				new_question.setContent(new_question.getContent().replace(
						"[x]", prev_answer));
				// Replace key words with other emotions user has expressed
				if (new_question.getContent().contains("[y]")) {
					
					// If there are emotions
					if (emotions.size() > 1) {
						index = (int) (Math.random() * ((emotions.size()-1) + 1));
						other_replace = emotions.get(index);
						if (other_replace.equals(prev_answer)) other_replace = "not";
					} else {
						// Fill in some dummy emotions
						if (answer.equals("sad"))
							other_replace = "scared";
						else
							other_replace = "sad";
					}
					if (other_replace.equals("")) other_replace = "not";
					// Replace
					new_question.setContent(new_question.getContent().replace(
							"[y]", other_replace));
				}
				updateEmotions();
				return new_question;
			}
		}
		return null;
	}

	/** Updates the file containing a user's emotions */
	public void updateEmotions() {
		FileOutputStream fs;
		try {
			fs = context.openFileOutput("user_emotions", Context.MODE_PRIVATE);
			PrintWriter ps = new PrintWriter(fs);
			// Write out all ArrayList
			for (int i = 0; i < emotions.size(); i++) {
				ps.write(emotions.get(i)+"\n");
			}
			ps.flush();
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/** Loads the file containing a user's emotions */
	public void consultEmotions() {

		File f = new File(context.getFilesDir(), "user_emotions");
		Scanner scan;
		try {
			f.createNewFile();
			scan = new Scanner(f);
			while (scan.hasNext()) {
				// Add to emotions ArrayList
				emotions.add(scan.next());
			}
			scan.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
