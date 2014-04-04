package com.github.thiagolocatelli.paymill;

import com.github.thiagolocatelli.paymill.R;
import com.github.thiagolocatelli.paymill.PaymillApp.CONNECT_MODE;
import com.github.thiagolocatelli.paymill.PaymillApp.OAuthAuthenticationListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PaymillButton extends Button {
	
	private PaymillApp mPaymillApp;
	private Context mContext;
	private PaymillConnectListener mPaymillConnectListener;
	private CONNECT_MODE mConnectMode = CONNECT_MODE.DIALOG;

	public PaymillButton(Context context) {
		super(context);
		mContext = context;
		setupButton();
	}

	public PaymillButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setupButton();
	}

	public PaymillButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupButton();
	}

	private void setupButton() {
		
		if(mPaymillApp == null) {
			setButtonText(R.string.btnConnectText);
		}
		
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		
		setClickable(true);
		setBackgroundResource(R.drawable.button_paymill_connect);
		Drawable img = getContext().getResources().getDrawable(R.drawable.button_paymill_icon);
		img.setBounds( 0, 0, dpToPx(32), dpToPx(32) );
		setCompoundDrawables(img, null, null, null);
		
		setTextColor(getResources().getColor(R.color.paymill_btn_label_color));
		setTypeface(Typeface.DEFAULT_BOLD);
		
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(mPaymillApp == null) {
					Toast.makeText(mContext, 
							"PaymillApp obect needed. Call PaymillButton.setPaymillApp()", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mPaymillApp.isConnected()) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage(
						getResources().getString(R.string.dialogDisconnectText))
						.setCancelable(false)
						.setPositiveButton(getResources().getString(R.string.btnDialogYes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									mPaymillApp.resetAccessToken();
									setButtonText(R.string.btnConnectText);
									mPaymillApp.getOAuthAuthenticationListener().onSuccess();
								}
							})
						.setNegativeButton(getResources().getString(R.string.btnDialogNo),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
					final AlertDialog alert = builder.create();
					alert.show();					
				}
				else {
					if(mConnectMode == CONNECT_MODE.DIALOG) {
						mPaymillApp.displayDialog();
					}
					else {
						Activity parent = (Activity) mContext;
						Intent i = new Intent(getContext(), PaymillActivity.class);
						i.putExtra("url", mPaymillApp.getAuthUrl());
						i.putExtra("callbackUrl", mPaymillApp.getCallbackUrl());
						i.putExtra("tokenUrl", mPaymillApp.getTokenUrl());
						i.putExtra("secretKey", mPaymillApp.getSecretKey());
						i.putExtra("accountName", mPaymillApp.getAccountName());
						parent.startActivityForResult(i, PaymillApp.STRIPE_CONNECT_REQUEST_CODE);
					}
				}
				
			}
			
		});
		
	}
	
	private void setButtonText(int resourceId) {
		setText(resourceId);
	}
	
	/**
	 * 
	 * @param connectMode
	 */
	public void setConnectMode(CONNECT_MODE connectMode) {
		mConnectMode = connectMode;
	}
	
	/**
	 * 
	 * @param stripeApp
	 */
	public void setPaymillApp(PaymillApp paymillApp) {
		mPaymillApp = paymillApp;
		mPaymillApp.setListener(getOAuthAuthenticationListener());
		
		if(mPaymillApp.isConnected()) {
			setButtonText(R.string.btnDisconnectText);
		}
		else {
			setButtonText(R.string.btnConnectText);
		}
	}
	

	/**
	 * 
	 * @param paymillConnectListener
	 */
	public void addStripeConnectListener(PaymillConnectListener paymillConnectListener) {
		mPaymillConnectListener = paymillConnectListener;
		if(mPaymillApp != null) {
			mPaymillApp.setListener(getOAuthAuthenticationListener());
		}
	}
	
	private OAuthAuthenticationListener getOAuthAuthenticationListener() {
		
		return new OAuthAuthenticationListener() {

			@Override
			public void onSuccess() {
				Log.d("PaymillButton", "Calling OAuthAuthenticationListener.onSuccess()");
				if(mPaymillConnectListener != null) {
					if(mPaymillApp.isConnected()) {
						Log.d("PaymillButton", "Connected");
						setButtonText(R.string.btnDisconnectText);
						Log.d("PaymillButton", "Calling mStripeConnectListener.onConnected()");
						mPaymillConnectListener.onConnected();
					}
					else {
						Log.d("PaymillButton", "Disconnected");
						Log.d("PaymillButton", "Calling mStripeConnectListener.onDisconnected()");
						setButtonText(R.string.btnConnectText);
						mPaymillConnectListener.onDisconnected();
					}
				}
				else {
					Log.d("PaymillButton", "mStripeConnectListener is null");
				}
			}

			@Override
			public void onFail(String error) {
				Log.i("PaymillButton", "Calling OAuthAuthenticationListener.onFail()");
				if(mPaymillConnectListener != null) {
					mPaymillConnectListener.onError(error);
				}
			}
		};
	}
	
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}

	public int pxToDp(int px) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}

}
