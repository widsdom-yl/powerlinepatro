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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.zhy.base.fileprovider.FileProvider7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dczh.Util.FileUtil;
import dczh.View.ActionSheet;
import dczh.adapter.BaseAdapter;
import dczh.adapter.TowerProtolEditImageAdapter;
import dczh.model.LineTowerModel;

public class UploadDefectActivity extends BaseAppCompatActivity implements View.OnClickListener, BaseAdapter.OnItemClickListener, TowerProtolEditImageAdapter.DeleteClickListener {

    private static final String ARG_PARAM1 = "param1";
    private LineTowerModel model;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private TowerProtolEditImageAdapter mAdpter;
    private RecyclerView mRecyclerView;

    Button button_choose_image;
    ImageView imageView;
    private ActionSheet actionSheet;
    /******************************************/
    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    /******************************************/

    List<String> mFileArray = new ArrayList<>();
    Integer mTakePhotoIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_defect);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        model = (LineTowerModel)bundle.getSerializable(ARG_PARAM1);
        if ( actionBar != null)
        {
            setCustomTitle(getString(R.string.string_sign_defect), true);
            // setCustomTitle(model.getTowerName(), true);
        }

        spinner = findViewById(R.id.spinner);
        //数据
        data_list = new ArrayList<String>();
        data_list.add(model.getTowerName());
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");

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
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManage);
        mAdpter = new TowerProtolEditImageAdapter(mFileArray);
        mRecyclerView.setAdapter(mAdpter);
        mFileArray.add("");
        mAdpter.setOnItemClickListener(this);
        mAdpter.setmDeleteClickListener(this);
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
        showSheet();
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
}