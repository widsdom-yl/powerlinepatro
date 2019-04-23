package dczh.adapter;

import android.widget.ImageView;

import java.util.List;

import dczh.powerlinepatro.R;

public class TowerPartoImageAdapter extends BaseAdapter<Integer>{
    List<Integer> mColorList;

    public TowerPartoImageAdapter(List<Integer> list)
    {
        super(R.layout.grid_tower_patro_image, list);
    }

    protected void convert(BaseHolder holder, Integer color, final int position)
    {
        super.convert(holder, color, position);
        ImageView imageView = holder.getView(R.id.image_tower_patro);
        imageView.setBackgroundColor(color);


    }
}
