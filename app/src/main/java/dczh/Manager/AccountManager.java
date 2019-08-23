package dczh.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import dczh.MyApplication;

import static android.content.Context.MODE_PRIVATE;

public class AccountManager {
    private Context context;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    int uid;
    String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    private static class AccountManagerHolder {
        private static final AccountManager INSTANCE = new AccountManager(MyApplication.getInstance());
    }
    public static final AccountManager getInstance() {
        return AccountManagerHolder.INSTANCE;
    }
    private AccountManager (Context context)
    {
        this.context = context;
    }
    public void saveName(String name){

        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // if (isRemeber){

        editor.putString("name",name);
        // }
        editor.commit();
    }

    public void saveDeviceToken(String deviceToken){

        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // if (isRemeber){

        editor.putString("deviceToken",deviceToken);
        // }
        editor.commit();
    }


    public void saveAccount(String usr,String pwd,String name,boolean isRemeber){

        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("remeber",isRemeber);
        // if (isRemeber){

        editor.putString("usr",usr);
        editor.putString("pwd",pwd);
        editor.putString("name",name);
        // }
        editor.commit();
    }

    public void saveRemeber(boolean isRemeber){

        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("remeber",isRemeber);
        editor.commit();
    }

    public boolean getIsRemeberAccount(){
        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        boolean remeber = pref.getBoolean("remeber",false);//第二个参数为默认值
        return remeber;
    }
    public String getDefaultUsr(){
        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        String usr = pref.getString("usr","");//第二个参数为默认值
        return usr;
    }
    public String getDefaultPwd(){
        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        String pwd = pref.getString("pwd","");//第二个参数为默认值
        return pwd;
    }
    public String getDefaultDeviceToken(){
        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        String deviceToken = pref.getString("deviceToken","");//第二个参数为默认值
        return deviceToken;
    }

    public String getDefaultName(){
        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        String name = pref.getString("name","");//第二个参数为默认值
        return name;
    }


}