package dczh.adapter;

import android.widget.TextView;

import java.util.List;

import dczh.model.TowerAccountItemModel;
import dczh.powerlinepatro.R;



public class TowerAccountAdapter extends BaseAdapter<TowerAccountItemModel>
{



    public TowerAccountAdapter(List<TowerAccountItemModel> list)
    {
        super(R.layout.list_tower_account, list);
    }

    protected void convert(BaseHolder holder, TowerAccountItemModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView titleTv = holder.getView(R.id.text_title);
        TextView detailTv = holder.getView(R.id.text_info);
        titleTv.setText(model.getTowerAcountItemTitle());
        detailTv.setText(model.getTowerAcountItemInfo());


    }





}
