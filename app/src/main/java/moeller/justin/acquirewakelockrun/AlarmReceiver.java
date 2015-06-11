package moeller.justin.acquirewakelockrun;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import java.util.List;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.content.Context;

public class AlarmReceiver extends BroadcastReceiver {

	private final int LOCATION_REFRESH_TIME = 1;
	private final int LOCATION_REFRESH_DISTANCE = 1;
	//never release the wakelock
	private boolean running = false;
	
	private final LocationListener locationListener = new LocationListener(){

		@Override
		public void onLocationChanged(Location location) {
			Log.d("Locaiton", "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
	};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d("Alarm", "Alarm Received!!");
		//try{
		//	printCPUPercentages(context);
		//}catch(FileNotFoundException e){
		//	Log.e("AcquireWakelock", "Cannot open the CPU stats file");
		//}		
		scheduleAlarm(context, intent);
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		MyAppState state = (MyAppState)context.getApplicationContext();

		if(!pm.isScreenOn()){
            //startAndStopActivity(context);
            // Screen is off, so run apps here
            state.runNextApp(context);

		}
        else{
            state.deviceOn();

        }
	}
	private void scheduleAlarm(Context context, Intent intentAlarm){
		int millis = 900;
		Long time = new GregorianCalendar().getTimeInMillis() + millis;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, 0));
		//Log.d("Alarm", "Alarm Set!");
	}
	

}
