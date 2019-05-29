package dczh.adapter;


import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dczh.powerlinepatro.R;


public class HomePageAdapter extends BaseAdapter<String>
{



    public HomePageAdapter(List<String> list)
    {
        super(R.layout.recycle_view_home_page, list);
    }

    protected void convert(BaseHolder holder, String path, final int position)
    {
        super.convert(holder, path, position);
        ImageView backGroundImageView = holder.getView(R.id.backgroud_image);
        ImageView iconImageView = holder.getView(R.id.icon_image);
        TextView titleTextView = holder.getView(R.id.title_textview);
        titleTextView.setText(path);
        switch (position){
            case 0:
                backGroundImageView.setBackgroundColor(Color.parseColor("#0BA29A"));
                iconImageView.setImageResource(R.drawable.homepage_linepatro);

                break;
            case 1:
                backGroundImageView.setBackgroundColor(Color.parseColor("#FBB45A"));
                iconImageView.setImageResource(R.drawable.linecross);
                break;
            case 2:
                backGroundImageView.setBackgroundColor(Color.parseColor("#F96269"));
                iconImageView.setImageResource(R.drawable.homepage_defect);
                break;
            case 3:
                backGroundImageView.setBackgroundColor(Color.parseColor("#7CCF4B"));
                iconImageView.setImageResource(R.drawable.upload);
                break;
                default:
                    break;

        }

    }





}
