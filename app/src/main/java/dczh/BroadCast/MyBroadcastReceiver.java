package dczh.BroadCast;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import dczh.MyApplication;
import dczh.Service.UploadGPSService;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("MyBroadcastReceiver", "MyBroadcastReceiver onReceive");
        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {
            if("dczh.Service.UploadGPSService".equals(service.service.getClassName()))
//Service的类名
            {
                Log.i("MyBroadcastReceiver", "UploadGPSService is running ");
                isServiceRunning = true;
            }

        }
        if (!isServiceRunning) {
            Intent i = new Intent(context, UploadGPSService.class);
            Log.i("MyBroadcastReceiver", "MyBroadcastReceiver start UploadGPSService");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);

            }



        }
    }
}
