package com.debdroid.androidgaerestapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	//	Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
	//	setSupportActionBar(toolbar);
		ConnectivityManager cm =
		        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if (isConnected) {
			Toast.makeText(getApplicationContext(), "Connected to internet", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Not Connected to internet, application will not work", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		return super.onOptionsItemSelected(item);
	}

	public void addEmployee(View v) {
		Intent mIntent = new Intent(this, AddEmployee.class);
		startActivity(mIntent);
	}

	public void getDelUpdEmployee(View v) {
		Intent mIntent = new Intent(this, GetUpdDelEmployee.class);
		startActivity(mIntent);
	}

	public void listEmployee(View v) {
		Intent mIntent = new Intent(this, ListEmployee.class);
		startActivity(mIntent);
	}

	public void listDegEmployee(View v) {
		Intent mIntent = new Intent(this, ListByDesignation.class);
		startActivity(mIntent);
	}
}
