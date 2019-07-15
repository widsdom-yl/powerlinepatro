package dczh.Util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

//https://www.showdoc.cc/240135450293679?page_id=1368056435733879
public class Config {
    public static final String baseUrl = "http://gz.aliyuns.vip/svr/";
    public static final String serverUrl = "http://gz.aliyuns.vip/svr/user/";
    public static final String workUrl = "http://gz.aliyuns.vip/svr/work/";
    public static final String gblUrl = "http://gz.aliyuns.vip/svr/gbl/";
    //http://gz.aliyuns.vip/svr/gbl
    //https://www.showdoc.cc/240135450293679?page_id=1368009207406445
    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

}
