package dczh.adapter;

import android.widget.TextView;

import java.util.List;

import dczh.MyApplication;
import dczh.model.LineTowerModel;
import dczh.powerlinepatro.R;



public class LineTowerAdapter extends BaseAdapter<LineTowerModel>
{



    public LineTowerAdapter(List<LineTowerModel> list)
    {
        super(R.layout.list_tower_info, list);
    }

    protected void convert(BaseHolder holder, LineTowerModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView titleTv = holder.getView(R.id.text_title);
        TextView towerTypeTv = holder.getView(R.id.text_tower_type);
        TextView towerTypeDetailTv = holder.getView(R.id.text_tower_type_detail);
        TextView towerHeightTv = holder.getView(R.id.text_tower_height);
        titleTv.setText( model.getNme());
        towerTypeTv.setText(MyApplication.getInstance().getString(R.string.string_type)+":" +
                ""+model.getTpe());
//        towerTypeDetailTv.setText(MyApplication.getInstance().getString(R.string.string_tower_type)+model.getTowerTypeDetail());
//        towerHeightTv.setText(MyApplication.getInstance().getString(R.string.string_height)+model.getTowerHeight());


    }





}
