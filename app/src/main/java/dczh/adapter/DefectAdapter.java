package dczh.adapter;

import android.widget.TextView;

import java.util.List;

import dczh.MyApplication;
import dczh.model.TowerDefectModel;
import dczh.powerlinepatro.R;



public class DefectAdapter extends BaseAdapter<TowerDefectModel>
{
    public DefectAdapter(List<TowerDefectModel> list)
    {
        super(R.layout.list_tower_defect_item, list);
    }

    protected void convert(BaseHolder holder, TowerDefectModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView titleTv = holder.getView(R.id.text_title);
        TextView timeTv = holder.getView(R.id.text_tower_defect_time);
        TextView workerTv = holder.getView(R.id.text_tower_defect_worker);
        TextView statusTv = holder.getView(R.id.text_tower_defect_status);
        titleTv.setText(model.getNme());
        timeTv.setText(MyApplication.getInstance().getString(R.string.string_tower_defect_upload_time)+model.getDte());
        workerTv.setText(MyApplication.getInstance().getString(R.string.string_tower_defect_upload_worker)+model.getUsr());
        statusTv.setText(model.getStausDesc());


    }





}
