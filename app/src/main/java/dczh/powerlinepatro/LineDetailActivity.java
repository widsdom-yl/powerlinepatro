package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.BaseAdapter;
import dczh.adapter.LineTowerAdapter;
import dczh.model.LineModel;
import dczh.model.LineTowerModel;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LineDetailActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener {
    LineModel model;
    LineTowerAdapter mAapter;
    RecyclerView mRecyclerView;
    List<LineTowerModel> mList=new ArrayList<LineTowerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && actionBar != null)
        {
            model = (LineModel)bundle.getSerializable("lineName");
            setCustomTitle(model.getNme(), true);
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
//        for (int i = 0;i<5;++i){
//            LineTowerModel model = new LineTowerModel("ganta"+i,"耐张","SDGT-16",23.1);
//            list.add(model);
//        }
        requestLineTowerArray();
        mAapter = new LineTowerAdapter(mList);
        mAapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAapter);
    }

    public void requestLineTowerArray() {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("pid", ""+model.getId())
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"tower.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      lod.dismiss();
                                      Toast.makeText(LineDetailActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
                                  }
                              });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e(tag,"res is "+res);
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        lod.dismiss();
                        if (model != null && model.error_code==0){
                            String body = new Gson().toJson(model.data);
                            List<LineTowerModel> lists = GsonUtil.parseJsonArrayWithGson(body, LineTowerModel[].class);
                            mList = lists;
                            mAapter.resetMList(mList);
                            mAapter.notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(LineDetailActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, TowerPatroActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lineTowerName",mList.get(position));
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    LoadingDialog lod;
    static  final String tag = "LineDetailActivity";


}
