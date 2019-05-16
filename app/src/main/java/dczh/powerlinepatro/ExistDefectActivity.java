package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.BaseAdapter;
import dczh.adapter.DefectAdapter;
import dczh.model.LineTowerModel;
import dczh.model.TowerDefectModel;

public class ExistDefectActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener {
    RecyclerView mRecyclerView;
    DefectAdapter mAapter;
    private static final String ARG_PARAM1 = "param1";
    List<TowerDefectModel> list = new ArrayList<TowerDefectModel>();
    private LineTowerModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_defect);
        setCustomTitle(getString(R.string.string_defect), true);
        mRecyclerView = findViewById(R.id.recyler_tower_defect);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initValue();
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
    void initValue(){


        mAapter = new DefectAdapter(list);

        mRecyclerView.setAdapter(mAapter);
        mAapter.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DefectDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1,list.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
