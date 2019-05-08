package dczh.adapter;

import android.widget.ImageView;

import java.util.List;

import dczh.GlideApp;
import dczh.MyApplication;
import dczh.model.PatrolImageModel;
import dczh.powerlinepatro.R;

public class TowerPartoImageAdapter extends BaseAdapter<PatrolImageModel>{
    List<PatrolImageModel> mList;

    public TowerPartoImageAdapter(List<PatrolImageModel> list)
    {
        super(R.layout.grid_tower_patro_image, list);
    }

    protected void convert(BaseHolder holder, PatrolImageModel model, final int position)
    {
        super.convert(holder, model, position);
        ImageView imageView = holder.getView(R.id.image_tower_patro);
       // imageView.setBackgroundColor(color);
        GlideApp.with(MyApplication.getInstance()).asBitmap()
                .load(model.getImg())
                .centerCrop()
                //.placeholder(R.drawable.imagethumb_cn)//zhbzhb
                .placeholder(R.drawable.imagethumb)//zhbzhb
                .into(imageView);




    }
}
