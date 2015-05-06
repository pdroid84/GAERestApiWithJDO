package com.debdroid.androidgaerestapi;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.debdroid.apiwithjdo.dao.employeeendpoint.Employeeendpoint;
import com.debdroid.apiwithjdo.dao.employeeendpoint.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.gson.GsonFactory;

public class AddEmployee extends AppCompatActivity {

	EditText editID;
	EditText editName;
	EditText editSalary;
	EditText editDeg;
	Button btnAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_employee);
		editID = (EditText) findViewById(R.id.editTextID);
		editName = (EditText) findViewById(R.id.editTextName);
		editSalary = (EditText) findViewById(R.id.editTextSalary);
		editDeg = (EditText) findViewById(R.id.editTextDeg);
		btnAdd = (Button) findViewById(R.id.buttonAdd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_employee, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == android.R.id.home){
			NavUtils.navigateUpFromSameTask(this);
	        return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void addEmployee(View v) {
		String textID = editID.getText().toString();
		Log.d("DEB", "Eid string = " + textID);
		System.out.println("Eid string = " + textID);
		if (textID == "") {
			Log.d("DEB", "textID is SPACE");
		} else {
			Log.d("DEB", "textID is NOT SPACE");
		}
		if (textID == null) {
			Log.d("DEB", "textID is null");
		} else {
			Log.d("DEB", "textID is NOT null");
		}
		if (textID.length() == 0) {
			Log.d("DEB", "textID is blank");
			textID = null;
		} else {
			Log.d("DEB", "textID is NOT blank");
		}
		String textName = editName.getText().toString();
		String textSalary = editSalary.getText().toString();
		String textDeg = editDeg.getText().toString();
		if (textName.length() == 0 || textSalary.length() == 0
				|| textDeg.length() == 0) {
			Toast.makeText(getBaseContext(),
					"Except EID, all fields are mandatory.", Toast.LENGTH_SHORT)
					.show();
		} else {
			String[] params = { textName, textSalary, textDeg, textID };
			new AddEmployeeAsyncTask(AddEmployee.this).execute(params);
		}
	}

	private class AddEmployeeAsyncTask extends
			AsyncTask<String, Void, Employee> {
		Context context;
		private ProgressDialog pd;
		String errorCode;
		String errorMessage;

		public AddEmployeeAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Adding the Employee...");
			pd.show();
		}

		protected Employee doInBackground(String... params) {
			Employee response = null;
			try {
				Employeeendpoint.Builder builder = new Employeeendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Employeeendpoint service = builder.build();
				Employee emp = new Employee();
				emp.setEname(params[0]);
				emp.setSalary(Double.parseDouble(params[1]));
				emp.setDeg(params[2]);
				if (params[3] != null) {
					Log.d("DEB", "EID is NOT blank, so passing to server!");
					emp.setEid(Long.parseLong(params[3]));
				} else {
					Log.d("DEB", "EID is blank, so not passing to server!");
				}
				response = service.insertEmployee(emp).execute();
			} catch (IOException e) {
				Log.d("Could not Add Employee", e.getMessage(), e);
				Log.d("DEB", "The error message->" + e.toString());
				e.printStackTrace();
				if (e instanceof GoogleJsonResponseException) {
					GoogleJsonResponseException gresp = (GoogleJsonResponseException) e;
					JSONObject jObj = new JSONObject(gresp.getDetails());
					Log.d("DEB", "The gresp jsonerror->message=" + jObj.toString());
					try {
						errorCode = jObj.getString("code");
						JSONArray jArray = jObj.getJSONArray("errors");
						JSONObject errMsg = jArray.getJSONObject(0);
						errorMessage = errMsg.getString("message");
						Log.d("DEB", "The gresp->code=" + errorCode);
						Log.d("DEB1", "The gresp->message=" + errorMessage);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					Log.d("DEB", "The exception is not a type of GoogleJsonResponseException");
				}

			}
			return response;
		}

		protected void onPostExecute(Employee emp) {

			// Clear the progress dialog and the fields
			pd.dismiss();
			if (emp != null) {
				editName.setText("");
				editSalary.setText("");
				editDeg.setText("");
				editID.setText("");
				// Display success message to user
				Toast.makeText(getBaseContext(), "Employee added succesfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						getBaseContext(),
						"Problem with Employee add. Error code = " + errorCode
								+ " & Error message = " + errorMessage,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
