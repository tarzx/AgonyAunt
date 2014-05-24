package com.example.agonyaunt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import android.content.Context;

/** This class represents the Baby Aritficial Neural Network
 * @author Abigail Lowe
 */
public class Net {
	
	// Question manager
	QuestionManager qMan = new QuestionManager();
	// Neural net
	BasicNetwork network;
	// Context
	Context context;
	// Temporary file to use
	static File file;
	// Input and output streams
	InputStream is;
	static FileOutputStream fs;
	// Neural net's output
	double[] a_output;
	// Output as a qID
	int fin_output = 0;

	public Net(Context context) {
		this.context = context;
	}

	/** Loads the Encog file
	 * @param qID	The Tier 1 question ID
	 */
	public void load(double[] qID) {
		try {
			// Load up Encog file
			file = new File(context.getFilesDir(), "Encog");
			// If for some reason it doesn't exist, a backup exists in Assets file
			if (file.createNewFile() || file.length()==0){
				// Open neural net file
				is = context.getAssets().open("Encog");
				// Buffer
				byte[] b = new byte[is.available()];
				// Read in NN file to buffer
				is.read(b);
				is.close();
				// Write buffer to file 
				fs = new FileOutputStream(file);
				fs.write(b);
				fs.close();
			}
		
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// If everything succeeded, we can load the NN!
		createNet(qID);
	}
	
	/** Creates a ANN from the file
	 * @param qID	The Tier 1 question ID
	 */
	public void createNet(double[] qID){
		// Load from the file object
		network = (BasicNetwork) EncogDirectoryPersistence.loadObject(file);
		// Get the output for this input
		getOutput(qID);
	}

	/** Gets the output from the ANN
	 * @param qID	The Tier 1 question ID
	 */
	public void getOutput(double[] qID) {
		// Turn the input into MLData
		MLData input = new BasicMLData(qID);
		// Compute!
		MLData output = network.compute(input);
		
		// Iterate through output
		for (int i = 0; i < output.size(); i++) {
			// Cast it to an int and round it correctly
			a_output = output.getData();
			fin_output = (int) (a_output[i] + 0.5);
		}
		
		qMan.setSub_index(fin_output);
		// Shut down
		Encog.getInstance().shutdown();
	}

	// Getters
	public int getOutput() {
		return fin_output;
	}
	
}
