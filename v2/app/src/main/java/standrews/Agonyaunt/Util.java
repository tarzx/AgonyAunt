package standrews.Agonyaunt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/** This class handles dates
 * @author Jiachun Liu
 * @author Patomporn Loungvara
 */
public class Util {
    //Constants
    public static final int NUM_FREQUENCY = 7;
    public static final int NUM_SLOTS = 6;
    public static final int MIN_CONTROL = 1;
    public static final int MAX_CONTROL = 20;

    private static final int BUFFER_SIZE = 1024;

    // URL host
    // private static final String url_host = "http://10.0.2.2:8888/AndroidApp/";
    private static final String url_host = "http://pl44.host.cs.st-andrews.ac.uk/AndroidApp/v2/";

    // old
    public static final String url_add_to_slots = url_host + "add_to_slots_table.php";
    public static final String url_add_sub_record = url_host + "add_to_sub_table.php";
    public static final String INTERVENTION_FREQUENCY_NET_EG = "neuralNetIntervention.eg";
    public static final String INTERVENTION_SLOTS_NET_EG = "neuralNetInterventionSlots.eg";

    // URL list
    public static final String url_all_patients_info = url_host + "get_all_patients_info.php";
    public static final String url_patient_info = url_host + "get_patient_info.php";
    public static final String url_update_patient = url_host + "update_patient.php";
    public static final String url_delete_patient = url_host + "delete_patient.php";
    public static final String url_create_patient = url_host + "create_patient.php";
    public static final String url_set_preference = url_host + "set_preference.php";
    public static final String url_record_preference = url_host + "record_preference.php";
    public static final String url_record_select_sequence = url_host + "record_select_sequence.php";
    public static final String url_record_select_group_question = url_host + "record_select_group_question.php";
    public static final String TRAIN_FREQUENCY_INTERVENTION_NET_URL = url_host + "neuralNetFactory/runFrequencyInterventionNet.php";
    public static final String FREQUENCY_INTERVENTION_NET_URL = url_host + "neuralNetFactory/neuralNetfrequencyIntervention.eg";
    public static final String TRAIN_SLOT_INTERVENTION_NET_URL = url_host + "neuralNetFactory/runSlotInterventionNet.php";
    public static final String SLOT_INTERVENTION_NET_URL = url_host + "neuralNetFactory/neuralNetslotIntervention.eg";
    public static final String TRAIN_SELECT_SEQUENCE_NET_URL = url_host + "neuralNetFactory/runSelectSequenceNet.php";
    public static final String SELECT_SEQUENCE_NET_URL = url_host + "neuralNetFactory/neuralNetSelectSequence.eg";
    public static final String TRAIN_SELECT_GOAL_NET_URL = url_host + "neuralNetFactory/runSelectGoalNet.php";
    public static final String SELECT_GOAL_NET_URL = url_host + "neuralNetFactory/neuralNetSelectGoal.eg";
    public static final String TRAIN_SELECT_BEHAVIOUR_NET_URL = url_host + "neuralNetFactory/runSelectBehaviourNet.php";
    public static final String SELECT_BEHAVIOUR_NET_URL = url_host + "neuralNetFactory/neuralNetSelectBehaviour.eg";

    // file name
    public static final String FREQUENCY_INTERVENTION_NET_EG = "neuralNetfreqeuncyIntervention.eg";
    public static final String SLOT_INTERVENTION_NET_EG = "neuralNetslotIntervention.eg";
    public static final String SELECT_SEQUENCE_NET_EG = "neuralNetSelectSequence.eg";
    public static final String SELECT_GOAL_NET_EG = "neuralNetSelectGoal.eg";
    public static final String SELECT_BEHAVIOUR_NET_EG = "neuralNetSelectBehaviour.eg";

