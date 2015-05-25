package standrews.Agonyaunt;

import java.io.Serializable;

/** This class represents a question
 * @author Abigail Lowe
 */
public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	// Question attributes
	double[] ID;
	String content;
	String answer;
	Question[] subQuestions;
	Question parent;
	int rating;

	/** A main question */
	public Question(double[] ID, String content, String answer, Question[] subQuestions) {
		this.ID = ID;
		this.content = content;
		this.answer = answer;
		this.subQuestions = subQuestions;
	}

	/** A sub question */
	public Question(double[] ID, String content, String answer, Question parent, int rating) {
		this.ID = ID;
		this.content = content;
		this.answer = answer;
		this.parent = parent;
		this.rating = rating;
	}





	// Getters & Setters
	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Question getParent() {
		return parent;
	}

	public void setParent(Question parent) {
		this.parent = parent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double[] getID() {
		return ID;
	}

//	public void setID(double[] iD) {
//		ID = iD;
//	}

//	public String getAnswer() {
//		return answer;
//	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Question[] getSubQuestions() {
		return subQuestions;
	}

//	public void setSubQuestions(Question[] subQuestions) {
//		this.subQuestions = subQuestions;
//	}

}
