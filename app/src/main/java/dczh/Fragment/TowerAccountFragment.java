package dczh.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.TowerAccountAdapter;
import dczh.adapter.TowerPartoImageAdapter;
import dczh.model.LineTowerModel;
import dczh.model.PatrolImageModel;
import dczh.model.ResponseModel;
import dczh.model.TowerAccountItemModel;
import dczh.model.UploadFileRetModel;
import dczh.powerlinepatro.R;
import dczh.powerlinepatro.UploadDefectActivity;
import dczh.powerlinepatro.UploadPatroActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TowerAccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private LineTowerModel model;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView mRecyclerView_account;
    RecyclerView mRecyclerView_image;
    Button signPartoBtn;
    Button signDefectBtn;

    TowerAccountAdapter mAapter;
    TowerPartoImageAdapter mPatroAdapter;
    List<UploadFileRetModel>mList = new ArrayList<>();
    List<TowerAccountItemModel> list = new ArrayList<TowerAccountItemModel>();
    public TowerAccountFragment() {
        // TowerAccountFragment empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DefectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TowerAccountFragment newInstance(LineTowerModel param1, String param2) {
        TowerAccountFragment fragment = new TowerAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (LineTowerModel)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void resetModel(LineTowerModel m){
        if (m!=null){
            model = m;
            TowerAccountItemModel model1 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_name),model.getNme());
            TowerAccountItemModel model2 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type),model.getTpe());
//        TowerAccountItemModel model3 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type_detail),"SDGT-16");
//        TowerAccountItemModel model4 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_height),"23.1");
//        TowerAccountItemModel model5 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_big_distance),"117m");
//        TowerAccountItemModel model6 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_all_distance),"230m");
            list.clear();
            list.add(model1);
            list.add(model2);
//        list.add(model3);
//        list.add(model4);
//        list.add(model5);
//        list.add(model6);
            mAapter.resetMList(list);
            mAapter.notifyDataSetChanged();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_defect, container, false);

        View view = inflater.inflate(R.layout.fragment_ledger, container, false);
        initView(view);

        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
    @Override
    public void onResume(){
        super.onResume();
        requestLineTowerPartolImageArray();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void initView(View view){
        mRecyclerView_account = view.findViewById(R.id.recyler_tower_account_item);
        mRecyclerView_account.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView_account.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mRecyclerView_image = view.findViewById(R.id.recyler_tower_image_item);
        GridLayoutManager layoutManage = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView_image.setLayoutManager(layoutManage);

        signPartoBtn = view.findViewById(R.id.button_sign_parto);
        signDefectBtn = view.findViewById(R.id.button_sign_defect);
        signPartoBtn.setOnClickListener(this);
        signDefectBtn.setOnClickListener(this);
        initValue();
    }
    void initValue(){
        TowerAccountItemModel model1 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_name),model.getNme());
        TowerAccountItemModel model2 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type),model.getTpe());
//        TowerAccountItemModel model3 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type_detail),"SDGT-16");
//        TowerAccountItemModel model4 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_height),"23.1");
//        TowerAccountItemModel model5 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_big_distance),"117m");
//        TowerAccountItemModel model6 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_all_distance),"230m");

        list.add(model1);
        list.add(model2);
//        list.add(model3);
//        list.add(model4);
//        list.add(model5);
//        list.add(model6);
        mAapter = new TowerAccountAdapter(list);
        mRecyclerView_account.setAdapter(mAapter);

//        for (int i = 0;i<6;i++){
//            int color =  Color.rgb((int) MathUtil.nextDouble(0,255), (int)MathUtil.nextDouble(0,255), (int)MathUtil.nextDouble(0,255));
//            colorList.add(color);
//        }
        mPatroAdapter = new TowerPartoImageAdapter(mList);
        mRecyclerView_image.setAdapter(mPatroAdapter);



    }
//获取杆塔巡视图片
    public void requestLineTowerPartolImageArray() {

        if (lod == null)
        {
            lod = new LoadingDialog(this.getContext());
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("aid", "0")
                .add("pid", ""+model.getId()
                )
                .add("cnt","1")
                .build();





        final Request request = new Request.Builder()
                .url(Config.workUrl+"pic_get.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                lod.dismiss();
                Toast.makeText(TowerAccountFragment.this.getContext(), getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.e(tag,"res is "+res);
                final ResponseModel model  = GsonUtil.parseJsonWithGson(res,ResponseModel.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //
                        lod.dismiss();
                        if (model != null && model.error_code==0){
                            String body = new Gson().toJson(model.data);
                            List<PatrolImageModel> lists = GsonUtil.parseJsonArrayWithGson(body, PatrolImageModel[].class);
                            if (lists.size()>0){
                                String body1 = new Gson().toJson(lists.get(0).getImg());
                                List<UploadFileRetModel> mUrls  = GsonUtil.parseJsonArrayWithGson(body1, UploadFileRetModel[].class);
                                mList = mUrls;
                                mPatroAdapter.resetMList(mUrls);
                                mPatroAdapter.notifyDataSetChanged();
                            }
                            else{
                                mList = new ArrayList<>();
                                mPatroAdapter.resetMList(mList);
                                mPatroAdapter.notifyDataSetChanged();
                            }

                        }
                        else{
                            Toast.makeText(TowerAccountFragment.this.getContext(), getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    public static int REQUEST_CODE_1 = 1;
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_sign_parto){
            Intent intent = new Intent(this.getContext(), UploadPatroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAM1,model);
            intent.putExtras(bundle);
           // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_1);
        }
        else{
            Intent intent = new Intent(this.getContext(), UploadDefectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAM1,model);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_1);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    LoadingDialog lod;
    static  final String tag = "TowerAccountFragment";

}
