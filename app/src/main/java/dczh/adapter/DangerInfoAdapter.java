package dczh.adapter;

import android.widget.TextView;

import java.util.List;

import dczh.model.DangerInfoItemModel;
import dczh.powerlinepatro.R;

public class DangerInfoAdapter extends BaseAdapter<DangerInfoItemModel>{
    public DangerInfoAdapter(List<DangerInfoItemModel> list)
    {
        super(R.layout.list_danger_info, list);
    }

    protected void convert(BaseHolder holder, DangerInfoItemModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView title = holder.getView(R.id.text_title);
        TextView more = holder.getView(R.id.text_more);

        title.setText(model.getDangerInfoItem0());
        more.setText(model.getDangerInfoItem1());

    }
}
