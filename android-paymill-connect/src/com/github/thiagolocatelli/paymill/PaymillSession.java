package com.github.thiagolocatelli.paymill;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.util.Log;

public class PaymillSession {

	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String SHARED = "_PaymillAccount_Preferences";
	private static final String API_MERCHANT_ID = "merchant_id";
	private static final String API_ACCESS_TOKEN = "access_token";
	private static final String API_REFRESH_TOKEN = "refresh_token";
	private static final String API_TOKEN_TYPE = "token_type";
	private static final String API_LIVE_MODE = "live_mode";

	public PaymillSession(Context context, String accountName) {
		Log.i("PaymillSession", "PaymillSession[accountName]:					" + accountName);
		sharedPref = context.getSharedPreferences(accountName + SHARED, Context.MODE_PRIVATE);
	}

	public void storeMerchantId(String merchantId) {
		editor = sharedPref.edit();
		editor.putString(API_MERCHANT_ID, merchantId);
		editor.commit();
	}

	public void storeAccessToken(String accessToken) {
		editor = sharedPref.edit();
		editor.putString(API_ACCESS_TOKEN, accessToken);
		editor.commit();
	}
	
	public void storeRefreshToken(String refreshToken) {
		editor = sharedPref.edit();
		editor.putString(API_REFRESH_TOKEN, refreshToken);
		editor.commit();
	}	
	
	public void storeTokenType(String tokenType) {
		editor = sharedPref.edit();
		editor.putString(API_TOKEN_TYPE, tokenType);
		editor.commit();
	}
	
	public void storeLiveMode(boolean liveMode) {
		editor = sharedPref.edit();
		editor.putBoolean(API_LIVE_MODE, liveMode);
		editor.commit();
	}

	public String getMerchantId() {
		return sharedPref.getString(API_MERCHANT_ID, null);
	}

	public String getAccessToken() {
		return sharedPref.getString(API_ACCESS_TOKEN, null);
	}
	
	public String getRefreshToken() {
		return sharedPref.getString(API_REFRESH_TOKEN, null);
	}
	
	public String getTokenType() {
		return sharedPref.getString(API_TOKEN_TYPE, null);
	}
	
	public Boolean getLiveMode() {
		return sharedPref.getBoolean(API_LIVE_MODE, false);
	}	

	public void resetAccessToken() {
		editor = sharedPref.edit();
		editor.remove(API_ACCESS_TOKEN);
		editor.remove(API_REFRESH_TOKEN);
		editor.remove(API_MERCHANT_ID);
		editor.remove(API_TOKEN_TYPE);
		editor.remove(API_LIVE_MODE);
		editor.clear();
		editor.commit();
	}

}