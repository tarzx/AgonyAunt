package standrews.Agonyaunt;

import android.content.Context;
import android.content.res.Resources;

import java.util.Random;

/** This class handles Group of Questions
 * @author Patomporn Loungvara
 */
public class QuestionManager {
    private Resources res;
    private Random rand;

    private String[] echo;
    private String[] change_topic;
    private String change_topic_default, link, target_goal, target_thought, likeChoice;

    public QuestionManager(Resources res) {
        this.res = res;
        this.rand = new Random();

        echo = res.getStringArray(R.array.echo);
        change_topic = res.getStringArray(R.array.change_topic);
        change_topic_default = res.getString(R.string.change_topic_default);
        link = res.getString(R.string.link);
        target_thought = res.getString(R.string.target_thought);
        target_goal = res.getString(R.string.target_goal);
        likeChoice = res.getString(R.string.likeChoice);
    }

    private Question getPreControl() {
        Question q = new Question(questionType.CONTROL_LEVEL, answerType.CONTROL, 0, res.getString(R.string.preControl) + res.getString(R.string.controlChoice));
        return q;
    }
    private Question getPostControl() {
        Question q = new Question(questionType.CONTROL_LEVEL, answerType.CONTROL, 0, res.getString(R.string.postControl) + res.getString(R.string.controlChoice));
        return q;
    }

    private Question getQuestionG1(int prevGroup) {
        String[] g1 = res.getStringArray(R.array.g1);
        String[] g1_prefix = res.getStringArray(R.array.g1_prefix);

        int n = getRandom(g1.length);
        Question q = new Question(questionType.CONVERSATION, answerType.THOUGHT, 10, g1[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g1_prefix[n]);
        return q;
    }

    private Question getQuestionG2(int prevGroup, String prevAns, String echoAns, boolean isEcho) {
        String[] g2 = res.getStringArray(R.array.g2);
        String g2_pattern = res.getString(R.string.g2_pattern);
        String g2_default = res.getString(R.string.g2_default);

        int n = getRandom(g2.length);
        Question q = new Question(questionType.CONVERSATION, answerType.ANSWER, 20, g2[n]);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setPrevAnswer(prevAns);
        q.setPattern(g2_pattern);
        q.setDefaultPattern(g2_default);
        q.setLink(link);
        return q;
    }

    private Question getQuestionG3(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho, boolean isAmbiguious) {
        String[] g3 = res.getStringArray(R.array.g3);
        String[] g3_prefix = res.getStringArray(R.array.g3_prefix);

        int n = getRandom(g3.length);
        Question q = new Question(questionType.CONVERSATION, answerType.GOAL, 30, g3[n]);
        q.setIsAmbiguous(isAmbiguious);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g3_prefix[n]);
        q.setIsChangeTopic(isChangeTopic);
        q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
        q.setDefaultTopic(change_topic_default);
        q.setTarget(target_goal);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setPrevAnswer(prevAns);
        q.setLink(link);
        return q;
    }

