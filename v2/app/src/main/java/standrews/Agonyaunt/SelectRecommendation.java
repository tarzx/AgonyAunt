package standrews.Agonyaunt;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/** This class handles Recommendation Value from Neural Network
 * @author Patomporn Loungvara
 */
public class SelectRecommendation {

    private static final Random rand = new Random();;
    private static final double RATE = 0.1;
    private static final double BASE_RATE = 0.8;

    public static int selectFrequency(Context context, int lastCtlLv, int age, boolean isMale) {
        FrequencyInterventionNet fin = new FrequencyInterventionNet(context);

        double[] freqRate = fin.getRate(lastCtlLv, age, isMale);

        return fin.getFreq(getMaxRandomRate(freqRate));
    }

    public static ArrayList<Integer> selectTimeSlot(Context context, int lastCtlLv, int age, boolean isMale) {
        TimeSlotInterventionNet tsin = new TimeSlotInterventionNet(context);

        double[] slotRate = tsin.getRate(lastCtlLv, age, isMale);

        ArrayList<Integer>  idxSet = getSetMaxRandomRate(slotRate);
        ArrayList<Integer>  slotSet = new ArrayList<Integer>();
        for (int idx : idxSet) {
            slotSet.add(tsin.getSlot(idx));
        }

        return slotSet;
    }

    public static int selectSequence(Context context, int ctlLv, int age, boolean isMale) {
        SelectSequenceNet ssq = new SelectSequenceNet(context);

        double[] seqRate = ssq.getRate(ctlLv, age, isMale);

        return ssq.getSequence(getMaxRandomRate(seqRate));
    }

    public static int selectReplayGoal(Context context, int ctlLv, int age, boolean isMale, int prevGroup) {
        SelectGQGoalNet sgqg = new SelectGQGoalNet(context);

        double[] rateGQGoal = sgqg.getRate(ctlLv, age, isMale, prevGroup);

        return sgqg.getGroupQuestion(getMaxRandomRate(rateGQGoal));
    }

    public static int selectReflectBehaviour(Context context, int ctlLv, int age, boolean isMale, int prevGroup) {
        SelectGQBehaviourNet sgqb = new SelectGQBehaviourNet(context);

        double[] rateGQBehaviour = sgqb.getRate(ctlLv, age, isMale, prevGroup);

        return sgqb.getGroupQuestion(getMaxRandomRate(rateGQBehaviour));
    }

    private static int getMaxRandomRate(double[] rate) {
        int maxIdx = 0;
        double max = Double.MIN_VALUE;
        for (int i=0; i<rate.length; i++) {
            double rndRate = rate[i] + getRandomRate();
            if (max<rndRate) {
                max = rndRate;
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    private static ArrayList<Integer> getSetMaxRandomRate(double[] rate) {
        double[] rndRate = new double[rate.length];
        double max = Double.MIN_VALUE;
        for (int i=0; i<rate.length; i++) {
            rndRate[i] = rate[i] + getRandomRate();
            if (max<rndRate[i]) {
                max = rndRate[i];
            }
        }
        ArrayList<Integer> maxSet = new ArrayList<Integer>();
        for (int i=0; i<rndRate.length; i++) {
            if (rndRate[i]>=BASE_RATE || rndRate[i]==max) {
                maxSet.add(i);
            }
        }
        return maxSet;
    }

    private static double getRandomRate() {
        switch (rand.nextInt(3)) {
            case 0:
                return 0;
            case 1:
                return RATE;
            default:
                return -RATE;
        }
    }
}
