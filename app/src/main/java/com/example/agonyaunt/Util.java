package com.example.agonyaunt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/** This class handles dates
 * @author Jiachun Liu
 */
public class Util {

	private final static TimeSlot[] timeslots = new TimeSlot[] {
		new TimeSlot(240, 180), new TimeSlot(420, 300),
		new TimeSlot(720, 300), new TimeSlot(1020, 240),
		new TimeSlot(1260, 180), new TimeSlot(0, 240) };

	public static boolean alarm_boot = false;

	/** Calculate the time of the next intervention
	 * @param frequency Frequency of intervention
	 * @param slots		Timeslots
	 * @return 			ArrayList of the next times
	 */
	public static ArrayList<Integer> calculateNextTimes(int frequency,
			boolean[] slots) {
		if (frequency == 0) {
			frequency = 1;
		}
		boolean b_temp = false;
		for (boolean slot : slots) {
			b_temp |= slot;
		}
		if (b_temp == false) {
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
		if (alarm_boot == false) {
			alarm_boot = true;
			return true;
		} else {
			return false;
		}
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}