    private Question getQuestionG4(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho, boolean isAmbiguious) {
        String[] g4 = res.getStringArray(R.array.g4);
        String[] g4_prefix = res.getStringArray(R.array.g4_prefix);

        int n = getRandom(g4.length);
        Question q = new Question(questionType.CONVERSATION, answerType.GOAL, 40, g4[n]);
        q.setIsAmbiguous(isAmbiguious);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g4_prefix[n]);
        q.setIsChangeTopic(isChangeTopic);
        q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
        q.setDefaultTopic(change_topic_default);
        q.setTarget(target_goal);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setPrevAnswer(prevAns);
        q.setLink(link);
        return q;
    }

    private Question getQuestionG5(int prevGroup, String echoAns, boolean isEcho) {
        String[] g5 = res.getStringArray(R.array.g5);

        int n = getRandom(g5.length);
        Question q = new Question(questionType.CONVERSATION, answerType.BEHAVIOUR, 50, g5[n]);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);
        return q;
    }

    private Question getQuestionG6(int prevGroup) {
        String[] g61 = res.getStringArray(R.array.g61);
        String[] g61_prefix = res.getStringArray(R.array.g61_prefix);
        String[] g62 = res.getStringArray(R.array.g62);
        String g62_pattern = res.getString(R.string.g62_pattern);
        String g62_default = res.getString(R.string.g62_default);

        int n = getRandom(g61.length);
        Question q = new Question(questionType.CONVERSATION, answerType.THOUGHT, 61, g61[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g61_prefix[n]);

        Question qs = new Question(questionType.CONVERSATION, answerType.ANSWER, 62, g62[n]);
        qs.setPattern(g62_pattern);
        qs.setDefaultPattern(g62_default);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getQuestionG7(int prevGroup, String prevAns, boolean isChangeTopic) {
        String[] g71 = res.getStringArray(R.array.g71);
        String[] g71_prefix = res.getStringArray(R.array.g71_prefix);
        String[] g72 = res.getStringArray(R.array.g72);

        int n = getRandom(g71.length);
        Question q = new Question(questionType.CONVERSATION, answerType.THOUGHT, 71, g71[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g71_prefix[n]);
        q.setIsChangeTopic(isChangeTopic);
        q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
        q.setDefaultTopic(change_topic_default);
        q.setTarget(target_thought);
        q.setPrevAnswer(prevAns);

        Question qs = new Question(questionType.CONVERSATION, answerType.ANSWER, 72, g72[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getQuestionG8(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho) {
        String[] g81 = res.getStringArray(R.array.g81);
        String[] g81_prefix = res.getStringArray(R.array.g81_prefix);
        String[] g82 = res.getStringArray(R.array.g82);

        int n = getRandom(g81.length);
        Question q = new Question(questionType.CONVERSATION, answerType.GOAL, 81, g81[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g81_prefix[n]);
        q.setIsChangeTopic(isChangeTopic);
        q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
        q.setDefaultTopic(change_topic_default);
        q.setTarget(target_goal);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setPrevAnswer(prevAns);
        q.setLink(link);

        Question qs = new Question(questionType.CONVERSATION, answerType.ANSWER, 82, g82[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getQuestionG9(int prevGroup) {
        String[] g91 = res.getStringArray(R.array.g91);
        String[] g91_prefix = res.getStringArray(R.array.g91_prefix);
        String[] g92 = res.getStringArray(R.array.g92);

        int n = getRandom(g91.length);
        Question q = new Question(questionType.CONVERSATION, answerType.THOUGHT, 91, g91[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g91_prefix[n]);

        Question qs = new Question(questionType.CONVERSATION, answerType.BEHAVIOUR, 92, g92[n]);
        qs.setEcho(echo[getRandom(echo.length)]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getQuestionG10(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
        String[] g101 = res.getStringArray(R.array.g101);
        String[] g102 = res.getStringArray(R.array.g102);

        int n = getRandom(g101.length);
        Question q = new Question(questionType.CONVERSATION, answerType.BEHAVIOUR, 101, g101[n]);
        q.setIsAmbiguous(isAmbiguous);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);

        Question qs = new Question(questionType.CONVERSATION, answerType.ANSWER, 102, g102[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getQuestionG11(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
        String[] g111 = res.getStringArray(R.array.g111);
        String[] g112 = res.getStringArray(R.array.g112);

        int n = getRandom(g111.length);
        Question q = new Question(questionType.CONVERSATION, answerType.BEHAVIOUR, 111, g111[n]);
        q.setIsAmbiguous(isAmbiguous);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);

        Question qs = new Question(questionType.CONVERSATION, answerType.ANSWER, 112, g112[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    private Question getSummary(String thought, String behaviour, String goal) {

        String summary = res.getString(R.string.summary);
        String summary_default = res.getString(R.string.summary_default);
        String summary_final = Transform.replacePartial("&&&",
                Transform.replaceTransformPattern("%%%",
                        Transform.replacePartial("###", summary, thought, summary_default), behaviour, false), goal, summary_default);
        Question q = new Question(questionType.SUMMARY, answerType.NONE, 0, summary_final);
        return q;
    }

    private Question getAmbiguous(int prevG, int G, String prevQ, String Q, boolean isFirst) {
        String ambPreQuestion = res.getString(R.string.ambPreQuestion);
        String ambQuestion = res.getString(R.string.ambQuestion);
        String amb_final = Transform.replacePattern("&&&",
                Transform.replacePattern("%%%", (isFirst ? ambPreQuestion : "") + ambQuestion + likeChoice, prevQ), Q);
        Question q = new Question(questionType.RATE_QUESTION, answerType.RATE, 0, amb_final);
        q.setPrevGroup(prevG);
        q.setAmbiguousGroup(G);
        return q;
    }

    private Question getIntervention(boolean isSlot, boolean isStart) {
        Question q;
        String preIntvQuestion = res.getString(R.string.preIntvQuestion);
        String slotIntvQuestion = res.getString(R.string.slotIntvQuestion);
        String freqIntvQuestion = res.getString(R.string.freqIntvQuestion);
        String intvQ_final = "";
        if (isSlot) {
            intvQ_final = (isStart?preIntvQuestion:"") + slotIntvQuestion + likeChoice;
            q = new Question(questionType.RATE_SLOTS, answerType.RATE, 0, intvQ_final);
        } else {
            intvQ_final = (isStart?preIntvQuestion:"") + freqIntvQuestion + likeChoice;
            q = new Question(questionType.RATE_FREQUENCY, answerType.RATE, 0, intvQ_final);
        }

        return q;
    }

    private Question getGroup3OR4(Context context, int ctlLv, int age, boolean isMale, int prevGroup,
                                 String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho) {
        int group = SelectRecommendation.selectReplayGoal(context, ctlLv, age, isMale, prevGroup);
        if (group==3) {
            return getQuestionG3(prevGroup, prevAns, echoAns, isChangeTopic, isEcho, true);
        } else {
            return getQuestionG4(prevGroup, prevAns, echoAns, isChangeTopic, isEcho, true);
        }
    }

    private Question getGroup10OR11(Context context, int ctlLv, int age, boolean isMale, int prevGroup,
                                   String echoAns, boolean isEcho) {
        int group = SelectRecommendation.selectReflectBehaviour(context, ctlLv, age, isMale, prevGroup);
        if (group==10) {
            return getQuestionG10(prevGroup, echoAns, isEcho, true);
        } else {
            return getQuestionG11(prevGroup, echoAns, isEcho, true);
        }
    }

    private int getRandom(int length) {
        return rand.nextInt(length);
    }

    public Question generateQuestion(Context context, int ctlLv, int age, boolean isMale, int set_slot, int set_freq, SequenceQuestion seqQ, int id) {
        Question q = null;
        if (id == 0) {
            q = this.getPreControl();
        } else {
            Question prevAmbQuestion, ambQuestion;
            Question question = seqQ.getQuestionId(id-1);
            String answer = seqQ.getAnswerId(id-1);
            switch (seqQ.getSeq()) {
                case 1:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG1(0);
                            break;
                        case 2:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 3:
                            q = this.getQuestionG5(question.getGroup(), answer, true);
                            break;
                        case 4:
                            q = this.getQuestionG8(question.getGroup(), seqQ.getThought(), "", true, false);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 7:
                            q = this.getPostControl();
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG1(0);
                            break;
                        case 2:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 3:
                            q = this.getGroup10OR11(context, ctlLv, age, isMale, question.getGroup(), answer, true);
                            break;
                        case 4:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 5:
                            q = this.getGroup3OR4(context, ctlLv, age, isMale, question.getGroup(), seqQ.getThought(), "", true, false);
                            break;
                        case 6:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 7:
                            q = this.getPostControl();
                            break;
                        case 8:
                            prevAmbQuestion = seqQ.getQuestionId(2);
                            ambQuestion = seqQ.getQuestionId(3);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        case 9:
                            prevAmbQuestion = seqQ.getQuestionId(4);
                            ambQuestion = seqQ.getQuestionId(5);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG9(0);
                            break;
                        case 2:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 3:
                            Question prevQuestion = seqQ.getQuestionId(id - 2);
                            q = prevQuestion.getNextQuestion(question.getPrevGroup(), "", answer, false);
                            break;
                        case 4:
                            q = this.getQuestionG8(question.getGroup(), seqQ.getThought(), "", true, false);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 7:
                            q = this.getPostControl();
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG6(0);
                            break;
                        case 2:
                            q = question.getNextQuestion(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 3:
                            q = this.getQuestionG5(question.getGroup(), answer, true);
                            break;
                        case 4:
                            q = this.getQuestionG8(question.getGroup(), seqQ.getThought(), "", true, false);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 7:
                            q = this.getPostControl();
                            break;
                        default:
                            break;
                    }
                    break;
                case 5:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG6(0);
                            break;
                        case 2:
                            q = question.getNextQuestion(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 3:
                            q = this.getGroup10OR11(context, ctlLv, age, isMale, question.getGroup(), answer, true);
                            break;
                        case 4:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 5:
                            q = this.getGroup3OR4(context, ctlLv, age, isMale, question.getGroup(), seqQ.getThought(), "", true, false);
                            break;
                        case 6:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 7:
                            q = this.getPostControl();
                            break;
                        case 8:
                            prevAmbQuestion = seqQ.getQuestionId(2);
                            ambQuestion = seqQ.getQuestionId(3);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        case 9:
                            prevAmbQuestion = seqQ.getQuestionId(4);
                            ambQuestion = seqQ.getQuestionId(5);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        default:
                            break;
                    }
                    break;
                case 6:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG5(0, "", false);
                            break;
                        case 2:
                            q = this.getQuestionG8(question.getGroup(), "", seqQ.getBehaviour(), false, true);
                            break;
                        case 3:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 4:
                            q = this.getQuestionG7(question.getGroup(), seqQ.getGoal(), true);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 7:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 8:
                            q = this.getPostControl();
                            break;
                        default:
                            break;
                    }
                    break;
                case 7:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG10(0, "", false, false);
                            break;
                        case 2:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 3:
                            q = this.getGroup3OR4(context, ctlLv, age, isMale, question.getGroup(), "", seqQ.getBehaviour(), false, true);
                            break;
                        case 4:
                            q = this.getQuestionG7(question.getGroup(), seqQ.getGoal(), true);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 7:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 8:
                            q = this.getPostControl();
                            break;
                        case 9:
                            prevAmbQuestion = seqQ.getQuestionId(2);
                            ambQuestion = seqQ.getQuestionId(3);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        default:
                            break;
                    }
                    break;
                case 8:
                    switch (id) {
                        case 1:
                            q = this.getQuestionG11(0, "", false, false);
                            break;
                        case 2:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 3:
                            q = this.getGroup3OR4(context, ctlLv, age, isMale, question.getGroup(), "", seqQ.getBehaviour(), false, true);
                            break;
                        case 4:
                            q = this.getQuestionG7(question.getGroup(), seqQ.getGoal(), true);
                            break;
                        case 5:
                            q = question.getNextQuestion(question.getGroup(), "", "", false);
                            break;
                        case 6:
                            q = this.getQuestionG2(question.getGroup(), seqQ.getThought(), "", false);
                            break;
                        case 7:
                            q = this.getSummary(seqQ.getThought(), seqQ.getBehaviour(), seqQ.getGoal());
                            break;
                        case 8:
                            q = this.getPostControl();
                            break;
                        case 9:
                            prevAmbQuestion = seqQ.getQuestionId(2);
                            ambQuestion = seqQ.getQuestionId(3);
                            q = this.getAmbiguous(prevAmbQuestion.getGroup(), ambQuestion.getGroup(), prevAmbQuestion.getQuestion(), ambQuestion.getQuestion(), true);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    System.out.println("Sequence number is not match. try again! " + seqQ.getSeq());
                    break;
            }
        }

        int qNum = seqQ.getNumQ();
        if (q==null && id<qNum) {
            if (set_slot==1 && set_freq==0) {
                // Only Set Slot
                q = this.getIntervention(false, true);
            } else if (set_slot==0 && set_freq==1) {
                // Only Set Freq
                q = this.getIntervention(true, true);
            } else if (id == qNum-2 && set_slot==0 && set_freq==0) {
                // Set Slot for both Not Set Slot and Set Freq
                q = this.getIntervention(false, true);
            } else if (id == qNum-1 && set_slot==0 && set_freq==0) {
                // Set Freq for both Not Set Slot and Set Freq
                q = this.getIntervention(true, false);
            }
        }

        return q;
    }
}