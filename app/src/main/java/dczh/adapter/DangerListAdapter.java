package dczh.adapter;

import android.graphics.Color;
import android.widget.TextView;

import java.util.List;

import dczh.MyApplication;
import dczh.model.DangerModel;
import dczh.powerlinepatro.R;

public class DangerListAdapter extends BaseAdapter<DangerModel>
{
    public DangerListAdapter(List<DangerModel> list)
    {
        super(R.layout.adapter_danger_list, list);
    }

    protected void convert(BaseHolder holder, DangerModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView title = holder.getView(R.id.text_title);
        TextView level = holder.getView(R.id.text_danger_level);
        TextView time = holder.getView(R.id.text_danger_create_time);
        TextView worker = holder.getView(R.id.text_danger_create_worker);
        title.setText(model.getNme());
        level.setText(MyApplication.getInstance().getString(model.getLevelDescID()));
        time.setText(MyApplication.getInstance().getString(R.string.string_danger_create_time)+model.getDts());
        worker.setText(MyApplication.getInstance().getString(R.string.string_danger_create_worker)+model.getAkt());
        switch (model.getLevel()){
            case 0:
                level.setTextColor(Color.rgb(255, 0, 0));
                break;
            case 1:
            case 2:
                //FFA619
                level.setTextColor(Color.rgb(0xff, 0xa6, 0x19));

                break;
                default:
                    break;
        }

    }
}
