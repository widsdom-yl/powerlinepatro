package dczh.powerlinepatro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import dczh.Manager.AccountManager;
import dczh.Util.Config;
import dczh.Util.FileUtil;
import dczh.Util.GsonUtil;
import dczh.View.ActionSheet;
import dczh.View.LoadingDialog;
import dczh.adapter.BaseAdapter;
import dczh.adapter.TowerProtolEditImageAdapter;
import dczh.model.LineTowerModel;
import dczh.model.ResponseModel;
import dczh.model.UploadFileRetModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UploadDefectActivity extends BaseAppCompatActivity implements View.OnClickListener, BaseAdapter.OnItemClickListener, TowerProtolEditImageAdapter.DeleteClickListener, AdapterView.OnItemSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private LineTowerModel model;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private TowerProtolEditImageAdapter mAdpter;
    private RecyclerView mRecyclerView;
    private EditText mEditDefect;
    List<LineTowerModel>nearTowerList = new ArrayList<>();
    Button button_choose_image;
    ImageView imageView;
    private ActionSheet actionSheet;
    /******************************************/
    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    /******************************************/

    List<String> mFileArray = new ArrayList<>();
    Integer mTakePhotoIndex = 0;


    double lat;
    double lot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_defect);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            model = (LineTowerModel)bundle.getSerializable(ARG_PARAM1);
            lat = bundle.getDouble(ARG_PARAM3);
            lot = bundle.getDouble(ARG_PARAM4);
        }

        if ( actionBar != null)
        {
            setCustomTitle(getString(R.string.string_sign_defect), true);
            // setCustomTitle(model.getTowerName(), true);
        }

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        //数据
        data_list = new ArrayList<String>();
        if (model != null){
            data_list.add(model.getNme());
        }



        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        /***************************************/
