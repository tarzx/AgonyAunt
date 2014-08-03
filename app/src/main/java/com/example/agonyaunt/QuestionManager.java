package com.example.agonyaunt;

import java.io.Serializable;
import java.util.Random;

/** This class manages questions functionalities
 * @author Abigail Lowe
 * @author Jiachun Liu
 * @author Teng Li
 */
@SuppressWarnings("serial")
public class QuestionManager implements Serializable {

	// Total number of questions asked
	int totalQs = 3;
	// Number of bits needed to represent a qID
	final int qIDbits = 4;
	// Number of first level questions
	final int NUM_QUESTIONS = 7;
	// Number of subQuestions per parent question
	final int SUB_PER_Q = 2;
	// Array of subQuestions
	Question[][] subQuestions = new Question[NUM_QUESTIONS][SUB_PER_Q];
	// Therapeutic questions - one chosen at random each time
	public Question[] therapQuestions = new Question[NUM_QUESTIONS];
	// Store a parent question
	Question rec_par;
	// Indexes
	int parent_index;
	int sub_index;

	// Constructor
	public QuestionManager() {
		setUpQuestions();
	}

	/** Stores questions */
	public void setUpQuestions() {
        therapQuestions[0] = new Question(new double[] { 0, 0, 0, 0 },
                "What's your goal?", "", subQuestions[0]);
		subQuestions[0][0] = new Question(new double[] { 0 },
				"Why is that important?", "", therapQuestions[0], -1);
		subQuestions[0][1] = new Question(new double[] { 1 },
				"Is that sensible?", "", therapQuestions[0], -1);


        therapQuestions[1] = new Question(new double[] { 0, 0, 0, 1 },
                "How are you feeling?", "", subQuestions[1]);
		subQuestions[1][0] = new Question(new double[] { 0 }, "Why is that?",
				"", therapQuestions[1], -1);
		subQuestions[1][1] = new Question(new double[] { 1 },
				"How long have you felt that way?", "", therapQuestions[1], -1);



        therapQuestions[2] = new Question(new double[] { 0, 0, 1, 0 },
                "What's going through your mind?", "", subQuestions[2]);
		subQuestions[2][0] = new Question(new double[] { 0 },
				"Do you think about that a lot?", "", therapQuestions[2], -1);
		subQuestions[2][1] = new Question(new double[] { 1 },
				"Does that trouble you?", "", therapQuestions[2], -1);


        therapQuestions[3] = new Question(new double[] { 0, 0, 1, 1 },
                "Are you in charge of your destiny?", "", subQuestions[3]);
		subQuestions[3][0] = new Question(new double[] { 0 },
				"Do you think it's normal to feel that?", "",
				therapQuestions[3], -1);
		subQuestions[3][1] = new Question(new double[] { 1 },
				"Are you comfortable with that?", "", therapQuestions[3], -1);



		therapQuestions[4] = new Question(new double[] { 0, 1, 0, 0 },
				"How do you feel today?", "", subQuestions[4]);
		subQuestions[4][0] = new Question(new double[] { 0 },
				"Why do you feel [x]?", "", therapQuestions[4], -1);
		subQuestions[4][1] = new Question(new double[] { 1 },
				"Would you rather feel [x] or [y]?", "", therapQuestions[4],
				-1);


        therapQuestions[5] = new Question(new double[] { 0, 1, 0, 1 },
                "I know you felt [x] last time. How do you feel now?", "", subQuestions[5]);
        subQuestions[5][0] = new Question(new double[] { 0 },
                "Any reasons lead you feel [x]?", "", therapQuestions[5], -1);
        subQuestions[5][1] = new Question(new double[] { 1 },
                "Any thing special happened to you?", "", therapQuestions[5],
                -1);

        therapQuestions[6] = new Question(new double[] { 0, 1, 1, 0 },
                "Since last time you said you felt [x], what's in your mind this time?", "", subQuestions[6]);
        subQuestions[6][0] = new Question(new double[] { 0 },
                "You said [x]? Why that?", "", therapQuestions[6], -1);
        subQuestions[6][1] = new Question(new double[] { 1 },
                "Is that a common emotion to you?", "", therapQuestions[6],
                -1);

        
	}

	/** Updates the rating of a subQuestion
	 * @param parentQ	The index of the parent question
	 * @param subQIndex			The index of the sub question
	 * @param newRating	The new rating
	 */
	public void update(int parentQ, int subQIndex, int newRating) {
		subQuestions[parentQ][subQIndex].setRating(newRating);
	}

	


	/** Get a random index of a question
	 * @return index	random index
	 */
	public int getRandomIndex() {
		Random random = new Random();
		int index = random.nextInt(NUM_QUESTIONS - 1);
		return index;
	}
	
	/** Get the next question from the NN
	 * @param net		The ANN
	 * @param parent	The parent
	 * @return nxtQ		The next question
	 */
	public Question getNextFromNet(Net net, Question parent) {
		Question nxtQ = getNSub(parent, net.getOutput());
		setRecPar(nxtQ);
		return nxtQ;
	}


    /** Given a question and a particular index, get a subquestion
     * @param parent	The parent
     * @param index		Index of the subquestion
     * @return 			The sub question at that index
     */
    public Question getNSub(Question parent, int index) {
        return parent.getSubQuestions()[index];
    }

    public Question getSubFromSubQuestionNet(Question parent, int index){
        Question nxtQ = getNSub(parent, index);
        setRecPar(nxtQ);
        return nxtQ;
    }










	
	
	// Getters and Setters

	public void setRecPar(Question parent) {
        rec_par = parent;
	}

	public Question getRecPar() {
		return rec_par;
	}

	public Question[][] getSubQuestions() {
		return subQuestions;
	}

	public void setSubQuestions(Question[][] subQuestions) {
		this.subQuestions = subQuestions;
	}

	public Question[] getTherapQuestions() {
		return therapQuestions;
	}

	public void setTherapQuestions(Question[] therapQuestions) {
		this.therapQuestions = therapQuestions;
	}


	public int getQIDbits() {
		return qIDbits;
	}

	public int getOutputBits() {
		return subQuestions[0][0].getID().length;
	}

	public int getNUM_QUESTIONS() {
		return NUM_QUESTIONS;
	}

	public int getSUB_PER_Q() {
		return SUB_PER_Q;
	}

	public int getParent_index() {
		return parent_index;
	}

	public void setParent_index(int parent_index) {
		this.parent_index = parent_index;
	}

	public int getSub_index() {
		return sub_index;
	}

	public void setSub_index(int sub_index) {
		this.sub_index = sub_index;
	}
	
	public int getTotalQs() {
		return totalQs;
	}

	public Question getTherapeuticQ(int i) {
		return therapQuestions[i];
	}
}
