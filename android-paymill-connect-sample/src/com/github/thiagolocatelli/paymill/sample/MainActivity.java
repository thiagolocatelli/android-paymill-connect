package com.github.thiagolocatelli.paymill.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.github.thiagolocatelli.paymill.PaymillApp;
import com.github.thiagolocatelli.paymill.PaymillApp.CONNECT_MODE;
import com.github.thiagolocatelli.paymill.sample.R;
import com.github.thiagolocatelli.paymill.PaymillButton;
import com.github.thiagolocatelli.paymill.PaymillConnectListener;
import com.stripe.Stripe;

public class MainActivity extends ActionBarActivity {

	private PaymillApp mApp, mApp2;
	private TextView tvSummary;
	private PaymillButton mStripeButton, mStripeButton2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mApp = new PaymillApp(this, "PaymillDemo", ApplicationData.CLIENT_ID, 
				ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);

		tvSummary = (TextView) findViewById(R.id.tvSummary);
		if (mApp.isConnected()) {
			tvSummary.setText("Connected as " + mApp.getAccessToken());
		}
		
		mStripeButton = (PaymillButton) findViewById(R.id.btnConnect1);
		mStripeButton.setPaymillApp(mApp);
		mStripeButton.addStripeConnectListener(new PaymillConnectListener() {

			@Override
			public void onConnected() {
				tvSummary.setText("Connected as " + mApp.getAccessToken());
			}

			@Override
			public void onDisconnected() {
				tvSummary.setText("Disconnected");
			}

			@Override
			public void onError(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
			
		});

		mApp2 = new PaymillApp(this, "PaymillDemo", ApplicationData.CLIENT_ID, 
				ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);
		mStripeButton2 = (PaymillButton) findViewById(R.id.btnConnect2);
		mStripeButton2.setPaymillApp(mApp2);
		mStripeButton2.setConnectMode(CONNECT_MODE.ACTIVITY);
		
		Stripe.apiKey = mApp.getAccessToken();
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(resultCode) {
		case PaymillApp.RESULT_CONNECTED:
			tvSummary.setText("Connected as " + mApp.getAccessToken());
			break;
		case PaymillApp.RESULT_ERROR:
			String error_description = data.getStringExtra("error_description");
			Toast.makeText(MainActivity.this, error_description, Toast.LENGTH_SHORT).show();
			break;
		}
		
	}


}
