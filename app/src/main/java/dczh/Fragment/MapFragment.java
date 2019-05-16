package dczh.Fragment;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

import dczh.Util.MapUtil;
import dczh.View.ActionSheet;
import dczh.model.LineTowerModel;
import dczh.powerlinepatro.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements AMap.OnMyLocationChangeListener, AMapLocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MapView mMapView = null;
    MyLocationStyle myLocationStyle;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    AMap aMap;
    private LineTowerModel model;
    private OnFragmentInteractionListener mListener;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(LineTowerModel param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void resetModel(LineTowerModel m){
        if (m!=null){
            model = m;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (LineTowerModel)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mMapView.setClickable(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false

//        aMap.setOnMyLocationChangeListener(this);



        mlocationClient = new AMapLocationClient(this.getContext());
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
//        mlocationClient.startLocation();



        LatLng latLng = new LatLng(model.getLat(),model.getLot());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(model.getNme()).snippet(model.getTpe()));
        aMap.setOnMarkerClickListener(markerClickListener);
        aMap.setOnInfoWindowClickListener(infoWindowClickListener);



        // 设置定位监听
        return view;


    }
    AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {
            Log.e(tag,"InfoWindow clicked");
            showSheet();
        }
    };

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.e(tag,"mark clicked");
            if (marker.isInfoWindowShown()){
                marker.hideInfoWindow();
            }
            else{
                marker.showInfoWindow();
            }

            return true;
        }
    };




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapView.onDestroy();
        mListener = null;
    }

    @Override
    public void onMyLocationChange(Location location) {

        Log.e("MapFragment","x:"+location.getLatitude());
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }


    private void showSheet() {
        boolean isGdMapInstalled = MapUtil.isGdMapInstalled();
        boolean isBdMapInstalled = MapUtil.isBaiduMapInstalled();
        if (isGdMapInstalled  || isBdMapInstalled){

            View.OnClickListener  clickListener_gd = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionSheet.dismiss();
                    MapUtil.openGaoDeNavi(MapFragment.this.getContext(), 0, 0, null, model.getLat(), model.getLot(), model.getNme());

                }
            };
            View.OnClickListener  clickListener_bd = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionSheet.dismiss();
                    MapUtil.openBaiDuNavi(MapFragment.this.getContext(), 0, 0, null, model.getLat(), model.getLot(), model.getNme());

                }
            };
            View.OnClickListener  clickListener_cancel = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionSheet.dismiss();
                }
            };
            if (isGdMapInstalled && isBdMapInstalled){
                actionSheet=new ActionSheet.DialogBuilder(this.getContext())
                        .addSheet(getString(R.string.string_gaode), clickListener_gd)
                        .addSheet(getString(R.string.string_baidu), clickListener_bd)
                        .addCancelListener(clickListener_cancel)
                        .create();
            }
            else if(isGdMapInstalled){
                actionSheet=new ActionSheet.DialogBuilder(this.getContext())
                        .addSheet(getString(R.string.string_gaode), clickListener_gd)
                        .addCancelListener(clickListener_cancel)
                        .create();
            }
            else{
                actionSheet=new ActionSheet.DialogBuilder(this.getContext())
                        .addSheet(getString(R.string.string_baidu), clickListener_bd)
                        .addCancelListener(clickListener_cancel)
                        .create();
            }

        }
        else{
            Toast.makeText(this.getContext(), getString(R.string.string_upload_img_success), Toast.LENGTH_LONG).show();
        }

    }
    private ActionSheet actionSheet;
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    static  final String tag = "MapFragment";
}
