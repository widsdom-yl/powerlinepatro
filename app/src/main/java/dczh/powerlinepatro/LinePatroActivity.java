package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;
import com.hgdendi.expandablerecycleradapter.ViewProducer;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.LineLevelAdapter;
import dczh.model.LineLevelModel;
import dczh.model.LineNameModel;


public class LinePatroActivity extends BaseAppCompatActivity implements BaseExpandableRecyclerViewAdapter.ExpandableRecyclerViewOnClickListener<LineLevelModel, LineNameModel> {

    List<LineLevelModel> list=new ArrayList<LineLevelModel>();
    LineLevelAdapter mAdapter;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_patro);
        setCustomTitle(getString(R.string.string_linepatro),true);
        mRecyclerView = findViewById(R.id.recyler_lineLevel);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initValue();


    }
    public void initValue(){
        for (int i = 0;i<5;++i){
            LineLevelModel model = new LineLevelModel("35kV",10,2000);
            List<LineNameModel> lineNameList=new ArrayList<LineNameModel>();
            for (int j = 0;j<5;++j){
                LineNameModel lineNameModel = new LineNameModel("小行"+i+":"+j);
                lineNameList.add(lineNameModel);
            }
            model.setmList(lineNameList);
            list.add(model);
        }
        mAdapter =new LineLevelAdapter(list);
        mAdapter.setEmptyViewProducer(new ViewProducer() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new DefaultEmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.empty, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder) {

            }
        });
        /*mAdapter.setHeaderViewProducer(new ViewProducer() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
                return new DefaultEmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false)
                );
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder) {

            }
        }, false);*/

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(this);
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
    public boolean onGroupLongClicked(LineLevelModel groupItem) {
        return false;
    }

    @Override
    public boolean onInterceptGroupExpandEvent(LineLevelModel groupItem, boolean isExpand) {
        return false;
    }

    @Override
    public void onGroupClicked(LineLevelModel groupItem) {

    }

    @Override
    public void onChildClicked(LineLevelModel groupItem, LineNameModel childItem) {

        Intent intent = new Intent(this, LineDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lineName",childItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
