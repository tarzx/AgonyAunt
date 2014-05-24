package com.example.agonyaunt;

/** This class represents a time slot
 * @author Jiachun Liu
 */

public class TimeSlot {

	private int start;
	private int duration;
	private String description;

	/**Takes the start time in minutes of the day and the duration of the slot in minutes */
	public TimeSlot(int start, int duration) {
		this.start = start;
		this.duration = duration;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