    //Tag
    // JSON Node names
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_PATIENTS = "patients";
    public static final String TAG_PATIENT = "patient";
    public static final String TAG_PID = "pid";
    public static final String TAG_NAME = "name";
    public static final String TAG_AGE = "age";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_SET_FREQ = "set_frequency";
    public static final String TAG_SET_SLOT = "set_slot";
    public static final String TAG_CONTROL_LEVEL = "control_level";
    public static final String TAG_SEQ1 = "seq1";
    public static final String TAG_SEQ2 = "seq2";
    public static final String TAG_SEQ3 = "seq3";
    public static final String TAG_SEQ4 = "seq4";
    public static final String TAG_SEQ5 = "seq5";
    public static final String TAG_SEQ6 = "seq6";
    public static final String TAG_SEQ7 = "seq7";
    public static final String TAG_SEQ8 = "seq8";
    public static final String TAG_PREVG = "previous_group";
    public static final String TAG_GROUP3 = "group3";
    public static final String TAG_GROUP4 = "group4";
    public static final String TAG_GROUP10 = "group10";
    public static final String TAG_GROUP11 = "group11";
    public static final String TAG_FREQ1 = "frequency1";
    public static final String TAG_FREQ2 = "frequency2";
    public static final String TAG_FREQ3 = "frequency3";
    public static final String TAG_FREQ4 = "frequency4";
    public static final String TAG_FREQ5 = "frequency5";
    public static final String TAG_FREQ6 = "frequency6";
    public static final String TAG_FREQ7 = "frequency7";
    public static final String TAG_SLOT1 = "slot1";
    public static final String TAG_SLOT2 = "slot2";
    public static final String TAG_SLOT3 = "slot3";
    public static final String TAG_SLOT4 = "slot4";
    public static final String TAG_SLOT5 = "slot5";
    public static final String TAG_SLOT6 = "slot6";

    // SharedPreferences Key
    public static final String KEY_PID = "pid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_SET_FREQ = "setFreq";
    public static final String KEY_FREQ = "frequency";
    public static final String KEY_SET_SLOT = "setSlot";
    public static final String KEY_CHECKBOX = "checkBoxesKey";
    public static final String KEY_CONTROL_LEVEL = "control_level";

    private static final TimeSlot[] timeslots = new TimeSlot[] {
		new TimeSlot(240, 180), new TimeSlot(420, 300),
		new TimeSlot(720, 300), new TimeSlot(1020, 240),
		new TimeSlot(1260, 180), new TimeSlot(0, 240) };

	public static boolean alarm_boot = false;

