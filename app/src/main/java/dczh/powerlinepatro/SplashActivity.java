package dczh.powerlinepatro;
//https://lanhuapp.com/url/7RsrQ
//http://gz.aliyuns.vip/adm/log.php
//https://www.showdoc.cc/317380933238324

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.message.PushAgent;

import java.io.IOException;

import dczh.Manager.AccountManager;
import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.model.ResponseModel;
import dczh.model.UserModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

       // tx_version
        TextView tx_version = findViewById(R.id.tx_version);
        tx_version.setText(""+Config.packageCode(this));

        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                IsAutoLogin = AccountManager.getInstance().getIsRemeberAccount();
                if (IsAutoLogin){
                    UserName = AccountManager.getInstance().getDefaultUsr();
                    Password = AccountManager.getInstance().getDefaultPwd();
                    if (UserName.length()>0 && Password.length()>0){
                        login(UserName, Password);
                    }
                }
                else{
                    openLoginActivity();
                }
            }
        }, 1 * 5000);

    }

    private void openLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(final String mUserName, final String mPassword) {

        String deviceToken  = AccountManager.getInstance().getDefaultDeviceToken();
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("user", mUserName)
                .add("pass", mPassword)
                .add("devicetoken", deviceToken)
                .build();


        final Request request = new Request.Builder()
                .url(Config.serverUrl+"login.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {

                                      Toast.makeText(SplashActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                                      openLoginActivity();
                                  }
                              });

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
                            String body = new Gson().toJson(model.data);

                            UserModel userModel = new Gson().fromJson(body,UserModel.class);
//
                            AccountManager.getInstance().saveAccount(mUserName, mPassword,userModel.getName(), IsAutoLogin);
//                            String token = model.getData().getToken();
                            Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("uid",userModel.getUid());
                            intent.putExtras(bundle);
                            AccountManager.getInstance().setUid(userModel.getUid());
                            AccountManager.getInstance().setUid(userModel.getUid());
                            AccountManager.getInstance().setToken(userModel.getToken());
                            startActivity(intent);
                            SplashActivity.this.finish();
                            Toast.makeText(SplashActivity.this, getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        }
                        else{
                            openLoginActivity();
                            Toast.makeText(SplashActivity.this, getString(R.string.error_login_failed), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });




    }


    LoadingDialog lod;
    boolean IsAutoLogin = false;
    String UserName = null;
    String Password = null;
    static final String  tag =  "SplashActivity";



}


