package dczh.model;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

public class LineLevelModel implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<LineModel>{


    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }


    String cod;
    private List<LineModel> mList;

    public List<LineModel> getmList() {
        return mList;
    }

    public void setmList(List<LineModel> mList) {
        this.mList = mList;
    }



    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public LineModel getChildAt(int childIndex) {
        return mList.size() <= childIndex ? null : mList.get(childIndex);
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }
}
