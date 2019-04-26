package dczh.powerlinepatro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import dczh.Manager.AccountManager;
import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.model.ResponseModel;
import dczh.model.UserModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
           Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.MODIFY_AUDIO_SETTINGS,
//            Manifest.permission.ACCESS_WIFI_STATE,

    };
    LoadingDialog lod;
    String UserName = null;
    String Password = null;
    boolean IsAutoLogin = false;
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    AppCompatCheckBox checkbox;
    static final String  tag =  "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        requestPermisson();
        findViewById(R.id.login_in_button).setOnClickListener(this);


        IsAutoLogin = AccountManager.getInstance().getIsRemeberAccount();



        mUserView = (AutoCompleteTextView) findViewById(R.id.editText_user);
        mPasswordView = (EditText) findViewById(R.id.editText_password);

        checkbox = findViewById(R.id.checkbox);
        checkbox.setChecked(IsAutoLogin);


        if (IsAutoLogin){
            UserName = AccountManager.getInstance().getDefaultUsr();
            mUserView.setText(UserName);
            Password = AccountManager.getInstance().getDefaultPwd();
            mPasswordView.setText(Password);
        }


        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                IsAutoLogin = isChecked;
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_in_button){
//            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//            startActivity(intent);
//            finish();
            attemptLogin();
        }
    }
    private void attemptLogin()
    {


        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String mUser = mUserView.getText().toString();
        String mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;



        // Check for a valid email address.
        if (TextUtils.isEmpty(mUser))
        {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mPassword))
        {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            login(mUser,mPassword);

        }
    }

    public void login(final String mUserName, final String mPassword) {
        hintKeyBoard();
//        ServerNetWork.getCommandApi()
//                .userLogin("yzliushui",
//                        StringUtil.getMD5String("yzrj2018"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer_login);
        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("user", mUserName)
                .add("pass", mPassword)
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.serverUrl+"login.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(LoginActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
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
                            AccountManager.getInstance().saveAccount(mUserName, mPassword, IsAutoLogin);
                            UserModel userModel = new Gson().fromJson(body,UserModel.class);
//
//                            String token = model.getData().getToken();
                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("uid",userModel.getUid());
                            intent.putExtras(bundle);

                            startActivity(intent);
                            LoginActivity.this.finish();
                            Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, getString(R.string.error_login_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    private String[] denied;
    void requestPermisson()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++)
            {
                if (PermissionChecker.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED)
                {
                    list.add(permissions[i]);
                }
            }
            if (list.size() != 0)
            {
                denied = new String[list.size()];
                for (int i = 0; i < list.size(); i++)
                {
//                    Log.e(tag, "add deny:" + i);
                    denied[i] = list.get(i);
                    ActivityCompat.requestPermissions(this, denied, i);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {


        boolean isDenied = false;
        for (int i = 0; i < denied.length; i++)
        {
            String permission = denied[i];
            for (int j = 0; j < permissions.length; j++)
            {
                if (permissions[j].equals(permission))
                {
                    if (grantResults[j] != PackageManager.PERMISSION_GRANTED)
                    {
                        isDenied = true;
                        break;
                    }
                }
            }
        }
        if (isDenied)
        {
//            Toast.makeText(this, getString(R.string.string_openPermission), Toast.LENGTH_SHORT).show();
        }
        else
        {


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}
