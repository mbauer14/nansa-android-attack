package moeller.justin.acquirewakelockrun;

import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MyTestService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MyTestService", "Starting Forever Thread!");
		ForeverThread f = new ForeverThread();
		f.start();
		return super.onStartCommand(intent, flags, startId);
		//return Service.START_STICKY;
	}
	
	
	class ForeverThread extends Thread{
		
		public void run(){
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			PowerManager.WakeLock w1 = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My partial wakelock");
			w1.acquire();
			//never release the wakelock
			boolean running = true;
			
			while(running){		
				
				double currSeconds = System.currentTimeMillis() / 1000.0;
				while((System.currentTimeMillis() / 1000.0) - currSeconds < 10.0){
					Random rand = new Random();
					double value = rand.nextDouble() * rand.nextDouble();
				}
				
				//Log.d("CPU PERCENTAGE", "Total Cpu %: " + totalCpuPercentage + " Cpu 1 %: " + cpu1Percentage + " Cpu 2 %: " + cpu2Percentage + " Cpu 3 %: " + cpu3Percentage + " Cpu 4 %: " + cpu4Percentage);	
			}
		}
	}
}
