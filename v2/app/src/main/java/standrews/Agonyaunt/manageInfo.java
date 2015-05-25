package standrews.Agonyaunt;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** This class represents the main menu
 * @author Patomporn Loungvara
 */
public class manageInfo {
    // Creating JSON Parser object
    private static JSONParser jParser = new JSONParser();

    public static  ArrayList<HashMap<String, String>> getAllPatientsInfo() {
        ArrayList<HashMap<String, String>> patientsList = null;

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(Util.url_all_patients_info, "GET", params);

            // Check your log cat for JSON response
            Log.d("All Patients: ", json.toString());

            // Checking for SUCCESS TAG
            int success = json.getInt(Util.TAG_SUCCESS);

            if (success == 1) {
                // patients found
                patientsList = new ArrayList<HashMap<String, String>>();

                // Getting Array of Patients
                JSONArray patients = json.getJSONArray(Util.TAG_PATIENTS);

                // looping through All Patient
                for (int i = 0; i < patients.length(); i++) {
                    JSONObject c = patients.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(Util.TAG_PID);
                    String name = c.getString(Util.TAG_NAME);
                    String age = c.getString(Util.TAG_AGE);
                    String gender = c.getString(Util.TAG_GENDER);
                    String set_frequency = c.getString(Util.TAG_SET_FREQ);
                    String set_slot = c.getString(Util.TAG_SET_SLOT);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(Util.TAG_PID, id);
                    map.put(Util.TAG_NAME, name);
                    map.put(Util.TAG_AGE, age);
                    map.put(Util.TAG_GENDER, gender.equals("0") ? "Male" : (gender.equals("1") ? "Female" : ""));
                    map.put(Util.TAG_SET_FREQ, set_frequency);
                    map.put(Util.TAG_SET_SLOT, set_slot);

                    // adding HashList to ArrayList
                    patientsList.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return patientsList;
    }

    public static HashMap<String, String> getPatientInfo(String pid, String ctlLv) {
        HashMap<String, String> patientInfo = null;

        try {
            // Building Parameters
            Log.d("What is the value of pid", pid);
            Log.d("What is the value of ctlLv", ctlLv);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Util.TAG_PID, pid));
            params.add(new BasicNameValuePair(Util.TAG_CONTROL_LEVEL, ctlLv));

            // getting patient details by making HTTP request
            // Note that patient details url will use GET request
            JSONObject json = jParser.makeHttpRequest(Util.url_patient_info, "GET", params);

            // check your log for json response
            Log.d("Single Patient Details", json.toString());

            // json success tag
            int success = json.getInt(Util.TAG_SUCCESS);
            Log.d("The success value is", success+"");

            if (success == 1) {
                patientInfo = new HashMap<String, String>();

                // successfully received patient details
                JSONArray patientObj = json.getJSONArray(Util.TAG_PATIENT); // JSON Array

                // looping through All Patient
                if (patientObj.length() > 0) {
                    // get first patient object from JSON Array
                    JSONObject patient = patientObj.getJSONObject(0);

                    Log.d("Json object patient: ", patient.toString());

                    // Storing each json item in variable
                    String id = patient.getString(Util.TAG_PID);
                    String name = patient.getString(Util.TAG_NAME);
                    String age = patient.getString(Util.TAG_AGE);
                    String gender = patient.getString(Util.TAG_GENDER);
                    String freq1 = (patient.isNull(Util.TAG_FREQ1)?"":patient.getString(Util.TAG_FREQ1));
                    String freq2 = (patient.isNull(Util.TAG_FREQ2)?"":patient.getString(Util.TAG_FREQ2));
                    String freq3 = (patient.isNull(Util.TAG_FREQ3)?"":patient.getString(Util.TAG_FREQ3));
                    String freq4 = (patient.isNull(Util.TAG_FREQ4)?"":patient.getString(Util.TAG_FREQ4));
                    String freq5 = (patient.isNull(Util.TAG_FREQ5)?"":patient.getString(Util.TAG_FREQ5));
                    String freq6 = (patient.isNull(Util.TAG_FREQ6)?"":patient.getString(Util.TAG_FREQ6));
                    String freq7 = (patient.isNull(Util.TAG_FREQ7)?"":patient.getString(Util.TAG_FREQ7));
                    String slot1 = (patient.isNull(Util.TAG_SLOT1)?"":patient.getString(Util.TAG_SLOT1));
                    String slot2 = (patient.isNull(Util.TAG_SLOT2)?"":patient.getString(Util.TAG_SLOT2));
                    String slot3 = (patient.isNull(Util.TAG_SLOT3)?"":patient.getString(Util.TAG_SLOT3));
                    String slot4 = (patient.isNull(Util.TAG_SLOT4)?"":patient.getString(Util.TAG_SLOT4));
                    String slot5 = (patient.isNull(Util.TAG_SLOT5)?"":patient.getString(Util.TAG_SLOT5));
                    String slot6 = (patient.isNull(Util.TAG_SLOT6)?"":patient.getString(Util.TAG_SLOT6));

                    // adding each child node to HashMap key => value
                    patientInfo.put(Util.TAG_PID, id);
                    patientInfo.put(Util.TAG_NAME, name);
                    patientInfo.put(Util.TAG_AGE, age);
                    patientInfo.put(Util.TAG_GENDER, gender);
                    patientInfo.put(Util.TAG_FREQ1, freq1);
                    patientInfo.put(Util.TAG_FREQ2, freq2);
                    patientInfo.put(Util.TAG_FREQ3, freq3);
                    patientInfo.put(Util.TAG_FREQ4, freq4);
                    patientInfo.put(Util.TAG_FREQ5, freq5);
                    patientInfo.put(Util.TAG_FREQ6, freq6);
                    patientInfo.put(Util.TAG_FREQ7, freq7);
                    patientInfo.put(Util.TAG_SLOT1, slot1);
                    patientInfo.put(Util.TAG_SLOT2, slot2);
                    patientInfo.put(Util.TAG_SLOT3, slot3);
                    patientInfo.put(Util.TAG_SLOT4, slot4);
                    patientInfo.put(Util.TAG_SLOT5, slot5);
                    patientInfo.put(Util.TAG_SLOT6, slot6);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return patientInfo;
    }

    public static int updatePatient(String pid, String name, String age, String gender) {
        int success = 0;
        try {
            Log.d("Track: ", "update patient");

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Util.TAG_PID, pid));
            params.add(new BasicNameValuePair(Util.TAG_NAME, name));
            params.add(new BasicNameValuePair(Util.TAG_AGE, age));
            params.add(new BasicNameValuePair(Util.TAG_GENDER, gender));

            // sending modified data through http request
            // Notice that update patient url accepts POST method
            JSONObject json = jParser.makeHttpRequest(Util.url_update_patient, "POST", params);

            // check json success tag
            success = json.getInt(Util.TAG_SUCCESS);

            Log.d("Json object patient: ", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            success = 0;
        }

        return success;
    }

    public static int updatePatientPreference(String pid, int set_slot, int[] slots, int set_frequency, int[] frequencies) {
        int success = 0;
        try {
            Log.d("Track: ", "update patient preference");

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Util.TAG_PID, pid));
            params.add(new BasicNameValuePair(Util.TAG_SET_SLOT, String.valueOf(set_slot)));
            params.add(new BasicNameValuePair(Util.TAG_SET_FREQ, String.valueOf(set_frequency)));
            if (set_slot==1) {
                params.add(new BasicNameValuePair(Util.TAG_SLOT1, String.valueOf(slots[0])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT2, String.valueOf(slots[1])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT3, String.valueOf(slots[2])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT4, String.valueOf(slots[3])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT5, String.valueOf(slots[4])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT6, String.valueOf(slots[5])));
            }
            if (set_frequency==1) {
                params.add(new BasicNameValuePair(Util.TAG_FREQ1, String.valueOf(frequencies[0])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ2, String.valueOf(frequencies[1])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ3, String.valueOf(frequencies[2])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ4, String.valueOf(frequencies[3])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ5, String.valueOf(frequencies[4])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ6, String.valueOf(frequencies[5])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ7, String.valueOf(frequencies[6])));
            }

            // sending modified data through http request
            // Notice that update patient url accepts POST method
            JSONObject json = jParser.makeHttpRequest(Util.url_set_preference, "POST", params);

            // check json success tag
            success = json.getInt(Util.TAG_SUCCESS);

            Log.d("Json object patient preference: ", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            success = 0;
        }

        return success;
    }

    public static int deletePatient(String pid) {
        // Check for success tag
        int success= 0;
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", pid));

            // getting patient details by making HTTP request
            JSONObject json = jParser.makeHttpRequest(
                    Util.url_delete_patient, "POST", params);

            // json success tag
            success = json.getInt(Util.TAG_SUCCESS);

            // check your log for json response
            Log.d("Delete patient", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static int createPatient(String name, String age, String gender, int set_slot, int[] slots, int set_frequency, int[] frequencies) {
        int success = 0;

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Util.TAG_NAME, name));
            params.add(new BasicNameValuePair(Util.TAG_AGE, age));
            params.add(new BasicNameValuePair(Util.TAG_GENDER, gender));
            params.add(new BasicNameValuePair(Util.TAG_SET_SLOT, String.valueOf(set_slot)));
            params.add(new BasicNameValuePair(Util.TAG_SET_FREQ, String.valueOf(set_frequency)));
            if (set_slot==1) {
                params.add(new BasicNameValuePair(Util.TAG_SLOT1, String.valueOf(slots[0])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT2, String.valueOf(slots[1])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT3, String.valueOf(slots[2])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT4, String.valueOf(slots[3])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT5, String.valueOf(slots[4])));
                params.add(new BasicNameValuePair(Util.TAG_SLOT6, String.valueOf(slots[5])));
            }
            if (set_frequency==1) {
                params.add(new BasicNameValuePair(Util.TAG_FREQ1, String.valueOf(frequencies[0])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ2, String.valueOf(frequencies[1])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ3, String.valueOf(frequencies[2])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ4, String.valueOf(frequencies[3])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ5, String.valueOf(frequencies[4])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ6, String.valueOf(frequencies[5])));
                params.add(new BasicNameValuePair(Util.TAG_FREQ7, String.valueOf(frequencies[6])));
            }

            // getting JSON Object
            // Note that create patient url accepts POST method
            JSONObject json = jParser.makeHttpRequest(Util.url_create_patient, "POST", params);

            // check for success tag
            success = json.getInt(Util.TAG_PID);

            // check log cat fro response
            Log.d("Create Response", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }
}
