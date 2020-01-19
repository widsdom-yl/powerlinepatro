package dczh.model;

public class DangerInfoItemModel {
    public String getDangerInfoItem0() {
        return dangerInfoItem0;
    }
    public DangerInfoItemModel(String item0,String item1){
        dangerInfoItem0 = item0;
        dangerInfoItem1 = item1;
    }

    public void setDangerInfoItem0(String dangerInfoItem0) {
        this.dangerInfoItem0 = dangerInfoItem0;
    }

    public String getDangerInfoItem1() {
        return dangerInfoItem1;
    }

    public void setDangerInfoItem1(String dangerInfoItem1) {
        this.dangerInfoItem1 = dangerInfoItem1;
    }

    String dangerInfoItem0;
    String dangerInfoItem1;

}
