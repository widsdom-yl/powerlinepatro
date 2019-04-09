package dczh.model;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

public class LineLevelModel implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<LineNameModel>{
    public String getLineLevelName() {
        return lineLevelName;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getLineTowerCount() {
        return lineTowerCount;
    }

    public void setLineLevelName(String lineLevelName) {
        this.lineLevelName = lineLevelName;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public void setLineTowerCount(int lineTowerCount) {
        this.lineTowerCount = lineTowerCount;
    }
    public LineLevelModel(String _lineLevelName,int _lineCount,int _lineTowerCount){
        lineLevelName = _lineLevelName;
        lineCount = _lineCount;
        lineTowerCount = _lineTowerCount;
    }
    String lineLevelName;
    int lineCount;
    int lineTowerCount;

    public List<LineNameModel> getmList() {
        return mList;
    }

    public void setmList(List<LineNameModel> mList) {
        this.mList = mList;
    }

    private List<LineNameModel> mList;

    @Override
    public int getChildCount() {
        return mList.size();
    }

    @Override
    public LineNameModel getChildAt(int childIndex) {
        return mList.size() <= childIndex ? null : mList.get(childIndex);
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }
}
