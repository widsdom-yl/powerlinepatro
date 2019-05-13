package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dczh.Manager.AccountManager;
import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.BaseAdapter;
import dczh.adapter.HomePageAdapter;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePageActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener, AMapLocationListener, View.OnClickListener {
    RecyclerView homePageRecyclerView;
    HomePageAdapter adapter;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().show();
        setCustomTitle(getString(R.string.string_main_title)+"gyl",false);
        list.add(getString(R.string.string_homepage0));
        list.add(getString(R.string.string_homepage1));
        list.add(getString(R.string.string_homepage2));
        list.add(getString(R.string.string_homepage3));

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
       mlocationClient.startLocation();

        initView();
    }
    void initView(){
        homePageRecyclerView = findViewById(R.id.home_grid_view);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        homePageRecyclerView.setLayoutManager(layoutManage);
        adapter = new HomePageAdapter(list);
        adapter.setOnItemClickListener(this);
        homePageRecyclerView.setAdapter(adapter);
        findViewById(R.id.upload_pos_btn).setOnClickListener(this);

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position){
            case 0:
                Intent intent = new Intent(this, LinePatroActivity.class);
                startActivity(intent);
                break;
            case 2:

                Intent intent2 = new Intent(this, DefectMainActivity.class);
                startActivity(intent2);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    long lat;
    long lot;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e("MapFragment","x:"+aMapLocation.getLatitude());
        LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        lat = (long) latLng.latitude;
        lot = (long) latLng.longitude;
    }
    public void uploadWorkerPos(long lat,long lot) {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uid", ""+ AccountManager.getInstance().getUid())
                .add("token",  AccountManager.getInstance().getToken())
                .add("lot",  ""+lat)
                .add("lat", ""+ lot)
                .build();



        final Request request = new Request.Builder()
                .url(Config.workUrl+"pos_add.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(HomePageActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e(tag,"res is "+res);
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        lod.dismiss();
                        if (model != null && model.error_code==0){
                            Toast.makeText(HomePageActivity.this, getString(R.string.string_uploadsuccess), Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(HomePageActivity.this, getString(R.string.string_uploadfailed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }
    LoadingDialog lod;
    static  final String tag = "HomePageActivity";

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.upload_pos_btn){
            uploadWorkerPos(lot,lat);
        }
    }
}
