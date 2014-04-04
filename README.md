android-paymill-connect
======================

Android Library for integrating with Paymill Connect using [Paymill Connect Oauth](https://paymill.com/en-gb/unite-documentation).

<center>
![Paymill Connect](https://github.com/thiagolocatelli/android-stripe-connect/raw/master/resources/screenshots/screenshot1.png "Paymill Connect")
![Paymill Connect](https://github.com/thiagolocatelli/android-paymill-connect/raw/master/resources/screenshots/screenshot2.png "Paymill Connect")
![Paymill Connect](https://github.com/thiagolocatelli/android-paymill-connect/raw/master/resources/screenshots/screenshot3.png "Paymill Connect")
</center>


## Usage

You can add the Paymill Connect button to your layout using the following XML code:


```XML
	<com.github.thiagolocatelli.paymill.PaymillButton
		android:id="@+id/btnPaymillConnect"
		android:layout_height="wrap_content"
		android:layout_width="200dip" 
		android:layout_gravity="center_horizontal"
		android:layout_marginTop="20dip"/>
```

You can create an utility class where you can define your application credentials, like the one below (This is obvilously insecure, make sure you keep all this information stored in a way its impossible to decompile):

```Java
public class ApplicationData {
	public static final String CLIENT_ID = "";
	public static final String CLIENT_SECRET = "";
	public static final String CALLBACK_URL = "";
}
```

Inside your Activity, you can manipulate the button and change its properties. You can either launch a Dialog to start the authentication or start a Activity.

```Java
PaymillApp = new PaymillApp(this, ApplicationData.CLIENT_ID, 
				ApplicationData.SECRET_KEY, ApplicationData.CALLBACK_URL);

mPaymillButton = (PaymillButton) findViewById(R.id.btnPaymillConnect);
		mPaymillButton.setPaymillApp(mApp);
		mPaymillButton.addPaymillConnectListener(new PaymillConnectListener() {

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
```

By default, when the Paymill Connect button is clicked, an Android Dialog will open and display the Paymill authentication page. If you would like to open an Activity instead of a Dialog, you can use **setConnectMode** to change such behavior.

```Java
mPaymillButton = (PaymillButton) findViewById(R.id.btnPaymillConnect);
mPaymillButton.setPaymillApp(mApp);
mPaymillButton.setConnectMode(CONNECT_MODE.ACTIVITY);
```
You also need to add to your AndroidManifest.xml the following line, which will allow the Paymill Connect button to start the authentication Activity.

```XML
<activity android:name="com.github.thiagolocatelli.paymill.PaymillActivity"  />
```

Once the authentication is finished, you can use the helper methods from the object PaymillApp to get the data you need, like the oauth access token required to make calls using the [Paymill Mobile-SDK for Android](https://github.com/paymill/paymill-android).

```JAVA
public void test() {
  String mPublicKey = mPaymillApp.getPublicKey();
  // the payment method ( cc or dd data)
  PMPaymentMethod method = PMFactory.genCardPayment("Max Mustermann", "4111111111111111", "12", "2015", "1234");
  // the payment parameters (currency, amount, description)
  PMPaymentParams params = PMFactory.genPaymentParams("EUR", 100, null);
  PMManager.generateToken(getApplicationContext(), method, params, PMService.ServiceMode.TEST, mPublicKey);
}
```

Download the sample application and git it a try.

## Contact

If you have any questions, please drop me a line: "thiago:locatelli$gmail:com".replace(':','.').replace('$','@')

