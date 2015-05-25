package standrews.Agonyaunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

/** This class handles dates
 * @author Teng
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

        String gender;

//        Change gender value
        if (sexMale){
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

        for (int j = 0; j < outputSlots.length; j++){
            outputSlots[j] = Math.floor(outputSlots[j] + 0.5);
            System.out.print(outputSlots[j] + " ");
        }

        System.out.println();

        return outputSlots  ;
    }


    public void loadNet(double[] input){
        try {
            boolean exist =false;
            File dir = new File(context.getFilesDir().getPath());
            File[] files = dir.listFiles();

            for(File file : files) {
                if (file.getName() == Util.INTERVENTION_SLOTS_NET_EG) exist = true;
            }

            // If for some reason it doesn't exist, a backup exists in Assets file
            if (!exist){
                // Load up Encog file
                netFile = new File(context.getFilesDir(), Util.INTERVENTION_SLOTS_NET_EG);
                InputStream inputStream = context.getAssets().open(Util.INTERVENTION_SLOTS_NET_EG);

                FileOutputStream outputStream = new FileOutputStream(netFile);
                byte buffer[] = new byte[1024];
                int dataSize;
                Log.w("My Track from here", Util.INTERVENTION_SLOTS_NET_EG + " copy");

                while ((dataSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, dataSize);
                }

                outputStream.close();
            } else {
                netFile = new File(context.getFilesDir(), Util.INTERVENTION_SLOTS_NET_EG);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        netWork = (BasicNetwork) EncogDirectoryPersistence.loadObject(netFile);
        netWork.compute(input, outputSlots);

    }
}