	/** Calculate the time of the next intervention
	 * @param frequency Frequency of intervention
	 * @param slots		Time Slots
	 * @return 			ArrayList of the next times
	 */
	public static ArrayList<Integer> calculateNextTimes(int frequency, boolean[] slots) {
		if (frequency == 0) {
			frequency = 1;
		}

		boolean b_temp = false;
		for (boolean slot : slots) {
			b_temp |= slot;
		}
		if (!b_temp) {
			slots[1] = true;
		}
		Random gen = new Random();

		ArrayList<Integer> nextTimes = new ArrayList<Integer>(frequency);
		ArrayList<Integer> checked = new ArrayList<Integer>(slots.length);
		int num_checked = 0;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i]) {
				num_checked++;
				checked.add(i);
			}
		}
		int[] frequencies = new int[num_checked];
		int remaining = frequency;
		if (remaining > frequencies.length) {
			for (int i = 0; i < frequencies.length - 1; i++) {
				frequencies[i] = gen.nextInt(Math.abs(remaining - (frequencies.length - i)));
				remaining -= frequencies[i];
			}
			frequencies[frequencies.length - 1] = remaining;
		} else {
			for (int i = 0; i < frequencies.length - 1; i++) {
				frequencies[i] = gen.nextInt(remaining);
				remaining -= frequencies[i];
			}
			frequencies[frequencies.length - 1] = remaining;
		}

		for (int i = 0; i < frequencies.length; i++) {
			int num_freq = frequencies[i];
			TimeSlot ts = timeslots[checked.get(i)];
			for (int j = 0; j < num_freq; j++) {
				int minute_of_day = gen.nextInt(ts.getDuration())
						+ ts.getStart();
				nextTimes.add(minute_of_day);
			}

		}
		return nextTimes;
	}

	public static boolean alarmBooted() {
		if (!alarm_boot) {
			alarm_boot = true;
			return true;
		} else {
			return false;
		}
	}

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static void trainNet(String trainURL) {
        //Train
        try {
            URL url = new URL(trainURL);
            try {

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                in.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.w("My Track from here", "Neural networks is being trained..." + trainURL);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void loadNet(Context context, String loadURL, String fileName) {
        try {
            if (checkNetwork(context)) {
                URL url = new URL(loadURL);
                URLConnection connection = url.openConnection();

                connection.connect();
                InputStream inputStream = connection.getInputStream();

                File neuralNet = new File(context.getFilesDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(neuralNet);

                byte buffer[] = new byte[BUFFER_SIZE];
                int dataSize;

                Log.w("My Track from here", "Update Neural Net File: " + fileName);

                while ((dataSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, dataSize);
                }

                outputStream.close();

            } else {

                boolean exist = false;
                File dir = new File(context.getFilesDir().getPath());
                File[] files = dir.listFiles();

                for (File file : files) {
                    if (file.getName() == fileName) exist = true;
                }

                // If for some reason it doesn't exist, a backup exists in Assets file
                if (!exist) {
                    // Load up Encog file
                    File netFile = new File(context.getFilesDir(), fileName);
                    InputStream inputStream = context.getAssets().open(fileName);

                    FileOutputStream outputStream = new FileOutputStream(netFile);
                    byte buffer[] = new byte[BUFFER_SIZE];
                    int dataSize;
                    Log.w("My Track from here", fileName + " copy");

                    while ((dataSize = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, dataSize);
                    }

                    outputStream.close();
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getNetFile(Context context, String fileName) {
        File netFile = null;

        try {
            boolean exist = false;
            File dir = new File(context.getFilesDir().getPath());
            File[] files = dir.listFiles();

            for (File file : files) {
                if (file.getName() == fileName) exist = true;
            }

            // If for some reason it doesn't exist, a backup exists in Assets file
            if (!exist) {
                // Load up Encog file
                netFile = new File(context.getFilesDir(), fileName);
                InputStream inputStream = context.getAssets().open(fileName);

                FileOutputStream outputStream = new FileOutputStream(netFile);
                byte buffer[] = new byte[1024];
                int dataSize;
                Log.w("My Track from here", fileName + " copy");

                while ((dataSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, dataSize);
                }

                outputStream.close();
            } else {
                Log.w("My Track from here", fileName + " exists");
                netFile = new File(context.getFilesDir(), fileName);
            }

            //System.out.println("Updated!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return netFile;
    }

    public static double[] refineBinary(final int input, int digit) {
        int decimal = input;
        double[] binary = new double[digit];
        for (int i=digit-1; i>=0; i--) {
            binary[i] = Math.floor(decimal/Math.pow(2, i));
            decimal %= Math.pow(2, i);
        }
        return binary;
    }

    public static double[] refineAge(final int age) {
        if (age<=17) {
            return new double[] { 0.0, 0.0, 0.0 };
        } else if (age<=24) {
            return new double[] { 0.0, 0.0, 1.0 };
        } else if (age<=49) {
            return new double[] { 0.0, 1.0, 0.0 };
        } else if (age<=64) {
            return new double[] { 0.0, 1.0, 1.0 };
        } else {
            return new double[] { 1.0, 0.0, 0.0 };
        }
    }

    public static double refineGender(final boolean isMale) {
        return (isMale?0.0:1.0);
    }

    public static ArrayList<Integer> getSlots(HashMap<String, String> preference) {
        ArrayList<Integer> slots = new ArrayList<Integer>();
        if (preference.get(TAG_SLOT1).equals("1")) slots.add(1);
        if (preference.get(TAG_SLOT2).equals("1")) slots.add(2);
        if (preference.get(TAG_SLOT3).equals("1")) slots.add(3);
        if (preference.get(TAG_SLOT4).equals("1")) slots.add(4);
        if (preference.get(TAG_SLOT5).equals("1")) slots.add(5);
        if (preference.get(TAG_SLOT6).equals("1")) slots.add(6);
        return slots;
    }

    public static int getFrequency(HashMap<String, String> preference) {
        int freq = 0;
        if (preference.get(TAG_FREQ1).equals("1")) freq = 1;
        else if (preference.get(TAG_FREQ2).equals("1")) freq = 2;
        else if (preference.get(TAG_FREQ3).equals("1")) freq = 3;
        else if (preference.get(TAG_FREQ4).equals("1")) freq = 4;
        else if (preference.get(TAG_FREQ5).equals("1")) freq = 5;
        else if (preference.get(TAG_FREQ6).equals("1")) freq = 6;
        else if (preference.get(TAG_FREQ7).equals("1")) freq = 7;
        return freq;
    }
}
