package dczh.powerlinepatro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class PatrolImageDetailActivity extends BaseAppCompatActivity {

    String imagePath;
    ImageView imageView;
    private static final String ARG_PARAM1 = "param1";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_image_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);

            setCustomTitle("", true);
        }
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            imagePath = (String) bundle.getString(ARG_PARAM1);

        }
        initView();
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

    void initView()
    {
        imageView = findViewById(R.id.detail_image);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();


        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = bitmap.getHeight() * screenWidth / bitmap.getWidth();
        imageView.setLayoutParams(lp);


    }
}
