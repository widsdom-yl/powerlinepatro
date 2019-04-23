package dczh.powerlinepatro;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dczh.View.ActionSheet;
import dczh.model.LineTowerModel;

//上传巡视图片
public class UploadPatroActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private LineTowerModel model;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    Button button_choose_image;
    ImageView imageView;
    private ActionSheet actionSheet;
    /******************************************/
    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    /******************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_patro);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        model = (LineTowerModel)bundle.getSerializable(ARG_PARAM1);
        if ( actionBar != null)
        {
            setCustomTitle(getString(R.string.string_sign_patro), true);
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
        button_choose_image = findViewById(R.id.button_choose_image);
        imageView = findViewById(R.id.imageView2);
        button_choose_image.setOnClickListener(this);
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
                .addSheet("雷军", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UploadPatroActivity.this, "当然是雷布斯！", Toast.LENGTH_SHORT).show();
                        actionSheet.dismiss();
                    }
                })
                .addSheet("马化腾", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UploadPatroActivity.this, "当然是小马哥！", Toast.LENGTH_SHORT).show();
                        actionSheet.dismiss();
                    }
                })
                .addSheet("马云", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UploadPatroActivity.this, "当然是马爸爸！", Toast.LENGTH_SHORT).show();
                        actionSheet.dismiss();
                    }
                })
                .addCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UploadPatroActivity.this, "容我三思", Toast.LENGTH_SHORT).show();
                        actionSheet.dismiss();
                    }
                })
                .create();
    }

}
