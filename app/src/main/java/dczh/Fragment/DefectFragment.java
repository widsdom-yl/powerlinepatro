package dczh.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dczh.Util.Config;
import dczh.Util.GsonUtil;
import dczh.View.LoadingDialog;
import dczh.adapter.BaseAdapter;
import dczh.adapter.DefectAdapter;
import dczh.model.LineTowerModel;
import dczh.model.ResponseModel;
import dczh.model.TowerDefectModel;
import dczh.powerlinepatro.DefectDetailActivity;
import dczh.powerlinepatro.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DefectFragment extends Fragment implements BaseAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView mRecyclerView;
    DefectAdapter mAapter;
    List<TowerDefectModel> mList = new ArrayList<TowerDefectModel>();
    private LineTowerModel model;

    public DefectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param model Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefectFragment newInstance(LineTowerModel model, String param2) {
        DefectFragment fragment = new DefectFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, model);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_defect, container, false);
        initView(view);
        return view;
    }

    void initView(View view){
        mRecyclerView = view.findViewById(R.id.recyler_tower_defect);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        initValue();
    }
    void initValue(){


        mAapter = new DefectAdapter(mList);

        mRecyclerView.setAdapter(mAapter);
        mAapter.setOnItemClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        requestTowerDefectArray();
        // Refresh the state of the +1 button each time the activity receives focus.

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this.getContext(), DefectDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1,mList.get(position));
        bundle.putSerializable(ARG_PARAM2,model);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void requestTowerDefectArray() {

        if (lod == null)
        {
            lod = new LoadingDialog(this.getContext());
        }
        lod.dialogShow();


        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("pid", ""+model.getId())
                .add("aid", "0")
                .add("cnt", "100")
                .build();




        MediaType mediaType = MediaType.parse("application/data");
        final Request request = new Request.Builder()
                .url(Config.workUrl+"pie_get.php")
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lod.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.error_request_failed), Toast.LENGTH_SHORT).show();
                    }
                });
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
                            List<TowerDefectModel> lists = GsonUtil.parseJsonArrayWithGson(body, TowerDefectModel[].class);
                            mList = lists;
                            mAapter.resetMList(mList);
                            mAapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(DefectFragment.this.getContext(), getString(R.string.error_request_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }

    LoadingDialog lod;
    static  final String tag = "DefectFragment";



}
