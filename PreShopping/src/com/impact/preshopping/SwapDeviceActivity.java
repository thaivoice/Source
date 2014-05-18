package com.impact.preshopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.impact.preshopping.activity.RegistrationActivity2;

public class SwapDeviceActivity extends Activity {

	private Button btnExit;
	private Button btnOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_swap_device);

		btnExit = (Button) findViewById(R.id.btnExit);
		btnOk = (Button) findViewById(R.id.btnOk);
		
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent updateReg = new Intent(getApplicationContext(), RegistrationActivity2.class);
				updateReg.putExtra("REQUEST_SWAP_DEVICE", true);
				startActivity(updateReg);
				finish();
			}
		});
	}
}
