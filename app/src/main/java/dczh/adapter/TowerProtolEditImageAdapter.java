package dczh.adapter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import dczh.MyApplication;
import dczh.powerlinepatro.R;

public class TowerProtolEditImageAdapter extends BaseAdapter<String> {

    public TowerProtolEditImageAdapter(List<String> list)
    {
        super(R.layout.grid_tower_protrol_edit_image, list);
    }

    protected void convert(BaseHolder holder, String filePath, final int position)
    {
        super.convert(holder, filePath, position);
        ImageView imageView = holder.getView(R.id.image_tower_patrol);
        ImageButton deleteButton = holder.getView(R.id.button_delete);
        if (filePath == null && filePath.length() == 0){
            imageView.setImageResource(R.drawable.add);
            deleteButton.setVisibility(View.INVISIBLE);
        }
        else{
            deleteButton.setVisibility(View.VISIBLE);
            //imageView.setBackgroundColor(color);
            RequestOptions requestOptions = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
            //将图片显示在ivImage上
            if (filePath.contains("jpeg") || filePath.contains("jpg") || filePath.contains("png")){
                Glide.with(MyApplication.getInstance()).load(filePath).apply(requestOptions).into(imageView);
            }
            else if(filePath.contains("mp4")){
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                imageView.setImageBitmap(bitmap);
            }


        }

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                    if (mDeleteClickListener != null)
                    {
                        mDeleteClickListener.onDeleteBtnCilck(view, position);
                    }

            }
        });



    }

    public interface DeleteClickListener
    {
        void onDeleteBtnCilck(View view, int position);

    }

    DeleteClickListener mDeleteClickListener;
    public void setmDeleteClickListener(DeleteClickListener listener)
    {
        mDeleteClickListener = listener;
    }


}
