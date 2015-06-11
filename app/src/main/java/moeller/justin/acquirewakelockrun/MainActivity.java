package moeller.justin.acquirewakelockrun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Browser;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView explanation;
	private ImageView imageBatteryState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		explanation = (TextView)findViewById(R.id.textView1);
		//batteryInfo = (TextView)findViewById(R.id.textView2);
		//imageBatteryState = (ImageView)findViewById(R.id.imageView1);
		
		explanation.setText("This schedules an alarm and forces battery drain on other apps.");
		
		//this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		MyAppState appState = ((MyAppState)getApplicationContext());
		int tabsOpen = appState.getTabsOpen();
		if(tabsOpen == 0){
			//startBrowser("http://www.wordaffects.com/batteryDrain/batteryDrain.html");
			scheduleInitialAlarm(900);
			appState.tabWasOpened();
		}
		
	}

	
	private void scheduleInitialAlarm(int milliseconds){
		Long time = new GregorianCalendar().getTimeInMillis() + milliseconds;
		Intent intentAlarm = new Intent(MainActivity.this, AlarmReceiver.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, 0));
		Log.d("Alarm", "Alarm Set!");
	}
	
	private void startService(){
		Intent myTestService = new Intent(MainActivity.this, MyTestService.class);
		startService(myTestService);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
