package dczh.powerlinepatro;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.BaseAdapter;
import dczh.adapter.HomePageAdapter;

public class HomePageActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener {
    RecyclerView homePageRecyclerView;
    HomePageAdapter adapter;
    List<String> list=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().show();
        setCustomTitle(getString(R.string.string_main_title)+"gyl",false);
        list.add(getString(R.string.string_homepage0));
        list.add(getString(R.string.string_homepage1));
        list.add(getString(R.string.string_homepage2));
        list.add(getString(R.string.string_homepage3));
        initView();
    }
    void initView(){
        homePageRecyclerView = findViewById(R.id.home_grid_view);
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        homePageRecyclerView.setLayoutManager(layoutManage);
        adapter = new HomePageAdapter(list);
        adapter.setOnItemClickListener(this);
        homePageRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
