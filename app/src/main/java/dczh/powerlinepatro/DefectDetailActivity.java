package dczh.powerlinepatro;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import dczh.Util.GsonUtil;
import dczh.adapter.TowerAccountAdapter;
import dczh.adapter.TowerPartoImageAdapter;
import dczh.model.LineTowerModel;
import dczh.model.TowerAccountItemModel;
import dczh.model.TowerDefectModel;
import dczh.model.UploadFileRetModel;

public class DefectDetailActivity extends BaseAppCompatActivity {

    RecyclerView mRecyclerView_item;
    RecyclerView mRecyclerView_image;
    TowerDefectModel mDefectModel;
    LineTowerModel mLineTowerModel;
    TowerAccountAdapter mAapter;
    TowerPartoImageAdapter mPatroAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<UploadFileRetModel> mLists = new ArrayList<>();
    List<TowerAccountItemModel> list = new ArrayList<TowerAccountItemModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && actionBar != null)
        {
            mDefectModel = (TowerDefectModel)bundle.getSerializable(ARG_PARAM1);
            mLineTowerModel = (LineTowerModel)bundle.getSerializable(ARG_PARAM2);
            String body1 = new Gson().toJson(mDefectModel.getImg());
            List<UploadFileRetModel> mUrls  = GsonUtil.parseJsonArrayWithGson(body1, UploadFileRetModel[].class);
            mLists = mUrls;
            setCustomTitle(mLineTowerModel.getNme(), true);
        }

        mRecyclerView_item = findViewById(R.id.recyler_tower_patrol);
        mRecyclerView_item.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView_item.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView_image = findViewById(R.id.recyler_tower_image);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 3);
        mRecyclerView_image.setLayoutManager(layoutManage);

        if (mDefectModel != null){
            TowerAccountItemModel model1 = new TowerAccountItemModel(getString(R.string.string_upload_time),mDefectModel.getDte());
            TowerAccountItemModel model2 = new TowerAccountItemModel(getString(R.string.string_defect_status),mDefectModel.getStausDesc());
            TowerAccountItemModel model3 = new TowerAccountItemModel(getString(R.string.string_upload_worker),mDefectModel.getUsr());
            TowerAccountItemModel model4 = new TowerAccountItemModel(getString(R.string.string_defect_info),mDefectModel.getNme());

            list.add(model1);
            list.add(model2);
            list.add(model3);
            list.add(model4);
        }


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
