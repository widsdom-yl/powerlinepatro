package dczh;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;


public class MyApplication extends Application
{
    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this;
        if (!MyContext.isInitialized())
        {
            MyContext.init(getApplicationContext());
        }

        CrashReport.initCrashReport(getApplicationContext(), "b5f18f38f0", true);


    }

    public static synchronized MyApplication getInstance()
    {

        return mInstance;
    }



}
