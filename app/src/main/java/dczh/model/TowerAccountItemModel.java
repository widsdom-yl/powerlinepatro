package dczh.model;

public class TowerAccountItemModel {
    public String getTowerAcountItemTitle() {
        return towerAcountItemTitle;
    }

    public TowerAccountItemModel(String towerAcountItemTitle, String towerAcountItemInfo) {
        this.towerAcountItemTitle = towerAcountItemTitle;
        this.towerAcountItemInfo = towerAcountItemInfo;
    }

    public void setTowerAcountItemTitle(String towerAcountItemTitle) {
        this.towerAcountItemTitle = towerAcountItemTitle;
    }

    String towerAcountItemTitle;

    public String getTowerAcountItemInfo() {
        return towerAcountItemInfo;
    }

    public void setTowerAcountItemInfo(String towerAcountItemInfo) {
        this.towerAcountItemInfo = towerAcountItemInfo;
    }

    String towerAcountItemInfo;
}
