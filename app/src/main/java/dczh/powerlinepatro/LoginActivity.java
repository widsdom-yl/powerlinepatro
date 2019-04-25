package dczh.powerlinepatro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
           Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.MODIFY_AUDIO_SETTINGS,
//            Manifest.permission.ACCESS_WIFI_STATE,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        requestPermisson();
        findViewById(R.id.login_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login_in_button){
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        }
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

}