//        button_choose_image = findViewById(R.id.button_choose_image);
//        imageView = findViewById(R.id.imageView2);
//        button_choose_image.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.recyler_defect_signin_image);
        mEditDefect = findViewById(R.id.edit_defect);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManage);
        mAdpter = new TowerProtolEditImageAdapter(mFileArray);
        mRecyclerView.setAdapter(mAdpter);
        mFileArray.add("");
        mAdpter.setOnItemClickListener(this);
        mAdpter.setmDeleteClickListener(this);
        findViewById(R.id.button_sign_defect).setOnClickListener(this);
        requestNearLineTowerArray();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }
    public static int RESULT_CODE = 1;
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("model", model);//根据key “text” 把获取OtherActivity中的edittext中的String回传给第一个activity
                setResult(RESULT_CODE, intent);//回传结果码，我这边也是给1，大于等于0即可，值随意
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int patrolImageCount = 0 ;
    int uploadedCount = 0;
    List<String> compressFiles = new ArrayList<>();
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_sign_defect){
            if (mFileArray.size()>1){
                if (lod == null)
                {
                    lod = new LoadingDialog(this);
                }
                lod.dialogShow();
                if (mFileArray.get(mFileArray.size()-1).length()>0){
                    patrolImageCount = mFileArray.size();
                }
                else{
                    patrolImageCount = mFileArray.size()-1;
                }
                uploadedCount=0;
                compressFiles.clear();
                for (String fileName : mFileArray){
                    compressPhoto(fileName);
                }

            }
        }

    }
    private void showSheet() {
        actionSheet=new ActionSheet.DialogBuilder(this)
                .addSheet(getString(R.string.string_album), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFromAlbum();
                        actionSheet.dismiss();
                    }
                })
                .addSheet(getString(R.string.string_take_photo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFromTakePhoto();
                        actionSheet.dismiss();
                    }
                })

                .addCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        actionSheet.dismiss();
                    }
                })
                .create();
    }
    void chooseFromAlbum(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
        } else {
            //已授权，获取照片
            choosePhoto();
        }
    }
    void chooseFromTakePhoto(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RC_TAKE_PHOTO);
        } else {
            //已授权，获取照片
            takePhoto();
        }
    }
    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    private String mTempPhotoPath;
    private Uri imageUri;

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo"+mTakePhotoIndex+".jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider7.getUriForFile(this,photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }


    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                choosePhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);

                if (!TextUtils.isEmpty(filePath)) {
                    //RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//                    将照片显示在 ivImage上
                    //Glide.with(this).load(filePath).apply(requestOptions1).into(imageView);
//                    mFileArray.add(filePath);
                    if (mFileArray.size() == 6){
                        mFileArray.remove(5);
                        mFileArray.add(filePath);
                    }
                    else{
                        mFileArray.add(mFileArray.size()-1,filePath);
                    }

                    mAdpter.resetMList(mFileArray);
                    mAdpter.notifyDataSetChanged();
                }
                break;
            case RC_TAKE_PHOTO:
                if (resultCode == -1){
                    ++mTakePhotoIndex;
//                    mFileArray.add(mTempPhotoPath);


                    if (mFileArray.size() == 6){
                        mFileArray.remove(5);
                        mFileArray.add(mTempPhotoPath);
                    }
                    else{
                        mFileArray.add(mFileArray.size()-1,mTempPhotoPath);
                    }


                    mAdpter.resetMList(mFileArray);
                    mAdpter.notifyDataSetChanged();
                    //RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                    //将图片显示在ivImage上
                    //Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(imageView);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if (position == mFileArray.size() -1 && mFileArray.get(position).length() == 0){
            showSheet();
        }
        else{
            // PatrolImageDetailActivity
            Intent intent = new Intent(this, PatrolImageDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PARAM1,mFileArray.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onDeleteBtnCilck(View view, int position) {
        mFileArray.remove(position);
        mAdpter.resetMList(mFileArray);
        mAdpter.notifyDataSetChanged();

    }

    public void requestNearLineTowerArray() {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody ;
        if (model != null){
            formBody= new FormBody.Builder()
                    .add("lot", ""+model.getLot())
                    .add("lat", ""+model.getLat())
                    .build();
        }
        else{
            formBody= new FormBody.Builder()
                    .add("lot", ""+lot)
                    .add("lat", ""+lat)
                    .build();
        }


        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"tower_near.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
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
                            String body = new Gson().toJson(model.data);
                            List<LineTowerModel> lists = GsonUtil.parseJsonArrayWithGson(body, LineTowerModel[].class);
                            nearTowerList = lists;
                            for (LineTowerModel model : nearTowerList){
                                boolean exist = false;
                                for (Iterator iter = data_list.iterator(); iter.hasNext();) {
                                    String str = (String)iter.next();
                                    if (str.equals(model.getNme())){
                                        exist = true;
                                        break;
                                    }
                                }
                                if (!exist){
                                    data_list.add(model.getNme());
                                }
                            }
                            arr_adapter.notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    public void compressPhoto(final String fileName){
        if (fileName.length()  == 0){
            return;
        }
        File fileDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }



        File file=new File(fileName);
        Luban.with(this)
                .load(file)
                .ignoreBy(100)
                .setTargetDir(fileDir.getAbsolutePath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        boolean  ret =!(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                        return ret;
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        Log.e(tag,"onStart");
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI

                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        Log.e(tag,"onSuccess");
                        //uploadImageData(file.getAbsolutePath());
                        compressFiles.add(file.getAbsolutePath());
                        if (compressFiles.size() == patrolImageCount){
                            uploadImageData(compressFiles);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag,"onError"+e.getLocalizedMessage());
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }
    /*上传文件*/
    public void uploadImageData(List<String> compressFiles){



        OkHttpClient client = new OkHttpClient();


        MultipartBody.Builder multiBuilder=new MultipartBody.Builder();


        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


        // 设置请求体
        multiBuilder.setType(MultipartBody.FORM);
//这里是 封装上传图片参数

        for (int i = 0;i<compressFiles.size();++i){
            File file=new File(compressFiles.get(i));
            RequestBody filebody = MultipartBody.create(MEDIA_TYPE_PNG, file);
            multiBuilder.addFormDataPart("file"+(i+1), file.getName(), filebody);
        }

        // 封装请求参数,这里最重要
        HashMap<String, String> params = new HashMap<>();
        params.put("uid",""+ AccountManager.getInstance().getUid());
        params.put("token",AccountManager.getInstance().getToken());
        params.put("ext","jpg");

        //参数以添加header方式将参数封装，否则上传参数为空
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multiBuilder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }




        RequestBody multiBody=multiBuilder.build();


        final Request request = new Request.Builder()
                .url(Config.gblUrl+"upload6.php")
                .post(multiBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e(tag,"upload res is "+res);
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //

                        if (model != null && model.error_code==0){
                            String body = new Gson().toJson(model.data);
                            UploadFileRetModel model = GsonUtil.parseJsonWithGson(body, UploadFileRetModel.class);
//
                            finishUploadImage(model);
                        }
                        else{
                            Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /*上传文件之后，提交结果*/
    public void finishUploadImage(UploadFileRetModel  retModel){
        OkHttpClient client = new OkHttpClient();
        String defectInfo =mEditDefect.getText().toString();

        FormBody formBody = new FormBody.Builder()
                .add("pid", ""+model.getId())
                .add("token", AccountManager.getInstance().getToken())
                .add("uid", ""+AccountManager.getInstance().getUid())
                .add("nme", defectInfo)
                .add("img", retModel.getUrl())
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"pie_add.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
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


                        if (model != null && model.error_code==0){
                            Toast.makeText(UploadDefectActivity.this, getString(R.string.string_upload_img_success), Toast.LENGTH_LONG).show();
                            ;

                            lod.dismiss();
                        }
                        else{
                            Toast.makeText(UploadDefectActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                            lod.dismiss();
                        }
                    }
                });
            }
        });
    }
    LoadingDialog lod;
    static  final String tag = "UploadDefectActivity";

    public static int REQUEST_CODE_1 = 1;
    public static int RESULT_CODE_1 = 1;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String towername = data_list.get(i);
        for (Iterator iter =nearTowerList.iterator(); iter.hasNext();) {
            LineTowerModel tempmodel = (LineTowerModel)iter.next();
            if (towername.equals(tempmodel.getNme())){
                model = tempmodel;
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}