package dczh;

import android.app.Application;



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



    }

    public static synchronized MyApplication getInstance()
    {

        return mInstance;
    }



}
