package com.debdroid.androidgaerestapi;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.debdroid.apiwithjdo.dao.employeeendpoint.Employeeendpoint;
import com.debdroid.apiwithjdo.dao.employeeendpoint.model.Employee;
import com.debdroid.apiwithjdo.dao.employeeendpoint.model.EmployeeCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class ListByDesignation extends AppCompatActivity {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter = null;
	private RecyclerView.LayoutManager mLayoutManager;
	Context appContext;
	EditText editDeg = null;
	String degText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Allow activity to show indeterminate progress-bar */
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_list_by_designation);
		appContext = getApplicationContext();
		mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_deg);
		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(appContext);
		mRecyclerView.setLayoutManager(mLayoutManager);
		editDeg = (EditText) findViewById(R.id.editTextDeg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_employee, menu);
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

	// public void listEmployee(View v) {
	// new EmployeeListAsyncTask(ListEmployee.this).execute();
	// }

	public void degListShow(View v) {
		degText = editDeg.getText().toString();
		if (degText.length() == 0) {
			Toast.makeText(getBaseContext(),
					"Enter the designation and then hit the button!",
					Toast.LENGTH_LONG).show();
		} else {
			String[] params = { degText };
			new EmployeeDegListAsyncTask(ListByDesignation.this)
					.execute(params);
		}
	}

	private class EmployeeDegListAsyncTask extends
			AsyncTask<String, Void, EmployeeCollection> {
		private ProgressDialog pd;
		Context context;

		public EmployeeDegListAsyncTask(Context ctx) {
			context = ctx;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Retrieving Employee list...");
			pd.show();
		}

		protected EmployeeCollection doInBackground(String... params) {
			EmployeeCollection employees = null;
			try {
				Employeeendpoint.Builder builder = new Employeeendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				Employeeendpoint service = builder.build();
				employees = service.listByDesignation(params[0]).execute();
			} catch (Exception e) {
				Log.d("Could not retrieve Employees", e.getMessage(), e);
			}
			return employees;
		}

		protected void onPostExecute(EmployeeCollection employees) {
			pd.dismiss();
			if (employees != null) {
				// Do something with the result.
				Log.d("DEB", "In onPostExecute now...");
				List<Employee> _list = employees.getItems();
				if(_list != null) {
				Log.d("DEB", "Data->" + employees.toString());
				mAdapter = new DebAdapter(_list, appContext);
				mRecyclerView.setAdapter(mAdapter);
				}
				else {
					Toast.makeText(getBaseContext(),
							"No record found for the designation "+degText,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getBaseContext(),
						"Not able to get the list from server!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
