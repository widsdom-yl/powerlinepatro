package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.DangerInfoAdapter;
import dczh.adapter.DangerInfoImageAdapter;
import dczh.model.DangerInfoItemModel;
import dczh.model.DangerModel;
import dczh.model.UploadFileRetModel;

public class DangerDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    RecyclerView mlistView_item;
    RecyclerView mListView_image;
    DangerModel dangerModel;
    private static final String ARG_PARAM1 = "param1";
    DangerInfoAdapter itemAdapter;
    List<DangerInfoItemModel> list = new ArrayList<DangerInfoItemModel>();
    List<UploadFileRetModel> imageList = new ArrayList<UploadFileRetModel>();
    Button btn_daily;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_detail);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            dangerModel = (DangerModel)bundle.getSerializable(ARG_PARAM1);
            setCustomTitle(dangerModel.getNme(), true);
        }

        mlistView_item = findViewById(R.id.listview_text);
        mlistView_item.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mlistView_item.setLayoutManager(new LinearLayoutManager(this));

        DangerInfoItemModel model1 = new DangerInfoItemModel(getString(R.string.string_danger_create_time)+dangerModel.getDts(),getString(R.string.string_danger_level)+":"+getString(dangerModel.getLevelDescID()));
        DangerInfoItemModel model2 = new DangerInfoItemModel(getString(R.string.string_danger_create_worker)+dangerModel.getAkt(),"");
        DangerInfoItemModel model3 = new DangerInfoItemModel(getString(R.string.string_danger_information)+dangerModel.getCmt(),"");
        list.add(model1);
        list.add(model2);
        list.add(model3);
        itemAdapter = new DangerInfoAdapter(list);
        mlistView_item.setAdapter(itemAdapter);

        mListView_image = findViewById(R.id.listview_image);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        mListView_image.setLayoutManager(layoutManage);

        imageList.add(new UploadFileRetModel(dangerModel.getIm1()));
        imageList.add(new UploadFileRetModel(dangerModel.getIm2()));

        DangerInfoImageAdapter adapter = new DangerInfoImageAdapter(imageList);
        mListView_image.setAdapter(adapter);


        btn_daily = findViewById(R.id.button_daily);
        btn_daily.setOnClickListener(this);


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
        if (view.getId() == R.id.button_daily){
            Intent intent = new Intent(this,DangerPatroDailyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAM1,dangerModel);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
