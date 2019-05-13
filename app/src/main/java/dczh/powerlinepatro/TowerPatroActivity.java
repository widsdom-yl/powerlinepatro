package dczh.powerlinepatro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import dczh.Fragment.DefectFragment;
import dczh.Fragment.MapFragment;
import dczh.Fragment.TowerAccountFragment;
import dczh.model.LineTowerModel;

//杆塔巡视信息 台账 位置 缺陷信息
public class TowerPatroActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView tx1, tx2, tx3;
    private LinearLayout mTab_item_container;
    LinearLayout content_container;
    private FragmentManager mFM = null;
    Fragment f1,f2,f3;
    private int mSelectIndex = 0;
    private View last, now;

    LineTowerModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_patro);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && actionBar != null)
        {
            model = (LineTowerModel)bundle.getSerializable("lineTowerName");
            setCustomTitle(model.getNme(), true);
        }
        init();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_container,f1)
                    .add(R.id.content_container,f2)
                    .add(R.id.content_container,f3)
                    .commit();
        }
        changeLedger();


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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    private void init() {
        mTab_item_container = (LinearLayout) findViewById(R.id.tab_item_container);



        tx1 = (TextView)findViewById(R.id.tx_1);
        tx2 = (TextView)findViewById(R.id.tx_2);
        tx3 = (TextView)findViewById(R.id.tx_3);



        tx1.setOnClickListener(this);
        tx2.setOnClickListener(this);
        tx3.setOnClickListener(this);



        content_container = (LinearLayout) findViewById(R.id.content_container);
        f1 = TowerAccountFragment.newInstance(model,"1");
        f2 = MapFragment.newInstance(model,"2");
        f3 = DefectFragment.newInstance(model,"3");

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.tx_1:{
                last = mTab_item_container.getChildAt(mSelectIndex);
                now = mTab_item_container.getChildAt(0);
                startAnimation(last, now);
                mSelectIndex = 0;
                changeLedger();
            }

            break;
            case  R.id.tx_2:{
                last = mTab_item_container.getChildAt(mSelectIndex);
                now = mTab_item_container.getChildAt(1);
                startAnimation(last, now);
                mSelectIndex = 1;
                changeMap();
            }
            break;
            case  R.id.tx_3:{
                last = mTab_item_container.getChildAt(mSelectIndex);
                now = mTab_item_container.getChildAt(2);
                startAnimation(last, now);
                mSelectIndex = 2;
                changeDefect();
            }

            break;
            default:
                break;
        }
    }
    private void startAnimation(View last, View now) {
        TranslateAnimation ta = new TranslateAnimation(last.getLeft(),
                now.getLeft(), 0, 0);
        ta.setDuration(300);
        ta.setFillAfter(true);

    }

    /**
     * 切换至台账
     */
    public void changeLedger() {

        getSupportFragmentManager().beginTransaction()
                .hide(f2)
                .hide(f3)
                .show(f1)
                .commit();


        this.settab();

        tx1.setTextColor(Color.parseColor("#1b9fd2"));
    }

    /**
     * 切换至地图
     */
    public void changeMap() {

        getSupportFragmentManager().beginTransaction()
                .hide(f1)
                .hide(f3)
                .show(f2)
                .commit();

        this.settab();

        tx2.setTextColor(Color.parseColor("#1b9fd2"));
    }
    /**
     * 切换至缺陷
     */
    public void changeDefect() {

        getSupportFragmentManager().beginTransaction()
                .hide(f1)
                .hide(f2)
                .show(f3)
                .commit();

        this.settab();

        tx3.setTextColor(Color.parseColor("#1b9fd2"));
    }
    public void settab(){

        //
        tx1.setTextColor(Color.parseColor("#666666"));
        tx2.setTextColor(Color.parseColor("#666666"));
        tx3.setTextColor(Color.parseColor("#666666"));

    }


}
