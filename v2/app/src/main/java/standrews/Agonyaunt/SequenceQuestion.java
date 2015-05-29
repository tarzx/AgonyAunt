package standrews.Agonyaunt;

import java.util.ArrayList;
import android.content.Context;

/** This class handles Sequence of questions
 * @author Patomporn Loungvara
 */
public class SequenceQuestion {
    private Context context;
    private int age;
    private boolean isMale;
    private int set_slot;
    private int set_freq;
    private int seq;
    private int preCtlLv;
    private int postCtlLv;
    private ArrayList<Question> qList;
    private ArrayList<String> aList;
    private String target_thought = "";
    private String target_behaviour = "";
    private String target_goal = "";

    public SequenceQuestion(Context context, int ctlLv, int age, boolean isMale, int set_slot, int set_freq) {
        this.context = context;
        this.preCtlLv = ctlLv;
        this.age = age;
        this.isMale = isMale;
        this.set_slot = set_slot;
        this.set_freq = set_freq;
        this.qList = new ArrayList<Question>();
        this.aList = new ArrayList<String>();
    }

    public int getSeq() { return this.seq; }
    public int getPreCtlLv() { return this.preCtlLv; }
    public int getPostCtlLv() { return this.postCtlLv; }
    public int getNumQ() {
        int numQ = 3; //Control Level Question + Summary
        if (this.set_slot==0) numQ++;
        if (this.set_freq==0) numQ++;
        switch (this.seq) {
            case 1: numQ += 5; break;
            case 2: numQ += 7; break;
            case 3: numQ += 5; break;
            case 4: numQ += 5; break;
            case 5: numQ += 7; break;
            case 6: numQ += 6; break;
            case 7: numQ += 7; break;
            case 8: numQ += 7; break;
            case 0: numQ = Integer.MAX_VALUE;
            default: break;
        }
        return numQ;
    }
    public Question getQuestionId(int id) {
        Question q = null;
        if (id<this.qList.size()) {
            q = this.qList.get(id);
        }
        return q;
    }
    public ArrayList<Question> getQuestionList() {
        return this.qList;
    }
    public ArrayList<String> getAnswerList() {
        return this.aList;
    }

    public void setQuestionList(ArrayList<Question> list) {
        this.qList = list;
    }
    public void setAnswerList(ArrayList<String> list) {
        this.aList = list;
    }
    public void setSeq(int seq) {
        this.seq = seq;
    }
    public void setPreCtlLv(int ctlLv) {
        this.preCtlLv = ctlLv;
    }
    public void setPostCtlLv(int ctlLv) {
        this.postCtlLv = ctlLv;
    }
    public void setQuestion(Question q) {
        q.setId(qList.size());
        this.qList.add(q);
        this.aList.add("");
    }

    public void setAnswer(int id, String answer) {
        this.aList.set(id, answer);
        if (this.qList.get(id).getAnswerType()==answerType.THOUGHT) {
            this.target_thought = answer;
        } else if (this.qList.get(id).getAnswerType()==answerType.BEHAVIOUR) {
            this.target_behaviour = answer;
        } else if (this.qList.get(id).getAnswerType()==answerType.GOAL) {
            this.target_goal = answer;
        }
    }


    public String getThought() {
        String ans ="";
        for (int i=0; i<qList.size(); i++) {
            if (qList.get(i).getAnswerType()==answerType.THOUGHT) {
                ans = aList.get(i);
                break;
            }
        }
        return ans;
    }
    public String getBehaviour() {
        String ans ="";
        for (int i=0; i<qList.size(); i++) {
            if (qList.get(i).getAnswerType()==answerType.BEHAVIOUR) {
                ans = aList.get(i);
                break;
            }
        }
        return ans;
    }
    public String getGoal() {
        String ans ="";
        for (int i=0; i<qList.size(); i++) {
            if (qList.get(i).getAnswerType()==answerType.GOAL) {
                ans = aList.get(i);
                break;
            }
        }
        return ans;
    }
    public String getAnswerId(int id) {
        String a = null;
        if (id<this.aList.size()) {
            a = this.aList.get(id);
        }
        return a;
    }


}

