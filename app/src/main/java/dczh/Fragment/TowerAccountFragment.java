package dczh.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dczh.Util.MathUtil;
import dczh.adapter.TowerAccountAdapter;
import dczh.adapter.TowerPartoImageAdapter;
import dczh.model.LineTowerModel;
import dczh.model.TowerAccountItemModel;
import dczh.powerlinepatro.R;
import dczh.powerlinepatro.UploadDefectActivity;
import dczh.powerlinepatro.UploadPatroActivity;


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
    List<Integer> colorList = new ArrayList<>();
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
        TowerAccountItemModel model1 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_name),"小魏1线1#");
        TowerAccountItemModel model2 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type),"耐张");
        TowerAccountItemModel model3 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_type_detail),"SDGT-16");
//        TowerAccountItemModel model4 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_height),"23.1");
//        TowerAccountItemModel model5 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_big_distance),"117m");
//        TowerAccountItemModel model6 = new TowerAccountItemModel(getContext().getString(R.string.string_tower_account_all_distance),"230m");

        list.add(model1);
        list.add(model2);
        list.add(model3);
//        list.add(model4);
//        list.add(model5);
//        list.add(model6);
        mAapter = new TowerAccountAdapter(list);
        mRecyclerView_account.setAdapter(mAapter);

        for (int i = 0;i<6;i++){
            int color =  Color.rgb((int) MathUtil.nextDouble(0,255), (int)MathUtil.nextDouble(0,255), (int)MathUtil.nextDouble(0,255));
            colorList.add(color);
        }
        mPatroAdapter = new TowerPartoImageAdapter(colorList);
        mRecyclerView_image.setAdapter(mPatroAdapter);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_sign_parto){
            Intent intent = new Intent(this.getContext(), UploadPatroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAM1,model);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this.getContext(), UploadDefectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAM1,model);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
