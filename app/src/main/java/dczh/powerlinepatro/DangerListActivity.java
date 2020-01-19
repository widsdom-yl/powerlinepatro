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
import dczh.adapter.DangerListAdapter;
import dczh.model.DangerModel;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DangerListActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener {

    RecyclerView mListView;
    DangerListAdapter mAapter;
    List<DangerModel> mList=new ArrayList<DangerModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_list);
        setCustomTitle(getString(R.string.string_danger),true);

        mListView = findViewById(R.id.listview);
        mListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mListView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public void onResume(){
        super.onResume();
        requestDangerList();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish(); // back button
                return true;
            case R.id.action_refresh:
                requestDangerList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }
    public void requestDangerList() {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();





        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"wxy.php")
                .get()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        Toast.makeText(DangerListActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
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
                            List<DangerModel> lists = GsonUtil.parseJsonArrayWithGson(body, DangerModel[].class);
                            mList = lists;
                            if (mAapter == null){
                                mAapter = new DangerListAdapter(mList);
                                mAapter.setOnItemClickListener(DangerListActivity.this);
                                mListView.setAdapter(mAapter);
                            }
                            else{
                                mAapter.resetMList(mList);
                                mAapter.notifyDataSetChanged();
                            }


                        }
                        else{
                            Toast.makeText(DangerListActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    LoadingDialog lod;
    static  final String tag = "DangerListActivity";
    private static final String ARG_PARAM1 = "param1";
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DangerDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1,mList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
