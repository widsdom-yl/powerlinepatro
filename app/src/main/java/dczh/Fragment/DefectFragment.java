package dczh.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dczh.adapter.BaseAdapter;
import dczh.adapter.DefectAdapter;
import dczh.model.LineTowerModel;
import dczh.model.TowerDefectModel;
import dczh.powerlinepatro.DefectDetailActivity;
import dczh.powerlinepatro.R;


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
    List<TowerDefectModel> list = new ArrayList<TowerDefectModel>();
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
        for (int i=0;i<5;++i){
            TowerDefectModel model = new TowerDefectModel("线路杆塔","2019-4-13 12:50","张三"+i,true);
            list.add(model);
        }

        mAapter = new DefectAdapter(list);

        mRecyclerView.setAdapter(mAapter);
        mAapter.setOnItemClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();

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
        bundle.putSerializable(ARG_PARAM1,model);
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

}
