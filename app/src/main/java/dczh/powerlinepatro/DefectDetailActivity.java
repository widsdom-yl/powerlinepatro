package dczh.powerlinepatro;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.TowerAccountAdapter;
import dczh.adapter.TowerPartoImageAdapter;
import dczh.model.PatrolImageModel;
import dczh.model.TowerAccountItemModel;
import dczh.model.TowerDefectModel;

public class DefectDetailActivity extends BaseAppCompatActivity {

    RecyclerView mRecyclerView_item;
    RecyclerView mRecyclerView_image;
    TowerDefectModel model;
    TowerAccountAdapter mAapter;
    TowerPartoImageAdapter mPatroAdapter;
    private static final String ARG_PARAM1 = "param1";

    List<PatrolImageModel> mLists = new ArrayList<>();
    List<TowerAccountItemModel> list = new ArrayList<TowerAccountItemModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && actionBar != null)
        {
            model = (TowerDefectModel)bundle.getSerializable(ARG_PARAM1);
            setCustomTitle(model.getTower(), true);
        }

        mRecyclerView_item = findViewById(R.id.recyler_tower_patrol);
        mRecyclerView_item.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView_item.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView_image = findViewById(R.id.recyler_tower_image);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        mRecyclerView_image.setLayoutManager(layoutManage);

        TowerAccountItemModel model1 = new TowerAccountItemModel(getString(R.string.string_upload_time),"2019-4-18");
        TowerAccountItemModel model2 = new TowerAccountItemModel(getString(R.string.string_defect_status),getString(R.string.string_handle));
        TowerAccountItemModel model3 = new TowerAccountItemModel(getString(R.string.string_upload_worker),"张三");

        list.add(model1);
        list.add(model2);
        list.add(model3);

        mAapter = new TowerAccountAdapter(list);
        mRecyclerView_item.setAdapter(mAapter);


        mPatroAdapter = new TowerPartoImageAdapter(mLists);
        mRecyclerView_image.setAdapter(mPatroAdapter);



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
}
