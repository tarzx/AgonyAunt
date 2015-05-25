package standrews.Agonyaunt;

import android.content.res.Resources;

import java.util.Random;

/** This class represents the main menu
 * @author Patomporn Loungvara
 */
public class QuestionsManager {
    private Resources res;
    private Random rand;

    private String[] echo;
    private String[] change_topic;
    private String change_topic_default, link, target_goal, target_thought, likeChoice;

    public QuestionsManager(Resources res) {
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

    public String getPreControl() {
        return res.getString(R.string.preControl) + res.getString(R.string.controlChoice);
    }
    public String getPostControl() {
        return res.getString(R.string.postControl) + res.getString(R.string.controlChoice);
    }

    public Questions getQuestionG1(int prevGroup) {
        String[] g1 = res.getStringArray(R.array.g1);
        String[] g1_prefix = res.getStringArray(R.array.g1_prefix);

        int n = getRandom(g1.length);
        Questions q = new Questions(10, g1[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g1_prefix[n]);
        return q;
    }

    public Questions getQuestionG2(int prevGroup, String prevAns, String echoAns, boolean isEcho) {
        String[] g2 = res.getStringArray(R.array.g2);
        String g2_pattern = res.getString(R.string.g2_pattern);
        String g2_default = res.getString(R.string.g2_default);

        int n = getRandom(g2.length);
        Questions q = new Questions(20, g2[n]);
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

    public Questions getQuestionG3(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho, boolean isAmbiguious) {
        String[] g3 = res.getStringArray(R.array.g3);
        String[] g3_prefix = res.getStringArray(R.array.g3_prefix);

        int n = getRandom(g3.length);
        Questions q = new Questions(30, g3[n]);
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

    public Questions getQuestionG4(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho, boolean isAmbiguious) {
        String[] g4 = res.getStringArray(R.array.g4);
        String[] g4_prefix = res.getStringArray(R.array.g4_prefix);

        int n = getRandom(g4.length);
        Questions q = new Questions(40, g4[n]);
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

    public Questions getQuestionG5(int prevGroup, String echoAns, boolean isEcho) {
        String[] g5 = res.getStringArray(R.array.g5);

        int n = getRandom(g5.length);
        Questions q = new Questions(50, g5[n]);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);
        return q;
    }

    public Questions getQuestionG6(int prevGroup) {
        String[] g61 = res.getStringArray(R.array.g61);
        String[] g61_prefix = res.getStringArray(R.array.g61_prefix);
        String[] g62 = res.getStringArray(R.array.g62);
        String g62_pattern = res.getString(R.string.g62_pattern);
        String g62_default = res.getString(R.string.g62_default);

        int n = getRandom(g61.length);
        Questions q = new Questions(61, g61[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g61_prefix[n]);

        Questions qs = new Questions(62, g62[n]);
        qs.setPattern(g62_pattern);
        qs.setDefaultPattern(g62_default);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public Questions getQuestionG7(int prevGroup, String prevAns, boolean isChangeTopic) {
        String[] g71 = res.getStringArray(R.array.g71);
        String[] g71_prefix = res.getStringArray(R.array.g71_prefix);
        String[] g72 = res.getStringArray(R.array.g72);

        int n = getRandom(g71.length);
        Questions q = new Questions(71, g71[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g71_prefix[n]);
        q.setIsChangeTopic(isChangeTopic);
        q.setChangeTopic(change_topic[getRandom(change_topic.length)]);
        q.setDefaultTopic(change_topic_default);
        q.setTarget(target_thought);
        q.setPrevAnswer(prevAns);

        Questions qs = new Questions(72, g72[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public Questions getQuestionG8(int prevGroup, String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho) {
        String[] g81 = res.getStringArray(R.array.g81);
        String[] g81_prefix = res.getStringArray(R.array.g81_prefix);
        String[] g82 = res.getStringArray(R.array.g82);

        int n = getRandom(g81.length);
        Questions q = new Questions(81, g81[n]);
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

        Questions qs = new Questions(82, g82[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public Questions getQuestionG9(int prevGroup) {
        String[] g91 = res.getStringArray(R.array.g91);
        String[] g91_prefix = res.getStringArray(R.array.g91_prefix);
        String[] g92 = res.getStringArray(R.array.g92);

        int n = getRandom(g91.length);
        Questions q = new Questions(91, g91[n]);
        q.setPrevGroup(prevGroup);
        q.setAnswerPrefix(g91_prefix[n]);

        Questions qs = new Questions(92, g92[n]);
        qs.setEcho(echo[getRandom(echo.length)]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public Questions getQuestionG10(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
        String[] g101 = res.getStringArray(R.array.g101);
        String[] g102 = res.getStringArray(R.array.g102);

        int n = getRandom(g101.length);
        Questions q = new Questions(101, g101[n]);
        q.setIsAmbiguous(isAmbiguous);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);

        Questions qs = new Questions(102, g102[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public Questions getQuestionG11(int prevGroup, String echoAns, boolean isEcho, boolean isAmbiguous) {
        String[] g111 = res.getStringArray(R.array.g111);
        String[] g112 = res.getStringArray(R.array.g112);

        int n = getRandom(g111.length);
        Questions q = new Questions(111, g111[n]);
        q.setIsAmbiguous(isAmbiguous);
        q.setPrevGroup(prevGroup);
        q.setIsEcho(isEcho);
        q.setEcho(echo[getRandom(echo.length)]);
        q.setPrevEcho(echoAns);
        q.setLink(link);

        Questions qs = new Questions(112, g112[n]);
        qs.setLink(link);

        q.setNextQuestion(qs);
        return q;
    }

    public String getSummary(String thought, String behaviour, String goal) {
        String summary = res.getString(R.string.summary);
        String summary_default = res.getString(R.string.summary_default);
        return Transform.replacePartial("&&&",
                Transform.replaceTransformPattern("%%%",
                        Transform.replacePartial("###", summary, thought, summary_default), behaviour, false), goal, summary_default);
    }

    public String getAmbiguous(String prevQ, String Q, boolean isFirst) {
        String ambPreQuestion = res.getString(R.string.ambPreQuestion);
        String ambQuestion = res.getString(R.string.ambQuestion);

        return Transform.replacePattern("&&&",
                Transform.replacePattern("%%%", (isFirst ? ambPreQuestion : "") + ambQuestion + likeChoice, prevQ), Q);
    }

    public String getIntvention(boolean isSlot, boolean isStart) {
        String preIntvQuestion = res.getString(R.string.preIntvQuestion);
        String slotIntvQuestion = res.getString(R.string.slotIntvQuestion);
        String freqIntvQuestion = res.getString(R.string.freqIntvQuestion);

        if (isSlot) {
            return (isStart?preIntvQuestion:"") + slotIntvQuestion + likeChoice;
        } else {
            return (isStart?preIntvQuestion:"") + freqIntvQuestion + likeChoice;
        }
    }

    private int getRandom(int length) {
        return rand.nextInt(length);
    }

    public Questions getGroup3OR4(SelectRecommendation sgq, int ctlLv, int age, boolean isMale, int prevGroup,
                                 String prevAns, String echoAns, boolean isChangeTopic, boolean isEcho) {
        int group = sgq.selectReplayGoal(ctlLv, age, isMale, prevGroup);
        if (group==3) {
            return getQuestionG3(prevGroup, prevAns, echoAns, isChangeTopic, isEcho, true);
        } else {
            return getQuestionG4(prevGroup, prevAns, echoAns, isChangeTopic, isEcho, true);
        }
    }

    public Questions getGroup10OR11(SelectRecommendation sgq, int ctlLv, int age, boolean isMale, int prevGroup,
                                   String echoAns, boolean isEcho) {
        int group = sgq.selectReflectBehaviour(ctlLv, age, isMale, prevGroup);
        if (group==10) {
            return getQuestionG10(prevGroup, echoAns, isEcho, true);
        } else {
            return getQuestionG11(prevGroup, echoAns, isEcho, true);
        }
    }

}
