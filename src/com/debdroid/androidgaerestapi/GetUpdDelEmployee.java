package com.debdroid.androidgaerestapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.debdroid.apiwithjdo.dao.employeeendpoint.Employeeendpoint;
import com.debdroid.apiwithjdo.dao.employeeendpoint.model.Employee;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.gson.GsonFactory;

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
import android.widget.EditText;
import android.widget.Toast;

public class GetUpdDelEmployee extends AppCompatActivity {

	EditText editEid;
	EditText editName;
	EditText editDeg;
	EditText editSalary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_employee);
		editEid = (EditText) findViewById(R.id.editEidGetEmp);
		editName = (EditText) findViewById(R.id.editNameGetEmp);
		editDeg = (EditText) findViewById(R.id.editDegGetEmp);
		editSalary = (EditText) findViewById(R.id.editSalaryGetEmp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_employee, menu);
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

	public void getEmployee(View v) {
		String textEid = editEid.getText().toString();
		if(textEid.length() == 0 )
		{
			Toast.makeText(
					getBaseContext(),
					"Enter the employee id and then hit the button!",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			String[] params = { textEid };
			new GetEmployeeAsyncTask(GetUpdDelEmployee.this).execute(params);
		}
	}

	public void updEmployee(View v) {
		String textEid = editEid.getText().toString();
		String textName = editName.getText().toString();
		String textDeg = editDeg.getText().toString();
		String textSalary = editSalary.getText().toString();
		if (textEid.length() == 0 && textName.length() == 0
				&& textDeg.length() == 0 && textSalary.length() == 0) {
			Toast.makeText(
					getBaseContext(),
					"First get an employee then try to update or enter valid data!",
					Toast.LENGTH_SHORT).show();
		} else {
			String[] params = { textEid, textName, textDeg, textSalary };
			new UpdEmployeeAsyncTask(GetUpdDelEmployee.this).execute(params);
		}
	}

	public void delEmployee(View v) {
		String textEid = editEid.getText().toString();
		if (textEid.length() == 0) {
			Toast.makeText(
					getBaseContext(),
					"First get an employee then try to delete or enter valid Emloyee id!",
					Toast.LENGTH_SHORT).show();
		} else {
			String[] params = { textEid };
			new DelEmployeeAsyncTask(GetUpdDelEmployee.this).execute(params);
		}
	}

	private class GetEmployeeAsyncTask extends
			AsyncTask<String, Void, Employee> {
		Context context;
		private ProgressDialog pd;
		String errorCode = null;
		String errorMessage = null;

		public GetEmployeeAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Getting the Employee...");
			pd.show();
		}

		protected Employee doInBackground(String... params) {
			Employee response = null;
			try {
				Employeeendpoint.Builder builder = new Employeeendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Employeeendpoint service = builder.build();
				response = service.getEmployee(Long.parseLong(params[0]))
						.execute();
			} catch (Exception e) {
				Log.d("Could not Get Employee", e.getMessage(), e);
				if (e instanceof GoogleJsonResponseException) {
					GoogleJsonResponseException gresp = (GoogleJsonResponseException) e;
					JSONObject jObj = new JSONObject(gresp.getDetails());
					Log.d("DEB",
							"The gresp jsonerror->message=" + jObj.toString());
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
					Log.d("DEB",
							"The exception is not a type of GoogleJsonResponseException");
				}
			}
			return response;
		}

		protected void onPostExecute(Employee emp) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			if (emp == null) {
				Log.d("DEB", "Employee data is NULL");
				Toast.makeText(getBaseContext(),
						"Employee data could not be retrieved. Error code = "+errorCode+" & error message = "+errorMessage,
						Toast.LENGTH_SHORT).show();
			} else {
				Log.d("DEB", "Employee data is NOT NULL");
				editName.setText(emp.getEname());
				editSalary.setText(emp.getSalary().toString());
				editDeg.setText(emp.getDeg());

				// Display success message to user
				Toast.makeText(getBaseContext(),
						"Employee data retrieved succesfully",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class UpdEmployeeAsyncTask extends
			AsyncTask<String, Void, Employee> {
		Context context;
		private ProgressDialog pd;
		String errorCode = null;
		String errorMessage = null;

		public UpdEmployeeAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Updating the Employee...");
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
				emp.setEid(Long.parseLong(params[0]));
				emp.setEname(params[1]);
				emp.setDeg(params[2]);
				emp.setSalary(Double.parseDouble(params[3]));
				response = service.updateEmployee(emp).execute();
			} catch (Exception e) {
				Log.d("Could not Update Employee", e.getMessage(), e);
				if (e instanceof GoogleJsonResponseException) {
					GoogleJsonResponseException gresp = (GoogleJsonResponseException) e;
					JSONObject jObj = new JSONObject(gresp.getDetails());
					Log.d("DEB",
							"The gresp jsonerror->message=" + jObj.toString());
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
					Log.d("DEB",
							"The exception is not a type of GoogleJsonResponseException");
				}
			}
			return response;
		}

		protected void onPostExecute(Employee emp) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			if (emp != null) {
				// Display success message to user
				editName.setText(emp.getEname());
				editSalary.setText(emp.getSalary().toString());
				editDeg.setText(emp.getDeg());
				Toast.makeText(getBaseContext(),
						"Employee data updated succesfully", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(
						getBaseContext(),
						"Employee data NOT updated succesfully. Error code = "
								+ errorCode + " & error message = "
								+ errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class DelEmployeeAsyncTask extends AsyncTask<String, Void, Integer> {
		Context context;
		private ProgressDialog pd;
		String errorCode = null;
		String errorMessage = null;

		public DelEmployeeAsyncTask(Context context) {
			this.context = context;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Deleting the Employee...");
			pd.show();
		}

		protected Integer doInBackground(String... params) {

			try {
				Employeeendpoint.Builder builder = new Employeeendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Employeeendpoint service = builder.build();
				service.removeEmployee(Long.parseLong(params[0])).execute();
			} catch (Exception e) {
				Log.d("Could not Delete Employee", e.getMessage(), e);
				if (e instanceof GoogleJsonResponseException) {
					GoogleJsonResponseException gresp = (GoogleJsonResponseException) e;
					JSONObject jObj = new JSONObject(gresp.getDetails());
					Log.d("DEB",
							"The gresp jsonerror->message=" + jObj.toString());
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
					Log.d("DEB",
							"The exception is not a type of GoogleJsonResponseException");
				}
				return -1;
			}
			return 0;
		}

		protected void onPostExecute(Integer resp) {
			// Clear the progress dialog and the fields
			pd.dismiss();
			Log.d("DEB", "Reponse from the background= " + resp);
			// Display success message to user
			if (resp == 0) {
				Toast.makeText(getBaseContext(),
						"Employee data deleted succesfully", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(
						getBaseContext(),
						"Problem! Employee data not deleted. Error code = "
								+ errorCode + " & error message = "
								+ errorMessage, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
