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
import dczh.adapter.LineTowerAdapter;
import dczh.model.LineNameModel;
import dczh.model.LineTowerModel;

public class LineDetailActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener {
    LineNameModel model;
    LineTowerAdapter mAapter;
    RecyclerView mRecyclerView;
    List<LineTowerModel> list=new ArrayList<LineTowerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && actionBar != null)
        {
            model = (LineNameModel)bundle.getSerializable("lineName");
            setCustomTitle(model.getLineName(), true);
        }
        mRecyclerView = findViewById(R.id.recyler_lineTower);
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
        for (int i = 0;i<5;++i){
            LineTowerModel model = new LineTowerModel("ganta"+i,"耐张","SDGT-16",23.1);
            list.add(model);
        }
        mAapter = new LineTowerAdapter(list);
        mAapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, TowerPatroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lineTowerName",list.get(position));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
