package com.example.agonyaunt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import android.content.Context;

/** This class transmits the input and output to the server for the ANN
 * @author Abigail Lowe
 */

public class Transmitter {

	// Port
	private final int PORT = 1556;
	QuestionManager quesMan;
	Context context;

	public Transmitter(QuestionManager quesMan) {
		this.quesMan = quesMan;
	}

	/** Write info out to server
	 * @param filename	File to write
	 * @param to_write	Info to write to server
	 * @param context	The Android context
	 */
	public void output(String filename, ArrayList<byte[]> to_write,
			Context context) {
		this.context = context;
		// Gets the string
		String message = getMessage(to_write);
		String response = "";
		try {
			// Open socket
			Socket clientSocket = new Socket("10.0.2.2", PORT);
			// Output and input streams
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			// Write message out
			outToServer.writeBytes(message + '\n');
			// Read message from server
			response = inFromServer.readLine();
			// Store response
			storeResponse(response);
			// Close socket
			clientSocket.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** Formats the message for the server
	 * @param to_write	The info for the server
	 * @return			The message for the server
	 */
	public String getMessage(ArrayList<byte[]> to_write) {
		String message = "";
		// Add some key attributes
		message = (Integer.toString(quesMan.getNUM_QUESTIONS()));
		message = message + (" ");
		message = message + Integer.toString(quesMan.getQIDbits());
		message = message + (" ");
		message = message + Integer.toString(quesMan.getOutputBits());
		message = message + (" ");
		// Add question IDs and next IDs
		for (int i = 0; i < to_write.size(); i++) {
			byte[] temp = to_write.get(i);
			for (int j = 0; j < temp.length; j++) {
				message = message + (char) temp[j];
			}
		}
		return message;
	}

	/** Store the server's response
	 * @param response	The response
	 */
	public void storeResponse(String response) {
		// Split into lines
		String[] response_arr = response.split("BREAK");
		PrintWriter pw;
		try {
			// Open Encog file
			pw = new PrintWriter(new FileOutputStream(new File(
					context.getFilesDir(), "Encog")));
			// Write to file
			for (int i = 0; i < response_arr.length; i++) {
				pw.write(response_arr[i] + "\n");
			}
			pw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
