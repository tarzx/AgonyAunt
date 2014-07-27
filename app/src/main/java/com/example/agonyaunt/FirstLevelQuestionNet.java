package com.example.agonyaunt;

import android.content.Context;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Teng on 27/07/2014.
 */
public class FirstLevelQuestionNet {
    Context context;

    public FirstLevelQuestionNet(Context context){
        this.context = context;
        System.out.println("Come to the First level question net");
    }


    static BasicNetwork netWork;
    static File netFile;

    static double[] outputFirstQuestion = new double[1];

    protected int computeFirstQuestion(double[] input){

        loadNet(input);
        return (int)Math.floor(outputFirstQuestion[0]*10 + 0.5)  ;
    }

    public void loadNet(double[] input){
        try {
            // Load up Encog file
            netFile = new File(context.getFilesDir(), "neuralNetFirstLevelQuestion.eg");
            // If for some reason it doesn't exist, a backup exists in Assets file
            if (netFile.createNewFile() || netFile.length()==0){
                // Open neural net file
                InputStream is = context.getAssets().open("neuralNetFirstLevelQuestion.eg");
                // Buffer
                byte[] b = new byte[is.available()];
                // Read in NN file to buffer
                is.read(b);
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
        netWork.compute(input, outputFirstQuestion);

    }
}
