package dczh.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import dczh.MyApplication;

import static android.content.Context.MODE_PRIVATE;

public class AccountManager {
    private Context context;
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
    public void saveAccount(String usr,String pwd,boolean isRemeber){

        SharedPreferences pref = context.getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("remeber",isRemeber);
        // if (isRemeber){

        editor.putString("usr",usr);
        editor.putString("pwd",pwd);
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


}