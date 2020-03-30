package dczh.powerlinepatro;

import android.Manifest;
import android.app.Activity;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import dczh.GlideApp;
import dczh.Manager.AccountManager;
import dczh.MyApplication;
import dczh.Util.Config;
import dczh.Util.FileUtil;
import dczh.Util.GsonUtil;
import dczh.View.ActionSheet;
import dczh.View.LoadingDialog;
import dczh.model.DangerModel;
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

public class DangerDailyUploadActivity extends BaseAppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    DangerModel dangerModel;
    private static final String ARG_PARAM1 = "param1";
    TextView tx_danger_name,tx_danger_process;
    EditText edit_note;
    ImageButton image_upload,button_delete;
    SeekBar seek_danger_process;
    CheckBox checkbox_danger_judge;
    Button button_submit;


    int gcj;//工程进度
    int icl;//近日是否有大型施工车辆

    String filePath="";//图片路径

    private ActionSheet actionSheet;//选择图片方式
    /******************************************/
    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    /******************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_daily_upload);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            dangerModel = (DangerModel)bundle.getSerializable(ARG_PARAM1);
        }
        setCustomTitle(getString(R.string.string_danger_daily_upload),true);
        initView();
        icl = 0;
        gcj = 30;

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

    void initView(){
        tx_danger_name = findViewById(R.id.tx_danger_name);
        tx_danger_process = findViewById(R.id.tx_danger_process);
        edit_note = findViewById(R.id.edit_note);
        image_upload = findViewById(R.id.image_upload);
        button_delete = findViewById(R.id.button_delete);
        seek_danger_process = findViewById(R.id.seek_danger_process);
        checkbox_danger_judge = findViewById(R.id.checkbox_danger_judge);
        button_submit = findViewById(R.id.button_submit);
        seek_danger_process.setOnSeekBarChangeListener(this);
        checkbox_danger_judge.setOnCheckedChangeListener(this);
        button_submit.setOnClickListener(this);
        image_upload.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        tx_danger_name.setText(getString(R.string.string_danger_name)+dangerModel.getNme());
        if (filePath.length() == 0){
            button_delete.setVisibility(View.INVISIBLE);
        }
        else{
            button_delete.setVisibility(View.VISIBLE);
        }
        gcj=30;
        tx_danger_process.setText(getString(R.string.string_danger_scene_process)+gcj+"%");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        gcj = i;
        tx_danger_process.setText(getString(R.string.string_danger_scene_process)+gcj+"%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        icl = b?1:0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_upload:
                showSheet();
                break;
                case R.id.button_delete:
                    filePath = "";
                    button_delete.setVisibility(View.INVISIBLE);
                    image_upload.setImageResource(R.mipmap.danger_upd_bg);
                    break;
            case R.id.button_submit:
                compressPhoto(filePath);
                break;
                 default:
                        break;
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
    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                }
                break;
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

        File photoFile = new File(fileDir, "photo0.jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();
        imageUri = FileProvider7.getUriForFile(this,photoFile);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            //return
            return;
        }


        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                //  Toast.makeText(this,uri.toString(),Toast.LENGTH_LONG).show();
                filePath = FileUtil.getFilePathByUri(this, uri);

                // Toast.makeText(this,filePath,Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(filePath)) {
                    RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                   // Glide.with(this).load(filePath).apply(requestOptions1).into(image_upload);
                    button_delete.setVisibility(View.VISIBLE);

                    GlideApp.with(MyApplication.getInstance()).asBitmap()
                            .load(filePath)
                            .centerCrop()
                            //.placeholder(R.drawable.imagethumb_cn)//zhbzhb
                            .placeholder(R.drawable.imagethumb)//zhbzhb
                            .into(image_upload);
                }
                break;
            case RC_TAKE_PHOTO:
                if (resultCode == -1){

                    RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                   //Glide.with(this).load(mTempPhotoPath).apply(requestOptions).into(image_upload);
                    filePath = mTempPhotoPath;
                    button_delete.setVisibility(View.VISIBLE);

                    GlideApp.with(MyApplication.getInstance()).asBitmap()
                            .load(filePath)
                            .centerCrop()
                            //.placeholder(R.drawable.imagethumb_cn)//zhbzhb
                            .placeholder(R.drawable.imagethumb)//zhbzhb
                            .into(image_upload);
                }
                break;
            default:
                break;
        }
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
                .ignoreBy(10)
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
                            uploadImageData(file.getAbsolutePath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(tag,"onError"+e.getLocalizedMessage());
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    /*上传文件*/
    public void uploadImageData(String compressFile){
        if (AccountManager.getInstance().getToken() == null || AccountManager.getInstance().getUid() ==0){
            Toast.makeText(DangerDailyUploadActivity.this, getString(R.string.error_relogin), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();

        OkHttpClient client = new OkHttpClient();


        MultipartBody.Builder multiBuilder=new MultipartBody.Builder();


        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


        // 设置请求体
        multiBuilder.setType(MultipartBody.FORM);
//这里是 封装上传图片参数

        {
            File file=new File(compressFile);
            RequestBody filebody = MultipartBody.create(MEDIA_TYPE_PNG, file);
            multiBuilder.addFormDataPart("file", file.getName(), filebody);
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
                .url(Config.gblUrl+"upload.php")
                .post(multiBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        Toast.makeText(DangerDailyUploadActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();

                    }
                });

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
                            lod.dismiss();
                            String body = new Gson().toJson(model.data);
                            UploadFileRetModel model = GsonUtil.parseJsonWithGson(body, UploadFileRetModel.class);
//
                            finishUploadImage(model);
                        }
                        else{
                            lod.dismiss();
                            Toast.makeText(DangerDailyUploadActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /*上传文件之后，提交结果*/
    public void finishUploadImage(UploadFileRetModel  retModel){
        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("pid", ""+dangerModel.getId())
                .add("token", AccountManager.getInstance().getToken())
                .add("uid", ""+AccountManager.getInstance().getUid())
                .add("gcj", ""+gcj+"%")
                .add("icl", ""+icl)
                .add("img", retModel.getOriUrl())
                .add("cmt", ""+icl)
                .build();




        final Request request = new Request.Builder()
                .url(Config.workUrl+"wxy_add.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(DangerDailyUploadActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(DangerDailyUploadActivity.this, getString(R.string.string_danger_daily_upload_success), Toast.LENGTH_LONG).show();
                            ;
                            lod.dismiss();
                            DangerDailyUploadActivity.this.finish();
                        }
                        else{
                            if (model.error_code == 1003){
                                Toast.makeText(DangerDailyUploadActivity.this,getString(R.string.string_danger_daily_upload_failed)+":"+model.error_msg+getString(R.string.string_danger_daily_reupload), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(DangerDailyUploadActivity.this,getString(R.string.string_danger_daily_upload_failed)+":"+model.error_msg, Toast.LENGTH_LONG).show();

                            }
                            lod.dismiss();
                        }
                    }
                });
            }
        });
    }


    static final  String tag = "DangerDailyUpload";
    LoadingDialog lod;

}
