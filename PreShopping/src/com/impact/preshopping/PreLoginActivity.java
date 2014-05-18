package com.impact.preshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PreLoginActivity extends Activity {

	private Button btnExistingUser;
	private Button btnNewUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prelogin);

	    getActionBar().setTitle("PreShopping");
//	    getActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.black));
		
		btnExistingUser = (Button) findViewById(R.id.btnExistingUser);
		btnNewUser = (Button) findViewById(R.id.btnNewUser);
		
		btnExistingUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent signin = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(signin);
				finish();
			}
		});
		
		btnNewUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent registration = new Intent(getApplicationContext(), RegistrationActivity.class);
				startActivity(registration);
				finish();
			}
		});
	}
}
