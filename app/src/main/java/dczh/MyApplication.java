package dczh;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import dczh.BroadCast.MyBroadcastReceiver;
import dczh.Manager.AccountManager;
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

        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, filter);

        CrashReport.initCrashReport(getApplicationContext(), "b5f18f38f0", true);


        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
// 参数一：当前上下文context；
// 参数二：应用申请的Appkey（需替换）；
// 参数三：渠道名称；
// 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
// 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, "5d394b830cafb297a0000ce6", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "c0dcac437ec55700efd0f264cbd0db0e");

        PushAgent mPushAgent = PushAgent.getInstance(this);
//
//
//
////注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG,"注册成功：deviceToken：-------->  " + deviceToken);
                AccountManager.getInstance().saveDeviceToken(deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
    }

    public static synchronized MyApplication getInstance()
    {

        return mInstance;
    }



}
