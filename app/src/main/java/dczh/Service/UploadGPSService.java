package dczh.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

import java.io.IOException;
import java.util.Random;

import dczh.Manager.AccountManager;
import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UploadGPSService extends Service implements AMapLocationListener {

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public double lat;
    public double lot;

    //初始化同志channel
    public static final String CHANNEL_ID_LOCATION = "dczh.Service";
    public void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID_LOCATION,"Location",NotificationManager.IMPORTANCE_DEFAULT));
        }
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        lat = (double) latLng.latitude;
        lot = (double) latLng.longitude;
    }

    Handler handler_afteruploadimage = new Handler(); // 用户上传图片后，3s之后上传用户位置信息

    Handler handler = new Handler();//app启动时候，调用hander，然后每5分钟调用一次
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (lat >0 && lot>0){
                uploadWorkerPos(lat,lot);
            }
            handler.postDelayed(runnable, 1000*60*5);
        }
    };
    Runnable runnable_afteruploadimage = new Runnable()
    {
        @Override
        public void run()
        {
            if (lat >0 && lot>0){
                uploadWorkerPos(lat,lot);
            }
        }
    };


    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public UploadGPSService getService() {
            return UploadGPSService.this;
        }


    }

    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();


    @Override
    public void onCreate() {
        Log.i("UploadGPSService","onCreate - Thread ID = " + Thread.currentThread().getId());
       // startForeground(1,new Notification());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_LOCATION);
            startForeground(1,builder.build());
        }


        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位

      //  handler.postDelayed(runnable, 5000);
        mlocationClient.startLocation();



        super.onCreate();
    }
//通过startService()，会执行这个方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("UploadGPSService", "onStartCommand - startId = " + startId + ", Thread ID = " + Thread.currentThread().getId());
       // return super.onStartCommand(intent, flags, startId);
        handler.postDelayed(runnable, 3000);
        return  START_STICKY;
    }
    //通过bindService调用，会执行这个方法
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       Log.e("UploadGPSService", "TestTwoService - onbind - from = " + intent.getStringExtra("from"));
       if (!mlocationClient.isStarted()){
           mlocationClient.startLocation();
       }
       return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("UploadGPSService", "TestTwoService - onUnbind - from = " + intent.getStringExtra("from"));

        return false;
    }


    @Override
    public void onDestroy() {
        Log.i("UploadGPSService", "onDestroy - Thread ID = " + Thread.currentThread().getId());
        handler.removeCallbacks(runnable);
        super.onDestroy();


    }
    public void uploadPosDelay3S() {
        handler_afteruploadimage.postDelayed(runnable_afteruploadimage,3000);
    }



    private final Random generator = new Random();


    //getRandomNumber是Service暴露出去供client调用的公共方法
    public int getRandomNumber() {
        return generator.nextInt();
    }

    public void uploadWorkerPos(double lat,double lot) {

        if (AccountManager.getInstance().getToken() == null || AccountManager.getInstance().getUid() ==0){

            return;
        }

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uid", ""+ AccountManager.getInstance().getUid())
                .add("token",  AccountManager.getInstance().getToken())
                .add("lot",  ""+lot)
                .add("lat", ""+ lat)
                .build();



        final Request request = new Request.Builder()
                .url(Config.workUrl+"pos_add.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                lod.dismiss();
//                Toast.makeText(HomePageActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e("UploadGPSService","res is "+res);
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);


            }
        });

    }

}
