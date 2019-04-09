package dczh.model;

import java.io.Serializable;

public class LineTowerModel implements Serializable {
    String towerName;
    String towerType;//类型
    String towerTypeDetail;//塔型
    Double towerHeight;//塔型

    public LineTowerModel(String towerName, String towerType, String towerTypeDetail, Double towerHeight) {
        this.towerName = towerName;
        this.towerType = towerType;
        this.towerTypeDetail = towerTypeDetail;
        this.towerHeight = towerHeight;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getTowerType() {
        return towerType;
    }

    public void setTowerType(String towerType) {
        this.towerType = towerType;
    }

    public String getTowerTypeDetail() {
        return towerTypeDetail;
    }

    public void setTowerTypeDetail(String towerTypeDetail) {
        this.towerTypeDetail = towerTypeDetail;
    }

    public Double getTowerHeight() {
        return towerHeight;

    }

    public void setTowerHeight(Double towerHeight) {
        this.towerHeight = towerHeight;
    }


}
