package dczh.powerlinepatro;
//选择查看缺陷隐患或者上报缺陷隐患
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DefectMainActivity extends BaseAppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_main);
        setCustomTitle(getString(R.string.string_defect),true);

        findViewById(R.id.button_defect_info).setOnClickListener(this);
        findViewById(R.id.button_upload_defect).setOnClickListener(this);
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
        if (view.getId() == R.id.button_defect_info){

        }
        else if(view.getId() == R.id.button_upload_defect){
            Intent intent = new Intent(this, UploadDefectActivity.class);

            startActivity(intent);
        }
    }
}
