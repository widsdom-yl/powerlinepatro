package dczh.powerlinepatro;
//选择查看缺陷隐患或者上报缺陷隐患
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

public class DefectMainActivity extends BaseAppCompatActivity implements View.OnClickListener, AMapLocationListener {
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_main);
        setCustomTitle(getString(R.string.string_defect),true);

        findViewById(R.id.button_defect_info).setOnClickListener(this);
        findViewById(R.id.button_upload_defect).setOnClickListener(this);

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_defect_info){
            Intent intent = new Intent(this, ExistDefectActivity.class);

            startActivity(intent);
        }
        else if(view.getId() == R.id.button_upload_defect){
            Intent intent = new Intent(this, UploadDefectActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putSerializable(ARG_PARAM1,model);
            bundle.putDouble(ARG_PARAM3,lat);
            bundle.putDouble(ARG_PARAM3,lot);
//            bundle.putDouble(ARG_PARAM4,119.585586);
//            bundle.putDouble(ARG_PARAM3,32.476958);

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    double lat;
    double lot;
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.e("MapFragment","x:"+aMapLocation.getLatitude());
        LatLng latLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        lat = (double) latLng.latitude;
        lot = (double) latLng.longitude;
    }
}
