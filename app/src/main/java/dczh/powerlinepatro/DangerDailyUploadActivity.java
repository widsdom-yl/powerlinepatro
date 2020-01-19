package dczh.powerlinepatro;

import android.os.Bundle;
import android.view.MenuItem;

import dczh.model.DangerModel;

public class DangerDailyUploadActivity extends BaseAppCompatActivity {
    DangerModel dangerModel;
    private static final String ARG_PARAM1 = "param1";
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

}
