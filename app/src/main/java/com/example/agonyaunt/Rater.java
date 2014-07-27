package com.example.agonyaunt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import android.content.Context;
import android.os.Build;

/** This class manages user ratings
 * @author Abigail Lowe
 */

public class Rater {

	Context context;
	// For writing
	InputStream is;
	FileOutputStream fs;
	// Question manager
	QuestionManager quesMan = new QuestionManager();
	int qID_bits = quesMan.getQIDbits();
	int num_Qs = quesMan.getNUM_QUESTIONS();
	int sub_per_Q = quesMan.getSUB_PER_Q();
	Question[] mainQs, subQs;
	// Vars for getting the best next Q
	int best_rating = 0;
	int best_id;
	// To write out
	ArrayList<byte[]> for_net_list = new ArrayList<byte[]>();
	int[][] store;

	public Rater(Context context) {
		this.context = context;
		loadFile();
	}

	/** Gets the ratings file */
	public void loadFile() {
		// Open ratings
		File f = new File(context.getFilesDir(), "ratings_for_app");
		Scanner scan;

		try {
			f.createNewFile();
			scan = new Scanner(f);

			// Iterate through file
			int i = 0;
			int j = 0;
			int sub_index;
			int rating;
			// Get the questions -> question in the first level
			mainQs = quesMan.getTherapQuestions();

			while (scan.hasNext()) {
				// Save ratings
				sub_index = Integer.parseInt(scan.next());
				rating = Integer.parseInt(scan.next());

				update(i, sub_index, rating, true);
				j++;
				if (j > 0 && j % 2 == 0)
					i++;
			}
			scan.close();
			calculate();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Print question and ratings for debugging */
	public void calculate() {
		mainQs = quesMan.getTherapQuestions();
		for (int i = 0; i < mainQs.length; i++) {
			System.out.println(mainQs[i].getContent());
			subQs = mainQs[i].getSubQuestions();
			for (int j = 0; j < subQs.length; j++) {
				System.out.println(subQs[j].getID()[0] + "   "
						+ subQs[j].getRating());
			}
		}

	}

	/** Update the rating of a subquestion
	 * @param parent	Index of the parent
	 * @param sub		Index of the sub question
	 * @param new_rate	New rating
	 * @param loading	Whether we're just loading the questions
	 */
	public void update(int parent, int sub, int new_rate, boolean loading) {
		// If we aren't just loading from the file
		
			int old_rate = mainQs[parent].getSubQuestions()[sub].getRating();
			// If the question has not been asked for the first time 
			// (i.e. rating is not -1)
			// And we aren't just loading the questions
			if (!loading && old_rate >= 0) {
				// Get the average for the new rate
				new_rate = (new_rate + old_rate) / 2;
			}
			// Update
			quesMan.update(parent, sub, new_rate);
			mainQs[parent].getSubQuestions()[sub].setRating(new_rate);

	}

	/** Get best question paring
	 * @throws IOExeption
	 */
	public void getBest() throws IOException {
		calculate();
		mainQs = quesMan.getTherapQuestions();
		// Iterate through questions
		for (int i = 0; i < mainQs.length; i++) {
			best_rating = 0;
			// Get sub questions
			subQs = mainQs[i].getSubQuestions();
			// Compare their rating
			for (int j = 0; j < subQs.length; j++) {
				// Check that all subQs don't have the same rating
				if ((subQs[0].getRating()) == subQs[1].getRating() || (subQs[0].getRating() == -1 && subQs[1].getRating() == -1)) {
					// If they do, prefer a random one
					Random r = new Random();
					best_id = r.nextInt(subQs.length);
					best_rating = subQs[best_id].getRating();
				// If one of them is still unasked, prefer that one 
				} else if (subQs[0].getRating() == -1 || subQs[1].getRating() == -1) {
					if (subQs[1].getRating() == -1) {
						best_id = 1;
					}
					if (subQs[0].getRating() == -1) {
						best_id = 0;
					}
					best_rating = -1;
				}

				// If there's a clear winner, update
				else if (subQs[j].getRating() > best_rating) {
					best_id = j;
					best_rating = subQs[j].getRating();
				}
			}
			// Add the best rating to the file array
			add(i, mainQs[i].getID(), best_id);
		}
		// Send out via the transmitter
		if (Build.FINGERPRINT.startsWith("generic")){
			Transmitter trans = new Transmitter(quesMan);
			trans.output("ratings_for_net", for_net_list, context);
		}
		format_for_app();
	}

	/** Puts question info in right format for net 
	 * @param index	Question index
	 * @param id	Question id
	 * @param best	Best rated id
	 */
	public void add(int index, double[] id, int best) {
		byte[] for_net = new byte[qID_bits + 1];

		for (int i = 0; i < id.length; i++) {
			for_net[i] = Double.toString(id[i]).getBytes()[0];
		}
		for_net[qID_bits] = Integer.toString(best).getBytes()[0];
		for_net_list.add(for_net);
	}

	/* Get data in the right format for writing back to rating file */
	public void format_for_app() {
		store = new int[mainQs.length][sub_per_Q];
		for (int i = 0; i < mainQs.length; i++) {
			subQs = mainQs[i].getSubQuestions();
			for (int j = 0; j < subQs.length; j++) {
				store[i][j] = subQs[j].getRating();
			}
		}
		toapp();
	}

	/** Writes ratings back to file */
	public void toapp() {
		try {
			// Open
			fs = context.openFileOutput("ratings_for_app", Context.MODE_PRIVATE);
			PrintWriter ps = new PrintWriter(fs);
			// Write
			for (int i = 0; i < mainQs.length; i++) {
				for (int j = 0; j < sub_per_Q; j++) {
					if (j % 2 == 0)
						ps.write("0");
					else
						ps.write("1");
					ps.write(" " + store[i][j] + " ");

				}
			}
			ps.flush();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
