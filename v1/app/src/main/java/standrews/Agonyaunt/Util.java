package standrews.Agonyaunt;

import android.util.Log;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

/** This class handles dates
 * @author Jiachun Liu
 */
public class Util {
    // URL host
    // private static final String url_host = "http://10.0.2.2:8888/AndroidApp/";
    private static final String url_host = "http://pl44.host.cs.st-andrews.ac.uk/AndroidApp/v1/";

    // URL list
    public static final String url_all_patients = url_host + "get_all_patients.php";
    public static final String url_patient_details = url_host + "get_patient_details.php";
    public static final String url_update_patient = url_host + "update_patient.php";
    public static final String url_delete_patient = url_host + "delete_patient.php";
    public static final String url_create_patient = url_host + "create_patient.php";
    public static final String url_add_to_slots = url_host + "add_to_slots_table.php";
    public static final String url_add_sub_record = url_host + "add_to_sub_table.php";
    public static final String TRAIN_INTERVENTION_FREQUENCY_NET_URL = url_host + "neuralNetFactory/runInterventionFrequencyNet.php";
    public static final String INTERVENTION_FREQUENCY_NET_URL = url_host + "neuralNetFactory/neuralNetIntervention.eg";
    public static final String TRAIN_INTERVENTION_SLOTS_NET_URL = url_host + "neuralNetFactory/runInterventionSlotsNet.php";
    public static final String INTERVENTION_SLOTS_NET_URL = url_host + "neuralNetFactory/neuralNetInterventionSlots.eg";
    public static final String TRAIN_FIRST_LEVEL_QUESTION_NET_URL = url_host + "neuralNetFactory/runFirstLevelQuestionNet.php";
    public static final String FIRST_LEVEL_QUESTION_NET_URL = url_host + "neuralNetFactory/neuralNetFirstLevelQuestion.eg";
    public static final String TRAIN_SUB_QUESTION_NET_URL = url_host + "neuralNetFactory/runSubQuestionNet.php";
    public static final String SUB_QUESTION_NET_URL = url_host + "neuralNetFactory/neuralNetSubQuestion.eg";

    // file name
    public static final String INTERVENTION_FREQUENCY_NET_EG = "neuralNetIntervention.eg";
    public static final String INTERVENTION_SLOTS_NET_EG = "neuralNetInterventionSlots.eg";
    public static final String FIRST_LEVEL_QUESTION_NET_EG = "neuralNetFirstLevelQuestion.eg";
    public static final String SUB_QUESTION_NET_EG = "neuralNetSubQuestion.eg";

    //Tag
    //public static final String NETWORK_TAG = "Network Track: ";

    private static final TimeSlot[] timeslots = new TimeSlot[] {
		new TimeSlot(240, 180), new TimeSlot(420, 300),
		new TimeSlot(720, 300), new TimeSlot(1020, 240),
		new TimeSlot(1260, 180), new TimeSlot(0, 240) };

	public static boolean alarm_boot = false;

	/** Calculate the time of the next intervention
	 * @param frequency Frequency of intervention
	 * @param slots		Timeslots
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

		ArrayList<Integer> nexttimes = new ArrayList<Integer>(frequency);
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
			int num_frequ = frequencies[i];
			TimeSlot ts = timeslots[checked.get(i)];
			for (int j = 0; j < num_frequ; j++) {
				int minute_of_day = gen.nextInt(ts.getDuration())
						+ ts.getStart();
				nexttimes.add(minute_of_day);
			}

		}
		return nexttimes;
	}

	public static boolean alarmBooted() {
		if (!alarm_boot) {
			alarm_boot = true;
			return true;
		} else {
			return false;
		}
	}

    public static void updateNetFile(Context context, String fileName, String URLname) {
        try {

            URL url = new URL(URLname);
            URLConnection connection = url.openConnection();

            connection.connect();
            InputStream inputStream = connection.getInputStream();

            File neuralNet = new File(context.getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(neuralNet);

            byte buffer[] = new byte[1024];
            int dataSize;
            //int loadedSize = 0;
            Log.w("My Track from here", fileName + " update");

            while ((dataSize = inputStream.read(buffer)) != -1) {
                //loadedSize += dataSize;
                outputStream.write(buffer, 0, dataSize);

            }

            outputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//	public static String getDate(long milliSeconds, String dateFormat) {
//		// Create a DateFormatter object for displaying date in specified
//		// format.
//		DateFormat formatter = new SimpleDateFormat(dateFormat);
//		// Create a calendar object that will convert the date and time value in
//		// milliseconds to date.
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(milliSeconds);
//		return formatter.format(calendar.getTime());
//	}
}
