package standrews.Agonyaunt;

import android.content.Context;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;

/** This class represents the main menu
 * @author Patomporn Loungvara
 */
public class FrequencyInterventionNet {
    private final int[] FREQ = { 1, 2, 3, 4, 5, 6, 7 };
    private final int CTRL_DIGIT = 5;
    private final int AGE_DIGIT = 3;
    private final int GENDER_DIGIT = 1;
    private final int NUM_INPUT_BITS = CTRL_DIGIT + AGE_DIGIT + GENDER_DIGIT;
    private final int NUM_OUTPUT_BITS = FREQ.length;

    private Context context;

    public FrequencyInterventionNet(Context context){
        this.context = context;
        System.out.println("Come to the Frequency Intervention net");
    }

    public int getFreq(int idx) {
        if (idx<FREQ.length) {
            return FREQ[idx];
        } else {
            return 0;
        }
    }

    public double[] getRate(int controlLevel, int age, boolean isMale) {
        //Refine
        double[] input = refineInput(controlLevel, age, isMale);
        double[] output = new double[NUM_OUTPUT_BITS];

        File netFile = Util.getNetFile(context, Util.FREQUENCY_INTERVENTION_NET_EG);

        BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(netFile);
        net.compute(input, output);

        return output;
    }

    private double[] refineInput(int controlLevel, int age, boolean isMale) {
        double[] input = new double[NUM_INPUT_BITS];
        double[] controlBit = Util.refineBinary(controlLevel, CTRL_DIGIT);
        double[] ageBit = Util.refineAge(age);
        double genderBit = Util.refineGender(isMale);

        for (int i=0; i<CTRL_DIGIT; i++) {
            input[i] = controlBit[i];
        }
        for (int i=0; i<AGE_DIGIT; i++) {
            input[i+CTRL_DIGIT] = ageBit[i];
        }
        input[CTRL_DIGIT+AGE_DIGIT] = genderBit;

        return input;
    }

}
