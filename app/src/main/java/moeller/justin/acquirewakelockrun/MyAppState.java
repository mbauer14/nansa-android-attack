package moeller.justin.acquirewakelockrun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAppState extends Application {

	private int tabsOpen = 0;
	private ArrayList<HashMap<String, Long>> prevStats = null;
	private boolean gmailOpened = false;
	private int appIndex = 0;
    private int runningApps = 0;
    private boolean isOn = true;


    List<String> allApps = Arrays.asList(
            "com.twitter.android",
            "com.snapchat.android",
            "reddit.news",
            "com.spotify.music",
            "com.facebook.orca",
            "com.google.android.apps.maps",
            "com.facebook.katana",
            "com.google.android.gm"
            );

    List<Integer> runningIndices = new ArrayList<Integer>();


	public void tabWasOpened(){
		tabsOpen = 1;
	}
	
	public int getTabsOpen(){
		return tabsOpen;
	}
	
	public ArrayList<HashMap<String, Long>> getPrevStats(){
		return prevStats;
	}
	
	public void setPrevStats(ArrayList<HashMap<String, Long>> prev){
		prevStats = prev;
	}

    public void runNextApp(Context context){
        if (isOn)
        {
            // We no long need list of everything running
            runningIndices.clear();
            //The screen was just turned off - get the state of all the apps so we
            ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for(int i = 0; i < taskInfos.size(); i++) {
                Integer index = allApps.indexOf(taskInfos.get(i).topActivity.getPackageName());
                if (index != -1) {
                    runningIndices.add(index);
                    Log.e("acquirewakelockrun", taskInfos.get(i).topActivity.getPackageName() + "package is running, starting app");
                }
            }

            runningApps = runningIndices.size();

        }
        isOn = false;

        if (runningApps > 0) {
            appIndex = (appIndex + 1) % runningApps;
            int whichApp = runningIndices.get(appIndex);
            String app = allApps.get(whichApp);
            Intent intentToStart = context.getPackageManager().getLaunchIntentForPackage(app);
            intentToStart.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("AcquireWakelock", "Starting " + app);

            try {
                context.startActivity(intentToStart);
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
                Log.e("AcquireWakelock", "Activity not installed");
            }

            Intent mainMenu = new Intent(Intent.ACTION_MAIN);
            mainMenu.addCategory(Intent.CATEGORY_HOME);
            mainMenu.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);

            try{
                context.startActivity(mainMenu);
            }catch(ActivityNotFoundException e){
                e.printStackTrace();
            }
        }

    }


    public void deviceOn() {isOn = true;}

    public boolean wasLastStateOn() {
        return isOn;

    };

}
