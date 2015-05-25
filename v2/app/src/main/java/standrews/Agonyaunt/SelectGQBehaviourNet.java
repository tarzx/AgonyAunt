package standrews.Agonyaunt;

import android.content.Context;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;

/** This class represents the main menu
 * @author Patomporn Loungvara
 */
public class SelectGQBehaviourNet {
    private final int[] GROUP = { 10, 11 };
    private final int CTRL_DIGIT = 5;
    private final int AGE_DIGIT = 3;
    private final int GENDER_DIGIT = 1;
    private final int PREVG_DIGIT = 4;
    private final int NUM_INPUT_BITS = CTRL_DIGIT + AGE_DIGIT + GENDER_DIGIT + PREVG_DIGIT;
    private final int NUM_OUTPUT_BITS = GROUP.length;

    private Context context;

    public SelectGQBehaviourNet(Context context){
        this.context = context;
        System.out.println("Come to the Select Group Question Behaviour net");
    }

    public int getGroupQuestion(int idx) {
        if (idx<GROUP.length) {
            return GROUP[idx];
        } else {
            return 0;
        }
    }

    public double[] getRate(int controlLevel, int age, boolean isMale, int prevGroup) {
        //Refine
        double[] input = refineInput(controlLevel, age, isMale, prevGroup);
        double[] output = new double[NUM_OUTPUT_BITS];

        File netFile = Util.getNetFile(context, Util.SELECT_BEHAVIOUR_NET_EG);

        BasicNetwork net = (BasicNetwork) EncogDirectoryPersistence.loadObject(netFile);
        net.compute(input, output);

        return output;
    }

    private double[] refineInput(int controlLevel, int age, boolean isMale, int prevGroup) {
        double[] input = new double[NUM_INPUT_BITS];
        double[] controlBit = Util.refineBinary(controlLevel, CTRL_DIGIT);
        double[] ageBit = Util.refineAge(age);
        double genderBit = Util.refineGender(isMale);
        double[] prevGBit = Util.refineBinary(prevGroup, PREVG_DIGIT);

        for (int i=0; i<CTRL_DIGIT; i++) {
            input[i] = controlBit[i];
        }
        for (int i=0; i<AGE_DIGIT; i++) {
            input[i+CTRL_DIGIT] = ageBit[i];
        }
        input[CTRL_DIGIT+AGE_DIGIT] = genderBit;
        for (int i=0; i<PREVG_DIGIT; i++) {
            input[i+CTRL_DIGIT+AGE_DIGIT+GENDER_DIGIT] = prevGBit[i];
        }

        return input;
    }

}
