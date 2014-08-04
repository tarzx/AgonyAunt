package com.example.agonyaunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Teng on 04/08/2014.
 */
public class InterventionSlotsNet {
    Context context;

    public InterventionSlotsNet(Context context){
        this.context = context;
        System.out.println("Come to the Intervention Slots Net");
    }


    static BasicNetwork netWork;
    static File netFile;

    static double[] outputSlots = new double[6];


    protected double[] computeSlots() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String age = sharedPref.getString("userAge", "0");
        String occ = sharedPref.getString("userOccupation", "");

        String sFemale = sharedPref.getString("sex0", "");
        String sMale = sharedPref.getString("sex1", "");
        boolean sexFemale = Boolean.parseBoolean(sFemale);
        boolean sexMale = Boolean.parseBoolean(sMale);

        String gender = null;

//        Change gender value
        if (sexMale == true){
            gender = "0.0";
        }else if (sexFemale == true){
            gender = "1.0";
        }else{
            gender = "2.0";
        }


        //						Change the occupation value
        if (occ.equals("Writer")) {
            occ = "0.0";
        }else if (occ.equals("Student")) {
            occ = "1.0";
        }else if (occ.equals("Freelancer")) {
            occ = "2.0";
        }else if (occ.equals("Not hired")) {
            occ = "3.0";
        }else {
            occ = "4.0";
        }


        double[] input = {Double.parseDouble("0."+age), Double.parseDouble(gender), Double.parseDouble(occ)};

        loadNet(input);

        for (int j = 0; j < outputSlots.length; j++){
            outputSlots[j] = Math.floor(outputSlots[j] + 0.5);
            System.out.print(outputSlots[j] + " ");
        }

        System.out.println();

        return outputSlots  ;
    }


    public void loadNet(double[] input){
        try {
            // Load up Encog file
            netFile = new File(context.getFilesDir(), "neuralNetInterventionSlots.eg");
            // If for some reason it doesn't exist, a backup exists in Assets file
            if (netFile.createNewFile() || netFile.length()==0){
                // Open neural net file
                InputStream is = context.getAssets().open("neuralNetInterventionSlots.eg");
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
        netWork.compute(input, outputSlots);

    }
}
