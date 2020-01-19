package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.BaseAdapter;
import dczh.adapter.DangerPatroDailyAdapter;
import dczh.model.DangerModel;
import dczh.model.DangerPatroDailyModel;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DangerPatroDailyActivity extends BaseAppCompatActivity implements BaseAdapter.OnItemClickListener, View.OnClickListener {
    DangerModel dangerModel;
    private static final String ARG_PARAM1 = "param1";
    RecyclerView mListView;
    DangerPatroDailyAdapter adapter;
    List<DangerPatroDailyModel> mList = new ArrayList<>();
    Button btn_upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_patro_daily);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            dangerModel = (DangerModel)bundle.getSerializable(ARG_PARAM1);
            setCustomTitle(dangerModel.getNme(), true);
        }
        mListView = findViewById(R.id.listview);
       // mListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mListView.setLayoutManager(new LinearLayoutManager(this));

        btn_upload = findViewById(R.id.button_upload);
        btn_upload.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        requestDailyPatroList();


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

    public void requestDailyPatroList() {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("pid", ""+dangerModel.getId())
                .add("aid", "0")
                .add("cnt", "20")
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"wxy_get.php")
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
                        Toast.makeText(DangerPatroDailyActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
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
                            List<DangerPatroDailyModel> lists = GsonUtil.parseJsonArrayWithGson(body, DangerPatroDailyModel[].class);
                            mList = lists;
                            if (adapter == null){
                                adapter = new DangerPatroDailyAdapter(mList);
                                adapter.setOnItemClickListener(DangerPatroDailyActivity.this);
                                mListView.setAdapter(adapter);
                            }
                            else{
                                adapter.resetMList(mList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            Toast.makeText(DangerPatroDailyActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    LoadingDialog lod;
    static  final String tag = "DangerPatroDaily";

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,DangerDailyUploadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1,dangerModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
