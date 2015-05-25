package standrews.Agonyaunt;

import android.content.Context;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** This class handles dates
 * @author Teng
 */
public class SubQuestionNet {
    Context context;

    public SubQuestionNet(Context context){
        this.context = context;
        System.out.println("Come to the sub question net");
    }


    static BasicNetwork netWork;
    static File netFile;

    static double[] outputSubQuestion = new double[1];

    protected int computeSubQuestion(double[] input){

        loadNet(input);
        return (int)Math.floor(outputSubQuestion[0] + 0.5)  ;
    }

    public void loadNet(double[] input){
        try {
            // Load up Encog file
            netFile = new File(context.getFilesDir(), "neuralNetSubQuestion.eg");
            // If for some reason it doesn't exist, a backup exists in Assets file
            if (netFile.createNewFile() || netFile.length()==0){
                // Open neural net file
                InputStream is = context.getAssets().open("neuralNetSubQuestion.eg");
                // Buffer
                byte[] b = new byte[is.available()];
                // Read in NN file to buffer
                //is.read(b)
                is.close();
                // Write buffer to file
                FileOutputStream fs = new FileOutputStream(netFile);
                fs.write(b);
                fs.close();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        netWork = (BasicNetwork) EncogDirectoryPersistence.loadObject(netFile);
        netWork.compute(input, outputSubQuestion);

    }
}
