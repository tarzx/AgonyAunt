package com.example.agonyaunt;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/** This class represent myProfile
 * @author Jiachun Liu
 * @author Teng
 */
public class MyProfile extends Activity {

	boolean initialIntervention = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);

		// Fields
		EditText editUsername = (EditText) findViewById(R.id.username); // global
		EditText editAge = (EditText) findViewById(R.id.age);
		EditText editOcc = (EditText) findViewById(R.id.occupation);
		RadioButton editSex0 = (RadioButton) findViewById(R.id.sex0);
		RadioButton editSex1 = (RadioButton) findViewById(R.id.sex1);

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

		// Get user input
	    String uname = sharedPref.getString("userName", "");
		String age = sharedPref.getString("userAge", "");
		String occ = sharedPref.getString("userOccupation", "");

		// Sex
		String s0 = sharedPref.getString("sex0", "");
		String s1 = sharedPref.getString("sex1", "");
		boolean sex0 = Boolean.parseBoolean(s0);
		boolean sex1 = Boolean.parseBoolean(s1);

        Log.i("System.out", "uName: " + uname + " Age: " + age + " Occ: " + occ + " sex0: " + sex0 + " sex1: " + sex1);
		// Handle new user
//		if (uname.equals("") && age.equals("") && occ.equals("")
//				&& s0.equals("") && s1.equals("")) {
//			initialIntervention = true;
//		}



        // Update variables
		editUsername.setText(uname);
		editAge.setText(age);
        editOcc.setText(occ);
		editSex0.setChecked(sex0);
		editSex1.setChecked(sex1);



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void backButtonProfile(View view) {
		// Closes activity. Goes back to activity which launched it
		this.finish();
	}

	/** Stores data
	 * @param uname	The user name
	 * @param age	The user's age
	 * @param occ	The user's occupation
	 * @param sex0	If they are male
	 * @param sex1	If they are female
	 * @return true	If data is stored successfully 
	 */
	public void createProfile(String uname, String age, String occ, boolean sex0, boolean sex1) {

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
//        editor.put(Key, value)
		editor.putString("userName", uname);
		editor.putString("userAge", age);
		editor.putString("userOccupation", occ);
		editor.putString("sex0", Boolean.toString(sex0));
		editor.putString("sex1", Boolean.toString(sex1));
		editor.commit();
        Toast.makeText(this, "Profile has been saved", Toast.LENGTH_SHORT).show();
	}

    public void saveProfile(View view){
        EditText name = (EditText) findViewById(R.id.username);
        String userName = String.valueOf(name.getText());

        EditText age = (EditText) findViewById(R.id.age);
        String userAge = String.valueOf(age.getText());

        EditText occupation = (EditText) findViewById(R.id.occupation);
        String userOccupation = String.valueOf(occupation.getText());

        RadioButton btnSex0 = (RadioButton) findViewById(R.id.sex0);
        boolean sex0 = btnSex0.isChecked();

        RadioButton btnSex1 = (RadioButton) findViewById(R.id.sex1);
        boolean sex1 = btnSex1.isChecked();
        createProfile(userName, userAge, userOccupation, sex0, sex1);
    }
}
