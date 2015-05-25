package standrews.Agonyaunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Teng on 14/07/2014.
 *
 */
public class InterventionNet {
    Context context;

    public InterventionNet(Context context){
        this.context = context;
        System.out.println("Come to the Intervention Net");
    }


    static BasicNetwork netWork;
    static File netFile;

    static double[] outputFrequency = new double[1];


    protected int computeFrequency() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        String age = sharedPref.getString("userAge", "0");
        String occ = sharedPref.getString("userOccupation", "");

        String sFemale = sharedPref.getString("sex0", "");
        String sMale = sharedPref.getString("sex1", "");
        boolean sexFemale = Boolean.parseBoolean(sFemale);
        boolean sexMale = Boolean.parseBoolean(sMale);

        String gender;

//        Change gender value
        if (sexMale) {
            gender = "0.0";
        }else if (sexFemale){
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


        return (int)Math.floor(outputFrequency[0]*10 + 0.5)  ;
    }


    public void loadNet(double[] input){
        try {
            boolean exist =false;
            File dir = new File(context.getFilesDir().getPath());
            File[] files = dir.listFiles();

            for(File file : files) {
                if (file.getName() == Util.INTERVENTION_FREQUENCY_NET_EG) exist = true;
            }

            // If for some reason it doesn't exist, a backup exists in Assets file
            if (!exist){
                // Load up Encog file
                netFile = new File(context.getFilesDir(), Util.INTERVENTION_FREQUENCY_NET_EG);
                InputStream inputStream = context.getAssets().open(Util.INTERVENTION_FREQUENCY_NET_EG);

                FileOutputStream outputStream = new FileOutputStream(netFile);
                byte buffer[] = new byte[1024];
                int dataSize;
                Log.w("My Track from here", Util.INTERVENTION_FREQUENCY_NET_EG + " copy");

                while ((dataSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, dataSize);
                }

                outputStream.close();
            } else {
                netFile = new File(context.getFilesDir(), Util.INTERVENTION_FREQUENCY_NET_EG);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        netWork = (BasicNetwork) EncogDirectoryPersistence.loadObject(netFile);
        netWork.compute(input, outputFrequency);

    }


}
