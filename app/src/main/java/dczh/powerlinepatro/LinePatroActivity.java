package dczh.powerlinepatro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;
import com.hgdendi.expandablerecycleradapter.ViewProducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.LineLevelAdapter;
import dczh.model.LineLevelModel;
import dczh.model.LineModel;
import dczh.model.ResponseModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LinePatroActivity extends BaseAppCompatActivity implements BaseExpandableRecyclerViewAdapter.ExpandableRecyclerViewOnClickListener<LineLevelModel, LineModel> {

    List<LineLevelModel> mLineLevelList=new ArrayList<LineLevelModel>();
    LineLevelAdapter mAdapter;
    RecyclerView mRecyclerView;
    TreeMap levelHashMap=new TreeMap();//key：线路电压登记 value：LineModel array
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_patro);
        setCustomTitle(getString(R.string.string_linepatro),true);
        mRecyclerView = findViewById(R.id.recyler_lineLevel);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initValue();
        //CrashReport.testJavaCrash();

    }
    public void initValue(){
        requestLineArray();
//        for (int i = 0;i<5;++i){
//            LineLevelModel model = new LineLevelModel("35kV",10,2000);
//            List<LineNameModel> lineNameList=new ArrayList<LineNameModel>();
//            for (int j = 0;j<5;++j){
//                LineNameModel lineNameModel = new LineNameModel("小行"+i+":"+j);
//                lineNameList.add(lineNameModel);
//            }
//            model.setmList(lineNameList);
//            list.add(model);
//        }
        mAdapter =new LineLevelAdapter(mLineLevelList);
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
    public void onChildClicked(LineLevelModel groupItem, LineModel childItem) {

        Intent intent = new Intent(this, LineDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lineName",childItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void requestLineArray() {

        if (lod == null)
        {
            lod = new LoadingDialog(this);
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("pid", "1000")
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"line.php")
                //.post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        Toast.makeText(LinePatroActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();

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
                            List<LineModel> lists = GsonUtil.parseJsonArrayWithGson(body, LineModel[].class);

                            for (LineModel m : lists){
                                if (levelHashMap.containsKey(m.getCod())){
                                    List<LineModel> array = (List<LineModel>) levelHashMap.get(m.getCod());
                                    array.add(m);
                                }
                                else{
                                    List<LineModel> array = new ArrayList<>();
                                    array.add(m);
                                    levelHashMap.put(m.getCod(),array);
                                }
                            }
                            Iterator iterator = levelHashMap.keySet().iterator();
                            while (iterator.hasNext()) {
                                Object key = iterator.next();
                                //System.out.println("tmp.get(key) is :"+levelHashMap.get(key));
                                Log.e(tag,"tmp.get(key) is :"+key);
                                LineLevelModel lineLevelModel = new LineLevelModel();
                                lineLevelModel.setCod((String) key);
                                lineLevelModel.setmList((List<LineModel>) levelHashMap.get(key));
                                mLineLevelList.add(lineLevelModel);
                            }

                            mAdapter.resetMList(mLineLevelList);



                        }
                        else{
                            Toast.makeText(LinePatroActivity.this, getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }
    LoadingDialog lod;
    static  final String tag = "LinePatroActivity";
}
