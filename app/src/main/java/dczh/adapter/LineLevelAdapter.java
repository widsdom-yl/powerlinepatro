package dczh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

import dczh.model.LineLevelModel;
import dczh.model.LineNameModel;
import dczh.powerlinepatro.R;

public class LineLevelAdapter extends
        BaseExpandableRecyclerViewAdapter<LineLevelModel, LineNameModel, LineLevelAdapter.GroupVH, LineLevelAdapter.ChildVH> {

    private List<LineLevelModel> mList;

    public LineLevelAdapter(List<LineLevelModel> list) {
        mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public LineLevelModel getGroupItem(int position) {
        return mList.get(position);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return new GroupVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_view_line_level, parent, false));
    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return new ChildVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycle_view_line_name, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, LineLevelModel sampleGroupBean, boolean isExpanding) {

//        if (sampleGroupBean.isExpandable()) {
//            holder.foldIv.setVisibility(View.VISIBLE);
//            holder.foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
//        } else {
//            holder.foldIv.setVisibility(View.INVISIBLE);
//        }

        holder.view_title.setText(sampleGroupBean.getLineLevelName());
        holder.view_linetower_count.setText(""+sampleGroupBean.getLineTowerCount());
        holder.view_line_count.setText(""+sampleGroupBean.getLineCount());

    }

    @Override
    public void onBindChildViewHolder(ChildVH holder, LineLevelModel groupBean, LineNameModel sampleChildBean) {
        holder.text_linename.setText(sampleChildBean.getLineName());
    }

    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        ImageView foldIv;
        TextView view_title;
        TextView view_linetower_count;
        TextView view_line_count;

        GroupVH(View itemView) {
            super(itemView);
           // foldIv = (ImageView) itemView.findViewById(R.id.group_item_indicator);
             view_title = itemView.findViewById(R.id.text_title);

             view_linetower_count = itemView.findViewById(R.id.text_linetower_count);

             view_line_count = itemView.findViewById(R.id.text_line_count);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
            //foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
        TextView text_linename;

        ChildVH(View itemView) {
            super(itemView);
            text_linename = (TextView) itemView.findViewById(R.id.text_linename);
        }
    }


}
