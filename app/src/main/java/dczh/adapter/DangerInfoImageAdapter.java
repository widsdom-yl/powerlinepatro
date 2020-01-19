package dczh.adapter;

import android.widget.ImageView;

import java.util.List;

import dczh.GlideApp;
import dczh.MyApplication;
import dczh.model.PatrolImageModel;
import dczh.model.UploadFileRetModel;
import dczh.powerlinepatro.R;

public class DangerInfoImageAdapter extends BaseAdapter<UploadFileRetModel>{
    List<PatrolImageModel> mList;

    public DangerInfoImageAdapter(List<UploadFileRetModel> list)
    {
        super(R.layout.grid_danger_image, list);
    }

    protected void convert(BaseHolder holder, UploadFileRetModel model, final int position)
    {
        super.convert(holder, model, position);
        ImageView imageView = holder.getView(R.id.danger_image_view);
        // imageView.setBackgroundColor(color);
        GlideApp.with(MyApplication.getInstance()).asBitmap()
                .load(model.getUrl())
                .centerCrop()
                //.placeholder(R.drawable.imagethumb_cn)//zhbzhb
                .placeholder(R.drawable.imagethumb)//zhbzhb
                .into(imageView);




    }
}
