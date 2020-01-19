package dczh.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dczh.GlideApp;
import dczh.MyApplication;
import dczh.model.DangerPatroDailyModel;
import dczh.powerlinepatro.R;


public class DangerPatroDailyAdapter extends BaseAdapter<DangerPatroDailyModel>
{
    public DangerPatroDailyAdapter(List<DangerPatroDailyModel> list)
    {
        super(R.layout.list_danger_parto_daily, list);
    }

    protected void convert(BaseHolder holder, DangerPatroDailyModel model, final int position)
    {
        super.convert(holder, model, position);
        TextView time = holder.getView(R.id.text_time);
        TextView worker = holder.getView(R.id.text_worker);
        TextView process = holder.getView(R.id.text_process);
        TextView scene = holder.getView(R.id.text_scene);
        ImageView image_scene = holder.getView(R.id.image_scene);
        String preWorker = MyApplication.getInstance().getString(R.string.string_danger_upload_worker);
        String preTime = MyApplication.getInstance().getString(R.string.string_danger_upload_time);
        String preProcess = MyApplication.getInstance().getString(R.string.string_danger_scene_process);
        String preJudge = MyApplication.getInstance().getString(R.string.string_danger_judge);
        String judgeY = MyApplication.getInstance().getString(R.string.string_yes);
        String judgeN = MyApplication.getInstance().getString(R.string.string_no);
        time.setText(preTime+model.getDte());
        worker.setText(preWorker+model.getUsr());
        worker.setText(preWorker+model.getUsr());
        process.setText(preProcess+model.getGcj());
        scene.setText(preJudge+(model.getIcl()==1?judgeY:judgeN));

        GlideApp.with(MyApplication.getInstance()).asBitmap()
                .load(model.getImg())
                .centerInside()
                //.placeholder(R.drawable.imagethumb_cn)//zhbzhb
                .placeholder(R.drawable.imagethumb)//zhbzhb
                .into(image_scene);


    }
}


